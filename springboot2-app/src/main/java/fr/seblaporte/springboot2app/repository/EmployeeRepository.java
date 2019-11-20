package fr.seblaporte.springboot2app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.seblaporte.springboot2app.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
