package com.example.bank.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.Model.History;
@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query(value = "SELECT * FROM history WHERE rrn = :rrn", nativeQuery = true)
	Optional<History> findByRRN(@Param("rrn") Long rrn);

}
