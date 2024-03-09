package com.example.dao;

import com.example.models.MesaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaDao extends JpaRepository<MesaModel, Long>{}
