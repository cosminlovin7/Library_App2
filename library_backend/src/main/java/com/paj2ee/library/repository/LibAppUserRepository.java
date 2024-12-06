package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibAppUserRepository extends JpaRepository<LibAppUser, Long> {

	Optional<LibAppUser> findByUsername(String username);

}
