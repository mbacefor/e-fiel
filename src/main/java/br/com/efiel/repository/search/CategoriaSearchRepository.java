package br.com.efiel.repository.search;

import br.com.efiel.domain.Categoria;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Categoria entity.
 */
public interface CategoriaSearchRepository extends ElasticsearchRepository<Categoria, Long> {
}
