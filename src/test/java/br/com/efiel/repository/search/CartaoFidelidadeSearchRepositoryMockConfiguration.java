package br.com.efiel.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CartaoFidelidadeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CartaoFidelidadeSearchRepositoryMockConfiguration {

    @MockBean
    private CartaoFidelidadeSearchRepository mockCartaoFidelidadeSearchRepository;

}
