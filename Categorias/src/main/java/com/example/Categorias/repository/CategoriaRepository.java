package com.example.Categorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Categorias.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
