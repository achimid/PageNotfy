package br.com.achimid.notfy.repository;

import br.com.achimid.notfy.model.DocPage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocPageRepository extends CrudRepository<DocPage, Integer>{
}
