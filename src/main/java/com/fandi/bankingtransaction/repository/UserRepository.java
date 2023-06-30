package com.fandi.bankingtransaction.repository;

import com.fandi.bankingtransaction.domain.Role;
import com.fandi.bankingtransaction.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndActive(String email, boolean isActive);

    boolean existsByEmailAndActive(String email, boolean isActive);

    boolean existsByRoleAndActive(Role admin, boolean isActive);

    Optional<User> findByIdAndActive(Long id, boolean isActive);

    @Query(value = "SELECT * FROM users u " +
            "WHERE (:name IS NULL OR u.name LIKE %:name%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%)" +
            "AND (:active IS NULL OR u.active = :active) ",
            countQuery = "SELECT COUNT(*) FROM users u " +
                    "WHERE (:name IS NULL OR u.name LIKE %:name%) " +
                    "AND (:email IS NULL OR u.email LIKE %:email%)",
            nativeQuery = true)
    Page<User> searchUsers(@Param("name") String name, @Param("email") String email, @Param("active") Boolean active, Pageable pageable);


}
