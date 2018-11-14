package com.idc.idc.service;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.model.Message;
import com.idc.idc.model.Task;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;

import java.util.List;

public interface UserService {
    void registerCustomer(UserRegistrationForm userRegistrationForm);

    void registerOperator(UserRegistrationForm userRegistrationForm, byte[] image);

    void registerDriver(UserRegistrationForm userRegistrationForm, byte[] image);

    Customer getCustomer(Long customerId);

    Operator getOperator(Long operatorId);

    Driver getDriver(Long driverId);

    List<Driver> getAllDrivers();

    Customer getCustomerByEmail(String email);

    Operator getOperatorByEmail(String email);

    Driver getDriverByEmail(String email);

    Customer submitCustomer(Customer customer);

    Operator submitOperator(Operator operator);

    Driver submitDriver(Driver driver);

    void setFirebaseTokenToDriver(Long driverId, String token);

    void notifyDriver(Driver driver, Task task);

    void notifyDriver(Message message);
}
