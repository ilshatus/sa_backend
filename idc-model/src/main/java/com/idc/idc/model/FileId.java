package com.idc.idc.model;

import com.idc.idc.model.abstracts.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "file_ids")
public class FileId extends AbstractEntity {
}
