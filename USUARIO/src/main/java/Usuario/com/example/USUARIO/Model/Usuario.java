package Usuario.com.example.USUARIO.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(nullable = false , unique = true)
    private String correo; 

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column (nullable = false)
    private String apellido;
    @Column (nullable = false)
    private String telefono;

    @Column(nullable = false)
    private Long rol;


}
