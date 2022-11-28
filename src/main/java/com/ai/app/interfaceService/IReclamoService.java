package com.ai.app.interfaceService;

import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Persona;
import com.ai.app.negocio.Reclamo;
import com.ai.app.negocio.Unidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReclamoService {

    List<Reclamo> findAll();

    Page<Reclamo> findAll(Pageable pageable);

    Reclamo findById(Integer idReclamo);

    void borrarPorId(Integer id);

    Reclamo crearReclamo(   Persona reclamante,
                            Edificio edificio,
                            String ubicacion,
                            String descripcion,
                            Unidad unidad
                            );
    Reclamo crearReclamo(   Persona reclamante,
                            Edificio edificio,
                            String ubicacion,
                            String descripcion
    );

    void guardarReclamo(Reclamo reclamo);

    List<Reclamo> leerReclamos();

    Reclamo leerReclamo(Integer idReclamo);

    Reclamo actualizar(Reclamo reclamo, Integer id);
}
