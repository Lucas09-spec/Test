package com.example.Soporte.controller;

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

import com.example.Soporte.model.Soporte;
import com.example.Soporte.service.SoporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
@RequestMapping("/api/v1/soportes")
public class SoporteController {
    
    @Autowired
    private SoporteService soporteService;

    @Operation(
        summary = "Obtener todos los Soportes",
        description = "Retorna una lista de todos los Soportes del sistema"
    )
    @ApiResponses(value= {
        @ApiResponse(
            responseCode = "200",
            description = "Soportes encontrados",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Soporte.class)))
        ),
        @ApiResponse(responseCode = "204" , description = "No se ecnontraron Soportes")
    })
    @GetMapping
    public ResponseEntity<List<Soporte>> getAllSoportes() {
        List<Soporte> soportes = soporteService.findAll();
        return ResponseEntity.ok(soportes);
    }


    @Operation(
        summary = "Obtener soporte por ID", 
        description = "Retorna el soporte correspondiente al ID proporcionado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Soporte encontrado", 
            content = @Content(schema = @Schema(implementation = Soporte.class))
        ),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Soporte> getSoporteById(@PathVariable Long id) {
        Optional<Soporte> soporte = soporteService.findById(id);
        return soporte.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }


    @Operation(
        summary = "Crear un nuevo soporte", 
        description = "Recibe la información del nuevo soporte y lo guarda en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Soporte creado exitosamente", 
            content = @Content(schema = @Schema(implementation = Soporte.class))
        ),
        @ApiResponse(responseCode = "400", description = "Error al crear el soporte")
    })
    @PostMapping
    public ResponseEntity<Soporte> createSoporte(@RequestBody Soporte nuevoSoporte) {
        try {
            Soporte soporteGuardado = soporteService.saveSoporte(nuevoSoporte);
            return ResponseEntity.ok(soporteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Operation(
        summary = "Actualizar soporte existente", 
        description = "Actualiza el soporte identificado por el ID con la información proporcionada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Soporte actualizado exitosamente", 
            content = @Content(schema = @Schema(implementation = Soporte.class))
        ),
        @ApiResponse(responseCode = "400", description = "Error al actualizar el soporte"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Soporte> updateSoporte(@PathVariable Long id, @RequestBody Soporte soporteActualizado) {
        Optional<Soporte> soporteExistente = soporteService.findById(id);

        if (soporteExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Asegurar que se mantenga el mismo ID
        soporteActualizado.setId(id);

        try {
            Soporte soporteGuardado = soporteService.saveSoporte(soporteActualizado);
            return ResponseEntity.ok(soporteGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Operation(
        summary = "Eliminar soporte", 
        description = "Elimina el soporte identificado por el ID proporcionado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Soporte eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoporte(@PathVariable Long id) {
        soporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
