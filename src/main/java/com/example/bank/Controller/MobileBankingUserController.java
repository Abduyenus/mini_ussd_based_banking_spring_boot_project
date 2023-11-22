package com.example.bank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bank.Model.MobileBankingUser;
import com.example.bank.Service.MobileBankingUserService;

@RestController
@RequestMapping("/api/v1/bank/mobileBankingUsers")
public class MobileBankingUserController {
	@Autowired
    private MobileBankingUserService mobileBankingUserService;

	//to register new mobile banking user
    @PostMapping("/register")
    public ResponseEntity<String> registerMobileBankingUser(@RequestBody MobileBankingUser mobileBankingUser) {
        return mobileBankingUserService.registerMobileBankingUser(mobileBankingUser);
    }
    
    //to display all mobile banking users
	@GetMapping
	public Iterable<MobileBankingUser> getMobileBankingUsers() {
		return mobileBankingUserService.allMobileBankingUsers();
	}
	
}
