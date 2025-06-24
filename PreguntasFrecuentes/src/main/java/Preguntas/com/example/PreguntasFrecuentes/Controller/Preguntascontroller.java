package Preguntas.com.example.PreguntasFrecuentes.Controller;

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

import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
import Preguntas.com.example.PreguntasFrecuentes.Service.PreguntasService;

@RestController
@RequestMapping("/api/v1/preguntas")
public class Preguntascontroller {
    @Autowired
    private PreguntasService preguntasService;
    @GetMapping
    public List<Preguntas> listar() {

        return preguntasService.obtenPreguntas();
     }

  @PostMapping
     public ResponseEntity<Preguntas> crear(@RequestBody Preguntas faq) {
        return ResponseEntity.ok(preguntasService.guardar(faq));
    }
     @DeleteMapping("{id}")
     public ResponseEntity<Void> eliminar(@PathVariable Long id){
        return ResponseEntity.noContent().build(); 

     } 

}
