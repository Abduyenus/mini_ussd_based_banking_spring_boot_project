package com.example.bank.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.Model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query(value = "SELECT * FROM transaction WHERE account_number = :accountNumber AND otp = :otp AND response_code = 'PENDING'", nativeQuery = true)
	Optional<Transaction> findPendingDepositTransactionWithOTP(@Param("accountNumber") int i, @Param("otp") String otp);

	@Query(value = "SELECT * FROM transaction t WHERE t.account_number = :accountNumber ORDER BY t.transaction_date DESC LIMIT 5", nativeQuery = true)
	List<Transaction> findLast5Transactions(@Param("accountNumber") int accountNumber);

	@Query(value = "SELECT account_number FROM Transaction WHERE rrn = :rrn", nativeQuery = true)
	Optional<Long> findAccountNumberByRRN(@Param("rrn") Long rrn);


}
