package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppBookWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppBookWrapperRepository extends JpaRepository<LibAppBookWrapper, Long>, JpaSpecificationExecutor<LibAppBookWrapper> {
}
