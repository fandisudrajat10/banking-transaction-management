package com.fandi.bankingtransaction.service.impl;

import com.fandi.bankingtransaction.domain.Customer;
import com.fandi.bankingtransaction.domain.Transaction;
import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.TransactionHistoryRequestDto;
import com.fandi.bankingtransaction.dto.TransactionHistoryResponseDto;
import com.fandi.bankingtransaction.dto.TransferRequestDto;
import com.fandi.bankingtransaction.dto.TransferResponseDto;
import com.fandi.bankingtransaction.repository.CustomerRepository;
import com.fandi.bankingtransaction.repository.TransactionRepository;
import com.fandi.bankingtransaction.service.TransactionService;
import com.fandi.bankingtransaction.service.UserService;
import com.fandi.bankingtransaction.util.SearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {


    private static final int MAX_DAY_RANGE = 7;
    private static final LocalTime START_TIME = LocalTime.of(0, 0, 1);
    private static final LocalTime END_TIME = LocalTime.of(23, 59, 59);


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Override
    public TransferResponseDto transfer(TransferRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Customer destinationCustomer = customerRepository.findByAccountNumberAndUserActive(requestDto.getDestinationAccountNo(), true)
                .orElseThrow(() -> new IllegalArgumentException("Destination customer not found"));

        BigDecimal customerBalance = customer.getBalance();
        if (customerBalance.compareTo(requestDto.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        customer.setBalance(customerBalance.subtract(requestDto.getAmount()));
        customer.setUpdatedAt(LocalDateTime.now());
        destinationCustomer.setBalance(destinationCustomer.getBalance().add(requestDto.getAmount()));
        destinationCustomer.setUpdatedAt(LocalDateTime.now());

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setDestinationCustomer(destinationCustomer);
        transaction.setTransactionType("Transfer");
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        TransferResponseDto responseDto = new TransferResponseDto();
        responseDto.setAccountNo(customer.getAccountNumber());
        responseDto.setDestinationAccountNo(requestDto.getDestinationAccountNo());
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setStatus("Success");
        return responseDto;

    }

    @Override
    public Page<TransactionHistoryResponseDto> getHistoryTransfer(TransactionHistoryRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        Optional<Customer> customerOptional = customerRepository.findByUser(user);
        if (!customerOptional.isPresent()){
            throw new IllegalArgumentException("Invalid User.");
        }
        Customer customer = customerOptional.get();
        LocalDateTime startDate = parseStartDate(requestDto.getStartDate());
        LocalDateTime endDate = parseEndDate(requestDto.getEndDate());

        validateDateRange(startDate, endDate);

        Page<Transaction> transactionPage = transactionRepository.getTransferHistoryByCustomerIdAndDate(customer.getId(), startDate, endDate, SearchUtils.toPageable(requestDto));

        List<TransactionHistoryResponseDto> responseDtoList = transactionPage.getContent().stream()
                .map(this::mapTransactionToResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDtoList, transactionPage.getPageable(), transactionPage.getTotalElements());
    }

    private LocalDateTime parseStartDate(String startDate) {
        if (startDate == null || startDate.isEmpty()) {
            return LocalDateTime.now().with(START_TIME);
        }

        try {
            LocalDate date = LocalDate.parse(startDate);
            return LocalDateTime.of(date, START_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid startDate format. Please use 'yyyy-MM-dd' format.", e);
        }
    }

    private LocalDateTime parseEndDate(String endDate) {
        if (endDate == null || endDate.isEmpty()) {
            return LocalDateTime.now().with(END_TIME);
        }

        try {
            LocalDate date = LocalDate.parse(endDate);
            return LocalDateTime.of(date, END_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid endDate format. Please use 'yyyy-MM-dd' format.", e);
        }
    }
    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than start date.");
        }

        long dayRange = ChronoUnit.DAYS.between(startDate, endDate);
        if (dayRange > MAX_DAY_RANGE) {
            throw new IllegalArgumentException("Date range cannot exceed 7 days.");
        }
    }
    private TransactionHistoryResponseDto mapTransactionToResponseDto(Transaction transaction) {
        TransactionHistoryResponseDto responseDto = new TransactionHistoryResponseDto();
        responseDto.setDestinationAccount(transaction.getDestinationCustomer().getAccountNumber());
        responseDto.setAmount(transaction.getAmount());
        responseDto.setTransactionDate(transaction.getTransactionDate().toString());
        return responseDto;
    }
}
