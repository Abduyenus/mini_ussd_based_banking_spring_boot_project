package com.example.bank.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bank.Model.History;
import com.example.bank.Model.Enumeration.ResponseCode;
import com.example.bank.Model.Enumeration.Side;
import com.example.bank.Model.Enumeration.TransactionCode;
import com.example.bank.Repository.HistoryRepository;

@Service
public class HistoryService {
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private AccountService accountService;

	//to get all history
	public Iterable<History> allHistory() {  
		return historyRepository.findAll();
	}
	
	//to insert history based on the passed values
    History createHistory(Long rrn, Side side, BigDecimal amount, TransactionCode transactionCode, ResponseCode responseCode) {
        History history = new History();
        history.setRrn(rrn);
        history.setTransactionCode(transactionCode);
        history.setSide(side);
        history.setAmount(amount);
        history.setResponseCode(responseCode);
        history.setTransactionDate(LocalDateTime.now());
        return historyRepository.save(history);
    }
}
