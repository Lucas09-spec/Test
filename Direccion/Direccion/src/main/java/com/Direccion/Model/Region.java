package com.Direccion.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Region")




public class Region {

       @Id
    private Long Id_reg;

    @Column (nullable = false)
    private String Nom_reg;




}
