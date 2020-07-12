package com.example.demo;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.employee.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntregationTests {
    
    @Autowired
	TestRestTemplate restTemplate = new TestRestTemplate();
    
	@LocalServerPort
    private int port;
	
	//***********************************************************
    //test request token
	//***********************************************************
    @Test
    public void RequestToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/request/token/Douglas");
        URI uri = new URI(baseUrl);

        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
        
        System.out.println(result.getHeaders());
        System.out.println(result.getBody());
        
        //Verify request succeed
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    
    //***********************************************************
    //test get all employee
    //***********************************************************
    @Test
    public void GetAllEmployeesWithNoToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/api/employees");
        URI uri = new URI(baseUrl);
         
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
        
        //Verify request succeed
        Assert.assertEquals(403, result.getStatusCodeValue());
    }
    
    @Test
    public void GetAllEmployeesWithToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/api/employees");
        URI uri = new URI(baseUrl);
         
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("AUTH_API_KEY", "ABCDEF-123456");
        
        ResponseEntity<String> result = new TestRestTemplate().exchange(
        		uri, HttpMethod.GET, new HttpEntity<Object>(headers),
                String.class);
        
        System.out.println(result.getHeaders());
        System.out.println(result.getBody());
        
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
    }
	
    //***********************************************************
    //test get selected employee
    //***********************************************************
    @Test
    public void GetEmployeesWithNoToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/api/employees/1");
        URI uri = new URI(baseUrl);
         
        ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
        
        //Verify request succeed
        Assert.assertEquals(403, result.getStatusCodeValue());
    }
    
    @Test
    public void GetEmployeesWithToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/api/employees/1");
        URI uri = new URI(baseUrl);
         
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("AUTH_API_KEY", "ABCDEF-123456");
        
        ResponseEntity<String> result = new TestRestTemplate().exchange(
        		uri, HttpMethod.GET, new HttpEntity<Object>(headers),
                String.class);
        
        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
    }
    
    //***********************************************************
    //test add new employee
    //***********************************************************
	@Test
    public void AddEmployeesWithNoToken() throws Exception {
    	
		final String baseUrl = createURLWithPort("/api/employees");
        URI uri = new URI(baseUrl);
        Employee employee = new Employee(10, "Korakot", "Lee", "lkorakot@gmail.com");
         
        HttpHeaders headers = getHeader();    
 
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
        
        //Verify request succeed
        Assert.assertEquals(403, result.getStatusCodeValue());
    }
	

	//***********************************************************
    //helper class
    //***********************************************************
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    private HttpHeaders getHeader() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.add("AUTH_API_KEY", "ABCDEF-123456");
    	
        return headers;
    }
}
