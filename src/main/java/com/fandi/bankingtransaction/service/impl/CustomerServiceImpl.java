package com.fandi.bankingtransaction.service.impl;

import com.fandi.bankingtransaction.domain.Customer;
import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.*;
import com.fandi.bankingtransaction.repository.CustomerRepository;
import com.fandi.bankingtransaction.repository.UserRepository;
import com.fandi.bankingtransaction.service.CustomerService;
import com.fandi.bankingtransaction.service.ValidationService;
import com.fandi.bankingtransaction.util.SearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    public String createAccountForCustomer(User user, CustomerRegisterRequestDto requestDto) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setAccountNumber(generateAccountNumber());
        customer.setAddress(requestDto.getAddress());
        customer.setBalance(requestDto.getInitialBalance());
        customer.setCustomerName(user.getName());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);
        return customer.getAccountNumber();
    }

    @Override
    public void updateCustomerDetail(String accountNo, CustomerUpdateRequestDto requestDto) {
        validationService.validateRequest(requestDto);
        Optional<Customer> customerOptional = customerRepository.findByAccountNumberAndUserActive(accountNo, true);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            User user = customer.getUser();
            if (user.isActive()) {
                customer.setCustomerName(requestDto.getName());
                customer.setAddress(requestDto.getAddress());
                customer.setUpdatedAt(LocalDateTime.now());
                customerRepository.save(customer);
                user.setEmail(requestDto.getEmail());
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is inactive");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Account Number");
        }
    }

    @Override
    public MyAccountResponseDto getMyAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByEmailAndActive(authentication.getName(), true);
        MyAccountResponseDto myAccountResponseDto = new MyAccountResponseDto();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Customer> customerOptional = customerRepository.findByUser(user);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                myAccountResponseDto.setAccountNumber(customer.getAccountNumber());
                myAccountResponseDto.setName(customer.getCustomerName());
                myAccountResponseDto.setAddress(customer.getAddress());
                myAccountResponseDto.setEmail(user.getEmail());
                myAccountResponseDto.setBalance(customer.getBalance());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is inactive");
            }
        }
        return myAccountResponseDto;

    }

    @Override
    public Page<CustomerSearchResponseDto> searchCustomer(CustomerSearchDto customerSearchDto) {
        Page<Customer> customers = customerRepository.searchCustomers(
                customerSearchDto.getName(),
                customerSearchDto.getEmail(),
                customerSearchDto.getAccountNo(),
                customerSearchDto.getAddress(),
                true,
                SearchUtils.toPageable(customerSearchDto));
        List<CustomerSearchResponseDto> customerList = customers.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(customerList, customers.getPageable(), customers.getTotalElements());
    }

    private CustomerSearchResponseDto toDto(Customer customer) {
        CustomerSearchResponseDto responseDto = new CustomerSearchResponseDto();
        responseDto.setId(customer.getUser().getId());
        responseDto.setAccountNo(customer.getAccountNumber());
        responseDto.setName(customer.getCustomerName());
        responseDto.setEmail(customer.getUser().getEmail());
        responseDto.setAddress(customer.getAddress());
        responseDto.setBalance(customer.getBalance());

        return responseDto;
    }

    public String generateAccountNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String currentTime = LocalDateTime.now().format(formatter);

        SecureRandom random = new SecureRandom();
        int randomSuffix = random.nextInt(1000);
        String accountNumber = currentTime + String.format("%03d", randomSuffix);

        while (accountNumberExists(accountNumber)) {
            randomSuffix = random.nextInt(1000);
            accountNumber = currentTime + String.format("%03d", randomSuffix);
        }

        return accountNumber;
    }

    private boolean accountNumberExists(String accountNumber) {
        return customerRepository.existsByAccountNumber(accountNumber);
    }
}
