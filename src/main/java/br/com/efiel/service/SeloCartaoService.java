package br.com.efiel.service;

import br.com.efiel.domain.SeloCartao;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SeloCartao.
 */
public interface SeloCartaoService {

    /**
     * Save a seloCartao.
     *
     * @param seloCartao the entity to save
     * @return the persisted entity
     */
    SeloCartao save(SeloCartao seloCartao);

    /**
     * Get all the seloCartaos.
     *
     * @return the list of entities
     */
    List<SeloCartao> findAll();


    /**
     * Get the "id" seloCartao.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SeloCartao> findOne(Long id);

    /**
     * Delete the "id" seloCartao.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the seloCartao corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SeloCartao> search(String query);
}
