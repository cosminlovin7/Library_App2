package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppUserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppUserAuthorityRepository extends JpaRepository<LibAppUserAuthority, Long> {
}
