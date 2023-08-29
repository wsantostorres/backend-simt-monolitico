package com.api.simt.repositories;

import com.api.simt.models.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RepositoryRestController
public interface EmployeeRepository extends JpaRepository<EmployeeModel, Long> {
}
