package br.com.efiel.web.rest;

import br.com.efiel.EfielApp;

import br.com.efiel.domain.Campanha;
import br.com.efiel.domain.Empresa;
import br.com.efiel.repository.CampanhaRepository;
import br.com.efiel.repository.search.CampanhaSearchRepository;
import br.com.efiel.service.CampanhaService;
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
import org.springframework.util.Base64Utils;
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
 * Test class for the CampanhaResource REST controller.
 *
 * @see CampanhaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EfielApp.class)
public class CampanhaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PREMIO = "AAAAAAAAAA";
    private static final String UPDATED_PREMIO = "BBBBBBBBBB";

    private static final String DEFAULT_REGRAS = "AAAAAAAAAA";
    private static final String UPDATED_REGRAS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NUMERO_SELOS = 0;
    private static final Integer UPDATED_NUMERO_SELOS = -1;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Autowired
    private CampanhaService campanhaService;

    /**
     * This repository is mocked in the br.com.efiel.repository.search test package.
     *
     * @see br.com.efiel.repository.search.CampanhaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CampanhaSearchRepository mockCampanhaSearchRepository;

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

    private MockMvc restCampanhaMockMvc;

    private Campanha campanha;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampanhaResource campanhaResource = new CampanhaResource(campanhaService);
        this.restCampanhaMockMvc = MockMvcBuilders.standaloneSetup(campanhaResource)
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
    public static Campanha createEntity(EntityManager em) {
        Campanha campanha = new Campanha()
            .nome(DEFAULT_NOME)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .premio(DEFAULT_PREMIO)
            .regras(DEFAULT_REGRAS)
            .expira(DEFAULT_EXPIRA)
            .numeroSelos(DEFAULT_NUMERO_SELOS);
        // Add required entity
        Empresa empresa = EmpresaResourceIntTest.createEntity(em);
        em.persist(empresa);
        em.flush();
        campanha.setEmpresa(empresa);
        return campanha;
    }

    @Before
    public void initTest() {
        campanha = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampanha() throws Exception {
        int databaseSizeBeforeCreate = campanhaRepository.findAll().size();

        // Create the Campanha
        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isCreated());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeCreate + 1);
        Campanha testCampanha = campanhaList.get(campanhaList.size() - 1);
        assertThat(testCampanha.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCampanha.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCampanha.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testCampanha.getPremio()).isEqualTo(DEFAULT_PREMIO);
        assertThat(testCampanha.getRegras()).isEqualTo(DEFAULT_REGRAS);
        assertThat(testCampanha.getExpira()).isEqualTo(DEFAULT_EXPIRA);
        assertThat(testCampanha.getNumeroSelos()).isEqualTo(DEFAULT_NUMERO_SELOS);

        // Validate the Campanha in Elasticsearch
        verify(mockCampanhaSearchRepository, times(1)).save(testCampanha);
    }

    @Test
    @Transactional
    public void createCampanhaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campanhaRepository.findAll().size();

        // Create the Campanha with an existing ID
        campanha.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Campanha in Elasticsearch
        verify(mockCampanhaSearchRepository, times(0)).save(campanha);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setNome(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPremioIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setPremio(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegrasIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setRegras(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiraIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setExpira(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroSelosIsRequired() throws Exception {
        int databaseSizeBeforeTest = campanhaRepository.findAll().size();
        // set the field null
        campanha.setNumeroSelos(null);

        // Create the Campanha, which fails.

        restCampanhaMockMvc.perform(post("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampanhas() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        // Get all the campanhaList
        restCampanhaMockMvc.perform(get("/api/campanhas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campanha.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].premio").value(hasItem(DEFAULT_PREMIO.toString())))
            .andExpect(jsonPath("$.[*].regras").value(hasItem(DEFAULT_REGRAS.toString())))
            .andExpect(jsonPath("$.[*].expira").value(hasItem(DEFAULT_EXPIRA.toString())))
            .andExpect(jsonPath("$.[*].numeroSelos").value(hasItem(DEFAULT_NUMERO_SELOS)));
    }
    
    @Test
    @Transactional
    public void getCampanha() throws Exception {
        // Initialize the database
        campanhaRepository.saveAndFlush(campanha);

        // Get the campanha
        restCampanhaMockMvc.perform(get("/api/campanhas/{id}", campanha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campanha.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.premio").value(DEFAULT_PREMIO.toString()))
            .andExpect(jsonPath("$.regras").value(DEFAULT_REGRAS.toString()))
            .andExpect(jsonPath("$.expira").value(DEFAULT_EXPIRA.toString()))
            .andExpect(jsonPath("$.numeroSelos").value(DEFAULT_NUMERO_SELOS));
    }

    @Test
    @Transactional
    public void getNonExistingCampanha() throws Exception {
        // Get the campanha
        restCampanhaMockMvc.perform(get("/api/campanhas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampanha() throws Exception {
        // Initialize the database
        campanhaService.save(campanha);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCampanhaSearchRepository);

        int databaseSizeBeforeUpdate = campanhaRepository.findAll().size();

        // Update the campanha
        Campanha updatedCampanha = campanhaRepository.findById(campanha.getId()).get();
        // Disconnect from session so that the updates on updatedCampanha are not directly saved in db
        em.detach(updatedCampanha);
        updatedCampanha
            .nome(UPDATED_NOME)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .premio(UPDATED_PREMIO)
            .regras(UPDATED_REGRAS)
            .expira(UPDATED_EXPIRA)
            .numeroSelos(UPDATED_NUMERO_SELOS);

        restCampanhaMockMvc.perform(put("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCampanha)))
            .andExpect(status().isOk());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeUpdate);
        Campanha testCampanha = campanhaList.get(campanhaList.size() - 1);
        assertThat(testCampanha.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCampanha.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCampanha.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testCampanha.getPremio()).isEqualTo(UPDATED_PREMIO);
        assertThat(testCampanha.getRegras()).isEqualTo(UPDATED_REGRAS);
        assertThat(testCampanha.getExpira()).isEqualTo(UPDATED_EXPIRA);
        assertThat(testCampanha.getNumeroSelos()).isEqualTo(UPDATED_NUMERO_SELOS);

        // Validate the Campanha in Elasticsearch
        verify(mockCampanhaSearchRepository, times(1)).save(testCampanha);
    }

    @Test
    @Transactional
    public void updateNonExistingCampanha() throws Exception {
        int databaseSizeBeforeUpdate = campanhaRepository.findAll().size();

        // Create the Campanha

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampanhaMockMvc.perform(put("/api/campanhas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campanha)))
            .andExpect(status().isBadRequest());

        // Validate the Campanha in the database
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Campanha in Elasticsearch
        verify(mockCampanhaSearchRepository, times(0)).save(campanha);
    }

    @Test
    @Transactional
    public void deleteCampanha() throws Exception {
        // Initialize the database
        campanhaService.save(campanha);

        int databaseSizeBeforeDelete = campanhaRepository.findAll().size();

        // Delete the campanha
        restCampanhaMockMvc.perform(delete("/api/campanhas/{id}", campanha.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Campanha> campanhaList = campanhaRepository.findAll();
        assertThat(campanhaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Campanha in Elasticsearch
        verify(mockCampanhaSearchRepository, times(1)).deleteById(campanha.getId());
    }

    @Test
    @Transactional
    public void searchCampanha() throws Exception {
        // Initialize the database
        campanhaService.save(campanha);
        when(mockCampanhaSearchRepository.search(queryStringQuery("id:" + campanha.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(campanha), PageRequest.of(0, 1), 1));
        // Search the campanha
        restCampanhaMockMvc.perform(get("/api/_search/campanhas?query=id:" + campanha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campanha.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].premio").value(hasItem(DEFAULT_PREMIO)))
            .andExpect(jsonPath("$.[*].regras").value(hasItem(DEFAULT_REGRAS)))
            .andExpect(jsonPath("$.[*].expira").value(hasItem(DEFAULT_EXPIRA.toString())))
            .andExpect(jsonPath("$.[*].numeroSelos").value(hasItem(DEFAULT_NUMERO_SELOS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campanha.class);
        Campanha campanha1 = new Campanha();
        campanha1.setId(1L);
        Campanha campanha2 = new Campanha();
        campanha2.setId(campanha1.getId());
        assertThat(campanha1).isEqualTo(campanha2);
        campanha2.setId(2L);
        assertThat(campanha1).isNotEqualTo(campanha2);
        campanha1.setId(null);
        assertThat(campanha1).isNotEqualTo(campanha2);
    }
}
