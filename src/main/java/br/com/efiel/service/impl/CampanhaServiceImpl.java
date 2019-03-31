package br.com.efiel.service.impl;

import br.com.efiel.service.CampanhaService;
import br.com.efiel.domain.Campanha;
import br.com.efiel.repository.CampanhaRepository;
import br.com.efiel.repository.search.CampanhaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Campanha.
 */
@Service
@Transactional
public class CampanhaServiceImpl implements CampanhaService {

    private final Logger log = LoggerFactory.getLogger(CampanhaServiceImpl.class);

    private final CampanhaRepository campanhaRepository;

    private final CampanhaSearchRepository campanhaSearchRepository;

    public CampanhaServiceImpl(CampanhaRepository campanhaRepository, CampanhaSearchRepository campanhaSearchRepository) {
        this.campanhaRepository = campanhaRepository;
        this.campanhaSearchRepository = campanhaSearchRepository;
    }

    /**
     * Save a campanha.
     *
     * @param campanha the entity to save
     * @return the persisted entity
     */
    @Override
    public Campanha save(Campanha campanha) {
        log.debug("Request to save Campanha : {}", campanha);
        Campanha result = campanhaRepository.save(campanha);
        campanhaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the campanhas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Campanha> findAll(Pageable pageable) {
        log.debug("Request to get all Campanhas");
        return campanhaRepository.findAll(pageable);
    }


    /**
     * Get one campanha by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Campanha> findOne(Long id) {
        log.debug("Request to get Campanha : {}", id);
        return campanhaRepository.findById(id);
    }

    /**
     * Delete the campanha by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Campanha : {}", id);
        campanhaRepository.deleteById(id);
        campanhaSearchRepository.deleteById(id);
    }

    /**
     * Search for the campanha corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Campanha> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Campanhas for query {}", query);
        return campanhaSearchRepository.search(queryStringQuery(query), pageable);    }
}
