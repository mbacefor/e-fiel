package br.com.efiel.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CampanhaSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CampanhaSearchRepositoryMockConfiguration {

    @MockBean
    private CampanhaSearchRepository mockCampanhaSearchRepository;

}
