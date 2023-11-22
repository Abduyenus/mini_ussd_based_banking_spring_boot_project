package com.example.bank.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.Model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("SELECT a.balance FROM Account a WHERE a.accountNumber = :accountNumber")
	BigDecimal findBalanceByAccountNumber(@Param("accountNumber") int accountNumber);

	Optional<Account> findByAccountNumber(int accountNumber);

	boolean existsByAccountNumber(int accountNumber);

	void deleteByAccountNumber(int accountNumber);

	@Query(value = "SELECT a.account_number FROM Account a WHERE a.customer_id = :customerId", nativeQuery = true)
	Optional<Integer> findAccountNumberByCustomerId(@Param("customerId") Long customerId);
	
	// AccountRepository.java
	@Query(value = "SELECT customer_id FROM Account WHERE account_number = :accountNumber", nativeQuery = true)
	Optional<Long> findCustomerIdByAccountNumber(@Param("accountNumber") int accountNumber);

	
	 Optional<Long> findCustomerIdByAccountNumber(Long accountNumber);
}

