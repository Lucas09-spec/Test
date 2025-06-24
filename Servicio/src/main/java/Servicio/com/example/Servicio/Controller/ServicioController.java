package Servicio.com.example.Servicio.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Servicio.com.example.Servicio.Model.Servicio;
import Servicio.com.example.Servicio.Service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/Servicio")
public class ServicioController {

    @Autowired 
    private ServicioService servicioService;

    @Operation(summary = "Obtener todos los servicios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicios encontrados"),
        @ApiResponse(responseCode = "204", description = "No hay servicios disponibles")
    })
    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerServicio() {
        List<Servicio> lista = servicioService.ObtenerLista(); 
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        } 
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Guardar un nuevo servicio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<Servicio> guardarServicio(@RequestBody Servicio nuevo) {
        return ResponseEntity.status(201).body(servicioService.guardarServicio(nuevo));
    }

    @Operation(summary = "Eliminar un servicio por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Servicio> eliminarServicio(@PathVariable Long id) {
        boolean eliminado = servicioService.eliminarServicio(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @Operation(summary = "Actualizar un servicio por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @PutMapping("{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio datosActualizados) {
        Servicio actualizado = servicioService.actualizarServicio(id, datosActualizados);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
