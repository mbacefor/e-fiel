package br.com.efiel.web.rest;

import br.com.efiel.EfielApp;

import br.com.efiel.domain.CartaoFidelidade;
import br.com.efiel.repository.CartaoFidelidadeRepository;
import br.com.efiel.repository.search.CartaoFidelidadeSearchRepository;
import br.com.efiel.service.CartaoFidelidadeService;
import br.com.efiel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static br.com.efiel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartaoFidelidadeResource REST controller.
 *
 * @see CartaoFidelidadeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EfielApp.class)
public class CartaoFidelidadeResourceIntTest {

    private static final LocalDate DEFAULT_DATA_CRIACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CRIACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_PREMIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PREMIO = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CartaoFidelidadeRepository cartaoFidelidadeRepository;

    @Autowired
    private CartaoFidelidadeService cartaoFidelidadeService;

    /**
     * This repository is mocked in the br.com.efiel.repository.search test package.
     *
     * @see br.com.efiel.repository.search.CartaoFidelidadeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CartaoFidelidadeSearchRepository mockCartaoFidelidadeSearchRepository;

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

    private MockMvc restCartaoFidelidadeMockMvc;

    private CartaoFidelidade cartaoFidelidade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartaoFidelidadeResource cartaoFidelidadeResource = new CartaoFidelidadeResource(cartaoFidelidadeService);
        this.restCartaoFidelidadeMockMvc = MockMvcBuilders.standaloneSetup(cartaoFidelidadeResource)
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
    public static CartaoFidelidade createEntity(EntityManager em) {
        CartaoFidelidade cartaoFidelidade = new CartaoFidelidade()
            .dataCriacao(DEFAULT_DATA_CRIACAO)
            .dataPremio(DEFAULT_DATA_PREMIO);
        return cartaoFidelidade;
    }

    @Before
    public void initTest() {
        cartaoFidelidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartaoFidelidade() throws Exception {
        int databaseSizeBeforeCreate = cartaoFidelidadeRepository.findAll().size();

        // Create the CartaoFidelidade
        restCartaoFidelidadeMockMvc.perform(post("/api/cartao-fidelidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartaoFidelidade)))
            .andExpect(status().isCreated());

        // Validate the CartaoFidelidade in the database
        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeCreate + 1);
        CartaoFidelidade testCartaoFidelidade = cartaoFidelidadeList.get(cartaoFidelidadeList.size() - 1);
        assertThat(testCartaoFidelidade.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
        assertThat(testCartaoFidelidade.getDataPremio()).isEqualTo(DEFAULT_DATA_PREMIO);

        // Validate the CartaoFidelidade in Elasticsearch
        verify(mockCartaoFidelidadeSearchRepository, times(1)).save(testCartaoFidelidade);
    }

    @Test
    @Transactional
    public void createCartaoFidelidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartaoFidelidadeRepository.findAll().size();

        // Create the CartaoFidelidade with an existing ID
        cartaoFidelidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartaoFidelidadeMockMvc.perform(post("/api/cartao-fidelidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartaoFidelidade)))
            .andExpect(status().isBadRequest());

        // Validate the CartaoFidelidade in the database
        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CartaoFidelidade in Elasticsearch
        verify(mockCartaoFidelidadeSearchRepository, times(0)).save(cartaoFidelidade);
    }

    @Test
    @Transactional
    public void checkDataCriacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartaoFidelidadeRepository.findAll().size();
        // set the field null
        cartaoFidelidade.setDataCriacao(null);

        // Create the CartaoFidelidade, which fails.

        restCartaoFidelidadeMockMvc.perform(post("/api/cartao-fidelidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartaoFidelidade)))
            .andExpect(status().isBadRequest());

        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCartaoFidelidades() throws Exception {
        // Initialize the database
        cartaoFidelidadeRepository.saveAndFlush(cartaoFidelidade);

        // Get all the cartaoFidelidadeList
        restCartaoFidelidadeMockMvc.perform(get("/api/cartao-fidelidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartaoFidelidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(DEFAULT_DATA_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].dataPremio").value(hasItem(DEFAULT_DATA_PREMIO.toString())));
    }
    
    @Test
    @Transactional
    public void getCartaoFidelidade() throws Exception {
        // Initialize the database
        cartaoFidelidadeRepository.saveAndFlush(cartaoFidelidade);

        // Get the cartaoFidelidade
        restCartaoFidelidadeMockMvc.perform(get("/api/cartao-fidelidades/{id}", cartaoFidelidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartaoFidelidade.getId().intValue()))
            .andExpect(jsonPath("$.dataCriacao").value(DEFAULT_DATA_CRIACAO.toString()))
            .andExpect(jsonPath("$.dataPremio").value(DEFAULT_DATA_PREMIO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCartaoFidelidade() throws Exception {
        // Get the cartaoFidelidade
        restCartaoFidelidadeMockMvc.perform(get("/api/cartao-fidelidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartaoFidelidade() throws Exception {
        // Initialize the database
        cartaoFidelidadeService.save(cartaoFidelidade);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCartaoFidelidadeSearchRepository);

        int databaseSizeBeforeUpdate = cartaoFidelidadeRepository.findAll().size();

        // Update the cartaoFidelidade
        CartaoFidelidade updatedCartaoFidelidade = cartaoFidelidadeRepository.findById(cartaoFidelidade.getId()).get();
        // Disconnect from session so that the updates on updatedCartaoFidelidade are not directly saved in db
        em.detach(updatedCartaoFidelidade);
        updatedCartaoFidelidade
            .dataCriacao(UPDATED_DATA_CRIACAO)
            .dataPremio(UPDATED_DATA_PREMIO);

        restCartaoFidelidadeMockMvc.perform(put("/api/cartao-fidelidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCartaoFidelidade)))
            .andExpect(status().isOk());

        // Validate the CartaoFidelidade in the database
        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeUpdate);
        CartaoFidelidade testCartaoFidelidade = cartaoFidelidadeList.get(cartaoFidelidadeList.size() - 1);
        assertThat(testCartaoFidelidade.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
        assertThat(testCartaoFidelidade.getDataPremio()).isEqualTo(UPDATED_DATA_PREMIO);

        // Validate the CartaoFidelidade in Elasticsearch
        verify(mockCartaoFidelidadeSearchRepository, times(1)).save(testCartaoFidelidade);
    }

    @Test
    @Transactional
    public void updateNonExistingCartaoFidelidade() throws Exception {
        int databaseSizeBeforeUpdate = cartaoFidelidadeRepository.findAll().size();

        // Create the CartaoFidelidade

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartaoFidelidadeMockMvc.perform(put("/api/cartao-fidelidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartaoFidelidade)))
            .andExpect(status().isBadRequest());

        // Validate the CartaoFidelidade in the database
        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CartaoFidelidade in Elasticsearch
        verify(mockCartaoFidelidadeSearchRepository, times(0)).save(cartaoFidelidade);
    }

    @Test
    @Transactional
    public void deleteCartaoFidelidade() throws Exception {
        // Initialize the database
        cartaoFidelidadeService.save(cartaoFidelidade);

        int databaseSizeBeforeDelete = cartaoFidelidadeRepository.findAll().size();

        // Delete the cartaoFidelidade
        restCartaoFidelidadeMockMvc.perform(delete("/api/cartao-fidelidades/{id}", cartaoFidelidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CartaoFidelidade> cartaoFidelidadeList = cartaoFidelidadeRepository.findAll();
        assertThat(cartaoFidelidadeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CartaoFidelidade in Elasticsearch
        verify(mockCartaoFidelidadeSearchRepository, times(1)).deleteById(cartaoFidelidade.getId());
    }

    @Test
    @Transactional
    public void searchCartaoFidelidade() throws Exception {
        // Initialize the database
        cartaoFidelidadeService.save(cartaoFidelidade);
        when(mockCartaoFidelidadeSearchRepository.search(queryStringQuery("id:" + cartaoFidelidade.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cartaoFidelidade), PageRequest.of(0, 1), 1));
        // Search the cartaoFidelidade
        restCartaoFidelidadeMockMvc.perform(get("/api/_search/cartao-fidelidades?query=id:" + cartaoFidelidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartaoFidelidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(DEFAULT_DATA_CRIACAO.toString())))
            .andExpect(jsonPath("$.[*].dataPremio").value(hasItem(DEFAULT_DATA_PREMIO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartaoFidelidade.class);
        CartaoFidelidade cartaoFidelidade1 = new CartaoFidelidade();
        cartaoFidelidade1.setId(1L);
        CartaoFidelidade cartaoFidelidade2 = new CartaoFidelidade();
        cartaoFidelidade2.setId(cartaoFidelidade1.getId());
        assertThat(cartaoFidelidade1).isEqualTo(cartaoFidelidade2);
        cartaoFidelidade2.setId(2L);
        assertThat(cartaoFidelidade1).isNotEqualTo(cartaoFidelidade2);
        cartaoFidelidade1.setId(null);
        assertThat(cartaoFidelidade1).isNotEqualTo(cartaoFidelidade2);
    }
}
