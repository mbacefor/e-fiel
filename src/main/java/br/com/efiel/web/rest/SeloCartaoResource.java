package br.com.efiel.web.rest;
import br.com.efiel.domain.SeloCartao;
import br.com.efiel.service.SeloCartaoService;
import br.com.efiel.web.rest.errors.BadRequestAlertException;
import br.com.efiel.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing SeloCartao.
 */
@RestController
@RequestMapping("/api")
public class SeloCartaoResource {

    private final Logger log = LoggerFactory.getLogger(SeloCartaoResource.class);

    private static final String ENTITY_NAME = "seloCartao";

    private final SeloCartaoService seloCartaoService;

    public SeloCartaoResource(SeloCartaoService seloCartaoService) {
        this.seloCartaoService = seloCartaoService;
    }

    /**
     * POST  /selo-cartaos : Create a new seloCartao.
     *
     * @param seloCartao the seloCartao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seloCartao, or with status 400 (Bad Request) if the seloCartao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/selo-cartaos")
    public ResponseEntity<SeloCartao> createSeloCartao(@Valid @RequestBody SeloCartao seloCartao) throws URISyntaxException {
        log.debug("REST request to save SeloCartao : {}", seloCartao);
        if (seloCartao.getId() != null) {
            throw new BadRequestAlertException("A new seloCartao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeloCartao result = seloCartaoService.save(seloCartao);
        return ResponseEntity.created(new URI("/api/selo-cartaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /selo-cartaos : Updates an existing seloCartao.
     *
     * @param seloCartao the seloCartao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seloCartao,
     * or with status 400 (Bad Request) if the seloCartao is not valid,
     * or with status 500 (Internal Server Error) if the seloCartao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/selo-cartaos")
    public ResponseEntity<SeloCartao> updateSeloCartao(@Valid @RequestBody SeloCartao seloCartao) throws URISyntaxException {
        log.debug("REST request to update SeloCartao : {}", seloCartao);
        if (seloCartao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SeloCartao result = seloCartaoService.save(seloCartao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, seloCartao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /selo-cartaos : get all the seloCartaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of seloCartaos in body
     */
    @GetMapping("/selo-cartaos")
    public List<SeloCartao> getAllSeloCartaos() {
        log.debug("REST request to get all SeloCartaos");
        return seloCartaoService.findAll();
    }

    /**
     * GET  /selo-cartaos/:id : get the "id" seloCartao.
     *
     * @param id the id of the seloCartao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seloCartao, or with status 404 (Not Found)
     */
    @GetMapping("/selo-cartaos/{id}")
    public ResponseEntity<SeloCartao> getSeloCartao(@PathVariable Long id) {
        log.debug("REST request to get SeloCartao : {}", id);
        Optional<SeloCartao> seloCartao = seloCartaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seloCartao);
    }

    /**
     * DELETE  /selo-cartaos/:id : delete the "id" seloCartao.
     *
     * @param id the id of the seloCartao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/selo-cartaos/{id}")
    public ResponseEntity<Void> deleteSeloCartao(@PathVariable Long id) {
        log.debug("REST request to delete SeloCartao : {}", id);
        seloCartaoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/selo-cartaos?query=:query : search for the seloCartao corresponding
     * to the query.
     *
     * @param query the query of the seloCartao search
     * @return the result of the search
     */
    @GetMapping("/_search/selo-cartaos")
    public List<SeloCartao> searchSeloCartaos(@RequestParam String query) {
        log.debug("REST request to search SeloCartaos for query {}", query);
        return seloCartaoService.search(query);
    }

}
