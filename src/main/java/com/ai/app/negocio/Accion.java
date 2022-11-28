package com.ai.app.negocio;

import com.ai.app.views.AccionView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name="acciones")
public class Accion {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int idAccion;

    @ManyToOne
    @JoinColumn(name="usuario")
    private Persona usuario;

    @Column
    private LocalDateTime fechaHora;

    @Column(length=50)
    private String operacion;

    @Column(length=30)
    private Entidad entidad;

    @Column(length = 30)
    private String idEntidad;

    @Column(length=1200)
    private String descripcion;
    //TODO en el front, chequear que la descripcion de modificacion del estado del reclamo que ingrese el admin sea <=1200 caracteres

    public AccionView toView(){

        if(usuario==null){
            return new AccionView(idAccion,null,fechaHora,operacion,entidad,idEntidad,descripcion);
        }
        return new AccionView(idAccion,usuario.toView(),fechaHora,operacion,entidad,idEntidad,descripcion);

    }

    public Accion(Persona usuario, LocalDateTime fechaHora, String operacion, Entidad entidad, String idEntidad,
                  String descripcion) {
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.operacion = operacion;
        this.entidad = entidad;
        this.idEntidad = idEntidad;
        this.descripcion = descripcion;

    }

}
