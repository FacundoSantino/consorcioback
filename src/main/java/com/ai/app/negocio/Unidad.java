package com.ai.app.negocio;


import com.ai.app.exceptions.UnidadException;
import com.ai.app.views.UnidadView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="unidades")

public class Unidad implements Serializable {

    private static final long serialVersionUID = 4561522332187895461L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identificador", nullable = false)
    private Integer identificador;

    @Column(length = 5,nullable = false)
    private String piso;


    @Column(length = 10,nullable = false)
    private String numero;

    @Column(length = 1)
    @Convert(converter= BooleanConverter.class)
    private boolean habitado;

    @ManyToOne
    @JoinColumn(name="codigoEdificio")
    private Edificio edificio;

    @ManyToMany
    @JoinTable(name = "duenios", joinColumns={
            @JoinColumn(name="identificador",nullable = false)
    },inverseJoinColumns = {@JoinColumn (name="documento",nullable = false)}
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Persona> duenios=new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "inquilinos", joinColumns={
            @JoinColumn(name="identificador",nullable = false)
    },inverseJoinColumns = {@JoinColumn (name="documento",nullable = false)}
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Persona> inquilinos=new ArrayList<>();

    public Unidad(String numero, String piso, Edificio edificio) {
        this.numero=numero;
        this.piso=piso;
        this.habitado=false;
        this.edificio=edificio;
    }
    
    public Unidad() {
    }

    public void transferir(Persona nuevoDuenio) {
        duenios = new ArrayList<Persona>();
        duenios.add(nuevoDuenio);
    }

    public void agregarInquilino(Persona persona){
        inquilinos.add(persona);
        this.habitado=true;
    }

    public void agregarDuenio(Persona persona){
        duenios.add(persona);
    }

    public void sacarInquilino(Persona persona) {

        for(Persona p: inquilinos){
            if (p.getDocumento().equals(persona.getDocumento())){
                inquilinos.remove(p);
                break;
            }
        }
        if(inquilinos.isEmpty()){
            this.habitado=false;
        }

    }

    public void sacarDuenio(Persona persona) {

        for(Persona p: duenios){
            if (p.getDocumento().equals(persona.getDocumento())){
                duenios.remove(p);
                break;
            }
        }
        if(duenios.isEmpty()){
            System.out.println("UNIDAD SIN DUENIO");
        }

    }

    public void prepararDelete(){
        getEdificio().sacarUnidad(this);
        this.edificio=null;
    }

	public Integer getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public boolean isHabitado() {
		return habitado;
	}

	public void setHabitado(boolean habitado) {
		this.habitado = habitado;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public List<Persona> getDuenios() {
		return duenios;
	}

	public void setDuenios(List<Persona> duenios) {
		this.duenios = duenios;
	}

	public List<Persona> getInquilinos() {
		return inquilinos;
	}

	public void setInquilinos(List<Persona> inquilinos) {
		this.inquilinos = inquilinos;
	}


    public UnidadView toView() {
        return new UnidadView(identificador,piso,numero,habitado,edificio.toView());
    }
    public void alquilar(Persona inquilino) throws UnidadException {
        if(!this.habitado) {
            this.habitado = true;
            inquilinos = new ArrayList<Persona>();
            inquilinos.add(inquilino);
        }
        else
            throw new UnidadException("La unidad esta ocupada");
    }
    public void liberar() {
        this.inquilinos = new ArrayList<Persona>();
        this.habitado = false;
    }

    public void habitar() throws UnidadException {
        if(this.habitado)
            throw new UnidadException("La unidad ya esta habitada");
        else
            this.habitado = true;
    }
}
