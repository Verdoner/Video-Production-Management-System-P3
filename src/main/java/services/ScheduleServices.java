package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;

import models.Meeting;
import models.MeetingTaskModel;
import models.Task;

public class ScheduleServices {
    

    public String GetTasksForDate(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        long time = data.get("date").asLong();
        List<Task> tasksForDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        for (Task task : ApplicationController.taskServices.getTasksForAccountList(ip)) {
            Date taskDeadline = sdf.parse(task.getDeadline());
            if(data.get("format").asText().equals("hour")){
                if(taskDeadline.getTime() >= time && taskDeadline.getTime() < time + 3600000){
                    tasksForDate.add(task);
                }
            }
            else{
                if(taskDeadline.getTime() >= time && taskDeadline.getTime() < time + 86400000){
                    tasksForDate.add(task);
                }
            }
        }
        return ApplicationController.objectMapper.writeValueAsString(tasksForDate);
    }

    public String GetMeetingsForDate(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        long time = data.get("date").asLong();
        List<Meeting> meetingsForDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        for (Meeting meeting : ApplicationController.meetingServices.getMeetingsForAccountList(ip)) {
            Date meetingStart = sdf.parse(meeting.getStartTime());
            if(data.get("format").asText().equals("hour")){
                if(meetingStart.getTime() >= time && meetingStart.getTime() < time + 3600000){
                    meetingsForDate.add(meeting);
                }
            }
            else{
                if(meetingStart.getTime() >= time && meetingStart.getTime() < time + 86400000){
                    meetingsForDate.add(meeting);
                }
            }
        }
        return ApplicationController.objectMapper.writeValueAsString(meetingsForDate);
    }
}
