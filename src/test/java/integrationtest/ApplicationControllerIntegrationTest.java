

package integrationtest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p3.Film.ApplicationController;
import com.p3.Film.Application;

import services.EmployeeServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(classes = { Application.class })
public class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    private void testGetIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("loginPage"));
    }

    @Test
    private void testGetProfilePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("profilePage"));
    }

    // Add more test cases for different endpoints

    @Test
    private void testPostRequest() throws Exception {
        // You can create a sample request body as a JSON string
        String requestBody = "{\"key\": \"value\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/someEndpoint")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(content().string("expectedResponse"));
    }

    // Add more test cases for different post endpoints

    // You can add more test cases as needed for different endpoints and scenarios
}
