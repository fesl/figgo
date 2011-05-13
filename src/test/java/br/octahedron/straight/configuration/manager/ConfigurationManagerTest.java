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
package br.octahedron.straight.configuration.manager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.configuration.Module;
import br.octahedron.straight.configuration.ModuleConfigurationInfoService;
import br.octahedron.straight.configuration.Module.TestBuilder;
import br.octahedron.straight.configuration.data.DomainConfiguration;
import br.octahedron.straight.configuration.data.DomainConfigurationDAO;
import br.octahedron.straight.configuration.data.ModuleConfiguration;
import br.octahedron.straight.configuration.data.ModuleConfigurationDAO;

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
	public void createDomainConfigurationTest() {
		// mock setup
		expect(this.domainDAO.count()).andReturn(0);
		expect(this.domainDAO.count()).andReturn(0);
		this.domainDAO.save(this.domain);
		expect(this.domainDAO.count()).andReturn(1);
		expect(this.domainDAO.getAll()).andReturn(this.result);
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		assertFalse(this.configurationManager.existsDomainConfiguration());
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertNull(domainConfig);
		this.configurationManager.createDomainConfiguration("test");
		domainConfig = this.configurationManager.getDomainConfiguration();
		assertNotNull(domainConfig);
		assertEquals("test", domainConfig.getDomainName());
		assertEquals(0, domainConfig.getModulesEnabled().size());
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void enableModuleTest() {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		this.moduleDAO.save(new TestBuilder().createModuleConfiguration());
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(0, domainConfig.getModulesEnabled().size());
		this.configurationManager.enableModules(Module.TEST);
		domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(1, domainConfig.getModulesEnabled().size());
		assertTrue(domainConfig.isModuleEnabled(Module.TEST.name()));
		// enabling already enabled module. nothing happens
		this.configurationManager.enableModules(Module.TEST);
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void disableModuleTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		DomainConfiguration domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(1, domainConfig.getModulesEnabled().size());
		assertTrue(domainConfig.isModuleEnabled(Module.TEST.name()));
		this.configurationManager.disableModules(Module.TEST);
		domainConfig = this.configurationManager.getDomainConfiguration();
		assertEquals(0, domainConfig.getModulesEnabled().size());
		assertFalse(domainConfig.isModuleEnabled(Module.TEST.name()));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void getModuleTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		Collection<DomainConfiguration> result = new LinkedList<DomainConfiguration>();
		result.add(this.domain);
		expect(this.domainDAO.getAll()).andReturn(result).anyTimes();
		expect(this.moduleDAO.get("TEST")).andReturn(new TestBuilder().createModuleConfiguration());
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		ModuleConfigurationInfoService moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertNotNull(moduleInfo);
		this.configurationManager.disableModules(Module.TEST);
		moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertNull(moduleInfo);
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void setPropertyTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		ModuleConfiguration moduleConf = new TestBuilder().createModuleConfiguration();
		expect(this.moduleDAO.get("TEST")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		ModuleConfigurationInfoService moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("some.value", moduleInfo.getPropertyValue("some.property"));
		this.configurationManager.setModuleProperty(Module.TEST, "some.property", "other.value");
		moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("other.value", moduleInfo.getPropertyValue("some.property"));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPropertyInvalidKeyTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		ModuleConfiguration moduleConf = new TestBuilder().createModuleConfiguration();
		expect(this.moduleDAO.get("TEST")).andReturn(moduleConf).anyTimes();

		try {
			// enable mock
			replay(this.domainDAO, this.moduleDAO);
			// test
			this.configurationManager.setModuleProperty(Module.TEST, "invalid.key", "value");
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test
	public void setPropertyValidationTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		ModuleConfiguration moduleConf = new TestBuilder().createModuleConfiguration();
		expect(this.moduleDAO.get("TEST")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		ModuleConfigurationInfoService moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("0", moduleInfo.getPropertyValue("int"));
		this.configurationManager.setModuleProperty(Module.TEST, "int", "10");
		moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("10", moduleInfo.getPropertyValue("int"));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPropertyValidationIllegalTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		ModuleConfiguration moduleConf = new TestBuilder().createModuleConfiguration();
		expect(this.moduleDAO.get("TEST")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		try {
			// test
			ModuleConfigurationInfoService moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
			assertEquals("0", moduleInfo.getPropertyValue("int"));
			this.configurationManager.setModuleProperty(Module.TEST, "int", "a");
		} finally {
			// check mocks
			verify(this.domainDAO, this.moduleDAO);
		}
	}

	@Test
	public void restorePropertiesTest() {
		// mock setup
		this.domain.addModule("TEST");
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		ModuleConfiguration moduleConf = new TestBuilder().createModuleConfiguration();
		expect(this.moduleDAO.get("TEST")).andReturn(moduleConf).anyTimes();

		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		this.configurationManager.setModuleProperty(Module.TEST, "int", "10");
		ModuleConfigurationInfoService moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("10", moduleInfo.getPropertyValue("int"));
		this.configurationManager.restoreModuleProperties(Module.TEST);
		moduleInfo = this.configurationManager.getModuleConfigurationInfoService(Module.TEST);
		assertEquals("0", moduleInfo.getPropertyValue("int"));
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}

	@Test
	public void restorePropertiesDisabledModuleTest() {
		// mock setup
		expect(this.domainDAO.count()).andReturn(1).anyTimes();
		expect(this.domainDAO.getAll()).andReturn(this.result).anyTimes();
		// enable mock
		replay(this.domainDAO, this.moduleDAO);
		// test
		this.configurationManager.restoreModuleProperties(Module.TEST);
		// check mocks
		verify(this.domainDAO, this.moduleDAO);
	}
}