package services;

import com.p3.Film.ApplicationController;
import models.Client;
import models.Meeting;
import models.Project;
import models.Phase;
import models.Task;

import java.time.LocalDate;
import java.util.Comparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectServices {
    public static TaskServices taskServices = new TaskServices();
    public static MeetingServices meetingServices = new MeetingServices();

    // Returns a list of all projects.
    public String getAllProjects() throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString(projectList);
    }

    // Returns a list of all projects that are related to a specific client.
    public String getProjectsForClient(String id) throws JsonProcessingException {
        String returnString = "";
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");
        id = id.replaceAll("\"", "");

        for (Project project : projectList) {
            if (project.getClientId().equals(id)) {
                returnString += ApplicationController.objectMapper.writeValueAsString(project);
            }
        }
        return returnString;
    }

    public String recentProjects() throws JsonProcessingException {
        String returnString = "";
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Sorting the projectList by deadline date
        projectList.sort(Comparator.comparing(project -> LocalDate.parse(project.getDeadline(), formatter)));
        returnString = ApplicationController.objectMapper.writeValueAsString(projectList);

        if (returnString.isEmpty()) {
            returnString = ApplicationController.objectMapper.writeValueAsString("Could not find project");
        }
        return returnString;
    }


    public String getClientForProject(ObjectNode clientId) throws JsonProcessingException {
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,"src/main/resources/data/clients.json");
        for (Client client : clientList) {
            if (client.getId().equals(clientId.get("clientId").asText())) {
                return ApplicationController.objectMapper.writeValueAsString(client.getName());
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("There is no client for this project");
    }

    public String getClientForProjectWithOutJSON(ObjectNode clientId) throws JsonProcessingException {
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,"src/main/resources/data/clients.json");

        for (Client client : clientList) {
            if (client.getId().equals(clientId.get("clientId").asText())) {
                return client.getName();
            }
        }
        return "There is no client for this project";
    }


    public String getProjectById(ObjectNode id) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");

        for (Project project : projectList) {
            if (project.getId().equals(id.get("projectId").asText())) {
                return ApplicationController.objectMapper.writeValueAsString(project);
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("This project does not exist");
    }

    public String getProjectByTaskId (ObjectNode data) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        String taskId = data.get("taskId").asText();
        for (Project project : projectList) {
            for (Phase phase : project.getPhases()) {
                for (Task task : phase.getTasks()) {
                    if (task.getId().equals(taskId)) {
                        return ApplicationController.objectMapper.writeValueAsString(project);
                    }
                }
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("This project does not exist");
    }

    public String getProjectByMeetingId (ObjectNode data) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        String meetingId = data.get("meetingId").asText();
        for (Project project : projectList) {
            for (Phase phase : project.getPhases()) {
                for (Meeting meeting : phase.getMeetings()) {
                    if (meeting.getId().equals(meetingId)) {
                        return ApplicationController.objectMapper.writeValueAsString(project);
                    }
                }
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("This project does not exist");
    }


    public String createProject(ObjectNode data) throws JsonProcessingException {
        int highestId = 0;

        List<Phase> phases = new ArrayList<>(Arrays.asList(
            new Phase("Idea Generation", "1", true),
            new Phase("Client Interview", "2", false),
            new Phase("Recording", "3", false),
            new Phase("Editing", "4", false),
            new Phase("End", "5", false)
        ));

        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");

        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,
                "src/main/resources/data/clients.json");

        for (Project project : projectList) {
            if (Integer.parseInt(project.getId()) > highestId) {
                highestId = Integer.parseInt(project.getId());
            }
        }

        for (Client client : clientList) {
            if (client.getId().equals(data.get("clientId").asText())) {
                client.setState("active");
            }
        }

        projectList.add(new Project(
                data.get("projectName").asText(),
                data.get("projectDescription").asText(),
                Integer.toString(highestId + 1),
                LocalDate.now().toString(),
                data.get("deadline").asText(),
                "Idea Generation",
                data.get("clientId").asText(),
                phases));

        ApplicationController.dataManager.writeToJson(clientList, "src/main/resources/data/clients.json");
        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Project Created");
    }

    public String deleteProject(ObjectNode data) throws JsonProcessingException { // reqBody should contain taskId
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");

        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getId().equals(data.get("id").asText())) {
                projectList.remove(i);
                ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
                return ApplicationController.objectMapper.writeValueAsString("Project deleted");
            }
        }

        return ApplicationController.objectMapper.writeValueAsString("No project exist");
    }

    public String setProjectStatus(ObjectNode data) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");

        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,
        "src/main/resources/data/clients.json");

        for (Project project : projectList) {
            if (project.getId().equals(data.get("id").asText())) {
                project.setStatus(data.get("status").asText());
                for (Client client : clientList) {
                    if (client.getId().equals(project.getClientId())) {
                        client.setState("active");
                    }
                    else {
                        client.setState("archived");
                    }
                }
                ApplicationController.dataManager.writeToJson(clientList, "src/main/resources/data/clients.json");
                ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
                return ApplicationController.objectMapper.writeValueAsString("Updated Status of Project");
            }
        }

        return ApplicationController.objectMapper.writeValueAsString("Project does not exist!");
    }

    public String updateProject(ObjectNode data) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class,
                "src/main/resources/data/projects.json");

        Project updatedProject;

        List<Phase> phases = new ArrayList<>();
        phases.add(new Phase(
                "Idea Generation",
                "1",
                data.get("phases").get(0).get("active").asBoolean(),
                meetingServices.addIdToPhaseMeetings(data.get("phases").get(0)),
                taskServices.addIdToPhaseTasks(data.get("phases").get(0))));
        phases.add(new Phase(
                "Client Interview",
                "2",
                data.get("phases").get(1).get("active").asBoolean(),
                meetingServices.addIdToPhaseMeetings(data.get("phases").get(1)),
                taskServices.addIdToPhaseTasks(data.get("phases").get(1))));
        phases.add(new Phase(
                "Recording",
                "3",
                data.get("phases").get(2).get("active").asBoolean(),
                meetingServices.addIdToPhaseMeetings(data.get("phases").get(2)),
                taskServices.addIdToPhaseTasks(data.get("phases").get(2))));
        phases.add(new Phase(
                "Editing",
                "4",
                data.get("phases").get(3).get("active").asBoolean(),
                meetingServices.addIdToPhaseMeetings(data.get("phases").get(3)),
                taskServices.addIdToPhaseTasks(data.get("phases").get(3))));
        phases.add(new Phase(
                "End",
                "5",
                data.get("phases").get(4).get("active").asBoolean(),
                meetingServices.addIdToPhaseMeetings(data.get("phases").get(4)),
                taskServices.addIdToPhaseTasks(data.get("phases").get(4))));

        updatedProject = new Project(
                data.get("name").asText(),
                data.get("description").asText(),
                data.get("id").asText(),
                data.get("creationDate").asText(),
                data.get("deadline").asText(),
                data.get("status").asText(),
                data.get("clientId").asText(),
                phases);

        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).getId().equals(data.get("id").asText())) {
                projectList.remove(i);
            }
        }

        projectList.add(updatedProject);


        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Project Updated");
    };
};

