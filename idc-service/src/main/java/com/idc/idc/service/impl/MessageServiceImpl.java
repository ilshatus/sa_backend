package com.idc.idc.service.impl;

import com.idc.idc.dto.form.PostMessageForm;
import com.idc.idc.model.Message;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;
import com.idc.idc.repository.MessageRepository;
import com.idc.idc.service.MessageService;
import com.idc.idc.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserService userService;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public List<Message> getMessages(Long driverId, Integer page, Integer limit) {
        Driver driver = userService.getDriver(driverId);
        PageRequest pageRequest = new PageRequest(page, limit, Sort.Direction.DESC, "postedDate");
        return messageRepository.getMessagesByDriver(driver, pageRequest);
    }

    @Override
    public void readMessages(List<Long> messageIds) {
        List<Message> messages = messageRepository.findAll(messageIds);
        for (Message message : messages) message.setIsRead(true);
        messageRepository.save(messages);
    }

    @Override
    public Message submitMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message submitMessage(PostMessageForm form) {
        Message message = form.toMessage();
        Driver driver = userService.getDriver(form.getDriverId());
        Operator operator = userService.getOperator(form.getOperatorId());
        message.setDriver(driver);
        message.setOperator(operator);
        return submitMessage(message);
    }

    @Override
    public List<Driver> getDriversWithUnreadMessages() {
        return null;
    }
}
