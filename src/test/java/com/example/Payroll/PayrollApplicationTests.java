package com.example.Payroll;

import com.example.Payroll.controllers.EmployeeController;
import com.example.Payroll.entities.Employee;
import com.example.Payroll.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class) // hace uso de junit 5
class PayrollApplicationTests {

    private MockMvc mockMvc;
    
    @Mock
    private EmployeeRepository employeeRepository; // Mock del repositorio

    @InjectMocks
    private EmployeeController employeeController; // inyectamos el mock en el controlador
    
    private final ObjectMapper objectMapper = new ObjectMapper(); // convertir a y desde json

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build(); // configura mockMvc
    }
    
    @Test
    public void testAddEmployee() throws Exception{
        // datos de prueba
        Employee empleado1 = new Employee("Felipe Sánchez", "empleado");
        Employee empleado1Response = new Employee(1L, "Felipe Sánchez", "empleado");
        
        // mock de la llamada y respuesta
        when(employeeRepository.save(any(Employee.class))).thenReturn(empleado1Response);
        
        // llamada y validación
        mockMvc.perform(MockMvcRequestBuilders
                .post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado1))) // con objectMapper convertimos el objeto a json
                //.content("{\"name\": \"Felipe Sánchez\", \"role\": \"empleado\"}"))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Felipe Sánchez"))
                .andExpect(jsonPath("$.role").value("empleado"));
                
    }
    
    @Test
    public void testGetAllEmployees() throws Exception {
        // datos de prueba
        Employee empleado1 = new Employee(1L, "Felipe Retamar", "programador");
        Employee empleado2 = new Employee(2L, "Antonio Perez", "asistente");
        List<Employee> empleados = List.of(empleado1, empleado2);
        
        System.out.println(empleados);
        
        // mock de la respuesta
        when(employeeRepository.findAll()).thenReturn(empleados);
        
        // hacemos la llamada y validamos los datos
        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) // Esto imprimirá la respuesta JSON en la consola
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Felipe Retamar"))
                .andExpect(jsonPath("$[0].role").value("programador"))
                .andExpect(jsonPath("$[1].name").value("Antonio Perez"))
                .andExpect(jsonPath("$[1].role").value("asistente"));     
    }
    
    @Test
    public void testGetOneEmployee() throws Exception {
        // datos de prueba
        Employee empleado1Response = new Employee(1L, "Felipe Sánchez", "empleado");
        
        // mock de la respuesta
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(empleado1Response));
        
        // llamada y validación
        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Felipe Sánchez"))
                .andExpect(jsonPath("$.role").value("empleado"));
    }
    
    @Test
    public void testModifyEmployee() throws Exception {
        // datos de prueba
        Employee empleadoModificado = new Employee (1L, "Felipe modificado", "modificado");
        
        // mock respuesta
        when(employeeRepository.save(any(Employee.class))).thenReturn(empleadoModificado);
        
        // llamada y validación
        mockMvc.perform(MockMvcRequestBuilders
                .put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoModificado)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Felipe modificado"))
                .andExpect(jsonPath("$.role").value("modificado"));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        // se

        // llamada y validación
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/employees/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
