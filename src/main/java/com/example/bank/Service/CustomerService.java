package com.example.bank.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
//import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bank.Model.Account;
import com.example.bank.Model.Customer;
import com.example.bank.Model.History;
import com.example.bank.Model.Transaction;
import com.example.bank.Repository.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private HistoryService historyService;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	// to add new customer
	@Transactional
	public ResponseEntity<String> addNewCustomer(Customer customer) {
		// Check if the customer already exists
		Optional<Long> existingCustomer = customerRepository.findByCIF(customer.getCif());

		if (existingCustomer.isPresent()) {
			// If the customer already exists, return an error response
			return ResponseEntity.badRequest().body("Customer with CIF " + customer.getCif() + " already exists.");
		}

		// Save the new customer
		customerRepository.save(customer);
		return ResponseEntity.ok("Account creation for account successfully processed.");
	}

	// to find all customers
	public Iterable<Customer> allCustomers() {
		return customerRepository.findAll();
	}

	// to delete single customer by id
	@Transactional
	public ResponseEntity<String> deleteByCif(Long cif) {
		Optional<Customer> customerOptional = customerRepository.findByCif(cif);

		if (customerOptional.isPresent()) {
			customerRepository.deleteByCif(cif);
			return ResponseEntity.ok("Customer with CIF " + cif + " has been deleted.");
		} else {
			return ResponseEntity.badRequest().body("No customer found with CIF " + cif);
		}
	}

	// to find single customer by cif
	public ResponseEntity<?> getCustomerByCif(Long cifNumber) {
		Optional<Customer> customerOptional = customerRepository.findByCif(cifNumber);

		if (customerOptional.isPresent()) {
			return ResponseEntity.ok(customerOptional.get());
		} else {
			return ResponseEntity.badRequest().body("Customer not found with CIF: " + cifNumber);
		}
	}

	// to update customer information
	public ResponseEntity<String> updateCustomer(Long cif, Customer updatedCustomer) {
		// Check if the customer exists
		Optional<Customer> existingCustomerOptional = customerRepository.findByCif(cif);

		if (existingCustomerOptional.isPresent()) {
			// Update the customer information
			Customer existingCustomer = existingCustomerOptional.get();
			existingCustomer.setFirstName(updatedCustomer.getFirstName());
			existingCustomer.setMiddleName(updatedCustomer.getMiddleName());
			existingCustomer.setLastName(updatedCustomer.getLastName());
//	            existingCustomer.setSalutation(updatedCustomer.getSalutation());
//	            existingCustomer.setEmail(updatedCustomer.getEmail());
//	            existingCustomer.setDob(updatedCustomer.getDob());
//	            existingCustomer.setPostalAddress(updatedCustomer.getPostalAddress());
//	            existingCustomer.setCity(updatedCustomer.getCity());
//	            existingCustomer.setCountry(updatedCustomer.getCountry());
//	            existingCustomer.setHomePhone(updatedCustomer.getHomePhone());
			existingCustomer.setMobilePhone(updatedCustomer.getMobilePhone());
			// Add more fields to update as needed

			// Save the updated customer
			Customer savedCustomer = customerRepository.save(existingCustomer);

			return ResponseEntity.ok("Updated Customer details: " + savedCustomer);
		} else {
			return ResponseEntity.badRequest().body("Customer with CIF " + cif + " not found.");
		}
	}

	// to Retrieve id by phone number
	public List<Long> findCustomerIdByPhoneNumber(String phoneNumber) {
		return customerRepository.findIdByPhoneNumber(phoneNumber);
	}

	// to search customer by cif
	public Optional<Long> getCustomerByCIF(Long cif) {
		return customerRepository.findByCIF(cif);
	}

	public Optional<Customer> getCustomerByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return customerRepository.findByMobilePhone(phoneNumber);
	}
	
    public Optional<String> getMobilePhoneByCustomerId(Long customerId) {
        return customerRepository.findMobilePhoneByCustomerId(customerId);
    }
}
