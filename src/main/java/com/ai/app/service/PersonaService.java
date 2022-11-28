package com.ai.app.service;

import com.ai.app.interfaceService.IPersonaService;
import com.ai.app.negocio.Persona;
import com.ai.app.repository.PersonaRepository;
import com.ai.app.repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService implements IPersonaService {


    private final PersonaRepository repositorio;
    private final UnidadRepository uni;

    @Autowired
    public PersonaService(PersonaRepository repositorio, UnidadRepository uni){
        this.repositorio=repositorio;
        this.uni=uni;
    }

    @Override
    public Iterable<Persona> findAll() {
        return repositorio.findAll();
    }

    @Override
    public void guardar(Persona persona) {
        repositorio.save(persona);
    }

    @Override
    public Page<Persona> findAll(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Override
    public Optional<Persona> findById(String documento) {
        return repositorio.findById(documento);
    }

    //El metodo save no se usa para guardar ya que la persona se guarda en unidad sea inquilino o duenio y se guarda o updatea la unidad
    public Persona update(Persona persona,String documento){
        Persona modificar=repositorio.getReferenceById(documento);
        modificar=persona;
        repositorio.save(modificar);
        return modificar;
    }


    @Override
    public void deleteById(String documento) {
        repositorio.deleteById(documento);
    }

    public void borrar(Persona persona){

        List<Integer> inq=repositorio.traerUnidadesInquilinos(persona.getDocumento());
        List<Integer> due=repositorio.traerUnidadesDuenios(persona.getDocumento());

        for(Integer i:inq){

            uni.findById(i).get().sacarInquilino(persona);

        }

        for(Integer d: due){

            uni.findById(d).get().sacarDuenio(persona);

        }

        repositorio.deleteById(persona.getDocumento());



    }

    public Persona buscarPorUsuario(String usuario){

        Iterable<Persona> personas=findAll();

        for(Persona p:personas) {
            if (p.getUsuario() != null) {
                if (p.getUsuario().equals(usuario)) {
                    return p;
                }
            }
        }
        return null;

    }

    public Persona crearPersona(String documento, String nombre) {
        return new Persona(documento,nombre);
    }
}
