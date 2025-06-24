package com.resena.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "usuario")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID de la reseña", example = "1")
    @JsonProperty("id_resena")
    private Long idResena;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que realizó la reseña", example = "100")
    @JsonProperty("id_usuario")
    private Long idUsuario;

    @Column(nullable = false)
    @Schema(description = "ID del servicio reseñado", example = "200")
    @JsonProperty("id_servicio")
    private Long idServicio;

    @Column(nullable = false)
    @Schema(description = "Comentario de la reseña", example = "Muy buen servicio")
    @JsonProperty("comentario")
    private String comentario;

    @Column(nullable = false)
    @Schema(description = "Fecha del comentario", example = "2025-06-22")
    @JsonProperty("f_com")
    private Date fCom;

    @Column(nullable = false)
    @Schema(description = "Nota o calificación de la reseña", example = "5")
    @JsonProperty("nota")
    private Integer nota;
}
