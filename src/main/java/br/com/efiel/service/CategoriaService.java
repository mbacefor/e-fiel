package br.com.efiel.service;

import br.com.efiel.domain.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Categoria.
 */
public interface CategoriaService {

    /**
     * Save a categoria.
     *
     * @param categoria the entity to save
     * @return the persisted entity
     */
    Categoria save(Categoria categoria);

    /**
     * Get all the categorias.
     *
     * @return the list of entities
     */
    List<Categoria> findAll();

    /**
     * Get all the Categoria with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Categoria> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" categoria.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Categoria> findOne(Long id);

    /**
     * Delete the "id" categoria.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the categoria corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Categoria> search(String query);
}
