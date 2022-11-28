package com.ai.app.repository;

import com.ai.app.negocio.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository  extends JpaRepository<Imagen,Integer> {
}
