package com.ai.app.service;

import com.ai.app.interfaceService.IUnidadService;
import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Persona;
import com.ai.app.negocio.Unidad;
import com.ai.app.repository.PersonaRepository;
import com.ai.app.repository.UnidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadService implements IUnidadService {

    private final UnidadRepository repositorio;
    private final PersonaRepository repoPerso;



    @Autowired
    public UnidadService(UnidadRepository repositorio, PersonaRepository repoPerso){
        this.repositorio=repositorio;
        this.repoPerso=repoPerso;
    }

    public Unidad crearUnidad(String numero, String piso, Edificio edificio){
        return  new Unidad(numero,piso,edificio);
    }

    @Override
    public Iterable<Unidad> findAll() {
        return repositorio.findAll();
    }

    @Override
    public Page<Unidad> findAll(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    @Override
    public Optional<Unidad> findById(Integer id) {
        return repositorio.findById(id);
    }


    public Unidad actualizar(Unidad unidad, Integer id){
        Unidad modificar=repositorio.getReferenceById(id);
        modificar=unidad;
        repositorio.save(modificar);
        return modificar;
    }

    public void agregarInquilino(Persona inquilino, Unidad unidad) {
        repoPerso.save(inquilino);
        unidad.agregarInquilino(inquilino);
        actualizar(unidad, unidad.getIdentificador()); //Antes tenía actualizar(unidad) ahora necesita id
    }

    public void agregarDuenio(Persona duenio, Unidad unidad) {
        repoPerso.save(duenio);
        unidad.agregarDuenio(duenio);
        actualizar(unidad, unidad.getIdentificador()); //Antes tenía actualizar(unidad) ahora necesita id
    }


    @Override
    public void deleteById(Integer id) {

        Unidad uni=repositorio.findById(id).get();

        uni.prepararDelete();

        actualizar(uni, uni.getIdentificador()); //Antes tenía actualizar(unidad) ahora necesita id

        repositorio.deleteById(id);

        List<String> aBorrar=repositorio.personasSinNada();

        for(String s:aBorrar){
            Optional<Persona> pers=repoPerso.findById(s);

            if(pers.isPresent()){
                if(pers.get().getUsuario()==null){
                    repoPerso.deleteById(s);
                }
            }
        }
    }

    public void delete(Unidad unidad) {
        deleteById(unidad.getIdentificador());
    }

    public Unidad buscarUnidad(String piso, String numero, Integer codigoEdificio) {

        List<Unidad> i=repositorio.findAll();

        for(Unidad u:i){
            if( u.getPiso().equals(piso)
                && u.getNumero().equals(numero)&&
                codigoEdificio==u.getEdificio().getCodigo()){
                return u;
            }
        }
        return null;

        //No considera los casos en que haya unidades identicas ya que
        //en primer lugar no debería haberlas

    }

}
