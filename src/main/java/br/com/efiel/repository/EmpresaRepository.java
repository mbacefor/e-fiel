package br.com.efiel.repository;

import br.com.efiel.domain.Empresa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Empresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query("select empresa from Empresa empresa where empresa.criador.login = ?#{principal.username}")
    List<Empresa> findByCriadorIsCurrentUser();

}
