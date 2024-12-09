package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppFileRepository extends JpaRepository<LibAppFile, Long> {
}