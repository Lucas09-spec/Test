package com.example.Soporte.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "soporte")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Soporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable=false)
    private String titulo;
    
    @Column
    private String descripcion;

    @Column(name = "fecha_inicio", nullable= false)
    private LocalDate fechaInicio;

    @Column(nullable=false)
    private Long categoriaId;
    
    @Column(nullable=false)
    private Long estadoId;

    @Column(nullable=false)
    private Long usuarioId;
    
}
