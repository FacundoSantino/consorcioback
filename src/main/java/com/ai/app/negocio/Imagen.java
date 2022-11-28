package com.ai.app.negocio;

import com.ai.app.views.ImagenView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="imagenes")

public class Imagen implements Serializable {

    private static final long serialVersionUID = 8181491450563145123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int numero;

    public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Reclamo getReclamo() {
		return reclamo;
	}

	public void setReclamo(Reclamo reclamo) {
		this.reclamo = reclamo;
	}

	@Column(nullable = false,length = 300)
    private String path;

    @Column(length = 10)
    private String tipo;

	@ManyToOne
    @JoinColumn(name="idReclamo")
    @JsonIgnore
    private Reclamo reclamo;

    public Imagen(String path, String tipo, Reclamo reclamo){
        this.path=path;
        this.tipo=tipo;
        this.reclamo=reclamo;
    }
	public ImagenView toView() {
		return new ImagenView(numero, path, tipo);
	}

}
