package com.Direccion.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Direccion.Model.Region;

@Repository

public interface RegRepository extends  JpaRepository <Region,Long>{

}
