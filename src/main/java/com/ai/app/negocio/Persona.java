package com.ai.app.negocio;

import com.ai.app.views.PersonaView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="personas")

public class Persona implements Serializable {

    private static final long serialVersionUID = 8181491450123456789L;

    @Id
    @Column(length = 20)
    private String documento;

    @Column(length = 100)
    private String nombre;

	@Column(length=50)
	private String usuario;

	@Column(length = 50)
	private String contrasenia;

	@Column(length = 1)
	@Convert(converter=BooleanConverter.class)
	private boolean administrador;

	public Persona() {
		super();
	}

	public Persona(String documento, String nombre){
		this.documento=documento;
		this.nombre=nombre;
	}

	@Override
	public String toString() {
		return "Persona [documento=" + documento + ", nombre=" + nombre + "]";
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PersonaView toView() {
		return new PersonaView(documento,nombre);
	}
}
