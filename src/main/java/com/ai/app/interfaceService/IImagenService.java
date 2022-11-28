package com.ai.app.interfaceService;

import com.ai.app.negocio.Imagen;
import com.ai.app.negocio.Reclamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IImagenService {

    List<Imagen> findAll();

    Page<Imagen> findAll(Pageable pageable);

    Optional<Imagen> findById(Integer id);

    void update(Imagen imagen);

    void deleteById(Integer id);

    Imagen crearImagen(String path, String tipo, Reclamo reclamo);

    void guardarImagen(Imagen imagen);

    List<Imagen> leerImagenes();

    Imagen leerImagen(Integer id);

    Imagen actualizar(Imagen imagen, Integer id);
}
