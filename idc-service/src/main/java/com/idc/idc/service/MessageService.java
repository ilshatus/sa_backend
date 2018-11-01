package com.idc.idc.service;

import com.idc.idc.dto.form.PostMessageForm;
import com.idc.idc.model.Message;
import com.idc.idc.model.users.Driver;

import java.util.List;

public interface MessageService {
    List<Message> getMessages(Long driverId, Integer page, Integer limit);
    void readMessages(List<Long> messageIds);
    Message submitMessage(Message message);
    Message submitMessage(PostMessageForm form);
    List<Driver> getDriversWithUnreadMessages();
}
