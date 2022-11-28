package com.ai.app.interfaceService;

import com.ai.app.negocio.Unidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUnidadService {

    Iterable<Unidad> findAll();

    Page<Unidad> findAll(Pageable pageable);

    Optional<Unidad> findById(Integer id);

    void deleteById(Integer id);

}
