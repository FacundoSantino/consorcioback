package com.ai.app.controller;

import com.ai.app.negocio.Accion;
import com.ai.app.negocio.Entidad;
import com.ai.app.negocio.Persona;
import com.ai.app.service.AccionService;
import com.ai.app.views.AccionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value="api/acciones")
public class AccionController {

    @Autowired
    private AccionService service;

    @PostMapping(params={"usuario","fechaHora","operacion","entidad","idEntidad","descripcion"})
    public ResponseEntity<?> guardarAccion(@RequestParam Persona usuario, @RequestParam LocalDateTime fechaHora, @RequestParam String operacion,
                                           @RequestParam Entidad entidad, @RequestParam String idEntidad, @RequestParam String descripcion)
    {

        Accion accion = new Accion(usuario,fechaHora,operacion,entidad,idEntidad,descripcion);
        service.save(accion);
        return ResponseEntity.status(HttpStatus.CREATED).body(accion);
    }

    @GetMapping(path="/{idAccion}")
    public ResponseEntity<AccionView> leerAccion(@PathVariable int idAccion){

        Accion accion = service.findById(idAccion);
        if (accion==null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return new ResponseEntity<AccionView>(accion.toView(),null,HttpStatus.OK);
        }

    }

    @GetMapping
    public List<AccionView> leerAcciones(){
        List<Accion> lista = service.findAll();
        List<AccionView> lista2 = new ArrayList<>();
        for(Accion e:lista) {
            lista2.add(e.toView());
        }


        return StreamSupport
                .stream(lista2.spliterator(), false)
                .collect(Collectors.toList());
    }

}