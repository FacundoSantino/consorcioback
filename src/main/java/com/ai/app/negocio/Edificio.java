package com.ai.app.negocio;


import com.ai.app.views.EdificioView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="edificios")
public class Edificio implements Serializable {

    private static final long serialVersionUID = 8181491450563115882L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codigo;

    @Column(length = 100,nullable = false)
    private String nombre;

    @Column(length = 100,nullable = false)
    private String direccion;

    @OneToMany(mappedBy = "edificio",cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.JOIN)
    @JsonIgnore
    private List<Unidad> unidades = new java.util.ArrayList<>();

    public Edificio(String nombre, String direccion) {

        this.direccion=direccion;
        this.nombre=nombre;

    }
    
    public Edificio() {
    }

    @Override
	public String toString() {
		return "Edificio [codigo=" + codigo + ", nombre=" + nombre + ", direccion=" + direccion + ", unidades="
				+ unidades + "]";
	}

	public Edificio(String nombre, String direccion, List<Unidad> unidades) {

        this.direccion=direccion;
        this.nombre=nombre;
        this.unidades=unidades;

    }

    public Edificio(String nombre, String direccion, Unidad unidad) {
        this.direccion=direccion;
        this.nombre=nombre;
        agregarUnidad(unidad);
    }

    public void agregarUnidad(Unidad unidad){
        unidades.add(unidad);
    }

    public void sacarUnidad(Unidad unidad){
        unidades.remove(unidad);
    }

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}


    public Set<Persona> habilitados() {
        Set<Persona> habilitados = new HashSet<Persona>();
        for(Unidad unidad : unidades) {
            List<Persona> duenios = unidad.getDuenios();
            for(Persona p : duenios)
                habilitados.add(p);
            List<Persona> inquilinos = unidad.getInquilinos();
            for(Persona p : inquilinos)
                habilitados.add(p);
        }
        return habilitados;
    }


    public Set<Persona> duenios() {
        Set<Persona> resultado = new HashSet<Persona>();
        for(Unidad unidad : unidades) {
            List<Persona> duenios = unidad.getDuenios();
            for(Persona p : duenios)
                resultado.add(p);
        }
        return resultado;
    }

    public Set<Persona> habitantes() {
        Set<Persona> resultado = new HashSet<Persona>();
        for(Unidad unidad : unidades) {
            if(unidad.isHabitado()) {
                List<Persona> inquilinos = unidad.getInquilinos();
                if(inquilinos.size() > 0)
                    for(Persona p : inquilinos)
                        resultado.add(p);
                else {
                    List<Persona> duenios = unidad.getDuenios();
                    for(Persona p : duenios)
                        resultado.add(p);
                }
            }
        }
        return resultado;
    }

    public EdificioView toView() {
        return new EdificioView(codigo, nombre, direccion);
    }
}
