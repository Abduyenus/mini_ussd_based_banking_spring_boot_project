package com.example.bank.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Model.Account;
import com.example.bank.Service.AccountService;

@RestController
@RequestMapping(path = "/api/v1/bank/accounts")
public class AccountController {
	private AccountService accountService;

	@Autowired
	public void setAccountService(AccountService accountService) {
	    this.accountService = accountService;
	}


	@Autowired
	public AccountController(AccountService accountService) {
	    this.accountService = accountService;
	}

	// to retrieve all Account
	@GetMapping
	public Iterable<Account> getAllAccount() {
		return (Iterable<Account>) accountService.allAccount();
	}

	// to retrieve single account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<String> getSingleAccountDetails(@PathVariable int accountNumber) {
        return accountService.singleAccount(accountNumber);
    }

	// to delete single account
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable int accountNumber) {
        return accountService.deleteAccount(accountNumber);
    }
	
	// to update account information
    @PutMapping("/{accountNumber}")
    public ResponseEntity<String> updateAccount(@PathVariable int accountNumber, @RequestBody Account updatedAccount) {
        return accountService.updateAccount(accountNumber, updatedAccount);
    }
	
}
