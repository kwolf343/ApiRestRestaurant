package com.example.services;

import com.example.dao.MesaDao;
import com.example.dao.ReservacionDao;
import com.example.models.ReservacionMesaModel;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dao.ReservacionMesaDao;
import com.example.models.MesaModel;
import com.example.models.ReservacionModel;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservacionMesaServiceImp implements ReservacionMesaService{
    @Autowired
    ReservacionMesaDao reservacionMesaDao;
    
    @Autowired
    MesaDao MesaDao;
    
    @Autowired
    ReservacionDao reservacionDao;
    
    @Transactional(readOnly=true)
    @Override
    public ArrayList<ReservacionMesaModel> obtenerReservacionMesas(){
        return (ArrayList<ReservacionMesaModel>) reservacionMesaDao.findAll();
    }

    @Transactional
    @Override
    public ReservacionMesaModel guardarReservacionMesa(ReservacionMesaModel reservacionMesaModel) {
        if (reservacionMesaModel.getMesa() == null) {
            throw new IllegalArgumentException("La mesa no se proporcionó correctamente en la reservación.");
        }
        if (reservacionMesaModel.getMesa().getId() != null) {
            Optional<MesaModel> optionalMesa = MesaDao.findById(reservacionMesaModel.getMesa().getId());
            if (!optionalMesa.isPresent()) {
                throw new IllegalArgumentException("Mesa no encontrada con el ID proporcionado.");
            }
            reservacionMesaModel.setMesa(optionalMesa.get());
        } else {
            reservacionMesaModel.setMesa(MesaDao.save(reservacionMesaModel.getMesa()));
        }
        if (reservacionMesaModel.getReservacion().getId() != null) {
            Optional<ReservacionModel> optionalReservacion = reservacionDao.findById(reservacionMesaModel.getReservacion().getId());
            if (!optionalReservacion.isPresent()) {
                throw new IllegalArgumentException("Reservación no encontrada con el ID proporcionado.");
            }
            reservacionMesaModel.setReservacion(optionalReservacion.get());
        } else {
            reservacionMesaModel.setReservacion(reservacionDao.save(reservacionMesaModel.getReservacion()));
        }
        return reservacionMesaDao.save(reservacionMesaModel);
    }
    
    @Transactional(readOnly=true)
    @Override
    public Optional<ReservacionMesaModel> obtenerPorId(Long id){
        return reservacionMesaDao.findById(id);
    }
    
    @Transactional
    @Override
    public boolean eliminarReservacionMesa(Long id){
        try{
            reservacionMesaDao.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
