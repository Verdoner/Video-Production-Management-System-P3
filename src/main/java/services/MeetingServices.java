package services;

import models.Meeting;
import models.Participant;
import models.Project;
import models.Task;
import models.Phase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;

public class MeetingServices {
    ProjectServices projectServices = new ProjectServices();

    private List<Meeting> meetings;

    /**
     * Returns a list of meeting objects assigned to the logged in employee
     * @param ip
     * @return
     */
    public List<Meeting> getMeetingsForAccountList(String ip){
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Meeting> meetingList = new ArrayList<>();
        String employeeId = ApplicationController.employeeServices.getLoggedInEmployee(ip).getId();
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(Meeting meeting : phase.getMeetings()){
                    for(Participant participant : meeting.getParticipants()){
                        if(participant.getEmployeeId().equals(employeeId) && !project.getStatus().equals("archived")){
                            meetingList.add(meeting);
                        }
                    }
                }
            }
        }
        return meetingList;
    }

    // Checks if a meeting is accepted by all participants.
    public Boolean isAccepted(String meetingId) {
        Boolean meetingAccepted = true;
        for (Meeting meeting : meetings) {
            if (meeting.getId() == meetingId) {
                List<Participant> participants = meeting.getParticipants();
                for (Participant participant : participants) {
                    if (participant.isAccepted() == false) {
                        meetingAccepted = false;
                    }
                }
            }
        }
        return meetingAccepted;
    }

    /**
     * Returns a meeting as a json string by id
     * @param ip
     * @return
     */
    public String getMeetingById(ObjectNode identifier) throws JsonProcessingException {
        Project project = ApplicationController.objectMapper
                .readValue(projectServices.getProjectById(identifier), Project.class);
        List<Phase> phaseList = project.getPhases();

        for (Phase phase : phaseList) {
            if (phase.getId().equals(identifier.get("phaseId").asText())) {
                List<Meeting> meetingList = phase.getMeetings();
                for (Meeting meeting : meetingList) {
                    if (meeting.getId().equals(identifier.get("meetingId").asText())) {
                        return ApplicationController.objectMapper.writeValueAsString(meeting);
                    }
                }
            }
        }

        return ApplicationController.objectMapper.writeValueAsString("This meeting does not exist");
    }

    /**
     * Returns a list of all meeting for all phases in all projects
     * @param ip
     * @param data
     * @return
     * @throws JsonProcessingException
     */
    public List<Meeting> getAllMeetings() {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");
        List<Meeting> allMeetings = new ArrayList<>();

        for (Project project : projectList) {
            for (Phase phase : project.getPhases()) {
                for (Meeting meeting : phase.getMeetings()) {
                    allMeetings.add(meeting);
                }
            }
        }

        return allMeetings;
    }
    
    public List<Meeting> addIdToPhaseMeetings(JsonNode data) throws JsonMappingException, JsonProcessingException {
        String jsonString = ApplicationController.objectMapper.writeValueAsString(data.get("meetings"));
        List<Meeting> meetingList = ApplicationController.objectMapper.readValue(jsonString, new TypeReference<List<Meeting>>(){});
    
        List<Meeting> allMeetings = getAllMeetings();
        
        int highestExistingMeetingId = 0;
        for (Meeting meeting : allMeetings) {
            if (Integer.parseInt(meeting.getId()) >= highestExistingMeetingId) {
                highestExistingMeetingId = Integer.parseInt(meeting.getId());
            }
        }

        for (Meeting meeting : meetingList) {
            if (meeting.getId().equals("")) {
                meeting.setId(Integer.toString(highestExistingMeetingId + 1));
                highestExistingMeetingId++;
            }
        }

        return meetingList;
    }

    public List<Meeting> getMeetingForAccountForDate1(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Meeting> refinedList = new ArrayList<>();
        String employeeId = ApplicationController.employeeServices.getLoggedInEmployee(ip).getId();
        String date = data.get("date").asText();
        String format = data.get("format").asText();
        SimpleDateFormat dateFormat;
    
        if (format.equals("month")) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        }
        try {
            Date parsedDate = dateFormat.parse(date);

            for (Project project : projectList) {
                for (Phase phase : project.getPhases()) {
                    for (Meeting meeting : phase.getMeetings()) {
                        String meetingStartTime = meeting.getStartTime();
                        Date parsedMeetingStartTime = dateFormat.parse(meetingStartTime);
                        SimpleDateFormat JustDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date1 = JustDateFormat.format(parsedMeetingStartTime);
                        String date2 = JustDateFormat.format(parsedDate);
                        if (date2.equals(date1)) {
                            List<Participant> participants = meeting.getParticipants();
                            for (Participant participant : participants) {
                                if (participant.getEmployeeId().equals(employeeId)) {
                                    refinedList.add(meeting);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return refinedList;
        } catch (Exception ex) {
            System.out.println("Error in getMeetingForAccountForDate: " + ex.getMessage());
            throw ex; 
        }
        finally {
            dateFormat = null;
        }
    }  
    
    public String getMeetingForAccountForDate(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
       
        try {
           var refinedList = getMeetingForAccountForDate1(ip, data);
            return ApplicationController.objectMapper.writeValueAsString(refinedList);
        } catch (Exception ex) {
            System.out.println("Error in getMeetingForAccountForDate: " + ex.getMessage());
            throw ex; 
        }
    }  
}
