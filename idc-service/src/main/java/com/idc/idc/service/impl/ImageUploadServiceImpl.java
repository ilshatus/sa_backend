package com.idc.idc.service.impl;

import com.idc.idc.exception.StorageException;
import com.idc.idc.model.FileId;
import com.idc.idc.repository.FileIdRepository;
import com.idc.idc.service.ImageUploadService;
import com.idc.idc.settings.StorageSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobAccess;
import org.jclouds.blobstore.options.PutOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {
    private static Detector detector = new DefaultDetector(MimeTypes.getDefaultMimeTypes());

    private StorageSettings storageSettings;

    private BlobStore blobStore;

    private FileIdRepository fileIdRepository;

    private static TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

    @Autowired
    public ImageUploadServiceImpl(StorageSettings storageSettings,
                                  @Qualifier("uploadBlobStore") BlobStore blobStore,
                                  FileIdRepository fileIdRepository) {
        this.storageSettings = storageSettings;
        this.blobStore = blobStore;
        this.fileIdRepository = fileIdRepository;
    }


    private MediaType getContentType(byte[] data) {
        long startTime = System.currentTimeMillis();
        MediaType contentType = null;
        try {
            contentType = detector.detect(TikaInputStream.get(data), new Metadata());
        } catch (Exception e) {
            log.info("Failed to probe content type of byte array", e);
        }
        log.debug("Content type [{}] detection for byte[] of size [{}] took [{}] ms", contentType, data.length, System.currentTimeMillis() - startTime);

        return contentType;
    }

    private boolean isImage(byte[] data) {
        if (data == null) return false;
        MediaType contentType = getContentType(data);
        return StringUtils.equalsIgnoreCase("image", contentType.getType());
    }

    @Override
    public String getExtension(byte[] data) {
        MediaType contentType = getContentType(data);
        try {
            MimeType mimeType = tikaConfig.getMimeRepository().forName(contentType.toString());
            if (mimeType != null) {
                return mimeType.getExtension().replace(".", "");
            }
        } catch (Exception e) {
            log.info(String.format("Failed to get extension for content type [%s]", contentType), e);
        }
        return null;
    }

    @Override
    public String uploadPublicImage(byte[] data, String filePath) {
        if (!isImage(data)) {
            throw new StorageException("File is not image file");
        }
        String[] fileNameParts = filePath.split("\\.(?=[^\\.]+$)");
        String extension = FilenameUtils.getExtension(filePath);
        FileId fileId = new FileId();
        fileId = fileIdRepository.save(fileId);
        filePath = fileNameParts[0] + "-" + System.currentTimeMillis() + fileId.getId() + "." + extension;
        try {
            Blob blob = blobStore.blobBuilder(filePath)
                    .payload(data)
                    .contentLength(data.length)
                    .contentType(getContentType(data).toString())
                    .build();
            PutOptions putOptions = new PutOptions();
            putOptions.setBlobAccess(BlobAccess.PUBLIC_READ);
            blobStore.putBlob(storageSettings.getContainer(), blob, putOptions);
        } catch (Exception e) {
            throw new StorageException("File " + filePath + " uploading failed");
        }
        return getPublicFileUrl(filePath);
    }

    private String getPublicFileUrl(String filePath) {
        return blobStore.blobMetadata(storageSettings.getContainer(), filePath).getUri().toASCIIString();
    }
}
