package Preguntas.com.example.PreguntasFrecuentes.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
import Preguntas.com.example.PreguntasFrecuentes.Service.PreguntasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/preguntas")
public class Preguntascontroller {

    @Autowired
    private PreguntasService preguntasService;
    
    @Operation(
        summary = "Listar Preguntas Frecuentes", 
        description = "Devuelve la lista de todas las preguntas frecuentes almacenadas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de preguntas frecuentes encontrada",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Preguntas.class)))
        )
    })
    @GetMapping
    public List<Preguntas> listar() {
        return preguntasService.obtenPreguntas();
    }

    @Operation(
        summary = "Crear Pregunta Frecuente", 
        description = "Permite crear y almacenar una nueva pregunta frecuente en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pregunta creada exitosamente",
            content = @Content(schema = @Schema(implementation = Preguntas.class))
        )
    })
    @PostMapping
    public ResponseEntity<Preguntas> crear(@RequestBody Preguntas faq) {
        return ResponseEntity.ok(preguntasService.guardar(faq));
    }

    @Operation(
        summary = "Eliminar Pregunta Frecuente", 
        description = "Elimina una pregunta frecuente identificada por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pregunta eliminada exitosamente")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        preguntasService.eleminar(id);
        return ResponseEntity.noContent().build();
    }
}
