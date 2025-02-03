package com.example.Payroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import com.example.Payroll.entities.Employee;
import com.example.Payroll.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private Long alexId;
    private Long felipeId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // limpiamos la base de datos y creamos dos entidades
        employeeRepository.deleteAll();
        Employee alex = new Employee("alex", "manager");
        Employee felipe = new Employee("felipe", "employee");
        employeeRepository.save(alex);
        employeeRepository.save(felipe);
        // como al borrar la base de datos no se reinician los id, los guardo para utilizarlos en los test
        this.alexId = alex.getId();
        this.felipeId = felipe.getId();
    }

    @Test
    public void testGetAllEmployees() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("alex"))
                .andExpect(jsonPath("$[0].role").value("manager"))
                .andExpect(jsonPath("$[1].name").value("felipe"))
                .andExpect(jsonPath("$[1].role").value("employee"));
    }

    @Test
    public void testGetEmployeeById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees/" + alexId))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("alex"))
                .andExpect(jsonPath("$.role").value("manager"));
    }
    
    @Test
    public void testGetEmployeeByIdAndNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/employees/999"))
                    //.andDo(print())
                    .andExpect(status().isNotFound());
    }

    @Test
    public void testAddEmployee() throws Exception{
        Employee employee = new Employee("juan", "employee");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("juan"))
                .andExpect(jsonPath("$.role").value("employee"));
    }

    @Test
    public void testUpdateEmployeeThatNoExists() throws Exception{
        Employee employee = new Employee("juan", "employee");
        mockMvc.perform(MockMvcRequestBuilders
                .put("/employees/" + 999)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("juan"))
                .andExpect(jsonPath("$.role").value("employee"));
    }
    
    @Test
    public void testUpdateEmployeeThatExists() throws Exception {
        Employee modifiedEmployee = new Employee(alexId, "alex modificado", "manager modificado");
        
        mockMvc.perform(MockMvcRequestBuilders
                .put("/employees/" + alexId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifiedEmployee)))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("alex modificado"))
                .andExpect(jsonPath("$.role").value("manager modificado"));
    }

    @Test
    public void testDeleteEmployee() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/employees/" + alexId))
                //.andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

}
