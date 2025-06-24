package Rol.com.example.ROL.Controller;

import Rol.com.example.ROL.Model.RolModel;
import Rol.com.example.ROL.Service.RolService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RolController.class)
public class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGuardarRol() throws Exception {
        RolModel nuevoRol = new RolModel(1L, "Administrador");

        when(rolService.guardarRol(Mockito.any(RolModel.class))).thenReturn(nuevoRol);

        mockMvc.perform(post("/api/v1/Roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoRol)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombreRol").value("Administrador"));
    }

    @Test
    void testObtenerTodosLosRoles() throws Exception {
        List<RolModel> roles = Arrays.asList(
                new RolModel(1L, "Administrador"),
                new RolModel(2L, "Usuario")
        );

        when(rolService.getRol()).thenReturn(roles);

        mockMvc.perform(get("/api/v1/Roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testObtenerRolPorId_existente() throws Exception {
        RolModel rol = new RolModel(1L, "Administrador");

        when(rolService.obtenerRolPorId(1L)).thenReturn(rol);

        mockMvc.perform(get("/api/v1/Roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreRol").value("Administrador"));
    }

    @Test
    void testObtenerRolPorId_noExistente() throws Exception {
        when(rolService.obtenerRolPorId(99L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/Roles/99"))
                .andExpect(status().isNotFound());
    }
}
