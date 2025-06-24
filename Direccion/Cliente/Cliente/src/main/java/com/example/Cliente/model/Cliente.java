package com.example.Cliente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@Table (name = "usuario")

@NoArgsConstructor
public class Cliente {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long Id;

    @Column (nullable = false) 
    private String Pnombre;

    @Column (nullable = false) 
    private String Apaterno;

    @Column (nullable = false) 
    private String contrasena;

    @Column (nullable = false) 
    private String correo;






















}
