package org.jboss.as.quickstarts.datagrid.prova.repo;

import org.jboss.as.quickstarts.datagrid.prova.model.ProcessedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDataRepo extends JpaRepository<ProcessedData, Long>{
	

}
