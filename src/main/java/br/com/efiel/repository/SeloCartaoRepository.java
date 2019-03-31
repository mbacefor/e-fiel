package br.com.efiel.repository;

import br.com.efiel.domain.SeloCartao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SeloCartao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeloCartaoRepository extends JpaRepository<SeloCartao, Long> {

}
