package com.example.Categorias.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Categorias.model.Categoria;
import com.example.Categorias.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    
    @Operation(
        summary = "Obtener todas las categorías", 
        description = "Retorna una lista de categorías si existen; en caso contrario devuelve un código 204."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Categorías encontradas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Categoria.class)))
        ),
        @ApiResponse(responseCode = "204", description = "No se encontraron categorías")
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategoria(){
        List<Categoria> lista = categoriaService.getCategorias();
        if(lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "Obtener categoría por ID", 
        description = "Retorna la categoría correspondiente al ID especificado."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Categoría encontrada",
            content = @Content(schema = @Schema(implementation = Categoria.class))
        ),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaporId(@PathVariable Long id){
        try {
            Categoria categoria = categoriaService.getCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Crear una nueva categoría", 
        description = "Recibe la información de una nueva categoría y la guarda en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Categoría creada exitosamente",
            content = @Content(schema = @Schema(implementation = Categoria.class))
        )
    })
    @PostMapping
    public ResponseEntity<Categoria> guardarCliente(@RequestBody Categoria nuevo){
        return ResponseEntity.status(201).body(categoriaService.saveCategoria(nuevo));
    }

    @Operation(
        summary = "Eliminar una categoría", 
        description = "Elimina la categoría identificada por el ID proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoporte(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

