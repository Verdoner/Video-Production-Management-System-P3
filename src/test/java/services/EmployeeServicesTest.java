package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;
import com.p3.Film.DataManager;

import models.Employee;

public class EmployeeServicesTest {
    private EmployeeServices employeeServices;

    @BeforeEach
    void setUp() {
        ApplicationController.dataManager = new DataManager(); 
        ApplicationController.objectMapper = new ObjectMapper();

        employeeServices = new EmployeeServices();
    }
    
    @Test
    void testGetAllEmployees() throws Exception {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                        new Employee("John Doe", "Johndoe@gmail.com", "Admin", "1", "password", "12345678"),
                        new Employee("Jane Doe", "Janedoe@gmail.com", "Editor", "2", "password123", "87654321")
                )
        );
        String result = employeeServices.getAllEmployees();
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("Editor"));
    }

    @Test
    void testGetAllEmployeesWhenEmployeesDoesNotExist() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        EmployeeServices employeeServices = new EmployeeServices();

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(List.of());
        String result = employeeServices.getAllEmployees();

        assertEquals("[]", result);
    }

    @Test
    void testGetLoggedInEmployee() throws Exception{
        //Mocking the list of logged-in employees
        List<Employee> loggedInEmployees = new ArrayList<>();
        Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
        loggedInEmployee.setIp("127.0.0.1");
        loggedInEmployees.add(loggedInEmployee);

        ApplicationController.loggedInEmployees = loggedInEmployees;

        //Test when the employee is logged in
        Employee retrievedEmployee = employeeServices.getLoggedInEmployee("127.0.0.1");

        assertNotNull(retrievedEmployee);
        assertEquals(loggedInEmployee, retrievedEmployee);
    }

    @Test
    void testGetLoggedInEmployeeWhenEmployeeIsNotLoggedIn() throws Exception{
        //Mocking the list of logged-in employees
        List<Employee> loggedInEmployees = new ArrayList<>();
        Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
        loggedInEmployee.setIp("127.0.0.1");
        loggedInEmployees.add(loggedInEmployee);

        ApplicationController.loggedInEmployees = loggedInEmployees;

        //Test when the employee is not logged in
        Employee retrievedEmployee = employeeServices.getLoggedInEmployee("192.168.0.1");

        assertNull(retrievedEmployee);
    }

    @Test
    void testWhenEmployeeIsLoggedIn() throws Exception {
        //Mocking the list of logged-in employees
        List<Employee> loggedInEmployees = new ArrayList<>();
        Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
        loggedInEmployee.setIp("127.0.0.1");
        loggedInEmployees.add(loggedInEmployee);

        ApplicationController.loggedInEmployees = loggedInEmployees;

        //Update the employee list in the EmployeeServices instance
        employeeServices.updateEmployeeList();

        //Test when the employee is logged in
        assertTrue(employeeServices.isLoggedIn("127.0.0.1"));
    }

    @Test
    void testWhenEmployeeIsNotLoggedIn() throws Exception {
        //Mocking the list of logged-in employees
        List<Employee> loggedInEmployees = new ArrayList<>();
        Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
        loggedInEmployee.setIp("127.0.0.1");
        loggedInEmployees.add(loggedInEmployee);

        ApplicationController.loggedInEmployees = loggedInEmployees;

        //Update the employee list in the EmployeeServices instance
        employeeServices.updateEmployeeList();

        //Test when the employee is not logged in
        assertFalse(employeeServices.isLoggedIn("192.168.0.1"));
    }

    @Test
    void testSuccesfullLogout() throws Exception {
         //Mocking the list of logged-in employees
         List<Employee> loggedInEmployees = new ArrayList<>();
         Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
         loggedInEmployee.setIp("127.0.0.1");
         loggedInEmployees.add(loggedInEmployee);
 
         ApplicationController.loggedInEmployees = loggedInEmployees;
 
         //Test successful logout
         String ip = "127.0.0.1";
         String result = employeeServices.logout(ip);
 
         assertNotNull(result);
         assertTrue(result.contains("logout successful"));
 
         //Verify that the employee is no longer in the logged-in list
         assertFalse(ApplicationController.loggedInEmployees.contains(loggedInEmployee));
    }

    @Test
    void testFailedLogout() throws Exception {
        //Mocking the list of logged-in employees
         List<Employee> loggedInEmployees = new ArrayList<>();
         Employee loggedInEmployee = new Employee("John Doe", "test@example.com", "Employee", "1", "password", "20809010");
         loggedInEmployee.setIp("127.0.0.1");
         loggedInEmployees.add(loggedInEmployee);
 
         ApplicationController.loggedInEmployees = loggedInEmployees;
         //Test logout failure (employee not found)
         String ip = "192.168.0.1";
         String result = employeeServices.logout(ip);
 
         assertNotNull(result);
         assertTrue(result.contains("logout failed"));
    }

    @Test
    void testUpdateEmployeeList() {
         //Before the update, we make sure the employees list is null
         assertEquals(null, employeeServices.employees);

         //Call the method to update the employee list
         employeeServices.updateEmployeeList();

         //After the update, we make sure the employees list is not null and contains data
         assertTrue(employeeServices.employees != null && !employeeServices.employees.isEmpty());
     }

    @Test
    void testDeleteEmployee() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of projects to be returned when the readFromJson method is called
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(new Employee("John Doe", "John@doe.com", "Admin", "1", "password", "12345678"));
        when(mockDataManager.readFromJson(eq(Employee.class), anyString())).thenReturn(mockEmployeeList);

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "1");

        String result = employeeServices.deleteEmployee(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Employee.class), anyString());
        verify(mockDataManager, times(1)).writeToJson(anyList(), anyString());

        assertEquals("\"Account deleted\"", result);
    }

    @Test
    void testDeleteEmployeeWhenEmployeeDoesNotExist() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of employees to be returned when the readFromJson method is called
        List<Employee> mockEmployeeList = new ArrayList<>();
        mockEmployeeList.add(new Employee("John Doe", "John@doe.com", "Admin", "1", "password", "12345678"));
        when(mockDataManager.readFromJson(eq(Employee.class), anyString())).thenReturn(mockEmployeeList);

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "2");

        String result = employeeServices.deleteEmployee(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Employee.class), anyString());

        assertEquals("\"Account not found\"", result);
    }

}
