package br.com.efiel.service;

import br.com.efiel.domain.CartaoFidelidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CartaoFidelidade.
 */
public interface CartaoFidelidadeService {

    /**
     * Save a cartaoFidelidade.
     *
     * @param cartaoFidelidade the entity to save
     * @return the persisted entity
     */
    CartaoFidelidade save(CartaoFidelidade cartaoFidelidade);

    /**
     * Get all the cartaoFidelidades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CartaoFidelidade> findAll(Pageable pageable);


    /**
     * Get the "id" cartaoFidelidade.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CartaoFidelidade> findOne(Long id);

    /**
     * Delete the "id" cartaoFidelidade.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cartaoFidelidade corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CartaoFidelidade> search(String query, Pageable pageable);
}
