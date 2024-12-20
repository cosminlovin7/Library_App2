package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppBookRepository extends JpaRepository<LibAppBook, Long> {
}
