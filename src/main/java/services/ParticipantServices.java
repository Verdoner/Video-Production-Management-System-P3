package services;

import java.util.List;

import models.Participant;

public class ParticipantServices {
    private List<Participant> participants;
    //Adds a participant to a list of all participants.
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    //Removes a participant from a list of all participants.
    public void removeParticipant(Participant participant) {
        participants.remove(participant);
    }
}
