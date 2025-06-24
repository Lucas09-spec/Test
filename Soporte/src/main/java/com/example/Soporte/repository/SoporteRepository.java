package com.example.Soporte.repository;

import org.springframework.data.jpa.repository.JpaRepository;  // Proporciona métodos CRUD predefinidos
import org.springframework.stereotype.Repository;           // Marca la interfaz como componente de persistencia

import com.example.Soporte.model.Soporte;

@Repository                                          // Registra este repositorio como bean de Spring
public interface SoporteRepository extends JpaRepository<Soporte, Long> {
    // Métodos de consulta personalizados se pueden agregar aquí si es necesario.
}