package com.example.Cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Cliente.model.Cliente;

@Repository

public interface ClienteRepository extends JpaRepository<Cliente,Long>  {



}
