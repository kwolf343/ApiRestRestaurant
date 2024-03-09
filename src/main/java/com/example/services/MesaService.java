package com.example.services;

import com.example.models.MesaModel;
import java.util.ArrayList;
import java.util.Optional;

public interface MesaService {
	public ArrayList<MesaModel> obtenerMesas();
	public MesaModel guardarMesa(MesaModel MesaM);
	public Optional<MesaModel> obtenerPorId(Long id);
	public boolean eliminarMesa(Long id);
}
