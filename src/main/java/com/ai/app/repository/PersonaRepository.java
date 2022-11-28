package com.ai.app.repository;

import com.ai.app.negocio.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String>
{

    @Query(value = "select d.identificador from personas p "+
            "join duenios d on d.documento=p.documento "+
            "where p.documento= :PersonaDocumento"
            ,nativeQuery = true
    )
    List<Integer> traerUnidadesDuenios(@Param("PersonaDocumento")String documento);

    @Query(value = "select i.identificador from personas p "+
            "join inquilinos i on i.documento=p.documento "+
            "where p.documento= :PersonaDocumento"
            ,nativeQuery = true
    )
    List<Integer> traerUnidadesInquilinos(@Param("PersonaDocumento")String documento);
}
