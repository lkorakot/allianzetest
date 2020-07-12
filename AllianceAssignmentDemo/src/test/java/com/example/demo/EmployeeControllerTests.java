package com.example.demo;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.employee.Employee;
import com.example.demo.employee.EmployeeRepository;
import com.example.demo.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTests {

	@LocalServerPort
    private int port;
	
	//get authenticate header from property file
    @Value("${http.auth.tokenName}")
    private String authHeaderName;
    
    //this super token use for hack the api for testing propose only
    //TODO: remove this for production deploy
    @Value("${http.auth.superToken}")
    private String superToken;
    
	TestRestTemplate restTemplate = new TestRestTemplate();
    
	HttpHeaders headers = new HttpHeaders();
    
//	@MockBean
//	@Autowired
//	private EmployeeRepository employeeRepository;
//	
//	@MockBean
//	@Autowired
//    private UserRepository userRepository;

//	String authenticateKey = "AUTH_API_KEY";
//	String authorizedUser = "Jacob";
//	Employee mockEmployee = new Employee(100,"Korakot","Lee","lkorakot@Gmail.com");
	
//	@Test
//    public void testCreateStudent() throws Exception {
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//          createURLWithPort("/students"), HttpMethod.POST, entity, String.class);
//        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
//        assertTrue(actual.contains("/students"));
//    }    
	@Test
    public void AddEmployees() throws Exception {
    	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.add("AUTH_API_KEY", "ABCDEF-123456");    	
    	
    	String testEmployee = "{\n" + 
    			"    \"firstName\": \"Korakot\",\n" + 
    			"    \"lastName\": \"Lee\",\n" + 
    			"    \"emailId\": \"korakot@email.com\"\n" + 
    			"}";
    	
        HttpEntity<String> entity = new HttpEntity<String>(testEmployee, headers);
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/api/employees"), HttpMethod.POST, entity, String.class);
        
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        
        //JSONAssert.assertEquals(expected, response.getBody(), false);
    }
	
    @Test
    public void testRetrieveToken() throws Exception {
    	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.add("AUTH_API_KEY", "ABCDEF-123456"); 
    	
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
          createURLWithPort("/request/token/Korakot"), HttpMethod.GET, entity, String.class);
        
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        
        //JSONAssert.assertEquals(expected, response.getBody(), false);
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
	
	
}
