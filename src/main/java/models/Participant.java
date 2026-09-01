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
public class Participant {
    private boolean accepted;
    private String employeeId;

    public Participant(boolean accepted, String employeeId) {
        this.accepted = accepted;
        this.employeeId = employeeId;
    }
}
