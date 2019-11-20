package fr.seblaporte.springboot2app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import fr.seblaporte.springboot2app.model.Employee;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(
		classes = Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
		scripts = {"classpath:schema-h2.sql", "classpath:data-h2.sql"},
		config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED),
		executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void should_get_all_employees() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

	@Test
	public void should_get_employee_by_id() {
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/1", Employee.class);
		assertNotNull(employee);
	}

	@Test
	public void should_create_employee_successfully() {
		Employee employee = new Employee();
		employee.setEmail("admin@gmail.com");
		employee.setFirstName("admin");
		employee.setLastName("admin");

		ResponseEntity<Employee> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee, Employee.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
	}

	@Test
	public void should_update_employee_successfully() {
		int id = 1;
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		employee.setFirstName("admin1");
		employee.setLastName("admin2");

		restTemplate.put(getRootUrl() + "/employees/" + id, employee);

		Employee updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		assertNotNull(updatedEmployee);
	}

	@Test
	public void should_delete_employee_successfully() {
		int id = 1;
		Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		assertNotNull(employee);

		restTemplate.delete(getRootUrl() + "/employees/" + id);

		try {
			employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
		} catch (final HttpClientErrorException e) {
			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}
