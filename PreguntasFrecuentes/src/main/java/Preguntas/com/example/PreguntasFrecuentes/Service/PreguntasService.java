package Preguntas.com.example.PreguntasFrecuentes.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
import Preguntas.com.example.PreguntasFrecuentes.Repository.PreguntaRepository;

@Service
public class PreguntasService {
    @Autowired
    private PreguntaRepository preguntaRepository;


    public List<Preguntas> obtenPreguntas(){
        return preguntaRepository.findAll();
    }
public Preguntas guardar(Preguntas pre){
    return preguntaRepository.save(pre);
}
public void eleminar(Long id){
    preguntaRepository.deleteById(id);
}
}
