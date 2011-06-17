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

import br.octahedron.commons.eventbus.NamespaceEvent;

/**
 * @author vitoravelino
 *
 */
public class DomainUploadEvent extends NamespaceEvent {

	private static final long serialVersionUID = 7549574720736587162L;

	private String blobKey;
	
	public DomainUploadEvent(String domainName, String blobKey) {
		super(domainName);
		this.blobKey = blobKey;
	}

	public String getBlobKey() {
		return blobKey;
	}

}