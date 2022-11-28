package com.ai.app.repository;

import com.ai.app.negocio.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadRepository extends JpaRepository<Unidad,Integer> {
    @Query(
            value=  "SELECT documento FROM personas P WHERE NOT EXISTS"+
                    " (SELECT documento from inquilinos where documento=P.documento) "+
                    "AND NOT EXISTS (SELECT documento from duenios where documento=P.documento)"
         ,nativeQuery = true)
    List<String> personasSinNada();
}
