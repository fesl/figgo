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
package br.octahedron.figgo.modules.configuration.data;

import java.util.Set;
import java.util.TreeSet;

import br.octahedron.cotopaxi.datastore.GenericDAO;
import br.octahedron.cotopaxi.datastore.NamespaceManager;
import br.octahedron.cotopaxi.inject.Inject;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * @author Danilo Queiroz
 */
public class DomainConfigurationDAO extends GenericDAO<DomainConfiguration> {

	public static final String NAMESPACE_KEY = "namespace_memcache_key";
	private final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
	
	@Inject
	private NamespaceManager namespaceManager;
	
	public DomainConfigurationDAO() {
		super(DomainConfiguration.class);
	}

	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}

	@SuppressWarnings("unchecked")
	public Set<DomainConfiguration> getDomainsConfiguration() {
		Set<DomainConfiguration> domainsConfiguration = (Set<DomainConfiguration>) this.memcacheService.get(NAMESPACE_KEY);
		if (domainsConfiguration == null) {
			domainsConfiguration = this.createDomainsConfiguration();
			this.memcacheService.put(NAMESPACE_KEY, domainsConfiguration);
		}
		return domainsConfiguration;
	}

	/**
	 * Returns a domains configuration list for all existing namespaces
	 */
	private Set<DomainConfiguration> createDomainsConfiguration() {
		Set<DomainConfiguration> domainsConfiguration = new TreeSet<DomainConfiguration>();
		for (String namespace : namespaceManager.getNamespaces()) {
			try {
				namespaceManager.changeToNamespace(namespace);
				if (this.count() != 0) {
					domainsConfiguration.add(this.getAll().iterator().next());
				}
			} finally {
				namespaceManager.changeToPreviousNamespace();
			}
		}
		return domainsConfiguration;
	}
}
