package repository;

import models.Client;

import java.util.List;

public class ClientRepository {
    private List<Client> clients;

    //Checks if a client exist with the given id, and returns the client if the client exist or null if the client does not exist.
    public Client getClientById(String clientId) {
    for (Client client : clients) {
    if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }
    //Checks if a client exist before updating their attributes.
    public void updateClientInformation(Client client, String newClientName, String newContactEmail, String newPhoneNumber, String newState) {
        client.setName(newClientName);
        client.setEmail(newContactEmail);
        client.setPhone(newPhoneNumber);
        client.setState(newState);
    }
    //Archieves a given client.
    public void archiveClient(Client client){
        client.setState("Archived");
    }
    //Reactivates a given client.
    public void reactivateClient(Client client){
        client.setState("Active");
    }
}
