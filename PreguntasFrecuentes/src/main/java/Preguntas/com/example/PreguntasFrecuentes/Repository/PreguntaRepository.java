package Preguntas.com.example.PreguntasFrecuentes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
@Repository
public interface PreguntaRepository extends JpaRepository<Preguntas , Long>{

}
