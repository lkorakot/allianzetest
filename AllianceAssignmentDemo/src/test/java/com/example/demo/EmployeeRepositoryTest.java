package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.employee.Employee;
import com.example.demo.employee.EmployeeRepository;

@RunWith(SpringRunner.class)
public class EmployeeRepositoryTest {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    //***********************************************************
    //test additional query
	//***********************************************************
    @Test
    public void testFindByName() {
        Employee employee = employeeRepository.findByFirstName("Douglas");
        //Assert
        assertThat(employee.getFirstName()).isEqualTo("Douglas");
    }
}
