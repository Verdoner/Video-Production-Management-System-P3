package models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

//Getters and setters created for all instance variables using Lombok.
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting {
    private String name;
    private String description;
    private String id;
    private String startTime;
    private String duration;
    private List<Participant> participants;


    public Meeting(String name, String description, String meetingId ,String startTime, String duration, List<Participant> participants) {
        this.name = name;
        this.description = description;
        this.id = meetingId;
        this.startTime = startTime;
        this.duration = duration;
        this.participants = participants;
    }
}
