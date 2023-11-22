package com.example.bank.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.Model.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
	
	@Query("SELECT c.cif FROM Customer c WHERE c.cif = :cif")
	Optional<Long> findByCIF(Long cif);

    @Query(value = "SELECT id FROM Customer WHERE mobile_phone = :phoneNumber", nativeQuery = true)
    List<Long> findIdByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	
    Optional<Customer> findByCif(Long cifNumber);
    
    void deleteByCif(Long cif);
    
    Optional<Customer> findByMobilePhone(String mobilePhone);

    @Query(value = "SELECT mobile_phone FROM Customer WHERE id = :customerId", nativeQuery = true)
    Optional<String> findMobilePhoneByCustomerId(@Param("customerId") Long customerId);

}