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
package br.octahedron.commons.blobstore;

import br.octahedron.commons.eventbus.Event;

/**
 * @author Vítor Avelino
 *
 */
public class UserUploadEvent implements Event {
	
	private static final long serialVersionUID = 6035526466916526937L;

	private String blobKey;

	private String target;
	
	public UserUploadEvent(String target, String blobKey) {
		this.target = target;
		this.blobKey = blobKey;
	}

	public String getBlobKey() {
		return blobKey;
	}

	public String getTarget() {
		return target;
	}
}
