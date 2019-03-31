package br.com.efiel.web.rest;
import br.com.efiel.domain.CartaoFidelidade;
import br.com.efiel.service.CartaoFidelidadeService;
import br.com.efiel.web.rest.errors.BadRequestAlertException;
import br.com.efiel.web.rest.util.HeaderUtil;
import br.com.efiel.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CartaoFidelidade.
 */
@RestController
@RequestMapping("/api")
public class CartaoFidelidadeResource {

    private final Logger log = LoggerFactory.getLogger(CartaoFidelidadeResource.class);

    private static final String ENTITY_NAME = "cartaoFidelidade";

    private final CartaoFidelidadeService cartaoFidelidadeService;

    public CartaoFidelidadeResource(CartaoFidelidadeService cartaoFidelidadeService) {
        this.cartaoFidelidadeService = cartaoFidelidadeService;
    }

    /**
     * POST  /cartao-fidelidades : Create a new cartaoFidelidade.
     *
     * @param cartaoFidelidade the cartaoFidelidade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartaoFidelidade, or with status 400 (Bad Request) if the cartaoFidelidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cartao-fidelidades")
    public ResponseEntity<CartaoFidelidade> createCartaoFidelidade(@Valid @RequestBody CartaoFidelidade cartaoFidelidade) throws URISyntaxException {
        log.debug("REST request to save CartaoFidelidade : {}", cartaoFidelidade);
        if (cartaoFidelidade.getId() != null) {
            throw new BadRequestAlertException("A new cartaoFidelidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartaoFidelidade result = cartaoFidelidadeService.save(cartaoFidelidade);
        return ResponseEntity.created(new URI("/api/cartao-fidelidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cartao-fidelidades : Updates an existing cartaoFidelidade.
     *
     * @param cartaoFidelidade the cartaoFidelidade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartaoFidelidade,
     * or with status 400 (Bad Request) if the cartaoFidelidade is not valid,
     * or with status 500 (Internal Server Error) if the cartaoFidelidade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cartao-fidelidades")
    public ResponseEntity<CartaoFidelidade> updateCartaoFidelidade(@Valid @RequestBody CartaoFidelidade cartaoFidelidade) throws URISyntaxException {
        log.debug("REST request to update CartaoFidelidade : {}", cartaoFidelidade);
        if (cartaoFidelidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CartaoFidelidade result = cartaoFidelidadeService.save(cartaoFidelidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartaoFidelidade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cartao-fidelidades : get all the cartaoFidelidades.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cartaoFidelidades in body
     */
    @GetMapping("/cartao-fidelidades")
    public ResponseEntity<List<CartaoFidelidade>> getAllCartaoFidelidades(Pageable pageable) {
        log.debug("REST request to get a page of CartaoFidelidades");
        Page<CartaoFidelidade> page = cartaoFidelidadeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cartao-fidelidades");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /cartao-fidelidades/:id : get the "id" cartaoFidelidade.
     *
     * @param id the id of the cartaoFidelidade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartaoFidelidade, or with status 404 (Not Found)
     */
    @GetMapping("/cartao-fidelidades/{id}")
    public ResponseEntity<CartaoFidelidade> getCartaoFidelidade(@PathVariable Long id) {
        log.debug("REST request to get CartaoFidelidade : {}", id);
        Optional<CartaoFidelidade> cartaoFidelidade = cartaoFidelidadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartaoFidelidade);
    }

    /**
     * DELETE  /cartao-fidelidades/:id : delete the "id" cartaoFidelidade.
     *
     * @param id the id of the cartaoFidelidade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cartao-fidelidades/{id}")
    public ResponseEntity<Void> deleteCartaoFidelidade(@PathVariable Long id) {
        log.debug("REST request to delete CartaoFidelidade : {}", id);
        cartaoFidelidadeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cartao-fidelidades?query=:query : search for the cartaoFidelidade corresponding
     * to the query.
     *
     * @param query the query of the cartaoFidelidade search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cartao-fidelidades")
    public ResponseEntity<List<CartaoFidelidade>> searchCartaoFidelidades(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CartaoFidelidades for query {}", query);
        Page<CartaoFidelidade> page = cartaoFidelidadeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cartao-fidelidades");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
