/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.datagrid.prova;

import javax.inject.Inject;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.marshall.core.MarshalledEntry;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;
import org.jboss.as.quickstarts.datagrid.prova.persistence.PersistenceContext;
import org.jboss.as.quickstarts.datagrid.prova.repo.RawDataRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionThreadPolicy;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.persistence.jdbc.configuration.JdbcBinaryStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.infinispan.persistence.spi.CacheLoader;
import org.infinispan.persistence.spi.CacheWriter;
import org.infinispan.persistence.spi.InitializationContext;

/**
 * Creates a DefaultCacheManager which is configured programmatically. Infinispan's libraries need to be bundled with the
 * application.
 * 
 * @author Burr Sutter
 * @author Martin Gencur
 * 
 */
@ApplicationScoped
public class MyCacheManagerProvider{

		private static final long ENTRY_LIFESPAN = 1800 * 1000; // 30 minuti
//    	private static final long ENTRY_LIFESPAN = 10 * 1000; // 30 sec
		private static final int MAX_ENTRIES = (int) (Math.pow(2, 31));
    
    @Inject
    private Logger log;

    private DefaultCacheManager manager;

    public DefaultCacheManager getCacheManager() {
        if (manager == null) {
            log.info("\n\n DefaultCacheManager does not exist - constructing a new one\n\n");

            GlobalConfiguration glob = new GlobalConfigurationBuilder().clusteredDefault() // Builds a default clustered
                                                                                           // configuration
                    .transport().addProperty("configurationFile", "jgroups-udp.xml") // provide a specific JGroups configuration
                    .globalJmxStatistics().allowDuplicateDomains(true).enable() // This method enables the jmx statistics of
                    
                    // the global configuration and allows for duplicate JMX domains
                    
                    .build(); // Builds the GlobalConfiguration object
            Configuration loc = new ConfigurationBuilder().jmxStatistics().enable() // Enable JMX statistics
                    .clustering().cacheMode(CacheMode.DIST_SYNC)
                    // Set Cache mode to DISTRIBUTED with SYNCHRONOUS replication
                    .hash().numOwners(2) // Keeps two copies of each key/value pair
                    .expiration().lifespan(ENTRY_LIFESPAN) // Set expiration - cache entries expire after some time (given by
                    // the lifespan parameter) and are removed from the cache (cluster-wide).
                    .eviction()
                    	.maxEntries(MAX_ENTRIES).strategy(EvictionStrategy.LRU)	//prova persistenza dati
                    	.threadPolicy(EvictionThreadPolicy.DEFAULT)
                   
                    
                    	
//                    	.persistence()
//                    	.addStore(JdbcBinaryStoreConfigurationBuilder.class)
//                    	.fetchPersistentState(false)
//                    	.ignoreModifications(false)
//                    	.purgeOnStartup(false)
//                    	.table()
//                    	.dropOnExit(true)
//                    	.createOnStart(true)
//                    	.tableNamePrefix("ISPN_BUCKET_TABLE")
//                    	.idColumnName("ID_COLUMN").idColumnType("VARCHAR(255)")
//                    	.dataColumnName("DATA_COLUMN").dataColumnType("BINARY")
//                    	.timestampColumnName("TIMESTAMP_COLUMN").timestampColumnType("BIGINT")
//                    	.connectionPool()
//                 //   	.connectionUrl("jdbc:postgresql://localhost:5432/provaDbPoc")
//                    	.connectionUrl("jdbc:h2:mem:infinispan_binary_based;DB_CLOSE_DELAY=-1")
//                    	.username("sa")
//                 //   	.username("postgres")
//                 //    	.password("1234")
//                 //   	.driverClass("org.postgresql.Driver")
//                    	.driverClass("org.h2.Driver")
                    	
                    .build();
            
            manager = new DefaultCacheManager(glob, loc, true);
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }

}
