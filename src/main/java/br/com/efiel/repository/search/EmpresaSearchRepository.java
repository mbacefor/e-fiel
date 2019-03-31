package br.com.efiel.repository.search;

import br.com.efiel.domain.Empresa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Empresa entity.
 */
public interface EmpresaSearchRepository extends ElasticsearchRepository<Empresa, Long> {
}
