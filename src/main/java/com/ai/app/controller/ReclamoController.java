package com.ai.app.controller;

import com.ai.app.negocio.*;
import com.ai.app.service.*;
import com.ai.app.views.AccionView;
import com.ai.app.views.ReclamoView;
import com.ai.app.views.UnidadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/reclamos")
public class ReclamoController {

    private final ReclamoService service;
    private final PersonaService pS;
    private final EdificioService eS;

    private final ImagenService iS;
    private final UnidadService uS;

    private final AccionService aS;

    private final AccionController aC;

    @Autowired
    public ReclamoController(ReclamoService service, PersonaService pS, EdificioService eS,UnidadService uS,ImagenService iS, AccionController aC,AccionService aS)
    {
        this.service=service;
        this.eS=eS;
        this.pS=pS;
        this.uS=uS;
        this.iS=iS;
        this.aC=aC;
        this.aS=aS;
    }


    //C

    @PostMapping(params={"documento","codigoEdificio","ubicacion","descripcion","codigoUnidad","usuario"})
    public ResponseEntity<?> crearReclamo(@RequestParam String documento, @RequestParam int codigoEdificio,
                                          @RequestParam String ubicacion, @RequestParam String descripcion, @RequestParam int codigoUnidad
    ,@RequestParam String usuario) {
        Optional<Persona> reclamante=pS.findById(documento);
        if(reclamante.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para la persona de documento "+documento+" pero esta no existía");
            return ResponseEntity.notFound().build();
        }
        Edificio edificio=eS.findById(codigoEdificio);
        if(edificio==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo ubicado en el edificio "+codigoEdificio+" pero este no existía.");
            return ResponseEntity.notFound().build();
        }
        Optional<Unidad> unidad=uS.findById(codigoUnidad);
        if(unidad.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para la unidad codigo "+codigoUnidad+" pero esta no existía.");
            return ResponseEntity.notFound().build();
        }
        Reclamo reclamo = service.crearReclamo(reclamante.get(),edificio,ubicacion,descripcion,unidad.get());
        if(reclamo==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para la unidad codigo "+codigoUnidad+" pero no tenía permisos.");
            return ResponseEntity.notFound().build();
        }
        service.guardarReclamo(reclamo);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,""+reclamo.getIdReclamo(),"El usuario "+
                usuario+" creó un reclamo para la unidad codigo "+codigoUnidad+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamo);
    }

    @PostMapping(params={"documento","codigoEdificio","ubicacion","descripcion","usuario"})
    public ResponseEntity<?> crearReclamo(@RequestParam String documento, @RequestParam int codigoEdificio, @RequestParam String ubicacion, @RequestParam String descripcion
    ,@RequestParam String usuario) {
        Optional<Persona> reclamante=pS.findById(documento);
        if(reclamante.isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para la persona de documento "+documento+" pero esta no existía");
            return ResponseEntity.notFound().build();
        }
        Edificio edificio=eS.findById(codigoEdificio);
        if(edificio==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para el edificio "+codigoEdificio+" pero este no existía.");
            return ResponseEntity.notFound().build();
        }
        Reclamo reclamo = service.crearReclamo(reclamante.get(),edificio,ubicacion,descripcion);
        if(reclamo==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó crear un reclamo para el edificio "+codigoEdificio+" pero no tenía permisos.");
            return ResponseEntity.notFound().build();
        }
        service.guardarReclamo(reclamo);
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"crearReclamo",Entidad.reclamo,""+reclamo.getIdReclamo(),"El usuario "+
                usuario+" creó un reclamo para el edificio "+codigoEdificio+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamo);
    }

    @GetMapping(path="/consultar",params={"documento"})
    public HashMap<Integer, List<UnidadView> >consultarPosibilidades(@RequestParam String documento)
    {
        HashMap<Integer, List<UnidadView>> res=new HashMap<>();
        Optional<Persona> buscada=pS.findById(documento);
        if(buscada.isPresent()){
            Persona p=buscada.get();
            List<Edificio> edificios= eS.findAll();
            for(Edificio e:edificios){
                List<Unidad> agregar=new ArrayList<>();
                List<Unidad> unidades=e.getUnidades();
                for(Unidad u:unidades){
                    for(Persona i:u.getInquilinos()){
                        if(i.getDocumento().equals(documento)){
                            agregar.add(u);
                        }
                    }
                    for(Persona d:u.getDuenios())
                    {
                        if(d.getDocumento().equals(documento) || p.isAdministrador())
                        {
                            if(!agregar.contains(u)){
                                agregar.add(u);
                            }
                        }

                    }

                }
                //pusheo al map
                if(!agregar.isEmpty()){
                    List<UnidadView> agregarv=new ArrayList<>();
                    for(Unidad un:agregar){
                        agregarv.add(un.toView());
                    }
                    res.put(e.getCodigo(),agregarv);
                }
            }
        }
        return res;
    }

    //R

    @GetMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> leerReclamo(@PathVariable Integer id,@RequestParam String usuario){
    	Reclamo reclamo = service.findById(id);
    	if (reclamo == null) {
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó consultar los datos del reclamo "+reclamo.getIdReclamo()+" pero este no existía.");
    		return ResponseEntity.notFound().build();
    	}
    	else {
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerReclamo",Entidad.reclamo,""+id,"El usuario "+
                    usuario+" consultó los datos del reclamo "+reclamo.getIdReclamo()+" y los obtuvo.");
    		return ResponseEntity.ok(reclamo.toView());
    	}
    }

    @GetMapping(params={"usuario"})
    public List<ReclamoView> leerReclamos(@RequestParam String usuario){
        List<Reclamo> lista = service.findAll();
        List<ReclamoView> lista2 = new ArrayList<>();
        for (Reclamo e: lista) {
            lista2.add(e.toView());
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"leerReclamos",Entidad.reclamo,"N/A","El usuario "+
                usuario+" consultó los datos de todos los reclamos y los obtuvo.");
        return StreamSupport
                .stream(lista2.spliterator(),false)
                .collect(Collectors.toList());
    }

    @GetMapping(params={"estado","usuario"})
    public List<ReclamoView> reclamosPorEstado(@RequestParam Estado estado,@RequestParam String usuario){
        List<Reclamo> lista = service.findAll();
        List<ReclamoView> lista2 = new ArrayList<>();
        for (Reclamo e: lista) {
            if (e.getEstado() == estado) {
                lista2.add(e.toView());
            }
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorEstado",Entidad.reclamo,"N/A","El usuario "+
                usuario+" consultó los datos de los reclamos de estado "+estado+" y los obtuvo.");
        return StreamSupport
                .stream(lista2.spliterator(),false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/edificios/{codigoEdificio}",params={"usuario"})
    public List<ReclamoView> reclamosPorEdificio(@PathVariable int codigoEdificio,@RequestParam String usuario){
        List<ReclamoView> resultado = new ArrayList<ReclamoView>();
        Edificio e=eS.findById(codigoEdificio);
        if(e==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorEdificio",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó consultar los datos de los reclamos del edificio codigo "+codigoEdificio+" pero este no existía.");
            return null;
        }
        List<Reclamo> reclamos=service.findAll();
        for(Reclamo r:reclamos){
            if(r.getEdificio().getCodigo()==codigoEdificio){
                resultado.add(r.toView());
            }
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorEdificio",Entidad.reclamo,"N/A","El usuario "+
                usuario+" consultó los datos de los reclamos del edificio codigo "+codigoEdificio+" y los obtuvo.");
        return StreamSupport
                .stream(resultado.spliterator(),false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/cambios",params={"usuario","idReclamo"})
    public List<AccionView> cambiosPorReclamo(@RequestParam String usuario, @RequestParam int idReclamo){
        Iterable<Accion> acciones=aS.findAll();
        List<AccionView> devolucion=new ArrayList<>();
        for(Accion a:acciones){
            if(a.getOperacion().equals("actualizarEstadoReclamo")){
                if(a.getIdEntidad().equals(Integer.toString(idReclamo))){
                    devolucion.add(a.toView());
                }
            }
        }
        return StreamSupport
                .stream(devolucion.spliterator(),false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/unidades",params={"codigoEdificio","piso","numero","usuario"})
    public List<ReclamoView> reclamosPorUnidad(@RequestParam int codigoEdificio,@RequestParam String piso,@RequestParam String numero,@RequestParam String usuario) {
        List<ReclamoView> resultado = new ArrayList<ReclamoView>();
        Unidad u=uS.buscarUnidad(piso,numero,codigoEdificio);
        if(u==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorUnidad",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó consultar los datos de los reclamos de la unidad piso "+piso+" numero "+numero+" del edificio codigo "+codigoEdificio+" pero esta no existía.");
            return null;
        }
        int identificador=u.getIdentificador();
        List<Reclamo> reclamos=service.findAll();
        for(Reclamo r:reclamos){
            if(r.getUnidad().getIdentificador()==identificador){
                resultado.add(r.toView());
            }
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorUnidad",Entidad.reclamo,"N/A","El usuario "+
                usuario+" consultó los datos de los reclamos de la unidad piso "+piso+" numero "+numero+" del edificio codigo "+codigoEdificio+" y los obtuvo.");
        return StreamSupport
                .stream(resultado.spliterator(),false)
                .collect(Collectors.toList());
    }

    @GetMapping(path="/personas/{documento}",params={"usuario"})
    public List<ReclamoView> reclamosPorPersona(@PathVariable String documento,@RequestParam String usuario) {
        List<ReclamoView> resultado = new ArrayList<ReclamoView>();
        List<Reclamo> reclamos=service.findAll();
        if(pS.findById(documento).isEmpty()){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorPersona",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó consultar los datos de los reclamos de la persona de documento "+documento+" pero esta no existía.");
            return null;
        }
        for(Reclamo r:reclamos){
            if(r.getReclamante().getDocumento().equals(documento)){
                resultado.add(r.toView());
            }
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"reclamosPorPersona",Entidad.reclamo,"N/A","El usuario "+
                usuario+" consultó los datos de los reclamos de la persona de documento "+documento+" y los obtuvo.");
        return StreamSupport
                .stream(resultado.spliterator(),false)
                .collect(Collectors.toList());
    }

    //U
    
    @PutMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> actualizarReclamo(@RequestBody Reclamo reclamo, @PathVariable(value="id") Integer id,@RequestParam String usuario){
        Reclamo recl=service.findById(id);
        if(recl==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó actualizar el reclamo de id "+id+" pero este no existía.");
            return ResponseEntity.notFound().build();
        }

        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarReclamo",Entidad.reclamo,""+id,"El usuario "+
                usuario+" actualizó el reclamo de id "+id+".");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(reclamo, id));
    }

    //D

    @DeleteMapping(params={"usuario"})
    public ResponseEntity<?> borrarReclamo(@RequestBody Reclamo reclamo,@RequestParam String usuario){
        Reclamo aux=service.findById(reclamo.getIdReclamo());
        if(aux==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó borrar el reclamo de id "+reclamo.getIdReclamo()+" pero este no existía.");
            return ResponseEntity.notFound().build();
        }
        service.borrarPorId(reclamo.getIdReclamo());
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarReclamo",Entidad.reclamo,""+reclamo.getIdReclamo(),"El usuario "+
                usuario+" borró el reclamo de id "+reclamo.getIdReclamo()+".");
        return ResponseEntity.ok("Reclamo "+reclamo.getIdReclamo()+" borrado exitosamente.");
    }

    @DeleteMapping(path="/{id}",params={"usuario"})
    public ResponseEntity<?> borrarReclamoPorId(@PathVariable(value="id") Integer id,@RequestParam String usuario){
        Reclamo reclamo=service.findById(id);
        if(reclamo!=null){
            service.borrarPorId(id);
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarReclamoPorId",Entidad.reclamo,""+id,"El usuario "+
                    usuario+" borró el reclamo de id "+id+".");
            return ResponseEntity.ok("El reclamo "+id+" fue borrado exitosamente.");
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"borrarReclamoPorId",Entidad.reclamo,"N/A","El usuario "+
                usuario+" intentó borrar el reclamo de id "+id+" pero este no existía.");
        return ResponseEntity.notFound().build();

    }

    @PutMapping(params={"estadoNuevo","numeroReclamo","usuario","motivo"})
    public ResponseEntity<?> actualizarEstadoReclamo(@RequestParam Estado estadoNuevo, @RequestParam Integer numeroReclamo,@RequestParam String usuario,@RequestParam String motivo) {
        Reclamo reclamo = service.findById(numeroReclamo);
        if(reclamo==null){
            aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarEstadoReclamo",Entidad.reclamo,"N/A","El usuario "+
                    usuario+" intentó actualizar el estado del reclamo id "+numeroReclamo+" pero este no existía.");
            return ResponseEntity.notFound().build();
        }
        aC.guardarAccion(pS.buscarPorUsuario(usuario), LocalDateTime.now(),"actualizarEstadoReclamo",Entidad.reclamo,""+numeroReclamo,"El usuario "+
                usuario+" actualizó el estado del reclamo de id "+numeroReclamo+" de "+reclamo.getEstado()+" a "+estadoNuevo+" ya que ,según escribió: "+motivo+".");
        reclamo.setEstado(estadoNuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.actualizar(reclamo, numeroReclamo));

    }

}
