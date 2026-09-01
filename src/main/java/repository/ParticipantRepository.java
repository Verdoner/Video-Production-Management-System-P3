package repository;

import models.Participant;

public class ParticipantRepository {

    //Allows a particapnt of a meeting to accept it by setting their status to 'true'.
    public void acceptMeeting(Participant participant){
        participant.setAccepted(true);
    }
    //Allows a particapnt of a meeting to decline it by setting their status to 'false'.
    public void declineMeeting(Participant participant){
        participant.setAccepted(false);
    }
}
