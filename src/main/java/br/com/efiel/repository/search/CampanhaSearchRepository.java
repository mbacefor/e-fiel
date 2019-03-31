package br.com.efiel.repository.search;

import br.com.efiel.domain.Campanha;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Campanha entity.
 */
public interface CampanhaSearchRepository extends ElasticsearchRepository<Campanha, Long> {
}
