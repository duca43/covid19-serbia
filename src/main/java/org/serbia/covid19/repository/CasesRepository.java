package org.serbia.covid19.repository;

import org.serbia.covid19.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasesRepository extends JpaRepository<Cases, Long>{
	Cases findTopByOrderByIdDesc();
}
