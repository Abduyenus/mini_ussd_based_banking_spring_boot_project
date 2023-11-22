package com.example.bank.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bank.Model.MobileBankingUser;

@Repository
public interface MobileBankingUserRepository extends JpaRepository<MobileBankingUser, Long> {

	@Query(value ="SELECT * FROM mobile_banking_user WHERE cif = :cif", nativeQuery = true)
	Optional<MobileBankingUser> findByCif(Long cif);

	Optional<MobileBankingUser> findByPhoneNumber(String phoneNumber);
}