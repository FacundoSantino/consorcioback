package com.ai.app.interfaceService;

import com.ai.app.exceptions.PersonaException;
import com.ai.app.negocio.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IPersonaService {

    Iterable<Persona> findAll();

    void guardar(Persona persona);
    Page<Persona> findAll(Pageable pageable);

    Optional<Persona> findById(String documento);

    Persona update(Persona persona,String documento);

    void deleteById(String documento);



}
