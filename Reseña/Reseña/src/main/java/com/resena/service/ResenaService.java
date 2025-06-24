package com.resena.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.resena.model.Resena;
import com.resena.repository.ResenaRepository;
import com.resena.WebClient.UserClient;
import com.resena.WebClient.WebServicio;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private WebServicio webServicio;

    @Autowired
    private UserClient webUser;

    public List<Resena> getResenas() {
        return resenaRepository.findAll();
    }

    public Resena getResenaPorId(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con ID: " + id));
    }

    public Resena saveResena(Resena nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("La reseña no puede ser null");
        }

        if (!StringUtils.hasText(nuevo.getComentario())) {
            throw new IllegalArgumentException("El comentario es obligatorio");
        }

        if (nuevo.getFCom() == null) {
            throw new IllegalArgumentException("La fecha del comentario es obligatoria");
        }

        if (nuevo.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }

        if (nuevo.getIdServicio() == null) {
            throw new IllegalArgumentException("El ID del servicio es obligatorio");
        }

        if (nuevo.getNota() == null || nuevo.getNota() < 1 || nuevo.getNota() > 5) {
            throw new IllegalArgumentException("La nota debe ser un valor entre 1 y 5");
        }

        // Validar existencia del servicio
        try {
            Map<String, Object> servicio = webServicio.getServicioById(nuevo.getIdServicio());
            if (servicio == null || servicio.isEmpty()) {
                throw new IllegalArgumentException("El servicio indicado no existe");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al validar el servicio: " + e.getMessage());
        }

        // Validar existencia del usuario
        try {
            Map<String, Object> usuario = webUser.getUserById(nuevo.getIdUsuario());
            if (usuario == null || usuario.isEmpty()) {
                throw new IllegalArgumentException("El usuario indicado no existe");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al validar el usuario: " + e.getMessage());
        }

        return resenaRepository.save(nuevo);
    }

    public Resena actualizarResena(Long id, Resena actualizada) {
        Resena existente = getResenaPorId(id);

        // Se reutiliza la validación completa
        actualizada.setIdResena(id); // asegurar que mantiene el ID original
        return saveResena(actualizada);
    }

    public void deleteResena(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con ID: " + id);
        }
        resenaRepository.deleteById(id);
    }
}
