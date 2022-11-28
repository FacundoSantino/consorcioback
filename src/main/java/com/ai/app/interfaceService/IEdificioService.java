package com.ai.app.interfaceService;


import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Unidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEdificioService {

    List<Edificio> findAll();

    Page<Edificio> findAll(Pageable pageable);

    Edificio findById(Integer id);

    Edificio update(Edificio edificio,Integer id);

    void deleteById(Integer id);

    Edificio crearEdificio(String nombre, String direccion);

    Edificio crearEdificio(String nombre, String direccion, List<Unidad> unidades);

    Edificio crearEdificio(String nombre, String direccion, Unidad unidad);

    void save(Edificio edificio);

    void agregarUnidad(Edificio edificio, Unidad unidad);

    Edificio buscarEdificio(String nombre, String direccion);
}
