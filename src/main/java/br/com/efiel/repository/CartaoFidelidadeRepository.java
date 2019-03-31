package br.com.efiel.repository;

import br.com.efiel.domain.CartaoFidelidade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CartaoFidelidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartaoFidelidadeRepository extends JpaRepository<CartaoFidelidade, Long> {

    @Query("select cartao_fidelidade from CartaoFidelidade cartao_fidelidade where cartao_fidelidade.dono.login = ?#{principal.username}")
    List<CartaoFidelidade> findByDonoIsCurrentUser();

}
