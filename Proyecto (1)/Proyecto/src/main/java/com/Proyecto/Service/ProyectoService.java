package com.Proyecto.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Proyecto.Model.Proyecto;
import com.Proyecto.Repository.ProyectoRepository;
import com.Proyecto.WebClient.EstadoClient;
import com.Proyecto.WebClient.TecnicoClient;
import com.Proyecto.WebClient.WebClientC1;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private WebClientC1 webClientC1;

    @Autowired
    private TecnicoClient tecnicoClient;

    @Autowired
    private EstadoClient estadoClient;

    public List<Proyecto> getProyectos() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> getProyectoById(Long id) {
        return proyectoRepository.findById(id);
    }

    public Proyecto saveProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);


    }

    public Proyecto cambiarEstadoProyecto(Long id, Long idEstado) {
        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(id);
        if (proyectoOpt.isEmpty()) {
            throw new RuntimeException("Proyecto con ID " + id + " no encontrado.");
        }
    
        try {
            estadoClient.getEstadoById(idEstado); 
        } catch (Exception e) {
            throw new RuntimeException("El ID de estado no existe.");
        }
    
        Proyecto proyecto = proyectoOpt.get();
        proyecto.setId_estado(idEstado);
        return proyectoRepository.save(proyecto);
    }
    
    public Proyecto asignarTecnicoAProyecto(Long idProyecto, Long idTecnico) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
            .orElseThrow(() -> new RuntimeException("Proyecto con ID " + idProyecto + " no encontrado."));
    
        try {
            tecnicoClient.getTecnicoById(idTecnico);
        } catch (Exception e) {
            throw new RuntimeException("El ID de técnico no existe.");
        }
    
        proyecto.setId_tecnico(idTecnico);
        return proyectoRepository.save(proyecto);
    }
    

    public void deleteProyecto(Long id) {
        proyectoRepository.deleteById(id);
    }
} 
