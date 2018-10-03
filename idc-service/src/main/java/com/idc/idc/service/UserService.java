package com.idc.idc.service;

import com.google.inject.ImplementedBy;
import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.model.User;

public interface UserService {
    void registerUser(UserRegistrationForm userRegistrationForm);

    User getUser(Long userId);

    User getUserByEmail(String email);

    User submitUser(User user);
}
