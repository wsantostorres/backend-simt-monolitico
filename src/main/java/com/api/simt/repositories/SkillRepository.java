package com.api.simt.repositories;

import com.api.simt.models.SkillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(exported = false)
public interface SkillRepository extends JpaRepository<SkillModel, Long> {
}
