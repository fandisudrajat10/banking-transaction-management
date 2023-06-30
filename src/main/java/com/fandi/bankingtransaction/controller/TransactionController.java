package com.fandi.bankingtransaction.controller;

import com.fandi.bankingtransaction.dto.*;
import com.fandi.bankingtransaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping(path = "/transfer")
    public WebResponse<TransferResponseDto> registerUser(@RequestBody TransferRequestDto requestDto) {
        TransferResponseDto responseDto = transactionService.transfer(requestDto);
        return WebResponse.<TransferResponseDto>builder().data(responseDto).build();
    }


    @GetMapping(path = "/history")
    public WebResponse<Page<TransactionHistoryResponseDto>> getTransactionHistory(TransactionHistoryRequestDto requestDto) {
        Page<TransactionHistoryResponseDto> responseDto = transactionService.getHistoryTransfer(requestDto);
        return WebResponse.<Page<TransactionHistoryResponseDto>>builder().data(responseDto).build();
    }
}
