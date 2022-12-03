package com.ai.app.controller;


import com.ai.app.exceptions.EdificioException;
import com.ai.app.negocio.Edificio;
import com.ai.app.negocio.Entidad;
import com.ai.app.negocio.Persona;
import com.ai.app.negocio.Unidad;
import com.ai.app.service.EdificioService;
import com.ai.app.service.PersonaService;
import com.ai.app.service.UnidadService;
import com.ai.app.views.EdificioView;
import com.ai.app.views.PersonaView;
import com.ai.app.views.UnidadView;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="/api/edificios")
public class EdificioController {


    private EdificioService service;

    private UnidadService uS;
    private final AccionController aC;
    private final PersonaService pS;


    @Autowired
    public EdificioController(EdificioService eS, UnidadService us,AccionController aC, PersonaService pS){
        this.service=eS;
        this.uS=us;
        this.aC = aC;
        this.pS = pS;
    }

    //C
    @PostMapping(params={"nombre","direccion","usuario"})
    public ResponseEntity<?> crearEdificio(@RequestParam String nombre, @RequestParam String direccion,@RequestParam String usuario){
        Edificio edificio = service.crearEdificio(nombre, direccion);
        service.save(edificio);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "crearEdificio", Entidad.edificio, ""+edificio.getCodigo(),
        		"El usuario "+usuario+" creo el edificio "+edificio.getCodigo()+" exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(edificio);
    }

    //R

    @GetMapping(path="/{id}",params={"usuario"})
    public @ResponseBody ResponseEntity<?> leerEdificio(@PathVariable Integer id, @RequestParam String usuario){
        Edificio edificio=service.findById(id);
        if(edificio==null){
        	aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "leerEdificio", Entidad.edificio, ""+id,
            		"El usuario "+usuario+" intentó buscar el edificio "+id+" pero este no existe.");
            return ResponseEntity.notFound().build();
        }
        else{
        	aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "leerEdificio", Entidad.edificio, ""+edificio.getCodigo(),
            		"El usuario "+usuario+" buscó el edificio "+edificio.getCodigo()+" y obtuvo su información.");
        	return new ResponseEntity<EdificioView>(edificio.toView(), null, HttpStatus.OK );
        }
    }

    @GetMapping(params={"usuario"})
    public List<EdificioView> leerEdificios(@RequestParam String usuario){
        List<Edificio> lista=service.findAll();
        List<EdificioView> lista2=new ArrayList<>();
        for(Edificio e:lista){
            lista2.add(e.toView());
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "leerEdificios", Entidad.edificio, "N/A",
        		"El usuario "+usuario+" busco todos los edificios y obtuvo su información.");
        return StreamSupport
                .stream(lista2.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/unidades",params={"codigo","usuario"})
    public List<UnidadView> getUnidadesPorEdificio(@RequestParam int codigo,@RequestParam String usuario) throws EdificioException {
        List<UnidadView> resultado = new ArrayList<UnidadView>();
        Edificio edificio = service.findById(codigo);
        List<Unidad> unidades = edificio.getUnidades();
        for(Unidad unidad : unidades)
            resultado.add(unidad.toView());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "getUnidadesPorEdificio", Entidad.edificio, ""+codigo,
        		"El usuario "+usuario+" busco las unidades del edificio "+codigo+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/habilitados",params={"codigo","usuario"})
    public List<PersonaView> habilitadosPorEdificio(@RequestParam int codigo,@RequestParam String usuario) throws EdificioException{
        List<PersonaView> resultado = new ArrayList<PersonaView>();
        Edificio edificio = service.findById(codigo);
        Set<Persona> habilitados = edificio.habilitados();
        for(Persona persona : habilitados)
            resultado.add(persona.toView());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "habilitadosPorEdificio", Entidad.edificio, ""+codigo,
        		"El usuario "+usuario+" busco los habilitados del edificio "+codigo+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/duenios",params={"codigo","usuario"})
    public List<PersonaView> dueniosPorEdificio(@RequestParam int codigo, @RequestParam String usuario) throws EdificioException{
        List<PersonaView> resultado = new ArrayList<PersonaView>();
        Edificio edificio = service.findById(codigo);
        Set<Persona> duenios = edificio.duenios();
        for(Persona persona : duenios)
            resultado.add(persona.toView());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "dueniosPorEdificio", Entidad.edificio, ""+codigo,
        		"El usuario "+usuario+" busco los dueños del edificio "+codigo+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/habitantes",params={"codigo","usuario"})
    public List<PersonaView> habitantesPorEdificio(@RequestParam int codigo,@RequestParam String usuario) throws EdificioException{
        List<PersonaView> resultado = new ArrayList<PersonaView>();
        Edificio edificio = service.findById(codigo);
        Set<Persona> habitantes = edificio.duenios();
        for(Persona persona : habitantes)
            resultado.add(persona.toView());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "habitantesPorEdificio", Entidad.edificio, ""+codigo,
        		"El usuario "+usuario+" busco los habitantes del edificio "+codigo+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }

    //U

    @PutMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> updateEdificio(@RequestBody Edificio edificio,@PathVariable Integer id,@RequestParam String usuario){
    	aC.guardarAccion(pS.findById(usuario).get(), LocalDateTime.now(), "updateEdificio", Entidad.edificio, ""+id,
        		"El usuario "+usuario+" actualizó el edificio"+id+"  exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.update(edificio,id));
    }

    @PutMapping(path="/{codigo}",params={"usuario","nombre","direccion"})
    public ResponseEntity<?> actualizarEdificio(@RequestParam String usuario,@RequestParam String nombre,@RequestParam String direccion, @PathVariable int codigo){
        Edificio oE=service.findById(codigo);
        if(oE!=null){
            oE.setNombre(nombre);
            oE.setDireccion(direccion);
            service.save(oE);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    //D
    @DeleteMapping(path="/{id}",params= {"usuario"})
    public ResponseEntity<?> borrarEdificio(@PathVariable Integer id,@RequestParam String usuario){
        if(service.findById(id)==null){
        	aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "borrarEdificio", Entidad.edificio, ""+id,
            		"El usuario "+usuario+" intentó borrar el edificio "+id+"  pero este no existe.");
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "borrarEdificio", Entidad.edificio, ""+id,
        		"El usuario "+usuario+" borró el edificio "+id+"  exitosamente.");
        return ResponseEntity.ok().build();
    }

    @PostMapping(path="/unidades",params={"numero","piso","edificioId","usuario"})
    public ResponseEntity<?> agregarUnidad(@RequestParam String numero, @RequestParam String piso, @RequestParam Integer edificioId,@RequestParam String usuario){
        Edificio edificio = service.findById(edificioId);
        Unidad unidad = uS.crearUnidad(numero, piso, edificio);
        edificio.agregarUnidad(unidad);
        service.update(edificio,edificioId);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(), "agregarUnidad", Entidad.edificio, ""+edificioId,
        		"El usuario "+usuario+" agregó la unidad del piso "+piso+" con numero "+numero+" al edificio "+edificioId+"  exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(unidad);
    }

}
