package com.idc.idc.repository;

import com.idc.idc.model.Message;
import com.idc.idc.model.users.Driver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getMessagesByDriver(Driver driver, Pageable pageable);
}
