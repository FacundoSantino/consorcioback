package com.ai.app.service;

import com.ai.app.controller.UnidadController;
import com.ai.app.interfaceService.IReclamoService;
import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Persona;
import com.ai.app.negocio.Reclamo;
import com.ai.app.negocio.Unidad;
import com.ai.app.repository.ImagenRepository;
import com.ai.app.repository.ReclamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReclamoService implements IReclamoService {

    private final ReclamoRepository repositorio;

    private final ImagenRepository imag;

    private final UnidadController uController;

    @Autowired
    public ReclamoService(ReclamoRepository repositorio,ImagenRepository imag,UnidadController uController){
        this.repositorio=repositorio;
        this.imag=imag;
        this.uController=uController;
    }


    @Override
    public List<Reclamo> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Page<Reclamo> findAll(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Override
    public Reclamo findById(Integer idReclamo) {
        Optional<Reclamo> oReclamo=repositorio.findById(idReclamo);

        if(oReclamo.isPresent()){
            return oReclamo.get();
        }

        return null;

    }

    @Override
    public void borrarPorId(Integer id) {
        Optional<Reclamo> aBorrar=repositorio.findById(id);
        if(aBorrar.isPresent()){
            repositorio.deleteById(id);
        }

    }

    @Override
    public Reclamo crearReclamo(Persona reclamante, Edificio edificio, String ubicacion, String descripcion, Unidad unidad) {
        if(reclamante.isAdministrador()){
            return new Reclamo(reclamante,edificio,ubicacion,descripcion,unidad);
        }
        if(unidad.getInquilinos().isEmpty()){
            List<Persona> duenios=unidad.getDuenios();
            for(Persona d:duenios){
                if(d.getDocumento().equals(reclamante.getDocumento())){
                    return new Reclamo(reclamante,edificio,ubicacion,descripcion,unidad);
                }
            }
        }
        else{
            List<Persona> inquilinos=unidad.getInquilinos();
            for(Persona i:inquilinos){
                if(i.getDocumento().equals(reclamante.getDocumento())){
                    return new Reclamo(reclamante,edificio,ubicacion,descripcion,unidad);
                }
            }
        }
        return null;
        //En este caso, la persona no tiene permisos y se devuelve null por lo que no se creará nada
    }

    @Override
    public Reclamo crearReclamo(Persona reclamante, Edificio edificio, String ubicacion, String descripcion) {

        if(reclamante.isAdministrador()){
            return new Reclamo(reclamante,edificio,ubicacion,descripcion);
        }

        List<Unidad> unidades=edificio.getUnidades();
        for(Unidad u:unidades){
            List<Persona> duenios=u.getDuenios();
            List<Persona> inquilinos=u.getInquilinos();
            for(Persona d:duenios){
                if(d.getDocumento().equals(reclamante.getDocumento())){
                    return new Reclamo(reclamante,edificio,ubicacion,descripcion);
                }
            }
            for(Persona i:inquilinos){
                if(i.getDocumento().equals(reclamante.getDocumento())){
                    return new Reclamo(reclamante,edificio,ubicacion,descripcion);
                }
            }
        }
        return null;
    }

    @Override
    public void guardarReclamo(Reclamo reclamo) {
        repositorio.save(reclamo);
    }

    @Override
    public List<Reclamo> leerReclamos() {
        return repositorio.findAll();
    }

    @Override
    public Reclamo leerReclamo(Integer idReclamo) {
        Optional<Reclamo> oReclamo=repositorio.findById(idReclamo);

        if(oReclamo.isPresent()){
            return oReclamo.get();
        }

        return null;
    }

    @Override
    public Reclamo actualizar(Reclamo reclamo, Integer id) {
        Reclamo modificar=repositorio.getReferenceById(id);
        modificar=reclamo;
        repositorio.save(modificar);
        return modificar;
    }
}
