package com.api.simt.repositories;

import com.api.simt.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestResource(exported = false)
public interface CourseRepository extends JpaRepository<CourseModel, Long> {
}
