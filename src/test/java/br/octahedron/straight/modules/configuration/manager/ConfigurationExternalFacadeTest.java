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
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.bank.BankConfigurationBuilder;
import br.octahedron.straight.modules.configuration.ConfigurationExternalFacade;
import br.octahedron.straight.modules.configuration.ModulesInfoService;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationView;

/**
 * Integration tests for Configuration Facades. This tests use mocks at DAO levels to test all the
 * iteration between Facade, {@link ConfigurationManager} and DAOs.
 * 
 * @author Danilo Queiroz
 * @author Erick Moreno
 */
public class ConfigurationExternalFacadeTest {

	private ConfigurationManager confManager;
	private ConfigurationExternalFacade facade;
	private DomainConfiguration domain;

	@Before
	public void setUp() {
		this.domain = new DomainConfiguration("test");
		this.confManager = createMock(ConfigurationManager.class);

		this.facade = new ConfigurationExternalFacade();
		this.facade.setConfigurationManager(this.confManager);
	}

	@Test
	public void createDomainTest() {
		// setup mock
		this.confManager.createDomainConfiguration("test");
		replay(this.confManager);
		// test
		this.facade.createDomainConfiguration("test");
		// verify
		verify(this.confManager);
	}

	@Test
	public void getDomainConfigurationTest() {
		// setup mock
		expect(this.confManager.getDomainConfiguration()).andReturn(this.domain);
		replay(this.confManager);
		// test
		DomainConfigurationView domain = this.facade.getDomainConfiguration();
		assertEquals(new DomainConfiguration("test"), domain);
		// verify
		verify(this.confManager);
	}

	@Test
	public void getModuleInfoServiceTest() {
		// setup mock
		expect(this.confManager.getDomainConfiguration()).andReturn(domain).times(2);
		replay(this.confManager);
		// test
		ModulesInfoService modules = this.facade.getModulesInfoService();
		for (String module : modules.getExistentModules()) {
			assertFalse(modules.isModuleEnabled(module));
		}
		// enable bank module
		this.domain.enableModule(Module.BANK.name());
		modules = this.facade.getModulesInfoService();
		assertTrue(modules.isModuleEnabled(Module.BANK.name()));
		// verify
		verify(this.confManager);
	}

	@Test
	public void changeModuleStateTest() {
		// setup mock
		this.confManager.enableModule(Module.BANK);
		expectLastCall().times(2);
		this.confManager.disableModule(Module.BANK);
		replay(this.confManager);
		// test
		this.facade.changeModuleState(Module.BANK, true);
		this.facade.changeModuleState(Module.BANK, true);
		this.facade.changeModuleState(Module.BANK, false);
		// verify
		verify(this.confManager);
	}

	@Test
	public void getModuleTest() {
		// setup mock
		expect(this.confManager.getModuleConfiguration(Module.BANK)).andReturn(new BankConfigurationBuilder().createModuleConfiguration());
		replay(this.confManager);
		// test
		assertNotNull(this.facade.getModuleConfiguration(Module.BANK));
		// verify
		verify(this.confManager);
	}

	@Test
	public void restoreDefaultsTest() {
		// setup mock
		this.confManager.restoreModuleProperties(Module.BANK);
		replay(this.confManager);
		// test
		this.facade.restoreModuleConfiguration(Module.BANK);
		// verify
		verify(this.confManager);
	}
	
	@Test
	public void updateModulePropertiesTest() {
		// setup mock
		this.confManager.setModuleProperty(Module.BANK, "name", "TestBank");
		this.confManager.setModuleProperty(Module.BANK, "currency", "Money");
		this.confManager.setModuleProperty(Module.BANK, "currencyAbreviation", "M$");
		replay(this.confManager);
		// test
		Map<String, String> values = new HashMap<String, String>();
		values.put("name", "TestBank");
		values.put("currency", "Money");
		values.put("currencyAbreviation", "M$");
		this.facade.updateModuleConfiguration(Module.BANK, values);
		// verify
		verify(this.confManager);
	}
}
