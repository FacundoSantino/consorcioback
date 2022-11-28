package com.ai.app.service;

import com.ai.app.interfaceService.IEdificioService;
import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Unidad;
import com.ai.app.repository.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EdificioService implements IEdificioService {

    private final EdificioRepository repositorio;

    private final UnidadService service;

    @Autowired
    public EdificioService(EdificioRepository repositorio, UnidadService service){
        this.repositorio=repositorio;
        this.service=service;
    }


    @Override
    public List<Edificio> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Page<Edificio> findAll(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Override
    public Edificio findById(Integer id) {
        Optional<Edificio> oEdificio= repositorio.findById(id);

        if(oEdificio.isPresent()){
            return oEdificio.get();
        }

        return null;
    }

    @Override
    public Edificio update(Edificio edificio,Integer id) {
        Edificio modificar=repositorio.getReferenceById(id);
        modificar=edificio;
        repositorio.save(modificar);
        return modificar;
    }



    @Override
    public void deleteById(Integer id) {
        Optional<Edificio> oEdificio=repositorio.findById(id);
        if(oEdificio.isPresent()) {
            delete(oEdificio.get());
        }
        else{
            System.out.println("No existe el edificio");
        }
    }

    public void delete(Edificio edificio){
        if(repositorio.existsById(edificio.getCodigo())){
            List<Unidad> unidades=edificio.getUnidades();
            for(Unidad u: unidades){
                service.delete(u);
            }

            repositorio.delete(edificio);

        }
        else {
            System.out.println("No existe el edificio");
        }
    }

    @Override
    public Edificio crearEdificio(String nombre, String direccion) {
        return new Edificio(nombre,direccion);
    }

    @Override
    public Edificio crearEdificio(String nombre, String direccion, List<Unidad> unidades) {
        return new Edificio(nombre,direccion,unidades);
    }

    public Edificio crearEdificio(Edificio edificio){
        return new Edificio(edificio.getNombre(),edificio.getDireccion(),edificio.getUnidades());
    }

    @Override
    public Edificio crearEdificio(String nombre, String direccion, Unidad unidad) {
        return new Edificio(nombre,direccion,unidad);
    }

    @Override
    public void save(Edificio edificio) {

        repositorio.save(edificio);

    }

    @Override
    public void agregarUnidad(Edificio edificio, Unidad unidad) {
        edificio.agregarUnidad(unidad);
        update(edificio,edificio.getCodigo());
    }

    @Override
    public Edificio buscarEdificio(String nombre, String direccion) {

        List<Edificio> edificios=findAll();

        for(Edificio e: edificios){
            if(e.getNombre().equals(nombre) && e.getDireccion().equals(direccion)){
                return e;
            }
        }

        return null;

    }
}
