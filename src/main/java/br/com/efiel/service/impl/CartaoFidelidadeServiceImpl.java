package br.com.efiel.service.impl;

import br.com.efiel.service.CartaoFidelidadeService;
import br.com.efiel.domain.CartaoFidelidade;
import br.com.efiel.repository.CartaoFidelidadeRepository;
import br.com.efiel.repository.search.CartaoFidelidadeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CartaoFidelidade.
 */
@Service
@Transactional
public class CartaoFidelidadeServiceImpl implements CartaoFidelidadeService {

    private final Logger log = LoggerFactory.getLogger(CartaoFidelidadeServiceImpl.class);

    private final CartaoFidelidadeRepository cartaoFidelidadeRepository;

    private final CartaoFidelidadeSearchRepository cartaoFidelidadeSearchRepository;

    public CartaoFidelidadeServiceImpl(CartaoFidelidadeRepository cartaoFidelidadeRepository, CartaoFidelidadeSearchRepository cartaoFidelidadeSearchRepository) {
        this.cartaoFidelidadeRepository = cartaoFidelidadeRepository;
        this.cartaoFidelidadeSearchRepository = cartaoFidelidadeSearchRepository;
    }

    /**
     * Save a cartaoFidelidade.
     *
     * @param cartaoFidelidade the entity to save
     * @return the persisted entity
     */
    @Override
    public CartaoFidelidade save(CartaoFidelidade cartaoFidelidade) {
        log.debug("Request to save CartaoFidelidade : {}", cartaoFidelidade);
        CartaoFidelidade result = cartaoFidelidadeRepository.save(cartaoFidelidade);
        cartaoFidelidadeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the cartaoFidelidades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartaoFidelidade> findAll(Pageable pageable) {
        log.debug("Request to get all CartaoFidelidades");
        return cartaoFidelidadeRepository.findAll(pageable);
    }


    /**
     * Get one cartaoFidelidade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CartaoFidelidade> findOne(Long id) {
        log.debug("Request to get CartaoFidelidade : {}", id);
        return cartaoFidelidadeRepository.findById(id);
    }

    /**
     * Delete the cartaoFidelidade by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CartaoFidelidade : {}", id);
        cartaoFidelidadeRepository.deleteById(id);
        cartaoFidelidadeSearchRepository.deleteById(id);
    }

    /**
     * Search for the cartaoFidelidade corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartaoFidelidade> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CartaoFidelidades for query {}", query);
        return cartaoFidelidadeSearchRepository.search(queryStringQuery(query), pageable);    }
}
