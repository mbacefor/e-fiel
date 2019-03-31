package br.com.efiel.service.impl;

import br.com.efiel.service.CategoriaService;
import br.com.efiel.domain.Categoria;
import br.com.efiel.repository.CategoriaRepository;
import br.com.efiel.repository.search.CategoriaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Categoria.
 */
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    private final CategoriaSearchRepository categoriaSearchRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaSearchRepository categoriaSearchRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaSearchRepository = categoriaSearchRepository;
    }

    /**
     * Save a categoria.
     *
     * @param categoria the entity to save
     * @return the persisted entity
     */
    @Override
    public Categoria save(Categoria categoria) {
        log.debug("Request to save Categoria : {}", categoria);
        Categoria result = categoriaRepository.save(categoria);
        categoriaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the categorias.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        log.debug("Request to get all Categorias");
        return categoriaRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Categoria with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Categoria> findAllWithEagerRelationships(Pageable pageable) {
        return categoriaRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one categoria by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> findOne(Long id) {
        log.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the categoria by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
        categoriaSearchRepository.deleteById(id);
    }

    /**
     * Search for the categoria corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> search(String query) {
        log.debug("Request to search Categorias for query {}", query);
        return StreamSupport
            .stream(categoriaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
