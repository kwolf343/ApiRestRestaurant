package com.example.dtos;

import com.example.models.MesaModel;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class reservacionDto {
    private Long id;
    private String persona;
    private String duiPersona;
    private Date fechaReservacion;
    private String fechaReserva;
    private String horaReserva;
    private Long numPersonas;
    private boolean activa;
    private List<MesaModel> mesas;
}
