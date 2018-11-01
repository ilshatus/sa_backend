package com.idc.idc.websocket;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;

import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.idc.idc.User;
import com.idc.idc.UserType;
import com.idc.idc.dto.form.PostMessageForm;
import com.idc.idc.dto.json.MessageJson;
import com.idc.idc.exception.AuthTokenParseException;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.Message;
import com.idc.idc.service.AuthTokenService;
import com.idc.idc.service.MessageService;
import com.idc.idc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class MessageEventHandler {
    private SocketIOServer server;
    private Map<UUID, Long> operators;
    private MessageService messageService;
    private UserService userService;
    private AuthTokenService authTokenService;

    @Autowired
    public MessageEventHandler(SocketIOServer server,
                               MessageService messageService,
                               UserService userService,
                               AuthTokenService authTokenService) {
        this.server = server;
        this.operators = new HashMap<>();
        this.messageService = messageService;
        this.userService = userService;
        this.authTokenService = authTokenService;
        server.addEventListener("post_message", PostMessageForm.class, this::chatEvent);
        server.addEventListener("auth", Token.class, this::authorizeEvent);
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("Client[{}] - Connected to chat module",
                client.getSessionId().toString());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        operators.remove(client.getSessionId());
        log.info("Client[{}] - Disconnected from chat module",
                client.getSessionId().toString());
    }

    private void authorizeEvent(SocketIOClient client, Token token, AckRequest request) {
        if (token.getToken() == null) {
            request.sendAckData("Auth failed, disconnecting..");
        }
        try {
            User user = authTokenService.getUserId(token.getToken());
            if (user.getUserType().equals(UserType.OPERATOR)) {
                operators.put(client.getSessionId(), user.getId());
                request.sendAckData("Auth successful");
            } else {
                throw new AuthTokenParseException("Wrong user type");
            }
        } catch (AuthTokenParseException e) {
            log.warn("Client[{}] {}", client.getSessionId(), e.getMessage());
            request.sendAckData("Auth failed, disconnecting..");
            client.disconnect();
        }
    }

    private void chatEvent(SocketIOClient client, PostMessageForm form, AckRequest request) {
        if (form.getText() == null ||
                form.getDriverId() == null ||
                form.getOperatorId() == null) {
            log.warn("Client[{}] validation error", client.getSessionId());
            request.sendAckData("Not valid message");
            return;
        }
        Long operatorId = operators.get(client.getSessionId());
        if (operatorId == null || !operatorId.equals(form.getOperatorId())) {
            log.warn("Client[{}] not authorized", client.getSessionId());
            request.sendAckData("Not authenticated, disconnecting..");
            client.disconnect();
            return;
        }
        try {
            form.setIsDriverInitiator(false);
            Message message = messageService.submitMessage(form);
            userService.notifyDriver(message);
            server.getBroadcastOperations().sendEvent("get_message", MessageJson.mapFromMessage(message));
            request.sendAckData("Message successfully send");
        } catch (NotFoundException e) {
            log.warn("Client[{}] validation error", client.getSessionId());
            request.sendAckData("Not valid message");
        }
    }

    public void sendMessage(Message message) {
        server.getBroadcastOperations().sendEvent("get_message", MessageJson.mapFromMessage(message));
    }
}
