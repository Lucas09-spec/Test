package Estado.com.example.ESTADO.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Estado.com.example.ESTADO.Model.ESTADO;
import Estado.com.example.ESTADO.Service.ESTADOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/estado")
public class ESTADOController {

    @Autowired 
    private ESTADOService estadoService; 

    
    @Operation(
        summary = "Obtener todos los estados",
        description = "Retorna la lista de todos los estados del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estados encontrados",
                     content = @Content(array = @ArraySchema(schema = @Schema(implementation = ESTADO.class)))),
        @ApiResponse(responseCode = "204", description = "No se encontraron estados")
    })
    @GetMapping
    public ResponseEntity<List<ESTADO>> obtenerEstado() {
        List<ESTADO> lista = estadoService.obEstados();
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
    
     
    @Operation(
        summary = "Guardar un estado",
        description = "Recibe la información de un nuevo estado y lo guarda en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estado guardado exitosamente",
                     content = @Content(schema = @Schema(implementation = ESTADO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody ESTADO nuevo) {
        try {
            ESTADO guardado = estadoService.guardarEstado(nuevo);
            return ResponseEntity.status(201).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
    
    
    @Operation(
        summary = "Actualizar un estado",
        description = "Actualiza el estado identificado por su ID con la información proporcionada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente",
                     content = @Content(schema = @Schema(implementation = ESTADO.class))),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ESTADO actualizado) {
        
        Optional<ESTADO> existe = estadoService.buscarEstadoPorId(id);
        if (existe.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        actualizado.setId(id);
        
        try {
            ESTADO guardado = estadoService.guardarEstado(actualizado);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
    
    
    @Operation(
        summary = "Eliminar un estado",
        description = "Elimina el estado identificado por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Estado eliminado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estadoService.eliminarEstado(id);
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }
}

