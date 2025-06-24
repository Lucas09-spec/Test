package com.example.Categorias.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Categorias.model.Categoria;
import com.example.Categorias.repository.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }

    public Categoria getCategoriaPorId(Long id){
        return categoriaRepository.findById(id)
        .orElseThrow(()-> new RuntimeException("Categoria No Encontrada"));
    }

    public Categoria saveCategoria(Categoria nuevo){
        return categoriaRepository.save(nuevo);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }

    
}
