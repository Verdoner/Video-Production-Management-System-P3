package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import models.Project;

public class ProjectServicesTest {

    private ProjectServices projectServices;

    @BeforeEach
    void setUp() {
        projectServices = new ProjectServices();
    }

    @Test
    void testGetAllProjects() throws JsonProcessingException{
        //Mocking the DataManger and making sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                        new Project("Project1", "Description1", "1", "2023-12-05", "2023-12-31", "Active", "Client1", List.of()),
                        new Project("Project2", "Description2", "2", "2023-12-06", "2023-12-31", "Active", "Client2", List.of())
                )
        );
        String result = projectServices.getAllProjects();
        assertTrue(result.contains("Project1"));
        assertTrue(result.contains("Project2"));
    }

     @Test
    void testGetAllProjectsWhenProjectsDoesNotExist() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;
        
        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(List.of());
        String result = projectServices.getAllProjects();

        assertEquals("[]", result);
    }

    @Test
    void testDeleteProject() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of projects to be returned when the readFromJson method is called
        List<Project> mockProjectList = new ArrayList<>();
        mockProjectList.add(new Project("TestProject", "test description", "1", "2023-02-25", "2023-10-25", "Active", "1", null));
        when(mockDataManager.readFromJson(eq(Project.class), anyString())).thenReturn(mockProjectList);

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "1");

        String result = projectServices.deleteProject(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Project.class), anyString());
        verify(mockDataManager, times(1)).writeToJson(anyList(), anyString());

        assertEquals("\"Project deleted\"", result);
    }

    @Test
    void testDeleteProjectWhenNoProjectExist() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Creating a mock list of projects to be returned when the readFromJson method is called
        when(mockDataManager.readFromJson(eq(Project.class), anyString())).thenReturn(new ArrayList<>());

        ObjectNode testData = JsonNodeFactory.instance.objectNode();
        testData.put("id", "1");

        String result = projectServices.deleteProject(testData);

        verify(mockDataManager, times(1)).readFromJson(eq(Project.class), anyString());

        assertEquals("\"No project exist\"", result);
    }

    @Test
    void getProjectsForClientTest() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

         //Creating a mock list of projects to be returned when the readFromJson method is called
        List<Project> mockProjectList = new ArrayList<>();
        mockProjectList.add(new Project("TestProject", "test description", "1", "2023-02-25", "2023-10-25", "Active", "1", null));
        mockProjectList.add(new Project("TestProject2", "Another test description", "2", "2023-05-25", "2023-10-02", "Active", "2", null));
        when(mockDataManager.readFromJson(eq(Project.class), anyString())).thenReturn(mockProjectList);

        String result = projectServices.getProjectsForClient("1");

        verify(mockDataManager, times(1)).readFromJson(eq(Project.class), anyString());

        assertNotNull(result);
        assertEquals("{\"name\":\"TestProject\",\"description\":\"test description\",\"id\":\"1\",\"creationDate\":\"2023-02-25\",\"deadline\":\"2023-10-25\",\"status\":\"Active\",\"clientId\":\"1\",\"phases\":null}", result);
    }

    @Test
    void getProjectsForClientTestWhenClientDoesNotHaveAProject() throws JsonProcessingException{
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

         //Creating a mock list of projects to be returned when the readFromJson method is called
        List<Project> mockProjectList = new ArrayList<>();
        mockProjectList.add(new Project("TestProject", "test description", "1", "2023-02-25", "2023-10-25", "Active", "1", null));
        mockProjectList.add(new Project("TestProject2", "Another test description", "2", "2023-05-25", "2023-10-02", "Active", "2", null));
        when(mockDataManager.readFromJson(eq(Project.class), anyString())).thenReturn(mockProjectList);

        String result = projectServices.getProjectsForClient("3");

        verify(mockDataManager, times(1)).readFromJson(eq(Project.class), anyString());

        assertNotNull(result);
        assertEquals("", result);
    }

}
