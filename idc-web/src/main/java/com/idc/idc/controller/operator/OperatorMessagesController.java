package com.idc.idc.controller.operator;

import com.idc.idc.CurrentUser;
import com.idc.idc.dto.json.MessageJson;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Message;
import com.idc.idc.response.Response;
import com.idc.idc.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Operator.Messages"})
@RestController
@RequestMapping(OperatorMessagesController.ROOT_URL)
@Slf4j
public class OperatorMessagesController {
    public static final String ROOT_URL = "/v1/operator/messages";

    private MessageService messageService;

    public OperatorMessagesController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation("Get messages")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization header",
                    defaultValue = "%JWTTOKEN%", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    public ResponseEntity<Response<List<MessageJson>>> getMessages(@RequestParam Long driverId,
                                                                   @RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<Message> messages = messageService.getMessages(driverId, page, limit);
            List<MessageJson> messageJsons = messages.stream()
                    .map(MessageJson::mapFromMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new Response<>(messageJsons), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new Response<>(null, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
