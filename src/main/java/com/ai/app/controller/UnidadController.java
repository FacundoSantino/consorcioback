package com.ai.app.controller;


import com.ai.app.exceptions.PersonaException;
import com.ai.app.exceptions.UnidadException;
import com.ai.app.negocio.*;
import com.ai.app.service.PersonaService;
import com.ai.app.service.UnidadService;
import com.ai.app.views.PersonaView;
import com.ai.app.views.UnidadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/unidades")
public class UnidadController {

    private final UnidadService service;

    private final PersonaService pS;

    private final AccionController aC;


    @Autowired
    public UnidadController(UnidadService service, PersonaService pS,AccionController aC){
        this.service=service;
        this.pS=pS;
        this.aC=aC;
    }

    //C

    //Las unidades no se guardan, se agregan al edificio y se actualiza este

    //R
    @GetMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> leerUnidad(@PathVariable int id,@RequestParam String usuario) {
        Unidad unidad =  service.findById(id).get();
        if (unidad == null) {
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerUnidad", Entidad.unidad,""+id,
                    "El usuario "+usuario+" intentó buscar la unidad "+id+" pero esta no existe.");
        	return ResponseEntity.notFound().build();
        }
        else {
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerUnidad", Entidad.unidad,""+id,
                    "El usuario "+usuario+" buscó la unidad "+id+" y obtuvo su información.");
        	return ResponseEntity.ok(unidad.toView());
        }
    }

    @GetMapping(params={"usuario"})
    public List<UnidadView> leerUnidades(@RequestParam String usuario){
        Iterable<Unidad> lista = service.findAll();
        List<UnidadView> lista2=new ArrayList<>();
        for (Unidad e: lista) {
            lista2.add(e.toView());
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerUnidades", Entidad.unidad,"N/A",
                "El usuario "+usuario+" consultó todas las unidades y obtuvo su información.");
        return StreamSupport
                .stream(lista2.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(params={"piso","numero","codigoEdificio","usuario"})
    public ResponseEntity<?> buscarUnidad(@RequestParam String piso, @RequestParam String numero, @RequestParam Integer codigoEdificio,@RequestParam String usuario){
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if (unidad == null) {
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"buscarUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" buscó la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" y esta no existe");
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(unidad);
        }
    }


    @GetMapping(path="/duenios",params={"codigoEdificio","piso","numero","usuario"})
    public List<PersonaView> dueniosPorUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String usuario) throws UnidadException {
        List<PersonaView> resultado = new ArrayList<PersonaView>();
        Unidad unidad = service.buscarUnidad(piso, numero, codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"dueniosPorUnidad", Entidad.persona,"N/A",
                    "El usuario "+usuario+" buscó los duenios de la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" y pero esta no existia.");
            return null;
        }
        List<Persona> duenios = unidad.getDuenios();
        for (Persona persona : duenios){
            resultado.add(persona.toView());
    }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"dueniosPorUnidad", Entidad.persona,"N/A",
                "El usuario "+usuario+" buscó los duenios de la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/inquilinos",params={"codigoEdificio","piso","numero","usuario"})
    public List<PersonaView> inquilinosPorUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String usuario) throws UnidadException {
        List<PersonaView> resultado = new ArrayList<PersonaView>();
        Unidad unidad = service.buscarUnidad(piso, numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"inquilinosPorUnidad", Entidad.persona,"N/A",
                    "El usuario "+usuario+" buscó los inquilinos de la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existia.");
            return null;
        }
        List<Persona> inquilinos = unidad.getInquilinos();
        for(Persona persona : inquilinos) {
            resultado.add(persona.toView());
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"inquilinosPorUnidad", Entidad.persona,"N/A",
                "El usuario "+usuario+" buscó los inquilinos de la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" y obtuvo su información.");
        return StreamSupport
                .stream(resultado.spliterator(), false)
                .collect(Collectors.toList());
    }


    //U
    @PutMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> actualizarUnidad(@RequestBody Unidad unidad, @PathVariable(value = "id") Integer id,@RequestParam String usuario){
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarUnidad", Entidad.unidad,""+id,
                "El usuario "+usuario+" realizó cambios sobre la unidad "+id+" y actualizó su información.");
    	return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(unidad,id));
    }

    @PutMapping(path="/{id}",params={"usuario","piso","numero"})
    public ResponseEntity<?> actualizarUnidad(@RequestParam String usuario, @RequestParam String piso, @RequestParam String numero,@PathVariable int id){
        Optional<Unidad> oU=service.findById(id);
        if(oU.isPresent()){
            Unidad unidad=oU.get();
            unidad.setNumero(numero);
            unidad.setPiso(piso);
            service.actualizar(unidad,id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }




    @PutMapping(path="/transferir",params={"codigoEdificio","piso","numero","documento","usuario"})
    public ResponseEntity<?> transferirUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String documento,@RequestParam String usuario) throws UnidadException, PersonaException {
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"transferirUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+"intentó transferir la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        Optional<Persona> persona = pS.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"transferirUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+"intentó transferir la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" hacia la persona de documento "+documento+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        unidad.transferir(persona.get());
        service.actualizar(unidad, unidad.getIdentificador());
        List<Persona> duenios =unidad.getDuenios();
        String imprimible="";
        for(Persona p:duenios){
            imprimible+=" "+p.getNombre()+" ";
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"transferirUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" transfirió la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" de"+imprimible+"hacia "+persona.get().getNombre()+".");
        return ResponseEntity.status(HttpStatus.OK).body(unidad.toView());
    }


    @PutMapping(path="/agregarDuenio",params={"codigoEdificio","piso","numero","documento","usuario"})
    public ResponseEntity<?> agregarDuenioUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String documento,@RequestParam String usuario) throws UnidadException, PersonaException {
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarDuenioUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+"intentó agregar un duenio a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        Optional<Persona> persona = pS.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarDuenioUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+"intentó agregar un duenio a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero la persona de documento "+documento+" no existía.");
            return ResponseEntity.notFound().build();
        }
        List<Persona> duenios=unidad.getDuenios();
        boolean agregar=true;
        for(Persona p:duenios){
            if(p.getDocumento().equals(persona.get().getDocumento())){
                agregar=false;
            }
        }

        if(!agregar){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarDuenioUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+" intentó agregar el duenio "+persona.get().getNombre()+" a la unidad piso "+piso+" numero "+numero+" del edificio " +
                            ""+codigoEdificio+" pero este ya era duenio.");
            return ResponseEntity.notFound().build();
        }

        unidad.agregarDuenio(persona.get());
        service.actualizar(unidad, unidad.getIdentificador());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarDuenioUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" agregó el duenio "+persona.get().getNombre()+" a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+".");
        return ResponseEntity.status(HttpStatus.OK).body(unidad.toView());
    }

    @PutMapping(path="/alquilar",params={"codigoEdificio","piso","numero","documento","usuario"})
    public ResponseEntity<?> alquilarUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String documento,@RequestParam String usuario) throws UnidadException, PersonaException{
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"alquilarUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" intentó alquilar la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existe.");
            return ResponseEntity.notFound().build();
        }
        Optional<Persona> persona = pS.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"alquilarUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+"alquiló la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" a la persona de documento "+documento+" pero esta no existe.");
            return ResponseEntity.notFound().build();
        }
        unidad.alquilar(persona.get());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"alquilarUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" alquiló la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" a la persona de documento "+documento+".");
        return ResponseEntity.status(HttpStatus.OK).body(service.actualizar(unidad, unidad.getIdentificador()));
    }

    @PutMapping(path="/agregarInquilino",params= {"codigoEdificio","piso","numero","documento","usuario"})
    public  ResponseEntity<?> agregarInquilinoUnidad(@RequestParam int codigoEdificio, @RequestParam String piso, @RequestParam String numero, @RequestParam String documento
    ,@RequestParam String usuario) throws UnidadException, PersonaException{
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarInquilinoUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" intentó agregar un inquilino a la unidad piso "+piso+" numero "+numero+" del edificio pero esta no existía");
            return ResponseEntity.notFound().build();
        }
        Optional<Persona> persona = pS.findById(documento);
        if(persona.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarInquilinoUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+" intentó agregar a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" a la persona de documento "+documento+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        List<Persona> inquilinos=unidad.getInquilinos();
        boolean agregar=true;
        for(Persona p:inquilinos){
            if(p.getDocumento().equals(persona.get().getDocumento())){
                agregar=false;
            }
        }

        if(!agregar){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarInquilinoUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                    "El usuario "+usuario+" intentó agregar el inquilino "+persona.get().getNombre()+" a la unidad piso "+piso+" numero "+numero+" del edificio " +
                            ""+codigoEdificio+" pero este ya era inquilino.");
            return ResponseEntity.notFound().build();
        }
        unidad.agregarInquilino(persona.get());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"agregarInquilinoUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" agregó a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" a la persona de documento "+documento+"" +
                        "a los inquilinos.");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(unidad, unidad.getIdentificador()));
    }

    @PutMapping(path="/liberar",params= {"codigoEdificio","piso","numero","usuario"})
    public ResponseEntity<?> liberarUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String usuario) throws UnidadException {
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"liberarUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" intentó liberar a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        unidad.liberar();
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"liberarUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" liberó a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(unidad, unidad.getIdentificador()));
    }

    @PutMapping(path="/habitar",params= {"codigoEdificio","piso","numero","usuario"})
    public ResponseEntity<Unidad> habitarUnidad(@RequestParam int codigoEdificio,@RequestParam String piso, String numero,@RequestParam String usuario) throws UnidadException {
        Unidad unidad = service.buscarUnidad(piso,numero,codigoEdificio);
        if(unidad==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"habitarUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" intentó habitar a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        unidad.habitar();
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"habitarUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" habitó a la unidad piso "+piso+" numero "+numero+" del edificio "+codigoEdificio+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(unidad, unidad.getIdentificador()));
    }

    //D
    @DeleteMapping(params={"usuario"})
    public ResponseEntity<?> borrarUnidad(@RequestBody Unidad unidad,@RequestParam String usuario){
        Optional<Unidad> unidad2=service.findById(unidad.getIdentificador());
        if(unidad2.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarUnidad", Entidad.unidad,"N/A",
                    "El usuario "+usuario+" intentó eliminar a la unidad de id "+unidad.getIdentificador()+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        service.deleteById(unidad.getIdentificador());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarUnidad", Entidad.unidad,""+unidad.getIdentificador(),
                "El usuario "+usuario+" eliminó a la unidad de id "+unidad.getIdentificador()+".");
        return ResponseEntity.ok("Unidad "+unidad.getIdentificador()+" eliminada exitosamente.");
    }

    @DeleteMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> borrarPorIdUnidad(@PathVariable(value="id")Integer id,@RequestParam String usuario){
        Optional<Unidad> unidad=service.findById(id);
        if(unidad.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarPorIdUnidad", Entidad.unidad,""+id,
                    "El usuario "+usuario+" intentó eliminar a la unidad de id "+id+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarPorIdUnidad", Entidad.unidad,""+id,
                "El usuario "+usuario+" eliminó a la unidad de id "+id+".");
        return ResponseEntity.ok("Unidad "+id+" eliminada exitosamente.");
    }
}
