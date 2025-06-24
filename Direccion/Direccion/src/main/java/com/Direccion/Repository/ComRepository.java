package com.Direccion.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Direccion.Model.Comuna;

public interface ComRepository extends JpaRepository <Comuna,Long> {

}
