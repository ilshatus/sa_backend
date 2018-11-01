package com.idc.idc.controller.driver;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.form.PostMessageForm;
import com.idc.idc.dto.json.MessageJson;
import com.idc.idc.dto.json.TaskJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Message;
import com.idc.idc.model.Task;
import com.idc.idc.model.enums.TaskStatus;
import com.idc.idc.response.Response;
import com.idc.idc.service.MessageService;
import com.idc.idc.websocket.MessageEventHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Driver.Messages"})
@RestController
@RequestMapping(DriverMessagesController.ROOT_URL)
@Slf4j
public class DriverMessagesController {
    public static final String ROOT_URL = "/v1/driver/messages";

    private MessageService messageService;

    private MessageEventHandler messageEventHandler;

    public DriverMessagesController(MessageService messageService,
                                    MessageEventHandler messageEventHandler) {
        this.messageService = messageService;
        this.messageEventHandler = messageEventHandler;
    }

    @ApiOperation("Get messages")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<MessageJson>>> getMessages(@AuthenticationPrincipal CurrentUser currentUser,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<Message> messages = messageService.getMessages(currentUser.getId(), page, limit);
            List<MessageJson> messageJsons = messages.stream()
                    .map(MessageJson::mapFromMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new Response<>(messageJsons), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("Post messages")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping
    public ResponseEntity<Response<String>> postMessage(@AuthenticationPrincipal CurrentUser currentUser,
                                                        @RequestParam String text) {
        try {
            PostMessageForm form = new PostMessageForm();
            form.setText(text);
            form.setDriverId(currentUser.getId());
            form.setIsDriverInitiator(true);
            Message message = messageService.submitMessage(form);
            messageEventHandler.sendMessage(message);
            return new ResponseEntity<>(new Response<>("success"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
