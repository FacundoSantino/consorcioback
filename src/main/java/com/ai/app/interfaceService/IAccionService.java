package com.ai.app.interfaceService;

import com.ai.app.negocio.Accion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAccionService {

    public List<Accion> findAll();

    public Accion findById(Integer idAccion);

    public Accion save(Accion accion);

}
