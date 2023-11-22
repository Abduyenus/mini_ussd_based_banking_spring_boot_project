package com.example.bank.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.Model.Customer;
import com.example.bank.Service.CustomerService;

@RestController
@RequestMapping(path = "/api/v1/bank")
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	public CustomerController() {

	}

	// to add new customer
	@PostMapping("/addCustomer")
	public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
		return customerService.addNewCustomer(customer);
	}

	// to retrieve all customers
	@GetMapping("/customers")
	public Iterable<Customer> getAllCustomers() {
		return customerService.allCustomers();
	}

	// to delete single customer
	@DeleteMapping("/customers/{cif}")
	public ResponseEntity<String> deleteCustomerByCif(@PathVariable Long cif) {
		return customerService.deleteByCif(cif);
	}

	// to retrieve single customer
	@GetMapping("/customers/{cif}")
	public ResponseEntity<?> getCustomer(@PathVariable Long cif) {
		return customerService.getCustomerByCif(cif);
	}

	// to update customer information
	@PutMapping("/customers/{cif}")
	public ResponseEntity<String> updateCustomer(@PathVariable Long cif, @RequestBody Customer updatedCustomer) {
		return customerService.updateCustomer(cif, updatedCustomer);
	}

}