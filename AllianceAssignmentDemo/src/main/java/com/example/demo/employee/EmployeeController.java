package com.example.demo.employee;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.RecordNotFoundException;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@RestController
@Validated
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public EmployeeRepository getRepository() {
		return employeeRepository;
	}
    
    @GetMapping("/request/token/{user}")
    public ResponseEntity<String> requestToken(@PathVariable(value = "user") String firstname, HttpSession session) {
    	Employee foundUser = employeeRepository.findByFirstName(firstname);
    	String message = "No User in System";
    	if(foundUser != null) {
    		String token = UUID.randomUUID().toString();
    		LocalDateTime expired = LocalDateTime.now().plusMinutes(15);
    		User user = userRepository.findByUser(firstname);
    		
    		if(user == null) {
    			user = new User(foundUser.getFirstName(), token, expired);
    		} else {
    			if(user.getExpired().isBefore(expired))
    				user.setExpired(expired);
    		}
    		userRepository.save(user);
    		
    		message="AUTH_API_KEY = "+ token;
    	}
    		
    	return ResponseEntity.ok().body(message);
    }
    
    @GetMapping("/api/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/api/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
        		.orElseThrow(() -> new RecordNotFoundException("Employee id '" + employeeId + "' does no exist"));
        return ResponseEntity.ok().body(employee);
    }
    
    @PostMapping("/api/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/api/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
        @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepository.findById(employeeId)
        		.orElseThrow(() -> new RecordNotFoundException("Employee id '" + employeeId + "' does no exist"));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/api/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
        		.orElseThrow(() -> new RecordNotFoundException("Employee id '" + employeeId + "' does no exist"));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
