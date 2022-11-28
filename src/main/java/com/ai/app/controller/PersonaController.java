package com.ai.app.controller;

import com.ai.app.negocio.Entidad;
import com.ai.app.negocio.Persona;
import com.ai.app.service.PersonaService;
import com.ai.app.views.PersonaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping(value="/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    private final PersonaService service;

    private final AccionController aC;

    @Autowired
    public PersonaController(PersonaService personaService, AccionController aC){
        this.service=personaService;
        this.aC=aC;
    }



    //C

    //Las personas no se guardan, se agregan a la lista de inquilinos o duenios de una unidad y se guarda la unidad

    @GetMapping(params={"usuario","contrasenia"})
    public ResponseEntity<?> login(@RequestParam String usuario, @RequestParam String contrasenia){
        Iterable<Persona> personas=service.findAll();
        for(Persona p:personas){
            if(p.getUsuario()!=null){
                if(p.getUsuario().equals(usuario)){
                    if(p.getContrasenia().equals(contrasenia)){
                        Persona persona=service.buscarPorUsuario(usuario);
                        aC.guardarAccion(persona, LocalDateTime.now(),"login", Entidad.persona,persona.getDocumento(),"El usuario "+usuario+" se logeó" +
                                " exitosamente.");
                        return ResponseEntity.ok(p.getDocumento()+"/"+(p.isAdministrador() ? "1" : "0"));
                    }
                    aC.guardarAccion(null, LocalDateTime.now(),"login", Entidad.persona,"N/A","Alguien intentó loguearse con el usuario de "+usuario+" pero este puso mal la contrasenia.");
                    return ResponseEntity.ok(" /2");
                }
            }
        }
        aC.guardarAccion(null, LocalDateTime.now(),"login", Entidad.persona,"N/A","Alguien intentó loguearse con el usuario de "+usuario+" pero este no existía.");
        return ResponseEntity.ok(" /3");
    }


    @PostMapping(params={"documento","nombre","usuario"})
    public ResponseEntity<PersonaView> agregarPersona(@RequestParam String documento, @RequestParam String nombre,@RequestParam String usuario) {
        Optional<Persona> check=service.findById(documento);
        if(check.isEmpty()){
            Persona persona = service.crearPersona(documento, nombre);
            service.guardar(persona);
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarPersona", Entidad.persona,documento,"El usuario "+usuario+" agregó a la persona de documento "+documento+".");
            return ResponseEntity.ok(persona.toView());
        }
        aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarPersona"
                , Entidad.persona,"N/A","El usuario "+usuario+" intentó agregar a la persona de documento "+documento+" pero esta ya existía.");
        return ResponseEntity.badRequest().build();
    }

    //R

    @GetMapping(path = "/{documento}",params={"usuario"})
    public ResponseEntity<?> leerPersona(@PathVariable String documento,@RequestParam String usuario){
        Optional<Persona> persona=service.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"leerPersona"
                    , Entidad.persona,"N/A","El usuario "+usuario+" intentó leer los datos de" +
                            " la persona de documento "+documento+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        else{
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"leerPersona"
                    , Entidad.persona,documento,"El usuario "+usuario+" leyó los datos de" +
                            " la persona de documento "+documento+".");
            return ResponseEntity.ok(persona.get().toView());
        }
    }

    @GetMapping(params={"usuario"})
    public List<PersonaView> leerPersonas(@RequestParam String usuario){
        Iterable<Persona> lista = service.findAll();
        List<PersonaView> lista2=new ArrayList<>();
        for(Persona e: lista) {
            lista2.add(e.toView());
        }
        aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"leerPersonas"
                , Entidad.persona,"N/A","El usuario "+usuario+" consultó los datos de todas las personas.");
        return StreamSupport
                .stream(lista2.spliterator(), false)
                .collect(Collectors.toList());
    };

    //U

    @PutMapping(params= {"documento","usuario"})
    public ResponseEntity<?> actualizarPersona(@RequestBody Persona persona,@RequestParam String documento,@RequestParam String usuario){
        if(service.findById(documento).isEmpty()){
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarPersona"
                    , Entidad.persona,"N/A","El usuario "+usuario+" intentó actualizar los datos de la persona de documento "+documento+" pero esta" +
                            " no existía.");
            return ResponseEntity.notFound().build();
        }
        aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarPersona"
                , Entidad.persona,documento,"El usuario "+usuario+" actualizó los datos de la persona de documento "+documento+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.update(persona,documento));
    }


    @PutMapping(path="/registro",params= {"usuario","contrasenia","documento"})
    public ResponseEntity<?> registrarUsuario(@RequestParam String usuario, @RequestParam String contrasenia,@RequestParam String documento) {
        Optional<Persona> persona = service.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"registrarUsuario"
                    , Entidad.persona,"N/A","Se intentó crear el usuario "+usuario+" para la persona de documento "+documento+" pero esta no existía.");
            return ResponseEntity.ok("La persona no existe.");
        }
        else{
            if (persona.get().getUsuario() != null) {
                aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"registrarUsuario"
                        , Entidad.persona,"N/A","Se intentó crear el usuario "+usuario+" para la persona de documento "+documento+" pero esta ya tenía " +
                                "un usuario");
                return ResponseEntity.ok("La persona ya tiene un usuario creado.");
            }

            Iterable<Persona> personas=service.findAll();

            for(Persona p:personas){
                if(p.getUsuario()!=null){
                    if(p.getUsuario().equals(usuario)){
                        aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"registrarUsuario"
                                , Entidad.persona,"N/A","Se intentó crear el usuario "+usuario+" para la persona de documento "+documento+" pero " +
                                        "el usuario ya estaba tomado.");
                        return ResponseEntity.ok("El usuario ya existe.");
                    }
                }
            }
            persona.get().setUsuario(usuario);
            persona.get().setContrasenia(contrasenia);
            service.update(persona.get(),documento);
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"registrarUsuario"
                    , Entidad.persona,documento,"El usuario "+usuario+" fue registrado exitosamente para la persona de documento "+documento+".");
            return ResponseEntity.status(HttpStatus.CREATED).body(persona.get().toView());
        }

    }

    //D

    @DeleteMapping(path="/{documento}",params={"usuario"})
    public ResponseEntity<?> borrarPersona(@PathVariable String documento,@RequestParam String usuario){
        Optional<Persona> persona=service.findById(documento);
        if(persona.isPresent()){
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarPersona"
                    , Entidad.persona,documento,"El usuario "+usuario+" borró a la persona de documento "+documento+".");
            service.borrar(persona.get());
            return ResponseEntity.ok().build();
        }
        else{
            aC.guardarAccion(service.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarPersona"
                    , Entidad.persona,"N/A","El usuario "+usuario+" intentó borrar a la persona de documento "+documento+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }

    }




}
