package br.com.efiel.repository;

import br.com.efiel.domain.Campanha;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Campanha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

}
