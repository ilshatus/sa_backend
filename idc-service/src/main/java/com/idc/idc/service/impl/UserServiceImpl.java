package com.idc.idc.service.impl;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.User;
import com.idc.idc.model.enums.UserRole;
import com.idc.idc.model.enums.UserState;
import com.idc.idc.repository.UserRepository;
import com.idc.idc.service.UserService;
import com.idc.idc.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordUtil passwordUtil;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public void registerUser(UserRegistrationForm userRegistrationForm) {
        User user = userRegistrationForm.toUser();
        user.setPasswordHash(passwordUtil.getHash(userRegistrationForm.getPassword()));
        user.setState(UserState.REGISTERED);
        user.setRole(UserRole.CUSTOMER);
        submitUser(user);
    }

    @Override
    public User getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        Optional<User> oneById = userRepository.findOneById(userId);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("User %d not found", userId)));
    }

    @Override
    public User submitUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.orElseThrow(() -> new NotFoundException("User not found by email " + email));
    }
}
