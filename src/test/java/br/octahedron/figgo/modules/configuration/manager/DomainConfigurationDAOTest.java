/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.figgo.modules.configuration.manager;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.datastore.namespace.AppEngineNamespaceManager;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.figgo.modules.configuration.data.DomainConfiguration;
import br.octahedron.figgo.modules.configuration.data.DomainConfigurationDAO;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Vítor Avelino
 * 
 */
public class DomainConfigurationDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final DomainConfigurationDAO domainConfigurationDAO = new DomainConfigurationDAO();
	private final NamespaceManager namespaceManager = new AppEngineNamespaceManager();
	
	@Before
	public void setUp() {
		this.helper.setUp();
		this.domainConfigurationDAO.setNamespaceManager(namespaceManager);
	}

	public void createDomains() {
		// storing domains configuration
		namespaceManager.changeToNamespace("octa");
		this.domainConfigurationDAO.save(new DomainConfiguration("octa"));
		namespaceManager.changeToNamespace("alua");
		this.domainConfigurationDAO.save(new DomainConfiguration("alua"));
		namespaceManager.changeToNamespace("mundo");
		this.domainConfigurationDAO.save(new DomainConfiguration("mundo"));
	}

	@Test
	public void getAllDomains() {
		this.createDomains();
		Set<DomainConfiguration> domainsConfiguration = this.domainConfigurationDAO.getDomainsConfiguration();
		Iterator<DomainConfiguration> iterator = domainsConfiguration.iterator();
		assertEquals(3, domainsConfiguration.size());
		assertEquals("alua", iterator.next().getDomainName());
		assertEquals("mundo", iterator.next().getDomainName());
		assertEquals("octa", iterator.next().getDomainName());

		// this time it will get from memcache
		domainsConfiguration = this.domainConfigurationDAO.getDomainsConfiguration();
		iterator = domainsConfiguration.iterator();
		assertEquals(3, domainsConfiguration.size());
		assertEquals("alua", iterator.next().getDomainName());
		assertEquals("mundo", iterator.next().getDomainName());
		assertEquals("octa", iterator.next().getDomainName());
	}
	
	@Test
	public void getDomainsConfiguration() {
		this.createDomains();
		Collection<DomainConfiguration> result = this.domainConfigurationDAO.getDomainsConfigurations(new String[] {"alua", "mundo"});
		Iterator<DomainConfiguration> iterator = result.iterator();
		assertEquals(2, result.size());
		assertEquals("alua", iterator.next().getDomainName());
		assertEquals("mundo", iterator.next().getDomainName());
		
		result = this.domainConfigurationDAO.getDomainsConfigurations(new String[] {"alua", "asdasdasdo"});
		iterator = result.iterator();
		assertEquals(1, result.size());
		assertEquals("alua", iterator.next().getDomainName());
		
		result = this.domainConfigurationDAO.getDomainsConfigurations(new String[] {"muahah"});
		assertEquals(0, result.size());
	}
}
