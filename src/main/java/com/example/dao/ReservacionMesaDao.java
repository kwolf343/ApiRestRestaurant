package com.example.dao;

import com.example.models.ReservacionMesaModel;
import com.example.models.ReservacionModel;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservacionMesaDao extends JpaRepository<ReservacionMesaModel, Long>{
    public abstract ArrayList<ReservacionMesaModel> findByReservacion(ReservacionModel reservacion);
}
