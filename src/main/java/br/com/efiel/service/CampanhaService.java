package br.com.efiel.service;

import br.com.efiel.domain.Campanha;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Campanha.
 */
public interface CampanhaService {

    /**
     * Save a campanha.
     *
     * @param campanha the entity to save
     * @return the persisted entity
     */
    Campanha save(Campanha campanha);

    /**
     * Get all the campanhas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Campanha> findAll(Pageable pageable);


    /**
     * Get the "id" campanha.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Campanha> findOne(Long id);

    /**
     * Delete the "id" campanha.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the campanha corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Campanha> search(String query, Pageable pageable);
}
