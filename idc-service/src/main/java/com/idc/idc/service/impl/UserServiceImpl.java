package com.idc.idc.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.exception.RegistrationException;
import com.idc.idc.model.Task;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;
import com.idc.idc.repository.CustomerRepository;
import com.idc.idc.repository.DriverRepository;
import com.idc.idc.repository.OperatorRepository;
import com.idc.idc.service.UserService;
import com.idc.idc.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private CustomerRepository customerRepository;
    private DriverRepository driverRepository;
    private OperatorRepository operatorRepository;
    private PasswordUtil passwordUtil;
    private FirebaseMessaging firebaseMessaging;
    
    @Autowired
    public UserServiceImpl(CustomerRepository customerRepository,
                           DriverRepository driverRepository,
                           OperatorRepository operatorRepository,
                           PasswordUtil passwordUtil,
                           FirebaseMessaging firebaseMessaging) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.operatorRepository = operatorRepository;
        this.passwordUtil = passwordUtil;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void registerCustomer(UserRegistrationForm userRegistrationForm) {
        Customer customer = userRegistrationForm.toCustomer();
        try {
            getCustomerByEmail(customer.getEmail());
        } catch (NotFoundException e) {
            customer.setPasswordHash(passwordUtil.getHash(userRegistrationForm.getPassword()));
            customer.setEmailConfirmed(false);
            submitCustomer(customer);
            return;
        }
        throw new RegistrationException(String.format("Customer with email %s already exists", customer.getEmail()));
    }

    @Override
    public Customer getCustomer(Long customerId) {
        if (customerId == null) {
            return null;
        }
        Optional<Customer> oneById = customerRepository.findOneById(customerId);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("Customer %d not found", customerId)));
    }

    @Override
    public Customer submitCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        Optional<Customer> oneByEmail = customerRepository.findOneByEmail(email);
        return oneByEmail.orElseThrow(() -> new NotFoundException("Customer not found by email " + email));
    }

    @Override
    public void registerOperator(UserRegistrationForm userRegistrationForm) {
        Operator operator = userRegistrationForm.toOperator();
        try {
            getOperatorByEmail(operator.getEmail());
        } catch (NotFoundException e) {
            operator.setPasswordHash(passwordUtil.getHash(userRegistrationForm.getPassword()));
            submitOperator(operator);
            return;
        }
        throw new RegistrationException(String.format("Operator with email %s already exists", operator.getEmail()));
    }

    @Override
    public void registerDriver(UserRegistrationForm userRegistrationForm) {
        Driver driver = userRegistrationForm.toDriver();
        try {
            getDriverByEmail(driver.getEmail());
        } catch (NotFoundException e) {
            driver.setPasswordHash(passwordUtil.getHash(userRegistrationForm.getPassword()));
            submitDriver(driver);
            return;
        }
        throw new RegistrationException(String.format("Driver with email %s already exists", driver.getEmail()));
    }

    @Override
    public Operator getOperator(Long operatorId) {
        if (operatorId == null) {
            return null;
        }
        Optional<Operator> oneById = operatorRepository.findOneById(operatorId);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("Operator %d not found", operatorId)));
    }

    @Override
    public Driver getDriver(Long driverId) {
        if (driverId == null) {
            return null;
        }
        Optional<Driver> oneById = driverRepository.findOneById(driverId);
        return oneById.orElseThrow(() -> new NotFoundException(String.format("Driver %d not found", driverId)));
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Operator getOperatorByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        Optional<Operator> oneByEmail = operatorRepository.findOneByEmail(email);
        return oneByEmail.orElseThrow(() -> new NotFoundException("Operator not found by email " + email));
    }

    @Override
    public Driver getDriverByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        Optional<Driver> oneByEmail = driverRepository.findOneByEmail(email);
        return oneByEmail.orElseThrow(() -> new NotFoundException("Driver not found by email " + email));
    }


    @Override
    public Operator submitOperator(Operator operator) {
        return operatorRepository.save(operator);
    }

    @Override
    public Driver submitDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void setFirebaseTokenToDriver(Long driverId, String token) {
        Driver driver = getDriver(driverId);
        driver.setFirebaseToken(token);
        submitDriver(driver);
    }

    @Override
    public void notifyDriver(Driver driver, Task task) {
        if (StringUtils.isBlank(driver.getFirebaseToken())) {
            log.info("Driver {} hasn't firebase token", driver.getId());
            return;
        }
        Message message = Message.builder()
                .setToken(driver.getFirebaseToken())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .putData("type", "task")
                .putData("id", task.getId().toString())
                .build();
        try {
            firebaseMessaging.send(message);
            log.info("Notification successfully sent");
        } catch (FirebaseMessagingException e) {
            log.info("Failed to send text to driver {}", driver.getId());
        }
    }

    @Override
    public void notifyDriver(com.idc.idc.model.Message message) {
        Driver driver = message.getDriver();
        if (StringUtils.isBlank(driver.getFirebaseToken())) {
            log.info("Driver {} hasn't firebase token", driver.getId());
            return;
        }
        Message firebaseMessage = Message.builder()
                .setToken(driver.getFirebaseToken())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .putData("type", "message")
                .putData("operator_name", message.getOperator().getName())
                .putData("text", message.getText())
                .putData("posted_date", String.valueOf(message.getPostedDate().getTime()))
                .build();
        try {
            firebaseMessaging.send(firebaseMessage);
            log.info("Notification successfully sent");
        } catch (FirebaseMessagingException e) {
            log.info("Failed to send text to driver {}", driver.getId());
        }
    }
}
