package com.example.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "RESERVACIONES_MESAS")
public class ReservacionMesaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "RESERVACION_ID")
    private ReservacionModel reservacion;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "MESA_ID")
    private MesaModel mesa;
}
