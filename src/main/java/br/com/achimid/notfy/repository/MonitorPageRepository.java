package br.com.achimid.notfy.repository;

import br.com.achimid.notfy.model.MonitorPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorPageRepository extends JpaRepository<MonitorPage, Integer> {
}
