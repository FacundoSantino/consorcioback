package com.ai.app.controller;


import com.ai.app.negocio.Entidad;
import com.ai.app.negocio.Imagen;
import com.ai.app.negocio.Reclamo;
import com.ai.app.service.ImagenService;
import com.ai.app.service.PersonaService;
import com.ai.app.service.ReclamoService;
import com.ai.app.views.ImagenView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/imagenes")
public class ImagenController {

    private final ImagenService service;
    private final ReclamoService rS;
    private final AccionController aC;
    private final PersonaService pS;

    @Autowired
    public ImagenController(ImagenService service, ReclamoService rS, AccionController aC, PersonaService pS){
        this.service=service;
        this.rS = rS;
        this.aC = aC;
        this.pS = pS;
        
    }

    //C

    @PostMapping(params={"path","tipo","idReclamo","usuario"})
    public ResponseEntity<?> crearImagen(@RequestParam String path, @RequestParam String tipo, @RequestParam int idReclamo,@RequestParam String usuario){
        Reclamo reclamo = rS.findById(idReclamo);
        Imagen imagen=service.crearImagen(path,tipo,reclamo);
        service.guardarImagen(imagen);
        aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "crearImagen", Entidad.imagen, ""+imagen.getNumero(),
                "El usuario "+usuario+" creó la imagen "+imagen.getNumero()+" exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(imagen);
    }

    //R

    @GetMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> leerImagen(@PathVariable Integer id, @RequestParam String usuario){
        Imagen imagen=service.leerImagen(id);
        if(imagen==null){
        	aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "leerImagen", Entidad.imagen, ""+id,
                    "El usuario "+usuario+" intentó buscar la imagen "+id+" pero esta no existe.");
            return ResponseEntity.notFound().build();
        }
        else{
        	aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "leerImagen", Entidad.imagen, ""+id,
                    "El usuario "+usuario+" buscó la imagen "+imagen.getNumero()+" y obtuvo su información.");
            return ResponseEntity.ok(imagen.toView());
        }
    }

    @GetMapping(params={"usuario"})
    public List<ImagenView> leerImagenes(@RequestParam String usuario){
        List<Imagen> lista = service.findAll();
        List<ImagenView> lista2 =new ArrayList<>();
        for(Imagen e: lista) {
            lista2.add(e.toView());
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "leerImagenes", Entidad.imagen, "N/A",
                "El usuario "+usuario+" buscó todas las imagenes y obtuvo su información.");
        return StreamSupport
                .stream(lista2.spliterator(), false)
                .collect(Collectors.toList());
    }

    //U

    @PutMapping(params= {"id","usuario"})
    public ResponseEntity<?> actualizarImagen(@RequestBody Imagen imagen,@PathVariable(value= "id") Integer id, @RequestParam String usuario){
    	aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "actualizarImagen", Entidad.imagen, ""+id,
                "El usuario "+usuario+" actualizó la imagen "+imagen.getNumero()+ " exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(imagen,id));
    }

    //D
    @DeleteMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> borrarImagen(@PathVariable(value="id") Integer numero, @RequestParam String usuario){
        service.deleteById(numero);
        aC.guardarAccion(pS.buscarPorUsuario(usuario),  LocalDateTime.now(), "borrarImagen", Entidad.imagen, ""+numero,
                "El usuario "+usuario+" borró la imagen "+numero+" exitosamente.");
        return ResponseEntity.ok().build();
    }

}
