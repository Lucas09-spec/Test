package com.example.Respuesta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Respuesta.model.Respuesta;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{

}
