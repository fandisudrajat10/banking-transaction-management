package com.fandi.bankingtransaction.repository;

import com.fandi.bankingtransaction.domain.Customer;
import com.fandi.bankingtransaction.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Customer> findByAccountNumberAndUserActive(String accountNo, boolean active);

    Optional<Customer> findByUser(User user);


    @Query(value = "SELECT * " +
            "FROM customer c " +
            "JOIN users u ON c.user_id = u.id " +
            "WHERE (:name IS NULL OR c.customer_name LIKE %:name%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%)" +
            "AND (:accountNo IS NULL OR c.account_number LIKE %:accountNo%)" +
            "AND (:address IS NULL OR c.address LIKE %:address%)"+
            "AND (:active IS NULL OR u.active = :active) ",
            countQuery = "SELECT COUNT(*) FROM customer c " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE (:name IS NULL OR c.customer_name LIKE %:name%) " +
                    "AND (:email IS NULL OR u.email LIKE %:email%)" +
                    "AND (:accountNo IS NULL OR c.account_number LIKE %:accountNo%)" +
                    "AND (:address IS NULL OR c.address LIKE %:address%)"+
                    "AND (:active IS NULL OR u.active = :active) ",
            nativeQuery = true)
    Page<Customer> searchCustomers(@Param("name") String name,
                                   @Param("email") String email,
                                   @Param("accountNo") String accountNo,
                                   @Param("address") String address,
                                   @Param("active") Boolean active,
                                   Pageable pageable);

}
