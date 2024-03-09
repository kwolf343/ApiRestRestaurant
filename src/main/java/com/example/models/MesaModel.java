package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="MESAS")
public class MesaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", unique=true, nullable = false)
    private Long id;
    
    @Column(name="CAPACIDAD")
    private Long capacidad;
}
