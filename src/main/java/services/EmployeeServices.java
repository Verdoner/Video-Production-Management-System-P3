package services;

import models.Employee;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;

public class EmployeeServices {
    public List<Employee> employees;

    /**
     * Updates the list of employees from file: accounts.json
     */
    public void updateEmployeeList() {
        employees = ApplicationController.dataManager.readFromJson(Employee.class, "src/main/resources/data/accounts.json");
    }

    //Returns a list of all employees.
    public String getAllEmployees() throws JsonProcessingException {
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        return ApplicationController.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeList);
        //return ApplicationController.objectMapper.writeValueAsString(employeeList); // this is the non-readable version
    }

    /**
     * Returns a single employee by id.
     * 
     * @param data The JSON object containing the employee id.
     * @return The JSON representation of the employee if found, otherwise "Employee not found".
     * @throws JsonProcessingException if there is an error processing the JSON.
     */
    public String getEmployee(ObjectNode data) throws JsonProcessingException {
        String id = data.get("id").asText();
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        for (Employee employee : employeeList) {
            if(employee.getId().equals(id)){
                return ApplicationController.objectMapper.writeValueAsString(employee);
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("Employee not found");
    }

    /**
     * Logs in the employee with the provided credentials.
     * 
     * @param data The JSON object containing the username and password.
     * @param ip The IP address of the client.
     * @return The JSON representation of the logged-in employee if successful, otherwise "login failed".
     * @throws JsonProcessingException if there is an error processing the JSON.
     */
    public String login(ObjectNode data, String ip) throws JsonProcessingException{
        updateEmployeeList();
        String username = data.get("username").asText();
        String password = data.get("password").asText();
        for(Employee employee : employees){
            if(employee.getEmail().equals(username) && employee.getPassword().equals(password)){
                if(!ApplicationController.loggedInEmployees.isEmpty()){
                    for(int i = 0; i < ApplicationController.loggedInEmployees.size(); i++){
                        if(ApplicationController.loggedInEmployees.get(i).getIp().equals(ip)){
                            ApplicationController.loggedInEmployees.remove(i);
                            ApplicationController.loggedInEmployees.add(employee);
                            employee.setIp(ip);
                            break;
                        }
                    }
                    ApplicationController.loggedInEmployees.add(employee);
                    employee.setIp(ip);
                }
                else{
                    ApplicationController.loggedInEmployees.add(employee);
                    employee.setIp(ip);
                }
                
                System.out.println("User with id: " + employee.getId() + " logged in successfully");
                System.out.println("this employee logged in: " + employee);
                return ApplicationController.objectMapper.writeValueAsString(employee);
            }
        }
        System.out.println("login failed");
        return ApplicationController.objectMapper.writeValueAsString("login failed");
    }

    /**
     * Checks if an employee is currently logged in based on their IP address.
     * 
     * @param ip the IP address of the employee
     * @return true if the employee is logged in, false otherwise
     */
    public boolean isLoggedIn(String ip){
        for(Employee employees : ApplicationController.loggedInEmployees){
            if(employees.getIp().equals(ip)){
                return true;
            }
        }
        return false;
    }

    /**
     * Logs out an employee based on their IP address.
     * 
     * @param ip the IP address of the employee
     * @return a JSON string indicating the status of the logout operation
     * @throws JsonProcessingException if there is an error processing JSON
     */
    public String logout(String ip) throws JsonProcessingException{
        for(Employee employees : ApplicationController.loggedInEmployees){
            if(employees.getIp().equals(ip)){
                ApplicationController.loggedInEmployees.remove(employees);
                return ApplicationController.objectMapper.writeValueAsString("logout successful");
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("logout failed");
    }
    
    /**
     * Retrieves the logged-in employee based on the provided IP address.
     *
     * @param ip The IP address of the employee.
     * @return The logged-in employee with the matching IP address, or null if not found.
     */
    public Employee getLoggedInEmployee(String ip){
        for(Employee employees : ApplicationController.loggedInEmployees){
            if(employees.getIp().equals(ip)){
                return employees;
            }
        }
        return null;
    }

    /**
     * Returns a list of all employees sorted by name.
     * 
     * @param ip The IP address of the logged-in employee.
     * @return A JSON string containing the list of employees sorted by name.
     * @throws JsonProcessingException if there is an error processing JSON
     */

    public String recentAccounts(String ip) throws JsonProcessingException{
        Employee loggedInE = getLoggedInEmployee(ip);

        if (loggedInE.getRole().equals("admin")) {
            String returnString = "";
            List<Employee> accountList = ApplicationController.dataManager.readFromJson(Employee.class, "src/main/resources/data/accounts.json");

            accountList.sort(Comparator.comparing(Employee::getName));
            returnString = ApplicationController.objectMapper.writeValueAsString(accountList);

            return returnString;
        } else {
            return ApplicationController.objectMapper.writeValueAsString("Not an admin");
        }
    }
  
    /**
     * Returns a specific employee.
     * @param ip
     * @return
     * @throws JsonProcessingException
     */
    public String getProfile(String ip) throws JsonProcessingException{
        Employee employee = getLoggedInEmployee(ip);
        return ApplicationController.objectMapper.writeValueAsString(employee);
    }

    /**
     * Updates the profile for the logged-in employee.
     * @param ip
     * @param data
     * @return
     * @throws JsonProcessingException
     */
    public String updateProfile(String ip, ObjectNode data) throws JsonProcessingException{
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        Employee loggedInEmployee = getLoggedInEmployee(ip);
        for(Employee employee : employeeList){
            if(employee.getId().equals(loggedInEmployee.getId())){
                employee.setName(data.get("name").asText());
                employee.setEmail(data.get("email").asText());
                employee.setPhone(data.get("phone").asText());
                ApplicationController.dataManager.writeToJson(employeeList, "src/main/resources/data/accounts.json");
                ApplicationController.loggedInEmployees.remove(loggedInEmployee);
                employee.setIp(ip);
                ApplicationController.loggedInEmployees.add(employee);
                return ApplicationController.objectMapper.writeValueAsString("update profile sucessful");
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("update profile failed");
    }


    /**
     * Updates an employee.
     * 
     * @param ip The IP address of the logged-in employee.
     * @param data The JSON object containing the new role.
     * @return A JSON string indicating the success or failure of the role update.
     * @throws JsonProcessingException If there is an error processing the JSON data.
     */
    public String updateEmployee(ObjectNode data) throws JsonProcessingException{
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        for(Employee employee : employeeList){
            if(employee.getId().equals(data.get("id").asText())){
                employee.setName(data.get("name").asText());
                employee.setRole(data.get("role").asText());
                employee.setPhone(data.get("phone").asText());
                employee.setEmail(data.get("email").asText());
                ApplicationController.dataManager.writeToJson(employeeList, "src/main/resources/data/accounts.json");
                return ApplicationController.objectMapper.writeValueAsString("update profile role sucessful");
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("update profile role failed");
    }

    /**
     * Updates the profile password for the logged-in employee.
     * 
     * @param ip The IP address of the logged-in employee.
     * @param data The JSON object containing the new password.
     * @return A JSON string indicating the success or failure of the password update.
     * @throws JsonProcessingException If there is an error processing the JSON data.
     */
    public String updateProfilePassword(String ip, ObjectNode data) throws JsonProcessingException{
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        Employee loggedInEmployee = getLoggedInEmployee(ip);
        for(Employee employee : employeeList){
            if(employee.getId().equals(loggedInEmployee.getId())){
                employee.setPassword(data.get("password").asText());
                ApplicationController.dataManager.writeToJson(employeeList, "src/main/resources/data/accounts.json");
                ApplicationController.loggedInEmployees.remove(loggedInEmployee);
                employee.setIp(ip);
                ApplicationController.loggedInEmployees.add(employee);
                return ApplicationController.objectMapper.writeValueAsString("update profile password sucessful");
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("update profile password failed");
    }

    /**
     * Returns the id of the logged-in employee.
     * @param ip
     * @return
     * @throws JsonProcessingException
     */
    public String getLoggedInEmployeeId (String ip) throws JsonProcessingException {
        return ApplicationController.objectMapper.writeValueAsString(getLoggedInEmployee(ip).getId());
    }

    /**
     * Creates a new account from the addAccount popup form and adds it to file: accounts.json
     * @throws JsonProcessingException
     * 
     * @param data requestbody containing submitted form data
     */
    public String addEmployee(ObjectNode data) throws JsonProcessingException{
        System.out.println(data);
        String name = data.get("name").asText();
        String role = data.get("role").asText();
        String email = data.get("email").asText();
        String phone = data.get("phone").asText();

        
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class, "src/main/resources/data/accounts.json");
        //find the highest account.id
        int HighestId = 0;
        for (Employee employee : employeeList) {
            if(Integer.parseInt(employee.getId()) > HighestId){
                HighestId = Integer.parseInt(employee.getId());
            }
        }
        employeeList.add(new Employee(name, email, role, Integer.toString(HighestId+1), "password", phone));

        ApplicationController.dataManager.writeToJson(employeeList, "src/main/resources/data/accounts.json");

        return ApplicationController.objectMapper.writeValueAsString("new account added");
    }

    /**
     * 
     * @param data fra deleteAccountBtn requestbody containing employee id
     * @return method success message
     * @throws JsonProcessingException
     */
    public String deleteEmployee(ObjectNode data) throws JsonProcessingException {
        List<Employee> employeeList = ApplicationController.dataManager.readFromJson(Employee.class,"src/main/resources/data/accounts.json");
        for (Employee employee : employeeList) {
            if(employee.getId().equals(data.get("id").asText())){
                System.out.println(employee);
                employeeList.remove(employee);
                ApplicationController.dataManager.writeToJson(employeeList, "src/main/resources/data/accounts.json");
                return ApplicationController.objectMapper.writeValueAsString("Account deleted");
            }
        }
        
        return ApplicationController.objectMapper.writeValueAsString("Account not found");
    }

    /**
     * Checks if the logged-in employee is an admin.
     * @param ip
     * @return
     */
    public boolean isAdmin(String ip){
        Employee loggedInEmployee = getLoggedInEmployee(ip);
        if(loggedInEmployee != null && loggedInEmployee.getRole().equals("admin")) return true;
        else return false;
    }
}

