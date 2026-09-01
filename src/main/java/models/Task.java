package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Getters and setters created for all instance variables using Lombok.
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String name;
    private String description;
    private String id;
    private String creationDate;
    private String deadline;
    private boolean completed;
    private String employeeId;


    public Task(String taskName, String taskDescription, String taskId, String creationDate, String deadline , boolean completed, String employee) {
        this.name = taskName;
        this.description = taskDescription;
        this.id = taskId;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.completed = completed;
        this.employeeId = employee;
    }
}
