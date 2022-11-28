package com.ai.app.service;

import com.ai.app.interfaceService.IAccionService;
import com.ai.app.negocio.Accion;
import com.ai.app.repository.AccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccionService implements IAccionService {

    private final AccionRepository repositorio;

    @Autowired
    public AccionService(AccionRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Accion> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Accion findById(Integer idAccion) {
        Optional<Accion> oAccion = repositorio.findById(idAccion);
        if (oAccion.isPresent()) {
            return oAccion.get();
        }

        return null;
    }

    @Override
    public Accion save(Accion accion) {
        return repositorio.save(accion);
    }
}