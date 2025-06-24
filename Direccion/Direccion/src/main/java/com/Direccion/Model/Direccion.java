package com.Direccion.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Direccion")


public class Direccion {


     @Id
    private Long Id_dir;

    @Column (nullable = false)

    private Long Id_reg;

    @Column (nullable = false)

    private Long Id_com;

     @Column (nullable = false)
     private String nom_dir;



}
