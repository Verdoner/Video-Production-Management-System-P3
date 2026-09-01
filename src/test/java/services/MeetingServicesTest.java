package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.p3.Film.ApplicationController;
import com.p3.Film.DataManager;

import models.Meeting;
import models.Phase;
import models.Project;

public class MeetingServicesTest {

    @Test
    void testGetAllMeetings() throws Exception {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        MeetingServices meetingServices = new MeetingServices();

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                    new Project("Project1", "Description1", "1", "2023-12-05", "2023-12-31", "Active", "Client1",
                    List.of(new Phase("Phase1", "1", true,
                            List.of(new Meeting("Meeting1", "Test meeting", "1",  "2023-12-10", "2", List.of())), null)))
                )
        );

        List<Meeting> result = meetingServices.getAllMeetings();
        assertFalse(result.isEmpty());
        assertEquals("Meeting1", result.get(0).getName());
        assertEquals("Test meeting", result.get(0).getDescription());
        assertEquals("1", result.get(0).getId());
        //assertEquals("[]", result.get(0).getParticipants());
    }

     @Test
    void testGetAllMeetingsWhenThereIsNoMeetings() throws Exception {
        //Mocking the DataManger and macking sure the Application controller uses the mock
        DataManager mockDataManager = mock(DataManager.class);
        ApplicationController.dataManager = mockDataManager;

        MeetingServices meetingServices = new MeetingServices();

        //Stubbing the call to readFromJson to return our wanted test value
        when(mockDataManager.readFromJson(any(), any())).thenReturn(
                List.of(
                    new Project("Project1", "Description1", "1", "2023-12-05", "2023-12-31", "Active", "Client1",
                    List.of(new Phase()))
                )
        );

        List<Meeting> result = meetingServices.getAllMeetings();
        assertTrue(result.isEmpty());
    }

}