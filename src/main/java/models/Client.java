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
public class Client {

    private String name;
    private String email;
    private String phone;
    private String id;
    private String state;
    private String chance;

    public Client(String name, String email, String phone, String id, String state, String chance) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.state = state;
        this.chance = chance;
    }
}
