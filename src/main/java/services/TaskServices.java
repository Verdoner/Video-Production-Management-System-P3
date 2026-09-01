package services;

import models.Phase;
import models.Project;
import models.Task;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.NullNode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;

public class TaskServices {
    ProjectServices projectServices = new ProjectServices();

    public List<Task> getTasksForAccountList(String ip){
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Task> taskList = new ArrayList<>();
        String employeeId = ApplicationController.employeeServices.getLoggedInEmployee(ip).getId();
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(Task task : phase.getTasks()){
                    if(task.getEmployeeId().equals(employeeId) && !project.getStatus().equals("archived")){
                        taskList.add(task);
                    }
                }
            }
        }
        return taskList;
    }

    /**
     * Retrieves the tasks assigned to the account associated with the given IP address.
     * 
     * @param ip The IP address of the account.
     * @return A string representation of the tasks assigned to the account.
     * @throws JsonProcessingException If an error occurs while processing JSON data.
     */
    public String getTasksForAccount(String ip, ObjectNode data) throws JsonProcessingException{
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Task> taskList = new ArrayList<>();
        String employeeId = ApplicationController.employeeServices.getLoggedInEmployee(ip).getId();
        Boolean searchEnabled = false;
        String search = "";
        if(!data.get("search").equals(NullNode.getInstance())){
            searchEnabled = true;
            search = data.get("search").asText();
        }
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(Task task : phase.getTasks()){
                    if(task.getEmployeeId().equals(employeeId) && !project.getStatus().equals("archived")){
                        if(searchEnabled){
                            if(task.getName().toLowerCase().contains(search.toLowerCase()) || task.getDescription().toLowerCase().contains(search.toLowerCase())){
                                taskList.add(task);
                            }
                        }
                        else{
                            System.out.println("task added");
                            taskList.add(task);
                        }
                    }
                }
            }
        }
        if(!taskList.isEmpty()){
            taskList.sort(Comparator.comparing(Task::getName));
        }
        return ApplicationController.objectMapper.writeValueAsString(taskList);
    }
    public String getTaskForAccountForDate_(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        
        Task task = new Task("Task", "Description", "001", "2023-11-23", "2023-11-23", false, "Malek" );
        List<Task> refinedList = new ArrayList<>();
        refinedList.add(task);
        
        return ApplicationController.objectMapper.writeValueAsString(refinedList); 
    }
    public String getTaskForAccountForDate(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        try {
    
            var refinedList = getTaskForAccountForDate1(ip, data);
            return ApplicationController.objectMapper.writeValueAsString(refinedList);  //return list of tasks for 2023-05-25 for 127.0.0.1
        } catch(Exception ex){
            System.out.println("Error in getTaskForAccountForDate: " + ex.getMessage());
            throw ex;
        }
    }
    public List<Task> getTaskForAccountForDate1(String ip, ObjectNode data) throws JsonProcessingException, ParseException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Task> taskList = new ArrayList<>();
        List<Task> refinedList = new ArrayList<>();
        String date = data.get("date").asText();  //2023-05-25
        String format = data.get("format").asText();  //true
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
                    for (Task task : phase.getTasks()) {
                        String taskDeadline = task.getDeadline();  //2023-05-25
                        Date parsedTaskDeadline = dateFormat.parse(taskDeadline);
                        SimpleDateFormat JustDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date1 = JustDateFormat.format(parsedTaskDeadline);
                        String date2 = JustDateFormat.format(parsedDate);
                        if (date2.equals(date1)) {
                            taskList.add(task);
                        }
                    }
                }
            }
            for (Task task : taskList) {
                if (task.getEmployeeId().equals(ApplicationController.employeeServices.getLoggedInEmployee(ip).getId())) {
                    refinedList.add(task);
                }
            }
            System.out.println("Tasks for the user: " + refinedList);
    
            return refinedList;  //return list of tasks for 2023-05-25 for 127.0.0.1
        } catch(Exception ex){
            System.out.println("Error in getTaskForAccountForDate: " + ex.getMessage());
            throw ex;
        }
        finally {
            dateFormat = null; // Release resources
        }
    }

    /**
     * Deletes a task based on the provided data.
     *
     * @param data The JSON object containing the task ID.
     * @return A JSON string representation of the message "Task deleted".
     * @throws JsonProcessingException If an error occurs while processing JSON.
     */

    public String deleteTask(ObjectNode data) throws JsonProcessingException {     //reqBody should contain taskId
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(int i=0; i<phase.getTasks().size(); i++){
                    if(phase.getTasks().get(i).getId().equals(data.get("taskId").asText())){
                        phase.getTasks().remove(i);
                    }
                }
            }
        }
        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Task deleted");
    }

    /**
     * Finish a task based on the provided data.
     * 
     * @param data The data containing the taskId of the task to be finished.
     * @return A JSON string representation of the message "Task finished".
     * @throws JsonProcessingException If an error occurs while processing JSON.
     */

    public String finishTask(ObjectNode data) throws JsonProcessingException {     //reqBody should contain taskId
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(int i=0; i<phase.getTasks().size(); i++){
                    if(phase.getTasks().get(i).getId().equals(data.get("taskId").asText())){
                        if(phase.getTasks().get(i).isCompleted()){
                            phase.getTasks().get(i).setCompleted(false);
                        }
                        else{
                            phase.getTasks().get(i).setCompleted(true);
                        }
                    }
                }
            }
        }
        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Task finished");
    }

    /**
     * Updates a task with the provided data.
     *
     * @param data The JSON object containing the task ID, name, description, and deadline.
     * @return A JSON string representation of the updated task.
     * @throws JsonProcessingException If there is an error processing the JSON data.
     */
    public String updateTask(ObjectNode data) throws JsonProcessingException {      //reqBody should contain task id, name, description, deadline
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(int i=0; i<phase.getTasks().size(); i++){
                    if(phase.getTasks().get(i).getId().equals(data.get("taskId").asText())){
                        phase.getTasks().get(i).setName(data.get("name").asText());
                        phase.getTasks().get(i).setDescription(data.get("description").asText());
                        phase.getTasks().get(i).setDeadline(data.get("deadline").asText());                      
                    }
                }
            }
        }
        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Task updated");
    }

    /**
     * Assigns an employee to a task based on the provided employee ID and task ID.
     * 
     * @param data The JSON object containing the employee ID and task ID.
     * @return A JSON string representation of the updated task.
     * @throws JsonProcessingException If an error occurs while processing the JSON.
     */
    public String assignEmployeeToTask(ObjectNode data) throws JsonProcessingException {     //reqBody should contain employye id and task id
        String employeeId = data.get("employeeId").asText();
        String taskId = data.get("taskId").asText();
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(int i=0; i<phase.getTasks().size(); i++){
                    if(phase.getTasks().get(i).getId().equals(taskId)){
                        phase.getTasks().get(i).setEmployeeId(employeeId);                    
                    }
                }
            }
        }
        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Task updated");
    }

    /**
     * Creates a task based on the provided data.
     * 
     * @param data The JSON object containing the task details.
     * @return A JSON string representation of the created task.
     * @throws JsonProcessingException If there is an error processing the JSON data.
     */
    public String createTask(ObjectNode data) throws JsonProcessingException {          //reqBody should contain phase id, name, description, deadline, employee id
        int highestExistingTaskId = 0;
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        for(Project project : projectList){
            for(Phase phase : project.getPhases()){
                for(int i=0; i<phase.getTasks().size(); i++){
                    if(Integer.parseInt(phase.getTasks().get(i).getId()) > highestExistingTaskId){
                        highestExistingTaskId = Integer.parseInt(phase.getTasks().get(i).getId());  
                    }             
                }
            }
        }
        for(Project project : projectList){
            for(int i = 0; i < project.getPhases().size(); i++){
                if(project.getPhases().get(i).getId().equals(data.get("phaseId").asText())){
                    project.getPhases().get(i).getTasks().add(
                        new Task(
                            data.get("name").asText(), 
                            data.get("description").asText(), 
                            Integer.toString(highestExistingTaskId+1), 
                            data.get("creationDate").asText(), 
                            data.get("deadline").asText(), 
                            false, 
                            data.get("employeeId").asText()
                        )
                    );
                }
            }
        }

        ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
        return ApplicationController.objectMapper.writeValueAsString("Task finished");
    }

    public String getTaskById (ObjectNode data) throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        String taskId = data.get("taskId").asText();
        for (Project project : projectList) {
            for (Phase phase : project.getPhases()) {
                for (Task task : phase.getTasks()) {
                    if (task.getId().equals(taskId)) {
                        return ApplicationController.objectMapper.writeValueAsString(task);
                    }
                }
            }
        }
        return ApplicationController.objectMapper.writeValueAsString("This task does not exist");
    }

    public List<Task> getAllTasks () {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Task> allTasks = new ArrayList<>();

        for (Project project : projectList) {
            for (Phase phase  : project.getPhases()) {
                for (Task task : phase.getTasks()) {
                    allTasks.add(task);
                }
            }
        }

        return allTasks;
    }

    public List<Task> addIdToPhaseTasks (JsonNode data) throws JsonProcessingException {

        String jsonString = ApplicationController.objectMapper.writeValueAsString(data.get("tasks"));
        List<Task> taskList = ApplicationController.objectMapper.readValue(jsonString, new TypeReference<List<Task>>(){});


        List<Task> allTasks = getAllTasks();

        int highestExistingTaskId = 0;
        for (Task task : allTasks) {
            if (Integer.parseInt(task.getId()) >= highestExistingTaskId) {
                highestExistingTaskId = Integer.parseInt(task.getId());
            }
        }
        for (Task task : taskList) {
            if (task.getId().equals("")) {
                task.setId(Integer.toString(highestExistingTaskId+1));
                highestExistingTaskId++;
            }
        }

        return taskList;
    }
    public String recentTasks() throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Task> tasks = new ArrayList<>();

        for (Project project : projectList) {
            for (Phase phase : project.getPhases()) {
                tasks.addAll(phase.getTasks());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        tasks.sort(Comparator.comparing(task -> LocalDate.parse(task.getDeadline(), formatter)));

        if (tasks.isEmpty()) {
            return ApplicationController.objectMapper.writeValueAsString("Could not find Task");
        }
        
        if(tasks.size() > 10){
            return ApplicationController.objectMapper.writeValueAsString(tasks.subList(0, 10));
        }
        else{
            return ApplicationController.objectMapper.writeValueAsString(tasks);
        }
    }


}

