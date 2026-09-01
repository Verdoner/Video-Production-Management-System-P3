package models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//Getters and setters created for all instance variables using Lombok.
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    private String name;
    private String description;
    private String id;
    private String creationDate;
    private String deadline;
    private String status;
    private String clientId;
    private List<Phase> phases;

    public Project(String name, String description, String id, String creationDate, String deadline, String status, String clientId, List<Phase> phases) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.creationDate = creationDate;
        this.deadline = deadline;
        this.status = status;
        this.clientId = clientId;
        this.phases = phases;
    }
}
