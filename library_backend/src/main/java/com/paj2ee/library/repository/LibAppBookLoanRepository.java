package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppBookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppBookLoanRepository extends JpaRepository<LibAppBookLoan, Long> {
}
