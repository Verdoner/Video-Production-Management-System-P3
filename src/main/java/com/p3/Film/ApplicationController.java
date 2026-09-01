package com.p3.Film;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;
import models.Employee;
import services.ClientServices;
import services.EmployeeServices;
import services.MeetingServices;
import services.ParticipantServices;
import services.PhaseServices;
import services.ProjectServices;
import services.ScheduleServices;
import services.TaskServices;

@Controller
public class ApplicationController {
    public static DataManager dataManager = new DataManager();
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static List<Employee> loggedInEmployees = new ArrayList<>();
    public static ClientServices clientServices = new ClientServices();
    public static ProjectServices projectServices = new ProjectServices();
    public static EmployeeServices employeeServices = new EmployeeServices();
    public static MeetingServices meetingServices = new MeetingServices();
    public static ParticipantServices participantServices = new ParticipantServices();
    public static PhaseServices phaseServices = new PhaseServices();
    public static TaskServices taskServices = new TaskServices();
    public static ScheduleServices scheduleServices = new ScheduleServices();

    /**
     * The get mapping for the index page
     * @param ip
     * @return
     */
    @GetMapping(value = "/")
    public String index(HttpServletRequest ip) {
        System.out.println("get /");
        if (employeeServices.isLoggedIn(ip.getRemoteAddr()))
            return "index";
        else
            return "login";
    }

    /**
     * The get mapping for the when writing a path in the url
     * @param id
     * @param ip
     * @return
     */

    @GetMapping(value = "/{path}")
    public String index(@PathVariable(value = "path") String id, HttpServletRequest ip) {
        System.out.println("get " + id);
        if (employeeServices.isLoggedIn(ip.getRemoteAddr())) {
            if(employeeServices.isAdmin(ip.getRemoteAddr())){
                switch (id.toLowerCase()) {
                    case "accounts":
                        return "accounts";
                }
            }
            switch (id.toLowerCase()) {
                case "login":
                    return "login";

                case "":
                    return "index";

                case "profile":
                    return "profile";

                case "clients":
                    return "clients";

                case "projects":
                    return "projects";

                case "schedule":
                    return "schedule";

                case "tasks":
                    return "tasks";

                default:
                    return "404";
            }
        } else
            return "login";
    }

    /**
     * The mapping for the post requests made from JavaScript to the functions in the service directory
     * @param request
     * @param reqBody
     * @param ip
     * @return
     * @throws JsonProcessingException
     * @throws ParseException
     */
    @PostMapping(value = "/{request}", produces = "text/html;charset=utf8")
    public ResponseEntity<String> request(@PathVariable String request, @RequestBody String reqBody, HttpServletRequest ip) throws JsonProcessingException, ParseException {
        String res = "";
        System.out.println("post " + request + ": from ip: " + ip.getRemoteAddr());
        ObjectNode jsonReqBody = (ObjectNode) objectMapper.readTree(reqBody);
        if(!reqBody.equals("{}")) System.out.println(reqBody);
        if(employeeServices.isAdmin(ip.getRemoteAddr())){
            switch(request){
                case "getEmployees" -> res = employeeServices.getAllEmployees();
                case "getEmployee" -> res = employeeServices.getEmployee(jsonReqBody);
                case "updateEmployee" -> res = employeeServices.updateProfile(ip.getRemoteAddr(),jsonReqBody);
                case "addEmployee" -> res = employeeServices.addEmployee(jsonReqBody);
                case "deleteEmployee" -> res = employeeServices.deleteEmployee(jsonReqBody);
            }
        }
        if(res.equals("")){
            switch(request){
                case "getProjects" -> res = projectServices.getAllProjects();
                case "createProject" -> res = projectServices.createProject(jsonReqBody);
                case "deleteProject" -> res = projectServices.deleteProject(jsonReqBody);
                case "getProjectsForClient" -> res = projectServices.getProjectsForClient(reqBody);
                case "getProjectById" -> res = projectServices.getProjectById(jsonReqBody);
                case "getProjectByMeetingId" -> res = projectServices.getProjectByMeetingId(jsonReqBody);
                case "getProjectByTaskId" -> res = projectServices.getProjectByTaskId(jsonReqBody);
                case "setProjectStatus" -> res = projectServices.setProjectStatus(jsonReqBody);
                case "updateProject" -> res = projectServices.updateProject(jsonReqBody);

                case "getAllClients" -> res = clientServices.getAllClients();
                case "getClient" -> res = clientServices.getClient(jsonReqBody);
                case "updateClient" -> res = clientServices.updateClient(jsonReqBody);
                case "createClient" -> res = clientServices.createClient(jsonReqBody);
                case "deleteClient" -> res = clientServices.deleteClient(jsonReqBody);
                //case "archiveClient" -> res = clientServices.archiveClient(jsonReqBody);
                //case "activateClient" -> res = clientServices.activateClient(jsonReqBody);
                case "getClientForProject" -> res = projectServices.getClientForProject(jsonReqBody);
                case "recentProjects" -> res = projectServices.recentProjects();
                case "recentTasks" -> res = taskServices.recentTasks();
                case "recentClients" -> res = clientServices.recentClients();
                case "recentAccounts" -> res = employeeServices.recentAccounts(ip.getRemoteAddr());

                case "getProfile" -> res = employeeServices.getProfile(ip.getRemoteAddr());
                case "updateProfile" -> res = employeeServices.updateProfile(ip.getRemoteAddr(), jsonReqBody);
                case "updateProfilePassword" -> res = employeeServices.updateProfilePassword(ip.getRemoteAddr(), jsonReqBody);
            
                case "getMeetingById" -> res = meetingServices.getMeetingById(jsonReqBody);
                case "getTaskById" -> res = taskServices.getTaskById(jsonReqBody);

                case "getTaskForAccountForDate" -> res = taskServices.getTaskForAccountForDate(ip.getRemoteAddr(), jsonReqBody);
                case "getMeetingForAccountForDate" -> res = meetingServices.getMeetingForAccountForDate(ip.getRemoteAddr(), jsonReqBody);
                case "getTasksForDate" -> res = scheduleServices.GetTasksForDate(ip.getRemoteAddr(), jsonReqBody);
                case "getMeetingsForDate" -> res = scheduleServices.GetMeetingsForDate(ip.getRemoteAddr(), jsonReqBody);
                case "getLoggedInEmployeeId" -> res = employeeServices.getLoggedInEmployeeId(ip.getRemoteAddr());

                case "createTask" -> res = taskServices.createTask(jsonReqBody);
                case "deleteTask" -> res = taskServices.deleteTask(jsonReqBody);
                case "finishTask" -> res = taskServices.finishTask(jsonReqBody);
                case "updateTask" -> res = taskServices.updateTask(jsonReqBody);
                case "getTasksForAccount" -> res = taskServices.getTasksForAccount(ip.getRemoteAddr(), jsonReqBody);

                case "login" -> res = employeeServices.login(jsonReqBody, ip.getRemoteAddr());
                case "logout" -> res = employeeServices.logout(ip.getRemoteAddr());
                case "isAdmin" -> res = employeeServices.isAdmin(ip.getRemoteAddr()) ? "true" : "false";

                default -> res = "error";
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
