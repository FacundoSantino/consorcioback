package com.ai.app.service;

import com.ai.app.interfaceService.IImagenService;
import com.ai.app.negocio.Imagen;
import com.ai.app.negocio.Reclamo;
import com.ai.app.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenService implements IImagenService {

    private final ImagenRepository repositorio;

    @Autowired
    public ImagenService(ImagenRepository repositorio) {
        this.repositorio = repositorio;
    }


    @Override
    public List<Imagen> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Page<Imagen> findAll(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Override
    public Optional<Imagen> findById(Integer id) {
        return repositorio.findById(id);
    }

    @Override
    public void update(Imagen imagen) {
        Imagen modificar=repositorio.getReferenceById(imagen.getNumero());
        modificar=imagen;
        repositorio.save(modificar);
    }

    @Override
    public void deleteById(Integer id) {
        repositorio.deleteById(id);
    }

    @Override
    public Imagen crearImagen(String path, String tipo, Reclamo reclamo) {
        return new Imagen(path,tipo,reclamo);
    }

    @Override
    public void guardarImagen(Imagen imagen) {
        repositorio.save(imagen);
    }

    @Override
    public List<Imagen> leerImagenes() {
        return repositorio.findAll();
    }

    @Override
    public Imagen leerImagen(Integer id) {
        Optional<Imagen> imagen=repositorio.findById(id);

        if(imagen.isPresent()){
            return imagen.get();
        }
        else{
            System.out.println("LA IMAGEN NO EXISTE");
            return null;

        }

    }

    @Override
    public Imagen actualizar(Imagen imagen, Integer id) {
        Imagen modificar=repositorio.getReferenceById(id);
        modificar=imagen;
        repositorio.save(modificar);
        return modificar;
    }
}
