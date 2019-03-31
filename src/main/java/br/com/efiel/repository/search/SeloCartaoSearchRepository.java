package br.com.efiel.repository.search;

import br.com.efiel.domain.SeloCartao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SeloCartao entity.
 */
public interface SeloCartaoSearchRepository extends ElasticsearchRepository<SeloCartao, Long> {
}
