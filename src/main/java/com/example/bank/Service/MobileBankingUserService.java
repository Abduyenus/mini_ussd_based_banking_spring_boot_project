package com.example.bank.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bank.Model.Customer;
import com.example.bank.Model.History;
import com.example.bank.Model.MobileBankingUser;
import com.example.bank.Model.Transaction;
import com.example.bank.Repository.MobileBankingUserRepository;

import java.util.Optional;

@Service
public class MobileBankingUserService {
	@Autowired
	private MobileBankingUserRepository mobileBankingUserRepository;

	@Autowired
	private CustomerService customerService;

	public MobileBankingUserService(MobileBankingUserRepository mobileBankingUserRepository,
			CustomerService customerService) {
		this.mobileBankingUserRepository = mobileBankingUserRepository;
		this.customerService = customerService;
	}

	// to register new mobile banking user
	public ResponseEntity<String> registerMobileBankingUser(MobileBankingUser mobileBankingUser) {
		// Check if the CIF exists in the Customer table
		Long cif = mobileBankingUser.getCif();
		String mobileNumber = mobileBankingUser.getPhoneNumber();
		Optional<Long> customerOptional = customerService.getCustomerByCIF(cif);
	    Optional<MobileBankingUser> mobileBankingUserOptional = mobileBankingUserRepository.findByCif(cif);
        if (mobileBankingUserOptional.isPresent()) {
            // to return Mobile Banking User already exists message
            return ResponseEntity.badRequest().body("Mobile Banking User with CIF " + cif + " already exists.");
        } 
        
        // Check if the phone number exists in the Customer table
        Optional<Customer> customerByPhoneNumberOptional = customerService.getCustomerByPhoneNumber(mobileNumber);
        if (!customerByPhoneNumberOptional.isPresent()) {
            // Return an error response if the phone number already exists
            return ResponseEntity.badRequest().body("Phone number " + mobileNumber + " is not registered in our Bank.");
        }
		
		if (customerOptional.isPresent()) {
			// CIF exists, create the MobileBankingUser
			mobileBankingUserRepository.save(mobileBankingUser);
			return ResponseEntity.ok(
					"Mobile Banking User created successfully with ID: " + mobileBankingUser.getMobileBankingUser_id());
		} else {
			//if CIF does not exist
			return ResponseEntity.badRequest().body("Customer with CIF " + cif + " does not exist.");
		}
	}
	

	//to see all mobile banking users
	public Iterable<MobileBankingUser> allMobileBankingUsers() {
		return mobileBankingUserRepository.findAll();
	}

}
