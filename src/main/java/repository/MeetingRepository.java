package repository;

import models.Employee;
import models.Meeting;
import models.Participant;

import java.util.List;
import java.util.ArrayList;


public class MeetingRepository {

    private List<Meeting> meetings;
    //Checks if a meeting exist with the given id, and returns the meeting if it exist or null if the meeting does not exist.
    public Meeting getMeetingById(String meetingId) {
        for (Meeting meeting : meetings) {
            if (meeting.getId() == meetingId) {
                return meeting;
            }
        }
        return null;
    }

    public List<Meeting> getAllMeetings() {
        return meetings;
    }
    
    //Checks if a meeting exist before updating its attributes.
    public void updateMeeting(Meeting meeting, String name, String newDescription, String newStartTime, String duration) {
        if (meetings.contains(meeting)) {
            meeting.setName(name);
            meeting.setDescription(newDescription);
            meeting.setStartTime(newStartTime);
            meeting.setDuration(duration);
        }
    }
    //Returns a list of all meetings that are scheduled in a specific time period. 
    // public List<Meeting> getMeetingsForDateRange(String startDate, String endDate) {
    //     List<Meeting> meetingsInRange = new ArrayList<>();
    //     for (Meeting meeting : meetings) {
    //         String meetingStartTime = meeting.getStartTime();
    //         String meetingEndTime = meeting.getEndTime();
    //         if (meetingStartTime.isAfter(startDate) && meetingEndTime.isBefore(endDate)) {
    //             meetingsInRange.add(meeting);
    //         }
    //     }
    //     return meetingsInRange;
    // }
    //Returns a list of all meetings for a specific employee.
    public List<Meeting> getMeetingsForAttendee(Participant participant) {
        List<Meeting> meetingsForAttendee = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (meeting.getParticipants().contains(participant)) {
                meetingsForAttendee.add(meeting);
            }
        }
        return meetingsForAttendee;
    }

        //Adds a new attendee to a meeting.
    public void addParticipant(Meeting meeting, Employee employee){
        Participant newParticipant = new Participant(false, employee.getId());
        meeting.getParticipants().add(newParticipant);
    }
    //Removes an attendee from a meeting.
    public void removeAttendee(Meeting meeting, Participant participant){
        meeting.getParticipants().remove(participant);
    }
}
