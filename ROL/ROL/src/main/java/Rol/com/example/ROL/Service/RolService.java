package Rol.com.example.ROL.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Rol.com.example.ROL.Model.RolModel;
import Rol.com.example.ROL.Repository.RolRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<RolModel> getRol() {
        return rolRepository.findAll();
    }

    public RolModel guardarRol(RolModel nuevo) {
        return rolRepository.save(nuevo);
    }

    public RolModel obtenerRolPorId(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    public RolModel actualizarRol(Long id, RolModel rolActualizado) {
        RolModel existente = rolRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombreRol(rolActualizado.getNombreRol());
            return rolRepository.save(existente);
        }
        return null;
    }
}
