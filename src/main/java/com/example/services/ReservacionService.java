package com.example.services;

import com.example.models.ReservacionModel;
import java.util.ArrayList;
import java.util.Optional;

public interface ReservacionService {
	public ArrayList<ReservacionModel> obtenerReservaciones();
	public ReservacionModel guardarReservacion(ReservacionModel ReservacionM);
	public Optional<ReservacionModel> obtenerPorId(Long id);
	public boolean eliminarReservacion(Long id);
	public ArrayList<ReservacionModel> obtenerPorPersona(String persona);
	public ArrayList<ReservacionModel> obtenerPorDuiPersona(String duiPersona);
	public ArrayList<ReservacionModel> obtenerPorFechaReserva(String fechaReserva);
}