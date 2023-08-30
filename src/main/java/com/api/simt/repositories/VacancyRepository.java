package com.api.simt.repositories;

import com.api.simt.models.VacancyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(exported = false)
public interface VacancyRepository extends JpaRepository<VacancyModel, Long> {

}
