package br.octahedron.straight.view.plugins;

import groovy.lang.Singleton;
import br.octahedron.straight.modules.users.UsersExternalFacade;

/**
 * @author Vítor Avelino
 */
@Singleton
public class FacadePluginManager {

	UsersExternalFacade usersManager;
	
	FacadePluginManager() {
		this.usersManager = new UsersExternalFacade();
	}

}