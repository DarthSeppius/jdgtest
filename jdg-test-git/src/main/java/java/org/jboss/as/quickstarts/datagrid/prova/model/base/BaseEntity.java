package org.jboss.as.quickstarts.datagrid.prova.model.base;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;


@MappedSuperclass
public abstract class BaseEntity<ID> {

 @Column(name = "creation_time", nullable = false)
 private Calendar creationTime;

 @Column(name = "modification_time", nullable = false)
 private Calendar modificationTime;

 @Version
 private long version;

 public abstract ID getId();

 public Calendar getCreationTime() {
     return creationTime;
 }

 public Calendar getModificationTime() {
     return modificationTime;
 }

 public long getVersion() {
     return version;
 }
 
 public void setCreationTime(Calendar creationTime) {
		this.creationTime = creationTime;
	}

	public void setModificationTime(Calendar modificationTime) {
		this.modificationTime = modificationTime;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@PrePersist
 public void prePersist() {
	Calendar now = Calendar.getInstance();
     this.creationTime = now;
     this.modificationTime = now;
 }

 @PreUpdate
 public void preUpdate() {
     this.modificationTime = Calendar.getInstance();
 }
}



