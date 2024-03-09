package com.example.controllers;

import com.example.dtos.ApiResponse;
import com.example.dtos.reservacionDto;
import com.example.models.MesaModel;
import com.example.models.ReservacionMesaModel;
import com.example.models.ReservacionModel;
import com.example.services.MesaService;
import com.example.services.ReservacionMesaService;
import com.example.services.ReservacionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservacion")
public class ReservacionController {

    @Autowired
    ReservacionService reservacionS;

    @Autowired
    ReservacionMesaService reservaMesaS;

    @Autowired
    MesaService mesaS;

    @GetMapping()
    public ArrayList<reservacionDto> obtenerReservaciones() {
        List<ReservacionModel> reservaciones = reservacionS.obtenerReservaciones();
        return (ArrayList<reservacionDto>) reservaciones.stream()
                .map(this::convertirReservacionADto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ArrayList<reservacionDto> obtenerResservacionPorId(@PathVariable("id") Long id) {
        Optional<ReservacionModel> reservaciones = this.reservacionS.obtenerPorId(id);
        return (ArrayList<reservacionDto>) reservaciones.stream()
                .map(this::convertirReservacionADto)
                .collect(Collectors.toList());
    }

    @GetMapping("/query")
    public ArrayList<reservacionDto> obtenerResservacionPorFecha(@RequestParam("fechaReserva") String fechaReserva){
        ArrayList<ReservacionModel> reservaciones = this.reservacionS.obtenerPorFechaReserva(fechaReserva);
        return (ArrayList<reservacionDto>) reservaciones.stream()
                .map(this::convertirReservacionADto)
                .collect(Collectors.toList());
    }
    
    @PostMapping()
    public ApiResponse guardarReservacion(@RequestBody reservacionDto reservacion) {
        ReservacionModel retorno = convertirDtoAReservacion(reservacion);
        //Si no vienen mesas en la reservacion
        if (reservacion.getMesas() == null) {
            ReservacionModel retornoReservacionModel = this.reservacionS.guardarReservacion(retorno);
            if (retornoReservacionModel == null) {
                return new ApiResponse(400, "La reservacion no pudo guardarse, revise la estructura del json", null, "/reservacion");
            }
            return new ApiResponse(200, "Reservacion guardada correctamente", retorno, "/reservacion");
        }
        //todas las mesas en la lista deben tener id
        if (!reservacion.getMesas().stream().allMatch(mesa -> mesa.getId() != null)) {
            return new ApiResponse(400, "Las mesas no se proporcionaron correctamente en la reservación.", null, "/reservacion");
        }
        //Revisar si los id en la lista de mesas que se reciben, existen en la lista de mesas almacenadas
        ArrayList<MesaModel> mesasEnBd = mesaS.obtenerMesas();
        ArrayList<MesaModel> mesasEnPost = (ArrayList<MesaModel>) reservacion.getMesas();

        for (MesaModel mesaPost : mesasEnPost) {
            boolean existeEnBd = false;
            for (MesaModel mesaBd : mesasEnBd) {
                if (mesaPost.getId() != null && mesaPost.getId().equals(mesaBd.getId())) {
                    // La mesa en la solicitud POST existe en la base de datos
                    existeEnBd = true;
                    break;
                }
            }
            if (!existeEnBd) {
                return new ApiResponse(400, "Las mesas proporcionadas no se econtraron en los registros", null, "/reservacion");
            }
        }
        //En este punto, verificamos que la reservacion y las mesas si son validas
        ReservacionModel retornoR = this.reservacionS.guardarReservacion(retorno);
        if (retornoR == null) {
            return new ApiResponse(400, "La reservacion no pudo guardarse, revise la estructura del json", null, "/reservacion");
        }
        //guardando todas las mesas en la reservacion
        boolean mesasGuardadas = true;
        for (MesaModel mesaPost : mesasEnPost) {
            ReservacionMesaModel reservacionMesaModel = new ReservacionMesaModel();
            reservacionMesaModel.setReservacion(retornoR);
            reservacionMesaModel.setMesa(mesaPost);
            try {
                reservaMesaS.guardarReservacionMesa(reservacionMesaModel);
            } catch (IllegalArgumentException e) {
                mesasGuardadas = false;
            }
        }
        if (!mesasGuardadas) {
            return new ApiResponse(400, "Las mesas presentaron error", null, "/reservacion");
        }
        return new ApiResponse(200, "Reservacion guardada correctamente", true, "/reservacion");
    }

    @DeleteMapping("/{id}")
    public ApiResponse eliminarPorId(@PathVariable("id") Long id) {
        boolean ok = this.reservacionS.eliminarReservacion(id);
        if (ok) {
            return new ApiResponse(200, "Se eliminó la reservacion con id " + id, null, "/reservacion");
        } else {
            return new ApiResponse(400, "No se pudo eliminar la reservacion con id " + id, null, "/reservacion");
        }
    }

    @PutMapping("/{id}")
    public ApiResponse actualizarReservacion(@PathVariable("id") Long id, @RequestBody reservacionDto reservacionActualizada) {
        Optional<ReservacionModel> reservacionExistenteOpt = reservacionS.obtenerPorId(id);
        if (reservacionExistenteOpt.isPresent()) {
            //Si la reservación existe la actualizamos
            ReservacionModel retorno;
            try {
                retorno = reservacionS.guardarReservacion(convertirDtoAReservacion(reservacionActualizada));
            } catch (IllegalArgumentException e) {
                return new ApiResponse(400, e.getMessage() + id, null, "/reservacion");
            }
            List<MesaModel> mesasParaActualizar = reservacionActualizada.getMesas();
            List<ReservacionMesaModel> reservacionesMesas = reservacionExistenteOpt.get().getReservacionMesa();
            for (int i = 0; i < reservacionesMesas.size(); i++) {
                reservaMesaS.eliminarReservacionMesa(reservacionesMesas.get(i).getId());
            }
            if (mesasParaActualizar != null) {
                for (int i = 0; i < mesasParaActualizar.size(); i++) {
                    ReservacionMesaModel RMM = new ReservacionMesaModel();
                    RMM.setMesa(mesasParaActualizar.get(i));
                    RMM.setReservacion(convertirDtoAReservacion(reservacionActualizada));
                    reservaMesaS.guardarReservacionMesa(RMM);
                }
            }

            return new ApiResponse(200, "Se actualizó la reservación con id " + id, true, "/reservacion");
        } else {
            return new ApiResponse(400, "No se encontró la reservación con id " + id, null, "/reservacion");
        }
    }

    private ReservacionModel convertirDtoAReservacion(reservacionDto reservacion) {
        ReservacionModel retorno = new ReservacionModel();
        retorno.setId(reservacion.getId());
        retorno.setPersona(reservacion.getPersona());
        retorno.setDuiPersona(reservacion.getDuiPersona());
        retorno.setFechaReservacion(reservacion.getFechaReservacion());
        retorno.setFechaReserva(reservacion.getFechaReserva());
        retorno.setHoraReserva(reservacion.getHoraReserva());
        retorno.setNumPersonas(reservacion.getNumPersonas());
        retorno.setActiva(reservacion.isActiva());
        return retorno;
    }

    private reservacionDto convertirReservacionADto(ReservacionModel reservacion) {
        reservacionDto retorno = new reservacionDto();
        retorno.setId(reservacion.getId());
        retorno.setPersona(reservacion.getPersona());
        retorno.setDuiPersona(reservacion.getDuiPersona());
        retorno.setFechaReservacion(reservacion.getFechaReservacion());
        retorno.setFechaReserva(reservacion.getFechaReserva());
        retorno.setHoraReserva(reservacion.getHoraReserva());
        retorno.setNumPersonas(reservacion.getNumPersonas());
        retorno.setActiva(reservacion.isActiva());
        List<ReservacionMesaModel> reservacionMesas = reservacion.getReservacionMesa();
        List<MesaModel> Mesa = new ArrayList<>();
        for (ReservacionMesaModel reservacionMesa : reservacionMesas) {
            Mesa.add(reservacionMesa.getMesa());
        }
        retorno.setMesas(Mesa);
        return retorno;
    }
}
