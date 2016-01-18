package org.jboss.as.quickstarts.datagrid.prova.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name="processedData")
@BatchSize(size=1000)
public class ProcessedData extends RawData{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1129343443881488736L;
	
	
	

}
