package org.jboss.as.quickstarts.datagrid.prova.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.as.quickstarts.datagrid.prova.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long>{
	
//	ArrayList<RawData> findRawDataByKey(String key);
	
	Set<RawData> findRawDataByHalfKey(String key);
	
}
