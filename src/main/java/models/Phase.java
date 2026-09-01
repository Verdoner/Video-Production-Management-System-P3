package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//Getters and setters created for all instance variables using Lombok.
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Phase {
    private String name;
    private String id;
    private boolean active;
    private List<Task> tasks = new ArrayList<>();
    private List<Meeting> meetings = new ArrayList<>();

    public Phase(String name, String id, boolean active) {
        this.name = name;
        this.id = id;
        this.active = active;
        this.tasks = new ArrayList<>();
        this.meetings = new ArrayList<>();
    }

    public Phase(String name, String id, boolean active, List<Meeting> meetings, List<Task> tasks) {
        this.name = name;
        this.id = id;
        this.active = active;
        this.tasks = tasks;
        this.meetings = meetings;
    }
}
