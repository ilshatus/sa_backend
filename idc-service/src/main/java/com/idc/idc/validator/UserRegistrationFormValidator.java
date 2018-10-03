package com.idc.idc.validator;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.repository.UserRepository;
import com.idc.idc.response.HttpResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserRegistrationFormValidator implements Validator {
    private static final int MIN_PASSWORD_LENGTH = 6;

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationFormValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UserRegistrationForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationForm registrationForm = (UserRegistrationForm) target;
        validateEmptyParams(errors, registrationForm);
        if (!errors.hasErrors()) {
            validatePassword(errors, registrationForm);
            validateEmail(errors, registrationForm);
            validateUserExisting(errors, registrationForm);
        }
    }

    private void validatePassword(Errors errors, UserRegistrationForm form) {
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            errors.reject(UserRegistrationForm.PASSWORD_FIELD, HttpResponseStatus.INVALID_PASSWORD);
        } else if (form.getPassword().length() < MIN_PASSWORD_LENGTH) {
            errors.reject(UserRegistrationForm.PASSWORD_FIELD, HttpResponseStatus.INVALID_PASSWORD);
        }
    }

    private void validateEmail(Errors errors, UserRegistrationForm form) {
        if (!EmailValidator.getInstance().isValid(form.getEmail())) {
            errors.reject(UserRegistrationForm.EMAIL, HttpResponseStatus.INVALID_PARAM);
        }
    }

    private void validateUserExisting(Errors errors, UserRegistrationForm form) {
        if (userRepository.findOneByEmail(form.getEmail().toLowerCase()).isPresent()) {
            errors.reject(UserRegistrationForm.EMAIL, HttpResponseStatus.DUPLICATE_EMAIL);
        }
    }

    private void validateEmptyParams(Errors errors, UserRegistrationForm userRegistrationForm) {
        if (StringUtils.isBlank(userRegistrationForm.getEmail())) {
            errors.reject(UserRegistrationForm.EMAIL, HttpResponseStatus.EMPTY_PARAM);
        }
        if (StringUtils.isBlank(userRegistrationForm.getPassword())) {
            errors.reject(UserRegistrationForm.PASSWORD_FIELD, HttpResponseStatus.EMPTY_PARAM);
        }
        if (StringUtils.isBlank(userRegistrationForm.getPasswordConfirm())) {
            errors.reject(UserRegistrationForm.PASSWORD_CONFIRM_FIELD, HttpResponseStatus.EMPTY_PARAM);
        }
        if (StringUtils.isBlank(userRegistrationForm.getName())) {
            errors.reject(UserRegistrationForm.NAME, HttpResponseStatus.EMPTY_PARAM);
        }
    }
}
