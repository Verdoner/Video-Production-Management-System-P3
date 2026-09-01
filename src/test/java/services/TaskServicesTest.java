package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.p3.Film.ApplicationController;
import com.p3.Film.DataManager;

import models.Phase;
import models.Project;
import models.Task;

public class TaskServicesTest {
    private TaskServices taskServices;

    @BeforeEach
    void setUp(){
        taskServices = new TaskServices();
    }

    @Test
    void testGetAllTasks() throws Exception {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                    new Project("Project1", "Description1", "1", "2023-12-05", "2023-12-31", "Active", "Client1",
                    List.of(new Phase("Phase1", "1", true, null, List.of(new Task("Task", "Task test", "1", "2023-05-25", "2023-12-21", false, "employee")))))
                )
        );

        List<Task> result = taskServices.getAllTasks();
        assertFalse(result.isEmpty());
        assertEquals("Task", result.get(0).getName());
        assertEquals("Task test", result.get(0).getDescription());
        assertEquals("1", result.get(0).getId());
        //assertEquals("[]", result.get(0).getParticipants());
    }

     @Test
    void testGetAllTasksWhenThereIsNoTasks() throws Exception {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                    new Project("Project1", "Description1", "1", "2023-12-05", "2023-12-31", "Active", "Client1",
                    List.of(new Phase()))
                )
        );

        List<Task> result = taskServices.getAllTasks();
        assertTrue(result.isEmpty());
    }
    
}
