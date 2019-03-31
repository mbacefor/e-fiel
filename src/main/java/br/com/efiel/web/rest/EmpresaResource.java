package br.com.efiel.web.rest;
import br.com.efiel.domain.Empresa;
import br.com.efiel.repository.EmpresaRepository;
import br.com.efiel.repository.search.EmpresaSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Empresa.
 */
@RestController
@RequestMapping("/api")
public class EmpresaResource {

    private final Logger log = LoggerFactory.getLogger(EmpresaResource.class);

    private static final String ENTITY_NAME = "empresa";

    private final EmpresaRepository empresaRepository;

    private final EmpresaSearchRepository empresaSearchRepository;

    public EmpresaResource(EmpresaRepository empresaRepository, EmpresaSearchRepository empresaSearchRepository) {
        this.empresaRepository = empresaRepository;
        this.empresaSearchRepository = empresaSearchRepository;
    }

    /**
     * POST  /empresas : Create a new empresa.
     *
     * @param empresa the empresa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empresa, or with status 400 (Bad Request) if the empresa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empresas")
    public ResponseEntity<Empresa> createEmpresa(@Valid @RequestBody Empresa empresa) throws URISyntaxException {
        log.debug("REST request to save Empresa : {}", empresa);
        if (empresa.getId() != null) {
            throw new BadRequestAlertException("A new empresa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Empresa result = empresaRepository.save(empresa);
        empresaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/empresas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empresas : Updates an existing empresa.
     *
     * @param empresa the empresa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empresa,
     * or with status 400 (Bad Request) if the empresa is not valid,
     * or with status 500 (Internal Server Error) if the empresa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empresas")
    public ResponseEntity<Empresa> updateEmpresa(@Valid @RequestBody Empresa empresa) throws URISyntaxException {
        log.debug("REST request to update Empresa : {}", empresa);
        if (empresa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Empresa result = empresaRepository.save(empresa);
        empresaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, empresa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empresas : get all the empresas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of empresas in body
     */
    @GetMapping("/empresas")
    public ResponseEntity<List<Empresa>> getAllEmpresas(Pageable pageable) {
        log.debug("REST request to get a page of Empresas");
        Page<Empresa> page = empresaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/empresas");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /empresas/:id : get the "id" empresa.
     *
     * @param id the id of the empresa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empresa, or with status 404 (Not Found)
     */
    @GetMapping("/empresas/{id}")
    public ResponseEntity<Empresa> getEmpresa(@PathVariable Long id) {
        log.debug("REST request to get Empresa : {}", id);
        Optional<Empresa> empresa = empresaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(empresa);
    }

    /**
     * DELETE  /empresas/:id : delete the "id" empresa.
     *
     * @param id the id of the empresa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        log.debug("REST request to delete Empresa : {}", id);
        empresaRepository.deleteById(id);
        empresaSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/empresas?query=:query : search for the empresa corresponding
     * to the query.
     *
     * @param query the query of the empresa search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/empresas")
    public ResponseEntity<List<Empresa>> searchEmpresas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Empresas for query {}", query);
        Page<Empresa> page = empresaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/empresas");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
