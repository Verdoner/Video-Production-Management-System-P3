package models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Getters and setters created for all instance variables using Lombok.
@Getter
@Setter
@NoArgsConstructor
public class Employee {
    private String name;
    private String email;
    private String role;
    private String id;
    private String password;
    private String ip;
    private String phone;

    public Employee(String name, String email, String role, String id, String password, String phone) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.id = id;
        this.password = password;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Employee{id='" + id + "', name='" + name + "', email='" + email + "', role='" + role + "', password='" + password + "', ip='" + ip + "'}";
    }
}
