package com.fandi.bankingtransaction.controller;

import com.fandi.bankingtransaction.dto.*;
import com.fandi.bankingtransaction.service.AccountService;
import com.fandi.bankingtransaction.service.CustomerService;
import com.fandi.bankingtransaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/my-profile")
    public WebResponse<UserDetailDto> getMyProfile() {
        UserDetailDto responseDto = userService.getMyProfile();
        return WebResponse.<UserDetailDto>builder().data(responseDto).build();
    }

    @GetMapping("/my-account")
    public WebResponse<MyAccountResponseDto> getMyAccount() {
        MyAccountResponseDto responseDto = customerService.getMyAccount();
        return WebResponse.<MyAccountResponseDto>builder().data(responseDto).build();
    }

    @PostMapping(path = "/change-password")
    public WebResponse<String> changePasswordUser(@RequestBody UserUpdatePasswordRequestDto requestDto) {
        accountService.changePassword(requestDto);
        return WebResponse.<String>builder().data("Password successfully updated").build();
    }

    @PostMapping(path = "/customer-register")
    public WebResponse<CustomerRegisterResponseDto> registerCustomer(@RequestBody CustomerRegisterRequestDto requestDto) {
        CustomerRegisterResponseDto responseDto = userService.registerNewCustomer(requestDto);
        return WebResponse.<CustomerRegisterResponseDto>builder().data(responseDto).build();
    }

    @PutMapping(path = "/customer-update/{accountNo}")
    public WebResponse<String> updateCustomer(@PathVariable String  accountNo, @RequestBody CustomerUpdateRequestDto requestDto) {
        customerService.updateCustomerDetail(accountNo, requestDto);
        return WebResponse.<String>builder().data("Customer Detail successfully updated").build();
    }


    @DeleteMapping(path = "/delete/{id}")
    public WebResponse<String> updateCustomer(@PathVariable Long  id) {
        userService.deleteUserByUserId(id);
        return WebResponse.<String>builder().data("Customer Detail successfully deleted").build();
    }


    @GetMapping(path = "/customer-search")
    public WebResponse<Page<CustomerSearchResponseDto>> searchCustomer(CustomerSearchDto customerSearchDto) {
        Page<CustomerSearchResponseDto> responseDtoPage = customerService.searchCustomer(customerSearchDto);
        return WebResponse.<Page<CustomerSearchResponseDto>>builder().data(responseDtoPage).build();
    }
}
