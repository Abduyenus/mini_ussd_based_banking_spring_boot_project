package com.example.bank.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Model.Transaction;
import com.example.bank.Service.TransactionService;
/*
 * 
 * 
 * 
 * 
 * **/
@RestController
@RequestMapping(path = "/api/v1/bank/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	// to show all transaction
	@GetMapping
	public Iterable<Transaction> getTransaction() {
		return transactionService.allTransaction();
	}

	// to retrieve current available balance
	@GetMapping("/balance/{accountNumber}")
	public ResponseEntity<String> getBalance(@PathVariable int accountNumber) {
		return transactionService.getBalanceByAccountNumber(accountNumber);
	}

	// to display last five transaction
	@GetMapping("/mini")
	public ResponseEntity<List<Transaction>> getLast5Transactions(@RequestParam int accountNumber) {
		return transactionService.getLast5Transactions(accountNumber);
	}

	// to deposit money
	@PostMapping("/deposit")
	public ResponseEntity<String> depositToAccount(@RequestBody Map<String, Object> requestBody) {
		int accountNumber = (int) requestBody.get("accountNumber");
		BigDecimal amount = new BigDecimal(String.valueOf(requestBody.get("amount")));
		return transactionService.depositToAccount(accountNumber, amount);
	}

	// to withdraw money
	@PostMapping("/withdraw")
	public ResponseEntity<String> withdrawFromAccount(@RequestBody Map<String, Object> requestBody) {
		int accountNumber = (int) requestBody.get("accountNumber");
		BigDecimal amount = new BigDecimal(String.valueOf(requestBody.get("amount")));
		return transactionService.withdrawFromAccount(accountNumber, amount);
	}

	// to transfer money
	@PostMapping("/transfer")
	public ResponseEntity<String> transferMoney(@RequestBody Map<String, Object> requestBody) {
		// Extract values from the requestBody map
		int senderAccountNumber = (int) requestBody.get("senderAccountNumber");
		int receiverAccountNumber = (int) requestBody.get("receiverAccountNumber");
		BigDecimal amount = new BigDecimal(String.valueOf(requestBody.get("amount")));
		return transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, amount);
	}

	// to initiate deposit with otp
	@PostMapping("/initiate-deposit")
	public ResponseEntity<String> initiateDeposit(@RequestBody Map<String, Object> requestBody) {
		// Extract values from the requestBody map
		int accountNumber = (int) requestBody.get("accountNumber");
		BigDecimal amount = new BigDecimal(String.valueOf(requestBody.get("amount")));
		return transactionService.depositWithOTP(accountNumber, amount);
	}

	// to complete deposit with OTP
	@PostMapping("/complete-deposit")
	public ResponseEntity<String> completeDepositWithOTP(@RequestBody Map<String, Object> requestBody) {
		// Extract values from the requestBody map
		String phoneNumber = (String) requestBody.get("phoneNumber");
		String otp = (String) requestBody.get("otp");
		int accountNumber = (int) requestBody.get("accountNumber");
		return transactionService.completeDepositWithOTP(phoneNumber, otp, accountNumber);
	}

	// to initiate withdrawal with OTP
	@PostMapping("/initiate-withdraw")
	public ResponseEntity<String> initiateWithdrawal(@RequestBody Map<String, Object> requestBody) {
		// Extract values from the requestBody map
		int accountNumber = (int) requestBody.get("accountNumber");
		BigDecimal amount = new BigDecimal(String.valueOf(requestBody.get("amount")));

		return transactionService.withdrawWithOTP(accountNumber, amount);
	}

	// to complete withdrawal with OTP
	@PostMapping("/complete-withdrawal")
	public ResponseEntity<String> completeWithdrawalWithOTP(@RequestBody Map<String, Object> requestBody) {
	    String phoneNumber = (String) requestBody.get("phoneNumber");
	    String otp = (String) requestBody.get("otp");
	    int accountNumber = (int) requestBody.get("accountNumber");
	    return transactionService.completeWithdrawalWithOTP(phoneNumber, otp, accountNumber);
	}


	// to buy mobile card/top up
	@GetMapping("/topup")
	public ResponseEntity<String> topUp(@RequestParam("accountNumber") int accountNumber,
			@RequestParam("paramValue") int paramValue) {
		return transactionService.topUpfetch(accountNumber, paramValue);
	}

}
