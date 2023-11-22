package com.example.bank.Service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.bank.Model.Account;
import com.example.bank.Repository.AccountRepository;
import com.example.bank.Repository.HistoryRepository;
import com.example.bank.Repository.TransactionRepository;

@Service
public class AccountService {

	private AccountRepository accountRepository;

	@Autowired
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	MobileBankingUserService mobileBankingUserService;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	// to retrieve all accounts
	public Iterable<Account> allAccount() {
		return accountRepository.findAll();
	}

	// to retrieve single account
	public ResponseEntity<String> singleAccount(int accountNumber) {
		Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
		if (optionalAccount.isPresent()) {
			Account account = optionalAccount.get();
			return ResponseEntity.ok("Account details: " + account);
		} else {
			return ResponseEntity.badRequest().body("Account Number " + accountNumber + " not found in our bank !");
		}
	}

	// to delete account
	@Transactional
	public ResponseEntity<String> deleteAccount(int accountNumber) {
		if (accountRepository.existsByAccountNumber(accountNumber)) {
			accountRepository.deleteByAccountNumber(accountNumber);
			return ResponseEntity.ok("Account with Account Number " + accountNumber + " has been deleted.");
		} else {
			return ResponseEntity.badRequest().body("Account not found for deletion.");
		}
	}

	// to update account information
	public ResponseEntity<String> updateAccount(int accountNumber, Account updatedAccount) {
		Optional<Account> existingAccountOptional = accountRepository.findByAccountNumber(accountNumber);
		if (existingAccountOptional.isPresent()) {
			Account existingAccount = existingAccountOptional.get();
			existingAccount.setBalance(updatedAccount.getBalance());
			existingAccount.setAccountStatus(updatedAccount.getAccountStatus());
			Account savedAccount = accountRepository.save(existingAccount);
			return ResponseEntity.ok("Updated Account details: " + savedAccount);
		} else {
			return ResponseEntity.badRequest().body("Account Number " + accountNumber + " not found in our bank !");
		}
	}
	
	
	   public Optional<Long> a(int accountNumber) {
	        return accountRepository.findCustomerIdByAccountNumber(accountNumber);
	    }
}