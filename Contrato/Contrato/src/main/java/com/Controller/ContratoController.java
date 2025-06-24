package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.model.Contrato;
import com.service.ContratoService;
import com.Webproyecto.WebUsuario;
import com.Webproyecto.ServicioClient;
import com.Webproyecto.DireccionClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/contrato")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private WebUsuario webUsuario;

    @Autowired
    private ServicioClient servicioClient;

    @Autowired
    private DireccionClient direccionClient;

    @Operation(summary = "Obtiene una lista con todos los contratos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de contratos obtenida con éxito",
            content = @Content(schema = @Schema(implementation = Contrato.class))),
        @ApiResponse(responseCode = "204", description = "No hay contratos para mostrar")
    })
    @GetMapping
    public ResponseEntity<List<Contrato>> obtenerContratos() {
        List<Contrato> lista = contratoService.getContratos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtiene un contrato por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contrato encontrado",
            content = @Content(schema = @Schema(implementation = Contrato.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido"),
        @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Contrato> obtenerContratoporId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Contrato contrato = contratoService.getContratoPorId(id);
            return ResponseEntity.ok(contrato);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Guarda un nuevo contrato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contrato creado con éxito",
            content = @Content(schema = @Schema(implementation = Contrato.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> guardarContrato(@RequestBody Contrato nuevo) {
        if (nuevo == null) {
            return ResponseEntity.badRequest().body("El contrato no puede ser nulo.");
        }

        if (nuevo.getFecha_inicio() == null ||
            nuevo.getFecha_final() == null ||
            nuevo.getFecha_contrato() == null ||
            nuevo.getTotal() == null ||
            nuevo.getId_direcc() == null ||
            nuevo.getId_usuario() == null ||
            nuevo.getId_servicio() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios.");
        }

        if (!webUsuario.existeUsuario(nuevo.getId_usuario())) {
            return ResponseEntity.badRequest().body("El ID de usuario no existe.");
        }

        if (!servicioClient.existeServicio(nuevo.getId_servicio())) {
            return ResponseEntity.badRequest().body("El ID de servicio no existe.");
        }

        if (!direccionClient.existeDireccion(nuevo.getId_direcc())) {
            return ResponseEntity.badRequest().body("El ID de dirección no existe.");
        }

        try {
            Contrato contratoGuardado = contratoService.saveContrato(nuevo);
            return ResponseEntity.status(201).body(contratoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }

    @Operation(summary = "Actualiza un contrato existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contrato actualizado con éxito",
            content = @Content(schema = @Schema(implementation = Contrato.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
        @ApiResponse(responseCode = "404", description = "Contrato no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarContrato(@PathVariable Long id, @RequestBody Contrato actualizado) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido.");
        }

        if (actualizado == null) {
            return ResponseEntity.badRequest().body("Datos del contrato no pueden ser nulos.");
        }

        if (actualizado.getFecha_inicio() == null ||
            actualizado.getFecha_final() == null ||
            actualizado.getFecha_contrato() == null ||
            actualizado.getTotal() == null ||
            actualizado.getId_direcc() == null ||
            actualizado.getId_usuario() == null ||
            actualizado.getId_servicio() == null) {
            return ResponseEntity.badRequest().body("Faltan campos obligatorios.");
        }

        if (!webUsuario.existeUsuario(actualizado.getId_usuario())) {
            return ResponseEntity.badRequest().body("El ID de usuario no existe.");
        }

        if (!servicioClient.existeServicio(actualizado.getId_servicio())) {
            return ResponseEntity.badRequest().body("El ID de servicio no existe.");
        }

        if (!direccionClient.existeDireccion(actualizado.getId_direcc())) {
            return ResponseEntity.badRequest().body("El ID de dirección no existe.");
        }

        try {
            Contrato contratoActualizado = contratoService.actualizarContrato(id, actualizado);
            return ResponseEntity.ok(contratoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }
}
