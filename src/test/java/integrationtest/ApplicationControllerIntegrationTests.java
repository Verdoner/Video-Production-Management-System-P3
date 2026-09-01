

package integrationtest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p3.Film.ApplicationController;
import com.p3.Film.Application;

import services.EmployeeServices;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.*;
import java.nio.file.Files;


@AutoConfigureMockMvc
@SpringBootTest(classes = { Application.class })
@TestInstance(Lifecycle.PER_CLASS)
public class ApplicationControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setUp() throws Exception{
        String requestBody = "{\"username\": \"username\", \"password\": \"password\"}";
        System.out.println(requestBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody));
    }

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(handler().methodName("index"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetIndex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"))
               .andDo(MockMvcResultHandlers.print());   
    }

    @Test
    public void testGetProfilePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
               .andExpect(status().isOk())
               .andExpect(view().name("profile"))
               .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetTaskPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(view().name("tasks"))
               .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetProjectPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
               .andExpect(status().isOk())
               .andExpect(view().name("projects"))
               .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void testGetAccountPage() throws Exception {
        String requestBody = "{\"username\": \"username\", \"password\": \"password\"}";
        MvcResult loginRequest = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
        String loginAccount = loginRequest.getResponse().getContentAsString();
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(view().name("accounts"))
               .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.post("/getEmployees")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        try {
        File f = new File("src\\main\\resources\\assets\\menu.mjs");
        String fileMenu = new String(Files.readAllBytes(f.toPath()));
        mockMvc.perform(MockMvcRequestBuilders.get("/assets/js/menu.mjs")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(fileMenu))
            .andDo(MockMvcResultHandlers.print());
    }   catch (IOException e) {
            e.printStackTrace();
    }
       try {
       File e = new File("src\\main\\resources\\assets\\popUp.mjs");
       String filePopup = new String(Files.readAllBytes(e.toPath()));
        mockMvc.perform(MockMvcRequestBuilders.get("/assets/js/popUp.mjs")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(filePopup))
            .andDo(MockMvcResultHandlers.print());
    } catch (IOException e) {
        e.printStackTrace();
    }
        try {
        File h = new File("src\\main\\resources\\assets\\accounts.mjs");
        String fileAccounts = new String(Files.readAllBytes(h.toPath()));
        mockMvc.perform(MockMvcRequestBuilders.get("/assets/js/accounts.mjs")
             .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(fileAccounts))
            .andDo(MockMvcResultHandlers.print());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
        File f = new File("src\\main\\resources\\assets\\main.mjs");
        String fileContent = new String(Files.readAllBytes(f.toPath()));
        mockMvc.perform(MockMvcRequestBuilders.get("/assets/js/main.mjs")
             .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(fileContent))
            .andDo(MockMvcResultHandlers.print());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mockMvc.perform(MockMvcRequestBuilders.post("/isAdmin")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginAccount))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        File readEmployeeList = new File("src\\main\\resources\\data\\accounts.json");
        String employeeListAsString = new String(Files.readAllBytes(readEmployeeList.toPath()));

        mockMvc.perform(MockMvcRequestBuilders.post("/getEmployees")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginAccount))
            .andExpect(status().isOk())
            .andExpect(content().string(employeeListAsString))
            .andDo(MockMvcResultHandlers.print());
        
        String addAccountRequestBody = "{\"name\":\"bobby\",\"phone\":\"12121212\",\"email\":\"bobby@fischer.dk\",\"role\":\"editor\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/addEmployee")
             .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(addAccountRequestBody))
            .andExpect(status().isOk())
            .andExpect(content().string("\"new account added\""))
            .andDo(MockMvcResultHandlers.print());
        
        File readEmployeeList2 = new File("src\\main\\resources\\data\\accounts.json");
        String employeeListAsString2 = new String(Files.readAllBytes(readEmployeeList2.toPath()));
        mockMvc.perform(MockMvcRequestBuilders.post("/getEmployees")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginAccount))
            .andExpect(status().isOk())
            .andExpect(content().string(employeeListAsString2))
            .andDo(MockMvcResultHandlers.print());
        
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteEmployee")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":\"3\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("\"Account deleted\""))
            .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/getEmployees")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(loginAccount))
            .andExpect(status().isOk())
            .andExpect(content().string(employeeListAsString))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetClientPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/clients"))
               .andExpect(status().isOk())
               .andExpect(view().name("clients"))
               .andDo(MockMvcResultHandlers.print());
    }

    // Add more test cases for different endpoints

    @Test
    //Testen går igennem hvilket vil sige man kan logge ind 2 gange :)
    public void testPostLogin() throws Exception {
        String requestBody = "{\"username\": \"username\", \"password\": \"password\"}";
        String responseString = "{\"name\":\"Simon\",\"email\":\"email\",\"role\":\"admin\",\"id\":\"1\",\"password\":\"password\",\"ip\":\"127.0.0.1\",\"phone\":\"12345678\"}";
        System.out.println(requestBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(content().string(responseString))
               .andDo(MockMvcResultHandlers.print());
    }    
}

