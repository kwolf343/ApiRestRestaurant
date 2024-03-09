package com.example.services;

import com.example.dao.MesaDao;
import com.example.models.ReservacionModel;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dao.ReservacionDao;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservacionServiceImp implements ReservacionService{
    @Autowired
    ReservacionDao reservacionesDao;
        
    @Transactional(readOnly=true)
    @Override
    public ArrayList<ReservacionModel> obtenerReservaciones(){
        return (ArrayList<ReservacionModel>) reservacionesDao.findAll();
    }
  
    @Transactional
    @Override
    public ReservacionModel guardarReservacion(ReservacionModel ReservacionM){
        return reservacionesDao.save(ReservacionM);
    }
    
    
    @Transactional(readOnly=true)
    @Override
    public Optional<ReservacionModel> obtenerPorId(Long id){
        return reservacionesDao.findById(id);
    }
    
    @Transactional
    @Override
    public boolean eliminarReservacion(Long id) {
        try {
            Optional<ReservacionModel> reservacionExistente = reservacionesDao.findById(id);
            if (reservacionExistente.isPresent()) {
                reservacionesDao.deleteById(id);
                return true; // Devuelve true si la eliminación fue exitosa
            } else {
                return false; // Devuelve false si el elemento no existe
            }
        } catch (Exception e) {
            return false; // Devuelve false si se produce una excepción
        }
    }
    
    @Transactional(readOnly=true)
    @Override
    public ArrayList<ReservacionModel> obtenerPorPersona(String persona){
        return reservacionesDao.findByPersona(persona);
    }
    
    @Transactional(readOnly=true)
    @Override
    public ArrayList<ReservacionModel> obtenerPorDuiPersona(String duiPersona){
        return reservacionesDao.findByDuiPersona(duiPersona);
    }
    
    @Transactional(readOnly=true)
    @Override
    public ArrayList<ReservacionModel> obtenerPorFechaReserva(String fechaReserva){
        return reservacionesDao.findByFechaReserva(fechaReserva);
    }
}
