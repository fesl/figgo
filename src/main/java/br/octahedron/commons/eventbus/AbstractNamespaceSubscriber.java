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
package br.octahedron.commons.eventbus;

import static br.octahedron.commons.database.NamespaceCommons.backToPreviousNamespace;
import static br.octahedron.commons.database.NamespaceCommons.changeToNamespace;

/**
 * A subscriber that deals with different namespaces, when receiving NamespaceEvents.
 * 
 * @author Danilo Penna Queiroz
 */
public abstract class AbstractNamespaceSubscriber implements Subscriber {
	
	private static final long serialVersionUID = -1528408209684378010L;
	private Class<? extends NamespaceEvent>[] events;

	public AbstractNamespaceSubscriber(Class<? extends NamespaceEvent> ... events) {
		this.events = events;
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.commons.eventbus.Subscriber#init()
	 */
	@Override
	public void init() {
		EventBus.subscribe(this, this.events);
	}

	/* (non-Javadoc)
	 * @see br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event)
	 */
	@Override
	public final void eventPublished(Event event) {
		try {
			if (event instanceof NamespaceEvent) {
				changeToNamespace(((NamespaceEvent)event).getNamespace());
			}
			this.processEvent(event);
		} finally {
			backToPreviousNamespace();
		}
	}

	/**
	 * @see Subscriber#eventPublished(Event)
	 */
	protected abstract void processEvent(Event event);


}
