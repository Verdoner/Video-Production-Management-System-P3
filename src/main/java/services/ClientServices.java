package services;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;
import models.Client;
import models.Project;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;


public class ClientServices {
    /**
     * Returns a client object by id
     */
    private Client getClientById(String id){
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,"src/main/resources/data/clients.json");
        for(Client client : clientList){
            if(client.getId().equals(id)){
                return client;
            }
        }
        return null;
    }

    /**
     * Returns a all clients as a json string to be used in the front end
     */
    public String getAllClients() throws JsonProcessingException {
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,"src/main/resources/data/clients.json");
        return ApplicationController.objectMapper.writeValueAsString(clientList);
    }

    /**
     * Deletes a client from the client json file and all projects associated with the client
     */
    public String deleteClient(ObjectNode data) throws JsonProcessingException {
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class, "src/main/resources/data/clients.json");
        String clientId = data.get("id").asText();
        
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        
        for(Project project : projectList) {
            if (project.getClientId().equals(clientId)) {
                projectList.remove(project);
                ApplicationController.dataManager.writeToJson(projectList, "src/main/resources/data/projects.json");
            }
        }
        
        for(int i=0; i<clientList.size(); i++){
            if(clientList.get(i).getId().equals(clientId)){
                clientList.remove(i);
                ApplicationController.dataManager.writeToJson(clientList, "src/main/resources/data/clients.json");
                return ApplicationController.objectMapper.writeValueAsString("Client deleted");
            }   
        }
    
        return ApplicationController.objectMapper.writeValueAsString("Client not found");
    }

    /**
     * Updates a client in the client json file
     */
    public String updateClient(ObjectNode data) throws JsonProcessingException {
        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class, "src/main/resources/data/clients.json");
        for (Client client : clientList) {
            if (client.getEmail().equals((data.get("email").asText())) && !client.getId().equals(data.get("id").asText())) {
                return ApplicationController.objectMapper.writeValueAsString("Email already exists");
            }
        }
        for(Client client : clientList){
            if (client.getId().equals(data.get("id").asText())) {
                client.setName( data.get("name").asText());
                client.setPhone( data.get("phone").asText());
                client.setEmail( data.get("email").asText());
                client.setChance( data.get("acceptance").asText());
                client.setState( data.get("state").asText());
            }
            break;
        }
        ApplicationController.dataManager.writeToJson(clientList, "src/main/resources/data/clients.json");
        return ApplicationController.objectMapper.writeValueAsString("Client updated");
    }
    
    /**
     * Creates a client in the client json file
     */
    public String createClient(ObjectNode data) throws JsonProcessingException {
        int numberOfIds = 0;

        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,
            "src/main/resources/data/clients.json");

        for (Client client : clientList) {
            if (client.getEmail().equals((data.get("mail").asText()))) {
                return ApplicationController.objectMapper.writeValueAsString("Email already exists");
            }
        }

        for (Client client : clientList) {
            if (Integer.parseInt(client.getId()) > numberOfIds) {
                numberOfIds = Integer.parseInt(client.getId());
            }
            
        }

        clientList.add(new Client(
                data.get("name").asText(),
                data.get("email").asText(),
                data.get("phone").asText(),
                Integer.toString(numberOfIds + 1),
                "active",
                data.get("acceptance").asText()
        ));

        ApplicationController.dataManager.writeToJson(clientList, "src/main/resources/data/clients.json");
        return ApplicationController.objectMapper.writeValueAsString("Client Created");
    }

    /**
     * Returns a client as a json string to be used in the front end
     */
    public String getClient(ObjectNode data) throws JsonProcessingException {
        String clientId = data.get("id").asText();

        List<Client> clientList = ApplicationController.dataManager.readFromJson(Client.class,
                "src/main/resources/data/clients.json");

        for (Client client : clientList) {
            if (client.getId().equals(clientId)) {
                return ApplicationController.objectMapper.writeValueAsString(client);
            }   
        }
        return ApplicationController.objectMapper.writeValueAsString("Couldn't find client");
    }

    /**
     * Returns a list of the 10 most recent clients as a json string to be used in the front end
     */
    public String recentClients() throws JsonProcessingException {
        List<Project> projectList = ApplicationController.dataManager.readFromJson(Project.class, "src/main/resources/data/projects.json");
        List<Client> clientList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        projectList.sort(Comparator.comparing(project -> LocalDate.parse(project.getDeadline(), formatter)));

        for (Project project : projectList) {
            Client client = getClientById(project.getClientId());
            if (client.getState().equals("active")) {
                clientList.add(client);
            }
        }

        if (clientList.size() > 10) clientList = clientList.subList(0, 10);
        return ApplicationController.objectMapper.writeValueAsString(clientList);
    }
}