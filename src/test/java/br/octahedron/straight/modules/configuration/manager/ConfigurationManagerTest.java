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
package br.octahedron.straight.modules.configuration.manager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.modules.DataAlreadyExistsException;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.Modules;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationDAO;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfigurationView;
import br.octahedron.straight.modules.configuration.data.ModuleConfigurationDAO;

/**
 * @author Danilo Queiroz
 */
public class ConfigurationManagerTest {

	private ConfigurationManager configurationManager;
	private DomainConfigurationDAO domainDAO;
	private ModuleConfigurationDAO moduleDAO;
	private DomainConfiguration domain;
	private Collection<DomainConfiguration> result;

	@Before
	public void setUp() {
		this.configurationManager = new ConfigurationManager();
		this.domain = new DomainConfiguration("test");
		this.domainDAO = createMock(DomainConfigurationDAO.class);
		this.moduleDAO = createMock(ModuleConfigurationDAO.class);
		this.configurationManager.setDomainConfigurationDAO(this.domainDAO);
		this.configurationManager.setModuleConfigurationDAO(this.moduleDAO);
		this.result = new LinkedList<DomainConfiguration>();
		this.result.add(this.domain);
	}

	@Test
	public void getDomainConfigurationOkTest() throws DataAlreadyExistsException {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).times(2);
		expect(this.domainDAO.getAll()).andReturn(this.result);
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		assertTrue(this.configurationManager.existsDomainConfiguration());
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertNotNull(domainConfig);
	}

	@Test(expected = DataDoesNotExistsException.class)
	public void getDomainConfigurationInexistentTest() throws DataAlreadyExistsException {
		// mock setup
		expect(this.domainDAO.count()).andReturn(0);
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			this.configurationManager.getDomainConfiguration();
		} finally {
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test(expected = DataAlreadyExistsException.class)
	public void createDomainConfigurationTest() throws DataAlreadyExistsException {
		// mock setup
		expect(this.domainDAO.count()).andReturn(0).times(2);
		this.domainDAO.save(this.domain);
		expect(this.domainDAO.count()).andReturn(1);
		expect(this.domainDAO.getAll()).andReturn(this.result);
		expect(this.domainDAO.count()).andReturn(1);
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		try {
			assertFalse(this.configurationManager.existsDomainConfiguration());
			this.configurationManager.createDomainConfiguration("test");
			DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
			assertNotNull(domainConfig);
			assertEquals("test", domainConfig.getDomainName());
			assertEquals(0, domainConfig.getModulesEnabled().size());
			this.configurationManager.createDomainConfiguration("test");
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test(expected = DataAlreadyExistsException.class)
	public void createExistentDomainConfigurationTest() throws DataAlreadyExistsException {
		try {
			// mock setup
			expect(this.domainDAO.count()).andReturn(1);
			// enable mock
			replay(this.domainDAO, this.moduleDAO);
			// test
			this.configurationManager.createDomainConfiguration("test");
			// check mocks
		} finally {
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test
	public void enableModuleTest() {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		expect(this.moduleDAO.exists(Modules.BANK.name())).andReturn(false);
		this.moduleDAO.save(Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration());
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(0, domainConfig.getModulesEnabled().size());
		this.configurationManager.enableModule(Modules.BANK);
		domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(1, domainConfig.getModulesEnabled().size());
		assertTrue(domainConfig.isModuleEnabled(Modules.BANK.name()));
		// enabling already enabled module. nothing happens
		this.configurationManager.enableModule(Modules.BANK);
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void disableModuleTest() {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(1, domainConfig.getModulesEnabled().size());
		assertTrue(domainConfig.isModuleEnabled(Modules.BANK.name()));
		this.configurationManager.disableModule(Modules.BANK);
		domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(0, domainConfig.getModulesEnabled().size());
		assertFalse(domainConfig.isModuleEnabled(Modules.BANK.name()));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}
	
	@Test
	public void getModuleTest() {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		Collection<DomainConfiguration> result = new LinkedList<DomainConfiguration>();
		result.add(this.domain);
		expect(this.domainDAO.getAll()).andReturn(result).anyTimes();
		expect(this.moduleDAO.get("BANK")).andReturn(Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration());
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			DomainSpecificModuleConfigurationView moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
			assertNotNull(moduleInfo);
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test(expected = DataDoesNotExistsException.class)
	public void getNonConfigurableModuleTest() {
		// mock setup
		this.domain.enableModule("AUTHORIZATION");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		Collection<DomainConfiguration> result = new LinkedList<DomainConfiguration>();
		result.add(this.domain);
		expect(this.domainDAO.getAll()).andReturn(result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			this.configurationManager.getModuleConfiguration(Modules.AUTHORIZATION);			
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}
	
	@Test(expected = DataDoesNotExistsException.class)
	public void getDisabledModuleTest() {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		Collection<DomainConfiguration> result = new LinkedList<DomainConfiguration>();
		result.add(this.domain);
		expect(this.domainDAO.getAll()).andReturn(result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			this.configurationManager.getModuleConfiguration(Modules.AUTHORIZATION);		
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test
	public void setPropertyTest() {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		DomainSpecificModuleConfiguration moduleConf = Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration();
		expect(this.moduleDAO.get("BANK")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		DomainSpecificModuleConfigurationView moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
		assertEquals("Banco", moduleInfo.getPropertyValue("name"));
		this.configurationManager.setModuleProperty(Modules.BANK, "name", "Octa Banco");
		moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
		assertEquals("Octa Banco", moduleInfo.getPropertyValue("name"));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPropertyInvalidKeyTest() {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		DomainSpecificModuleConfiguration moduleConf = Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration();
		expect(this.moduleDAO.get("BANK")).andReturn(moduleConf).anyTimes();

		try {
			// enable mock
			replay(this.domainDAO, this.moduleDAO);
			// test
			this.configurationManager.setModuleProperty(Modules.BANK, "invalid.key", "value");
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}


	@Test(expected = IllegalArgumentException.class)
	public void setPropertyValidationIllegalTest() {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		DomainSpecificModuleConfiguration moduleConf = Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration();
		expect(this.moduleDAO.get("BANK")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			DomainSpecificModuleConfigurationView moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
			assertEquals("Banco", moduleInfo.getPropertyValue("name"));
			this.configurationManager.setModuleProperty(Modules.BANK, "name", "b");
			fail();
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test
	public void restorePropertiesTest() throws DataDoesNotExistsException {
		// mock setup
		this.domain.enableModule("BANK");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		DomainSpecificModuleConfiguration moduleConf = Modules.getModuleSpec(Modules.BANK).getDomainSpecificModuleConfiguration();
		expect(this.moduleDAO.get("BANK")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		this.configurationManager.setModuleProperty(Modules.BANK, "name", "Octa Banco");
		DomainSpecificModuleConfigurationView moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
		assertEquals("Octa Banco", moduleInfo.getPropertyValue("name"));
		this.configurationManager.restoreModuleProperties(Modules.BANK);
		moduleInfo = this.configurationManager.getModuleConfiguration(Modules.BANK);
		assertEquals("Banco", moduleInfo.getPropertyValue("name"));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test(expected = DataDoesNotExistsException.class)
	public void restorePropertiesDisabledModuleTest() throws DataDoesNotExistsException {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			this.configurationManager.restoreModuleProperties(Modules.BANK);
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test(expected = DataDoesNotExistsException.class)
	public void restorePropertiesExistentDomainTest() throws DataDoesNotExistsException {
		// mock setup
		expect(this.domainDAO.count()).andReturn(0);
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			this.configurationManager.restoreModuleProperties(Modules.BANK);
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}
}
