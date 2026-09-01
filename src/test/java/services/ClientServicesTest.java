package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.p3.Film.ApplicationController;
import com.p3.Film.DataManager;

import models.Client;

public class ClientServicesTest {
    private ClientServices clientServices;

    @BeforeEach
    public void setUp() {
        clientServices = new ClientServices();
    }
  
    @Test
    void testGetAllClients() throws JsonProcessingException {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                        new Client("Client1", "client1@gmail.com", "12345678", "1", "Active", "Likely"),
                        new Client("Client2", "Client2@gmail.com", "87654321", "2", "Archieved", "Accepted")
                )
        );
        String result = clientServices.getAllClients();
        assertTrue(result.contains("Client1"));
        assertTrue(result.contains("Client2"));
    }

    @Test
    void testGetAllClientsWhenClientsDoNotExist() throws JsonProcessingException {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of()
        );
        String result = clientServices.getAllClients();
        assertEquals("[]", result);
    }
    //DeleteClient method er ikke skal laves om
    @Test
    void testDeleteClient() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of clients to be returned when the readFromJson method is called
        List<Client> mockClientList = new ArrayList<>();
        mockClientList.add(new Client("TestClient", "test@example.com", "12345678", "1", "active", "High"));
        when(mockDataManager.readFromJson(eq(Client.class), anyString())).thenReturn(mockClientList);

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "1");

        String result = clientServices.deleteClient(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Client.class), anyString());
        verify(mockDataManager, times(1)).writeToJson(anyList(), anyString());

        assertEquals("\"Client deleted\"", result);
    }
    //DeleteClient method er ikke skal laves om
    @Test
    void testDeleteClientWhenNoClientExist() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of clients to be returned when the readFromJson method is called
        when(mockDataManager.readFromJson(eq(Client.class), anyString())).thenReturn(new ArrayList<>());

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "1");

        String result = clientServices.deleteClient(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Client.class), anyString());
        //verify(mockDataManager, times(2)).writeToJson(anyList(), anyString());

        assertEquals("\"Client not found\"", result);
    }
}
