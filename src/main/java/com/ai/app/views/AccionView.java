package com.ai.app.views;

import java.time.LocalDateTime;

import com.ai.app.negocio.Entidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class AccionView {
    private int idAccion;
    private PersonaView usuario;
    private LocalDateTime fechaHora;
    private String operacion;
    private Entidad entidad;
    private String idEntidad;
    private String descripcion;



}