package org.jboss.as.quickstarts.datagrid.prova.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.infinispan.commons.marshall.SerializeWith;
import org.jboss.as.quickstarts.datagrid.prova.model.base.BaseEntity;
import org.jboss.as.quickstarts.datagrid.prova.model.custom.RDExternalizer;

@Entity
@Table(name="rawData")
@BatchSize(size=1000)
@SerializeWith(RDExternalizer.class)
public class RawData extends BaseEntity<Long> implements Serializable{
    

	/**
	 * 
	 */
	private static final long serialVersionUID = 7452099048320734527L;

	@Id
	@SequenceGenerator(name = "g_seq_raw", sequenceName = "seq_raw")
	@GeneratedValue(generator = "g_seq_raw", strategy=GenerationType.AUTO)
    @Column(name ="id_record")
    private Long id=0L;
    
    @Column(name = "halfKey", length = 20, nullable = false)
    private String halfKey;
    
    @Column(name = "firstValue", length = 20, nullable = true)
    private String firstValue;
    
    @Column(name = "secondValue", length = 20, nullable = true)
    private String secondValue; 
    
    @Column(name = "thirdValue", length = 20, nullable = true)
    private String thirdValue; 
    
    @Column(name = "fileName", length = 40, nullable = false)
    private String fileName;
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getHalfKey() {
		return halfKey;
	}

	public void setHalfKey(String halfKey) {
		this.halfKey = halfKey;
	}

	public String getThirdValue() {
		return thirdValue;
	}

	public void setThirdValue(String thirdValue) {
		this.thirdValue = thirdValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((firstValue == null) ? 0 : firstValue.hashCode());
		result = prime * result + ((halfKey == null) ? 0 : halfKey.hashCode());
		result = prime * result + ((secondValue == null) ? 0 : secondValue.hashCode());
		result = prime * result + ((thirdValue == null) ? 0 : thirdValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawData other = (RawData) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (firstValue == null) {
			if (other.firstValue != null)
				return false;
		} else if (!firstValue.equals(other.firstValue))
			return false;
		if (halfKey == null) {
			if (other.halfKey != null)
				return false;
		} else if (!halfKey.equals(other.halfKey))
			return false;
		if (secondValue == null) {
			if (other.secondValue != null)
				return false;
		} else if (!secondValue.equals(other.secondValue))
			return false;
		if (thirdValue == null) {
			if (other.thirdValue != null)
				return false;
		} else if (!thirdValue.equals(other.thirdValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RawData [halfKey=" + halfKey + ", firstValue=" + firstValue + ", secondValue=" + secondValue
				+ ", thirdValue=" + thirdValue + ", fileName=" + fileName + "]";
	}


}