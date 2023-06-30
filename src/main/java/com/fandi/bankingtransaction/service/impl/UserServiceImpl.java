package com.fandi.bankingtransaction.service.impl;

import com.fandi.bankingtransaction.domain.Role;
import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.*;
import com.fandi.bankingtransaction.repository.UserRepository;
import com.fandi.bankingtransaction.service.CustomerService;
import com.fandi.bankingtransaction.service.UserService;
import com.fandi.bankingtransaction.service.ValidationService;
import com.fandi.bankingtransaction.util.PasswordGenerator;
import com.fandi.bankingtransaction.util.SearchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerService accountService;

    @Autowired
    private ValidationService validationService;

    @Override
    public void createFirstAdmin() {
        if (!isAdminExist()) {
            User admin = new User();
            admin.setName(System.getenv("FIRST_USER_NAME"));
            admin.setEmail(System.getenv("FIRST_USER_EMAIL"));
            admin.setPassword(BCrypt.hashpw(System.getenv("FIRST_USER_PASSWORD"), BCrypt.gensalt()));
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            admin.setActive(true);
            userRepository.save(admin);
        }
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDto) {
        validationService.validateRequest(requestDto);
        if (Role.valueOf(requestDto.getRole()).equals(Role.CUSTOMER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Role");
        }
        if (!isEmailExist(requestDto.getEmail())) {

            String password = PasswordGenerator.generateInitialPassword(8);
            User user = new User();
            user.setName(requestDto.getName());
            user.setEmail(requestDto.getEmail());
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setRole(Role.valueOf(requestDto.getRole()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setActive(true);
            userRepository.save(user);

            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setName(requestDto.getName());
            responseDto.setEmail(requestDto.getEmail());
            responseDto.setInitialPassword(password);
            responseDto.setRole(requestDto.getRole());
            return responseDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used");
        }
    }

    @Override
    public UserDetailDto getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByEmailAndActive(authentication.getName(), true);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDetailDto detailDto = new UserDetailDto();
            detailDto.setId(user.getId());
            detailDto.setName(user.getName());
            detailDto.setEmail(user.getEmail());
            detailDto.setRole(user.getRole().toString());
            return detailDto;
        } else {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Override
    public UserDetailDto getUserById(Long id) {

        Optional<User> userOptional = userRepository.findByIdAndActive(id, true);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDetailDto detailDto = new UserDetailDto();
            detailDto.setId(user.getId());
            detailDto.setName(user.getName());
            detailDto.setEmail(user.getEmail());
            detailDto.setRole(user.getRole().toString());
            return detailDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
        }
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmailAndActive(email, true);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email doesn't exist");
        }
    }

    @Override
    public CustomerRegisterResponseDto registerNewCustomer(CustomerRegisterRequestDto requestDto) {
        validationService.validateRequest(requestDto);

        if (!isEmailExist(requestDto.getEmail())) {
            String password = PasswordGenerator.generateInitialPassword(8);
            User customer = new User();
            customer.setName(requestDto.getName());
            customer.setEmail(requestDto.getEmail());
            customer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            customer.setRole(Role.CUSTOMER);
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setActive(true);

            User savedUser = userRepository.save(customer);

            String accountNumber = accountService.createAccountForCustomer(savedUser, requestDto);
            CustomerRegisterResponseDto userRegisterResponseDto = new CustomerRegisterResponseDto();
            userRegisterResponseDto.setId(savedUser.getId());
            userRegisterResponseDto.setAccountNumber(accountNumber);
            userRegisterResponseDto.setName(requestDto.getName());
            userRegisterResponseDto.setAddress(requestDto.getAddress());
            userRegisterResponseDto.setEmail(requestDto.getEmail());
            userRegisterResponseDto.setBalance(requestDto.getInitialBalance());
            userRegisterResponseDto.setInitialPassword(password);

            return userRegisterResponseDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used");
        }
    }

    @Override
    public UserDetailDto getUserByEmail(Long id) {
        Optional<User> userOptional = userRepository.findByIdAndActive(id, true);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDetailDto detailDto = new UserDetailDto();
            detailDto.setId(user.getId());
            detailDto.setName(user.getName());
            detailDto.setEmail(user.getEmail());
            detailDto.setRole(user.getRole().toString());
            return detailDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @Override
    public void updateUserByEmail(Long id, UserRequestDto requestDto) {
        validationService.validateRequest(requestDto);
        Optional<User> userOptional = userRepository.findByIdAndActive(id, true);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(requestDto.getName());
            user.setEmail(requestDto.getEmail());
            user.setRole(Role.valueOf(requestDto.getRole()));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @Override
    public void deleteUserByUserId(Long id) {
        Optional<User> userOptional = userRepository.findByIdAndActive(id, true);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @Override
    public void changeUserPassword(String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByEmail(authentication.getName());
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Override
    public Page<SearchUserResponseDto> searchUserByNameAndEmail(UserSearchDto userSearchDto) {
        Page<User> users = userRepository.searchUsers(userSearchDto.getName(), userSearchDto.getEmail(), true, SearchUtils.toPageable(userSearchDto));
        List<SearchUserResponseDto> userList = users.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(userList, users.getPageable(), users.getTotalElements());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmailAndActive(username, true);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            User user = userOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole().toString())
            );
        }
    }

    private SearchUserResponseDto toDto(User user) {
        SearchUserResponseDto responseDto = new SearchUserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setCustomerName(user.getName());
        responseDto.setCustomerEmail(user.getEmail());
        responseDto.setRole(user.getRole().toString());
        return responseDto;
    }

    private boolean isAdminExist() {
        return userRepository.existsByRoleAndActive(Role.ADMIN, true);
    }

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmailAndActive(email, true);
    }
}
