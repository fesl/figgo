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

import static br.octahedron.cotopaxi.datastore.NamespaceManagerFacade.*;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.figgo.modules.configuration.data.DomainConfiguration;
import br.octahedron.figgo.modules.configuration.data.DomainConfigurationDAO;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Vítor Avelino
 * 
 */
public class DomainConfigurationDAOTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private final DomainConfigurationDAO domainDAO = new DomainConfigurationDAO();

	@Before
	public void setUp() {
		this.helper.setUp();
	}

	public void createDomains() {
		// storing domains configuration
		changeToNamespace("octa");
		this.domainDAO.save(new DomainConfiguration("octa"));
		changeToNamespace("alua");
		this.domainDAO.save(new DomainConfiguration("alua"));
		changeToNamespace("mundo");
		this.domainDAO.save(new DomainConfiguration("mundo"));
	}

	@Test
	public void getDomains() {
		this.createDomains();
		Set<DomainConfiguration> domainsConfiguration = this.domainDAO.getDomainsConfiguration();
		Iterator<DomainConfiguration> iterator = domainsConfiguration.iterator();
		assertEquals(3, domainsConfiguration.size());
		assertEquals("alua", iterator.next().getDomainName());
		assertEquals("mundo", iterator.next().getDomainName());
		assertEquals("octa", iterator.next().getDomainName());

		// this time it will get from memcache
		domainsConfiguration = this.domainDAO.getDomainsConfiguration();
		iterator = domainsConfiguration.iterator();
		assertEquals(3, domainsConfiguration.size());
		assertEquals("alua", iterator.next().getDomainName());
		assertEquals("mundo", iterator.next().getDomainName());
		assertEquals("octa", iterator.next().getDomainName());
	}
}
