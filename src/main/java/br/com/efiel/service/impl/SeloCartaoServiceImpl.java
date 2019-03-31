package br.com.efiel.service.impl;

import br.com.efiel.service.SeloCartaoService;
import br.com.efiel.domain.SeloCartao;
import br.com.efiel.repository.SeloCartaoRepository;
import br.com.efiel.repository.search.SeloCartaoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SeloCartao.
 */
@Service
@Transactional
public class SeloCartaoServiceImpl implements SeloCartaoService {

    private final Logger log = LoggerFactory.getLogger(SeloCartaoServiceImpl.class);

    private final SeloCartaoRepository seloCartaoRepository;

    private final SeloCartaoSearchRepository seloCartaoSearchRepository;

    public SeloCartaoServiceImpl(SeloCartaoRepository seloCartaoRepository, SeloCartaoSearchRepository seloCartaoSearchRepository) {
        this.seloCartaoRepository = seloCartaoRepository;
        this.seloCartaoSearchRepository = seloCartaoSearchRepository;
    }

    /**
     * Save a seloCartao.
     *
     * @param seloCartao the entity to save
     * @return the persisted entity
     */
    @Override
    public SeloCartao save(SeloCartao seloCartao) {
        log.debug("Request to save SeloCartao : {}", seloCartao);
        SeloCartao result = seloCartaoRepository.save(seloCartao);
        seloCartaoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the seloCartaos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SeloCartao> findAll() {
        log.debug("Request to get all SeloCartaos");
        return seloCartaoRepository.findAll();
    }


    /**
     * Get one seloCartao by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SeloCartao> findOne(Long id) {
        log.debug("Request to get SeloCartao : {}", id);
        return seloCartaoRepository.findById(id);
    }

    /**
     * Delete the seloCartao by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SeloCartao : {}", id);
        seloCartaoRepository.deleteById(id);
        seloCartaoSearchRepository.deleteById(id);
    }

    /**
     * Search for the seloCartao corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SeloCartao> search(String query) {
        log.debug("Request to search SeloCartaos for query {}", query);
        return StreamSupport
            .stream(seloCartaoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
