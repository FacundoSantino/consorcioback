package com.ai.app.negocio;

import com.ai.app.views.ImagenView;
import com.ai.app.views.ReclamoView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="reclamos")

public class Reclamo implements Serializable {

    private static final long serialVersionUID = 1234567891011121314L;

    public Integer getIdReclamo() {
		return idReclamo;
	}

	public void setIdReclamo(Integer idReclamo) {
		this.idReclamo = idReclamo;
	}

	public Persona getReclamante() {
		return reclamante;
	}

	public void setReclamante(Persona reclamante) {
		this.reclamante = reclamante;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Unidad getUnidad() {
		return unidad;
	}

	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReclamo;

    @ManyToOne
    @JoinColumn(name="documento")
    @JsonIgnore
    private Persona reclamante;

    @ManyToOne
    @JoinColumn(name="codigo")
    @JsonIgnore
    private Edificio edificio;

    @Column(length = 300)
    private String ubicacion;

    @Column(length = 1000)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="identificador")
    @JsonIgnore
    private Unidad unidad;
    //Puede ser null en caso de que el reclamo sea para un espacio común

	@Column(length = 20)
	private Estado estado;

	@OneToMany(mappedBy = "reclamo",cascade=CascadeType.ALL)
	private List<Imagen> imagenes=new ArrayList<>();


    public Reclamo(Persona reclamante, Edificio edificio, String ubicacion, String descripcion, Unidad unidad) {
        this.reclamante=reclamante;
        this.edificio=edificio;
        this.unidad=unidad;
        this.ubicacion=ubicacion;
        this.descripcion=descripcion;
		estado=Estado.nuevo;
    }

    public Reclamo(Persona reclamante, Edificio edificio, String ubicacion, String descripcion) {
        this.reclamante=reclamante;
        this.edificio=edificio;
        this.unidad=null;
        this.ubicacion=ubicacion;
        this.descripcion=descripcion;
		estado=Estado.nuevo;
    }

	public ReclamoView toView() {
		List<ImagenView> imageness = new ArrayList<>();
		for(Imagen i: this.imagenes){
			imageness.add(i.toView());
		}
		return new ReclamoView(idReclamo,reclamante.toView(),edificio.toView(),ubicacion,descripcion,unidad.toView(),estado,imageness);
	}

	public void agregarImagen(Imagen imagen) {
		imagenes.add(imagen);
	}
}
