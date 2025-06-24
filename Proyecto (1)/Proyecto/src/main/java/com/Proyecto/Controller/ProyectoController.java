package com.Proyecto.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Proyecto.Model.Proyecto;
import com.Proyecto.Service.ProyectoService;
import com.Proyecto.WebClient.WebClientC1;
import com.Proyecto.WebClient.TecnicoClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/proyecto")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private WebClientC1 contratoClient;

    @Autowired
    private TecnicoClient tecnicoClient;

    @Operation(summary="Permite obtener una lista con todos los Proyectos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de proyectos obtenida con éxito",
                content = @Content(schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "204", description = "No hay proyectos disponibles")
    })
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerProyectos() {
        List<Proyecto> lista = proyectoService.getProyectos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary="Permite obtener un Proyecto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto encontrado",
                content = @Content(schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProyectoPorId(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.getProyectoById(id);
        return proyecto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary="Permite guardar un nuevo Proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto guardado exitosamente",
                content = @Content(schema = @Schema(implementation = Proyecto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes")
    })
    @PostMapping
    public ResponseEntity<?> guardarProyecto(@RequestBody Proyecto nuevo) {
        try {
            if (nuevo.getComentarios() == null || nuevo.getComentarios().isBlank()) {
                return ResponseEntity.badRequest().body("El campo 'comentarios' es obligatorio.");
            }
            if (nuevo.getId_contrato() == null || nuevo.getId_contrato() <= 0 ||
                contratoClient.getContratoById(nuevo.getId_contrato()) == null) {
                return ResponseEntity.badRequest().body("El ID del contrato no es válido o no existe.");
            }
            if (nuevo.getId_tecnico() == null || nuevo.getId_tecnico() <= 0 ||
                tecnicoClient.getTecnicoById(nuevo.getId_tecnico()) == null) {
                return ResponseEntity.badRequest().body("El ID del técnico no es válido o no existe.");
            }
            if (nuevo.getId_estado() == null || nuevo.getId_estado() <= 0) {
                return ResponseEntity.badRequest().body("El campo 'Id_estado' debe ser positivo.");
            }
            if (nuevo.getFecha() != null && nuevo.getFecha().isAfter(java.time.LocalDate.now())) {
                return ResponseEntity.badRequest().body("La fecha no puede ser futura.");
            }

            Proyecto guardado = proyectoService.saveProyecto(nuevo);
            return ResponseEntity.ok(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al guardar el proyecto: " + e.getMessage());
        }
    }

    @Operation(summary="Permite asignar o cambiar un técnico a un Proyecto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Técnico asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PutMapping("/asignar-tecnico/{id}")
    public ResponseEntity<?> asignarTecnico(@PathVariable Long id, @RequestParam Long idTecnico) {
        Optional<Proyecto> proyectoOpt = proyectoService.getProyectoById(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (tecnicoClient.getTecnicoById(idTecnico) == null) {
            return ResponseEntity.badRequest().body("El ID del técnico no existe.");
        }
        Proyecto proyecto = proyectoOpt.get();
        proyecto.setId_tecnico(idTecnico);
        proyectoService.saveProyecto(proyecto);
        return ResponseEntity.ok("Técnico asignado correctamente.");
    }

    @Operation(summary="Permite cambiar el estado de un Proyecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del proyecto actualizado"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PutMapping("/cambiar-estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Long nuevoEstadoId) {
        Optional<Proyecto> proyectoOpt = proyectoService.getProyectoById(id);
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Proyecto proyecto = proyectoOpt.get();
        proyecto.setId_estado(nuevoEstadoId);
        proyectoService.saveProyecto(proyecto);
        return ResponseEntity.ok("Estado del proyecto actualizado correctamente.");
    }

    @Operation(summary="Permite eliminar un Proyecto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proyecto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.getProyectoById(id);
        if (proyecto.isPresent()) {
            proyectoService.deleteProyecto(id);
            return ResponseEntity.ok("Proyecto eliminado correctamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
