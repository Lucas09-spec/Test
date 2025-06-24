package Servicio.com.example.Servicio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Servicio.com.example.Servicio.Model.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio,Long>{

}
