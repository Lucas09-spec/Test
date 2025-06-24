package Servicio.com.example.Servicio.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@Table(name = "Servicios")
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del servicio", example = "1")
    private Long id; 

    @Column(unique = true, nullable = false, name = "Descripcion del servicio")
    @Schema(description = "Descripción del servicio", example = "Instalación de paneles solares")
    private String descripcion;

    @Column(nullable = false, name = "Precio del servicio")
    @Schema(description = "Precio del servicio en pesos chilenos", example = "150000")
    private Long precio; 

    @Column(nullable = false, name = "Disponibilidad")
    @Schema(description = "Indica si el servicio está disponible", example = "true")
    private boolean disp;
}