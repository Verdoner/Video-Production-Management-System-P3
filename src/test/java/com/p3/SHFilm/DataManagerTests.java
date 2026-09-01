package com.p3.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.exc.StreamReadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Employee;

@JsonTest
public class DataManagerTests {

    DataManager dataManager = new DataManager();
    ObjectMapper objectMapperMock = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Mocking the DataManager and ObjectMapper classes
        dataManager = mock(DataManager.class);
        objectMapperMock = mock(ObjectMapper.class);
        // Making sure that the ApplicationController uses the mock of the ObjectMapper
        ApplicationController.objectMapper = objectMapperMock;
    }

    @Test
    void testReadFromJson() throws StreamReadException, DatabindException, IOException {
        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(new Employee("John Doe", "john@example.com", "Admin", "1", "password", "12345678"));
        //Stubbing the call the readFromJson to return the wanted value
        when(dataManager.readFromJson(Employee.class, "test.json"))
                .thenReturn(expectedList);

        List<Employee> resultList = dataManager.readFromJson(Employee.class, "test.json");
        //Verifying that the readFromJson method is called with the correct parameters
        verify(dataManager).readFromJson(Employee.class, "test.json");
        
        //Asserting that the method call returns the expected results
        assertEquals(expectedList, resultList);
    }

    @Test
    void testWriteToJson() {
    }
}
