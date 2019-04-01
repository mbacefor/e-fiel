package br.com.efiel.web.rest;

import br.com.efiel.EfielApp;

import br.com.efiel.domain.SeloCartao;
import br.com.efiel.repository.SeloCartaoRepository;
import br.com.efiel.repository.search.SeloCartaoSearchRepository;
import br.com.efiel.service.SeloCartaoService;
import br.com.efiel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static br.com.efiel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.efiel.domain.enumeration.TipoSelo;
/**
 * Test class for the SeloCartaoResource REST controller.
 *
 * @see SeloCartaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EfielApp.class)
public class SeloCartaoResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final TipoSelo DEFAULT_TIPO = TipoSelo.COMPRA;
    private static final TipoSelo UPDATED_TIPO = TipoSelo.PROMOCAO;

    @Autowired
    private SeloCartaoRepository seloCartaoRepository;

    @Autowired
    private SeloCartaoService seloCartaoService;

    /**
     * This repository is mocked in the br.com.efiel.repository.search test package.
     *
     * @see br.com.efiel.repository.search.SeloCartaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private SeloCartaoSearchRepository mockSeloCartaoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSeloCartaoMockMvc;

    private SeloCartao seloCartao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SeloCartaoResource seloCartaoResource = new SeloCartaoResource(seloCartaoService);
        this.restSeloCartaoMockMvc = MockMvcBuilders.standaloneSetup(seloCartaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeloCartao createEntity(EntityManager em) {
        SeloCartao seloCartao = new SeloCartao()
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .tipo(DEFAULT_TIPO);
        return seloCartao;
    }

    @Before
    public void initTest() {
        seloCartao = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeloCartao() throws Exception {
        int databaseSizeBeforeCreate = seloCartaoRepository.findAll().size();

        // Create the SeloCartao
        restSeloCartaoMockMvc.perform(post("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isCreated());

        // Validate the SeloCartao in the database
        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeCreate + 1);
        SeloCartao testSeloCartao = seloCartaoList.get(seloCartaoList.size() - 1);
        assertThat(testSeloCartao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSeloCartao.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testSeloCartao.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the SeloCartao in Elasticsearch
        verify(mockSeloCartaoSearchRepository, times(1)).save(testSeloCartao);
    }

    @Test
    @Transactional
    public void createSeloCartaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seloCartaoRepository.findAll().size();

        // Create the SeloCartao with an existing ID
        seloCartao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeloCartaoMockMvc.perform(post("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isBadRequest());

        // Validate the SeloCartao in the database
        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the SeloCartao in Elasticsearch
        verify(mockSeloCartaoSearchRepository, times(0)).save(seloCartao);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = seloCartaoRepository.findAll().size();
        // set the field null
        seloCartao.setDescricao(null);

        // Create the SeloCartao, which fails.

        restSeloCartaoMockMvc.perform(post("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isBadRequest());

        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = seloCartaoRepository.findAll().size();
        // set the field null
        seloCartao.setValor(null);

        // Create the SeloCartao, which fails.

        restSeloCartaoMockMvc.perform(post("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isBadRequest());

        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = seloCartaoRepository.findAll().size();
        // set the field null
        seloCartao.setTipo(null);

        // Create the SeloCartao, which fails.

        restSeloCartaoMockMvc.perform(post("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isBadRequest());

        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeloCartaos() throws Exception {
        // Initialize the database
        seloCartaoRepository.saveAndFlush(seloCartao);

        // Get all the seloCartaoList
        restSeloCartaoMockMvc.perform(get("/api/selo-cartaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seloCartao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }
    
    @Test
    @Transactional
    public void getSeloCartao() throws Exception {
        // Initialize the database
        seloCartaoRepository.saveAndFlush(seloCartao);

        // Get the seloCartao
        restSeloCartaoMockMvc.perform(get("/api/selo-cartaos/{id}", seloCartao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(seloCartao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeloCartao() throws Exception {
        // Get the seloCartao
        restSeloCartaoMockMvc.perform(get("/api/selo-cartaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeloCartao() throws Exception {
        // Initialize the database
        seloCartaoService.save(seloCartao);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSeloCartaoSearchRepository);

        int databaseSizeBeforeUpdate = seloCartaoRepository.findAll().size();

        // Update the seloCartao
        SeloCartao updatedSeloCartao = seloCartaoRepository.findById(seloCartao.getId()).get();
        // Disconnect from session so that the updates on updatedSeloCartao are not directly saved in db
        em.detach(updatedSeloCartao);
        updatedSeloCartao
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .tipo(UPDATED_TIPO);

        restSeloCartaoMockMvc.perform(put("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSeloCartao)))
            .andExpect(status().isOk());

        // Validate the SeloCartao in the database
        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeUpdate);
        SeloCartao testSeloCartao = seloCartaoList.get(seloCartaoList.size() - 1);
        assertThat(testSeloCartao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSeloCartao.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testSeloCartao.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the SeloCartao in Elasticsearch
        verify(mockSeloCartaoSearchRepository, times(1)).save(testSeloCartao);
    }

    @Test
    @Transactional
    public void updateNonExistingSeloCartao() throws Exception {
        int databaseSizeBeforeUpdate = seloCartaoRepository.findAll().size();

        // Create the SeloCartao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeloCartaoMockMvc.perform(put("/api/selo-cartaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seloCartao)))
            .andExpect(status().isBadRequest());

        // Validate the SeloCartao in the database
        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SeloCartao in Elasticsearch
        verify(mockSeloCartaoSearchRepository, times(0)).save(seloCartao);
    }

    @Test
    @Transactional
    public void deleteSeloCartao() throws Exception {
        // Initialize the database
        seloCartaoService.save(seloCartao);

        int databaseSizeBeforeDelete = seloCartaoRepository.findAll().size();

        // Delete the seloCartao
        restSeloCartaoMockMvc.perform(delete("/api/selo-cartaos/{id}", seloCartao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SeloCartao> seloCartaoList = seloCartaoRepository.findAll();
        assertThat(seloCartaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SeloCartao in Elasticsearch
        verify(mockSeloCartaoSearchRepository, times(1)).deleteById(seloCartao.getId());
    }

    @Test
    @Transactional
    public void searchSeloCartao() throws Exception {
        // Initialize the database
        seloCartaoService.save(seloCartao);
        when(mockSeloCartaoSearchRepository.search(queryStringQuery("id:" + seloCartao.getId())))
            .thenReturn(Collections.singletonList(seloCartao));
        // Search the seloCartao
        restSeloCartaoMockMvc.perform(get("/api/_search/selo-cartaos?query=id:" + seloCartao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seloCartao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeloCartao.class);
        SeloCartao seloCartao1 = new SeloCartao();
        seloCartao1.setId(1L);
        SeloCartao seloCartao2 = new SeloCartao();
        seloCartao2.setId(seloCartao1.getId());
        assertThat(seloCartao1).isEqualTo(seloCartao2);
        seloCartao2.setId(2L);
        assertThat(seloCartao1).isNotEqualTo(seloCartao2);
        seloCartao1.setId(null);
        assertThat(seloCartao1).isNotEqualTo(seloCartao2);
    }
}
