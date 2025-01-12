package com.paj2ee.library.repository;

import com.paj2ee.library.model.LibAppBookCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibAppBookCollectionRepository extends JpaRepository<LibAppBookCollection, Long> {

	LibAppBookCollection findByName(String name);

}
