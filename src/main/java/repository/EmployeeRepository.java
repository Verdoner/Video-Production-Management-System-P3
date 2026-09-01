package repository;


import models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {

    private List<Employee> employees;
    //Checks if an employee exist with the given id, and returns the employee if the employee exist or null if the employee does not exist.
    public Employee getEmployeeById(String id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }
    //Returns a list of all employees with a specific role.
    public List<Employee> getEmployeesByRole(String role) {
        List<Employee> employeesByRole = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getRole().equalsIgnoreCase(role)) {
                employeesByRole.add(employee);
            }
        }
        return employeesByRole;
    }
}
