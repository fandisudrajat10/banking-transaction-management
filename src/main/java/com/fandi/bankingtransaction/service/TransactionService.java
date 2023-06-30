package com.fandi.bankingtransaction.service;

import com.fandi.bankingtransaction.dto.TransactionHistoryRequestDto;
import com.fandi.bankingtransaction.dto.TransactionHistoryResponseDto;
import com.fandi.bankingtransaction.dto.TransferRequestDto;
import com.fandi.bankingtransaction.dto.TransferResponseDto;
import org.springframework.data.domain.Page;

public interface TransactionService {

    TransferResponseDto transfer(TransferRequestDto requestDto);

    Page<TransactionHistoryResponseDto> getHistoryTransfer(TransactionHistoryRequestDto requestDto);
}
