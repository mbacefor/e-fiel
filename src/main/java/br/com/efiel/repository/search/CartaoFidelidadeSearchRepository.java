package br.com.efiel.repository.search;

import br.com.efiel.domain.CartaoFidelidade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CartaoFidelidade entity.
 */
public interface CartaoFidelidadeSearchRepository extends ElasticsearchRepository<CartaoFidelidade, Long> {
}
