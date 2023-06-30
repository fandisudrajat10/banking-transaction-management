package com.fandi.bankingtransaction.repository;

import com.fandi.bankingtransaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE (t.customer.id = :customerId OR t.destinationCustomer.id = :customerId) " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    Page<Transaction> getTransferHistoryByCustomerIdAndDate(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
