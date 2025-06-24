package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Contrato;
@Repository
public interface ContratoRepository extends JpaRepository<Contrato,Long> {

}
