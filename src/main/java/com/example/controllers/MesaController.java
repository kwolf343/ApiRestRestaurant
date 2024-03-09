package com.example.controllers;

import com.example.dtos.ApiResponse;
import com.example.models.MesaModel;
import com.example.services.MesaService;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    MesaService mesaS;

    @GetMapping()
    public ArrayList<MesaModel> obtenerMesas() {
        return (ArrayList<MesaModel>) mesaS.obtenerMesas();
    }

    @GetMapping(path = "/{id}")
    public Optional<MesaModel> obtenerMesaPorId(@PathVariable("id") Long id) {
        return this.mesaS.obtenerPorId(id);
    }

    @PostMapping()
    public ApiResponse guardarMesa(@RequestBody MesaModel mesa) {
        MesaModel m = this.mesaS.guardarMesa(mesa);
        return new ApiResponse(200, "Reservacion guardada correctamente", m, "/mesas");
    }

    @PutMapping("/{id}")
    public ApiResponse actualizarMesa(@PathVariable("id") Long id, @RequestBody MesaModel mesa) {
        Optional<MesaModel> mesaExistente = this.mesaS.obtenerPorId(id);
        if (mesaExistente.isPresent()) {
            mesaExistente.get();
            MesaModel m = new MesaModel();
            m.setId(id);
            m.setCapacidad(mesa.getCapacidad());
            try {
                return new ApiResponse(200, "Reservacion actualizada correctamente", this.mesaS.guardarMesa(m), "/mesas");
            } catch (Exception e) {
                return new ApiResponse(400, "Ocurrió un error al intentar actualizar la mesa con id " + id + " Error: " + e.getMessage(), null, "/mesas");
            }
        } else {
            return new ApiResponse(400, "No se encontró la mesa con id " + id, null, "/mesas");
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse eliminarPorId(@PathVariable("id") Long id) {
        Optional<MesaModel> m = this.mesaS.obtenerPorId(id);
        if (m.isPresent()) {
            boolean ok = this.mesaS.eliminarMesa(id);
            if (ok) {
                return new ApiResponse(200, "Se eliminó la mesa con id " + id, m.get(), "/mesas");
            } else {
                return new ApiResponse(400, "No se pudo eliminar la mesa con id " + id, false, "/mesas");
            }
        } else {
            return new ApiResponse(400, "No se encontró la mesa con id " + id, false, "/mesas");
        }
    }
}
