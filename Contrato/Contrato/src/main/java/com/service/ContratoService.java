package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Contrato;
import com.Repository.ContratoRepository;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    public List<Contrato> getContratos() {
        return contratoRepository.findAll();
    }

    public Contrato getContratoPorId(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato con ID " + id + " no encontrado"));
    }

    public Contrato saveContrato(Contrato contrato) {
        if (contrato == null) {
            throw new IllegalArgumentException("El contrato no puede ser nulo.");
        }

        if (contrato.getFecha_inicio() == null ||
            contrato.getFecha_final() == null ||
            contrato.getFecha_contrato() == null ||
            contrato.getTotal() == null ||
            contrato.getId_usuario() == null ||
            contrato.getId_servicio() == null ||
            contrato.getId_direcc() == null) {
            throw new IllegalArgumentException("Todos los campos del contrato deben estar completos.");
        }

        return contratoRepository.save(contrato);
    }

    public Contrato actualizarContrato(Long id, Contrato contratoActualizado) {
        Optional<Contrato> contratoExistente = contratoRepository.findById(id);

        if (contratoExistente.isPresent()) {
            Contrato contrato = contratoExistente.get();

            contrato.setFecha_contrato(contratoActualizado.getFecha_contrato());
            contrato.setFecha_inicio(contratoActualizado.getFecha_inicio());
            contrato.setFecha_final(contratoActualizado.getFecha_final());
            contrato.setTotal(contratoActualizado.getTotal());
            contrato.setId_usuario(contratoActualizado.getId_usuario());
            contrato.setId_direcc(contratoActualizado.getId_direcc());
            contrato.setId_servicio(contratoActualizado.getId_servicio());

            return contratoRepository.save(contrato);
        } else {
            throw new RuntimeException("Contrato con ID " + id + " no encontrado para actualizar");
        }
    }
}
