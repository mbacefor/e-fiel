package br.com.efiel.web.rest;

import br.com.efiel.EfielApp;

import br.com.efiel.domain.Categoria;
import br.com.efiel.repository.CategoriaRepository;
import br.com.efiel.repository.search.CategoriaSearchRepository;
import br.com.efiel.service.CategoriaService;
import br.com.efiel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the CategoriaResource REST controller.
 *
 * @see CategoriaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EfielApp.class)
public class CategoriaResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaRepository categoriaRepositoryMock;

    @Mock
    private CategoriaService categoriaServiceMock;

    @Autowired
    private CategoriaService categoriaService;

    /**
     * This repository is mocked in the br.com.efiel.repository.search test package.
     *
     * @see br.com.efiel.repository.search.CategoriaSearchRepositoryMockConfiguration
     */
    @Autowired
    private CategoriaSearchRepository mockCategoriaSearchRepository;

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

    private MockMvc restCategoriaMockMvc;

    private Categoria categoria;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoriaResource categoriaResource = new CategoriaResource(categoriaService);
        this.restCategoriaMockMvc = MockMvcBuilders.standaloneSetup(categoriaResource)
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
    public static Categoria createEntity(EntityManager em) {
        Categoria categoria = new Categoria()
            .descricao(DEFAULT_DESCRICAO)
            .ativo(DEFAULT_ATIVO);
        return categoria;
    }

    @Before
    public void initTest() {
        categoria = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategoria() throws Exception {
        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();

        // Create the Categoria
        restCategoriaMockMvc.perform(post("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isCreated());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate + 1);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testCategoria.isAtivo()).isEqualTo(DEFAULT_ATIVO);

        // Validate the Categoria in Elasticsearch
        verify(mockCategoriaSearchRepository, times(1)).save(testCategoria);
    }

    @Test
    @Transactional
    public void createCategoriaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoriaRepository.findAll().size();

        // Create the Categoria with an existing ID
        categoria.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaMockMvc.perform(post("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Categoria in Elasticsearch
        verify(mockCategoriaSearchRepository, times(0)).save(categoria);
    }

    @Test
    @Transactional
    public void checkAtivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaRepository.findAll().size();
        // set the field null
        categoria.setAtivo(null);

        // Create the Categoria, which fails.

        restCategoriaMockMvc.perform(post("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorias() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get all the categoriaList
        restCategoriaMockMvc.perform(get("/api/categorias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCategoriasWithEagerRelationshipsIsEnabled() throws Exception {
        CategoriaResource categoriaResource = new CategoriaResource(categoriaServiceMock);
        when(categoriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCategoriaMockMvc = MockMvcBuilders.standaloneSetup(categoriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCategoriaMockMvc.perform(get("/api/categorias?eagerload=true"))
        .andExpect(status().isOk());

        verify(categoriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCategoriasWithEagerRelationshipsIsNotEnabled() throws Exception {
        CategoriaResource categoriaResource = new CategoriaResource(categoriaServiceMock);
            when(categoriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCategoriaMockMvc = MockMvcBuilders.standaloneSetup(categoriaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCategoriaMockMvc.perform(get("/api/categorias?eagerload=true"))
        .andExpect(status().isOk());

            verify(categoriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCategoria() throws Exception {
        // Initialize the database
        categoriaRepository.saveAndFlush(categoria);

        // Get the categoria
        restCategoriaMockMvc.perform(get("/api/categorias/{id}", categoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categoria.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCategoria() throws Exception {
        // Get the categoria
        restCategoriaMockMvc.perform(get("/api/categorias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoria() throws Exception {
        // Initialize the database
        categoriaService.save(categoria);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCategoriaSearchRepository);

        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Update the categoria
        Categoria updatedCategoria = categoriaRepository.findById(categoria.getId()).get();
        // Disconnect from session so that the updates on updatedCategoria are not directly saved in db
        em.detach(updatedCategoria);
        updatedCategoria
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO);

        restCategoriaMockMvc.perform(put("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategoria)))
            .andExpect(status().isOk());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);
        Categoria testCategoria = categoriaList.get(categoriaList.size() - 1);
        assertThat(testCategoria.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCategoria.isAtivo()).isEqualTo(UPDATED_ATIVO);

        // Validate the Categoria in Elasticsearch
        verify(mockCategoriaSearchRepository, times(1)).save(testCategoria);
    }

    @Test
    @Transactional
    public void updateNonExistingCategoria() throws Exception {
        int databaseSizeBeforeUpdate = categoriaRepository.findAll().size();

        // Create the Categoria

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaMockMvc.perform(put("/api/categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoria)))
            .andExpect(status().isBadRequest());

        // Validate the Categoria in the database
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Categoria in Elasticsearch
        verify(mockCategoriaSearchRepository, times(0)).save(categoria);
    }

    @Test
    @Transactional
    public void deleteCategoria() throws Exception {
        // Initialize the database
        categoriaService.save(categoria);

        int databaseSizeBeforeDelete = categoriaRepository.findAll().size();

        // Delete the categoria
        restCategoriaMockMvc.perform(delete("/api/categorias/{id}", categoria.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Categoria> categoriaList = categoriaRepository.findAll();
        assertThat(categoriaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Categoria in Elasticsearch
        verify(mockCategoriaSearchRepository, times(1)).deleteById(categoria.getId());
    }

    @Test
    @Transactional
    public void searchCategoria() throws Exception {
        // Initialize the database
        categoriaService.save(categoria);
        when(mockCategoriaSearchRepository.search(queryStringQuery("id:" + categoria.getId())))
            .thenReturn(Collections.singletonList(categoria));
        // Search the categoria
        restCategoriaMockMvc.perform(get("/api/_search/categorias?query=id:" + categoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categoria.class);
        Categoria categoria1 = new Categoria();
        categoria1.setId(1L);
        Categoria categoria2 = new Categoria();
        categoria2.setId(categoria1.getId());
        assertThat(categoria1).isEqualTo(categoria2);
        categoria2.setId(2L);
        assertThat(categoria1).isNotEqualTo(categoria2);
        categoria1.setId(null);
        assertThat(categoria1).isNotEqualTo(categoria2);
    }
}
