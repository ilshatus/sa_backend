package com.idc.idc.service.impl;

import com.idc.idc.dto.form.UserRegistrationForm;
import com.idc.idc.exception.NotFoundException;
import com.idc.idc.model.users.Customer;
import com.idc.idc.model.enums.UserRole;
import com.idc.idc.model.enums.UserState;
import com.idc.idc.model.users.Driver;
import com.idc.idc.model.users.Operator;
import com.idc.idc.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;
    private PasswordUtil passwordUtil;
    
    @Autowired
    public UserServiceImpl(CustomerRepository customerRepository,
                           PasswordUtil passwordUtil) {
        this.customerRepository = customerRepository;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public void registerCustomer(UserRegistrationForm userRegistrationForm) {
        Customer customer = userRegistrationForm.toUser();
        customer.setPasswordHash(passwordUtil.getHash(userRegistrationForm.getPassword()));
        customer.setState(UserState.REGISTERED);
        submitCustomer(customer);
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

    }

    @Override
    public void registerDriver(UserRegistrationForm userRegistrationForm) {

    }

    @Override
    public Operator getOperator(Long operatorId) {
        return null;
    }

    @Override
    public Driver getDriver(Long driverId) {
        return null;
    }

    @Override
    public Operator getOperatorByEmail(String email) {
        return null;
    }

    @Override
    public Driver getDriverByEmail(String email) {
        return null;
    }

    @Override
    public Operator submitOperator(Operator customer) {
        return null;
    }

    @Override
    public Driver submitDriver(Driver customer) {
        return null;
    }
}
