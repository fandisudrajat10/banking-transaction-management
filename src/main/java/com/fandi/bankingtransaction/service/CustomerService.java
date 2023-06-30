package com.fandi.bankingtransaction.service;

import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.*;
import org.springframework.data.domain.Page;

public interface CustomerService {

    String createAccountForCustomer(User user, CustomerRegisterRequestDto requestDto);

    void updateCustomerDetail(String accountNo, CustomerUpdateRequestDto requestDto);

    MyAccountResponseDto getMyAccount();

    Page<CustomerSearchResponseDto> searchCustomer(CustomerSearchDto customerSearchDto);
}
