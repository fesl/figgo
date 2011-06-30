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
package br.octahedron.figgo.modules.admin.util;

/**
 * Indicates that an error occurs accessing Route53
 * 
 * @author Danilo Queiroz
 */
public class Route53Exception extends Exception {

	private static final long serialVersionUID = -7889264324186092069L;

	public Route53Exception() {
	}

	public Route53Exception(String message) {
		super(message);
	}

	public Route53Exception(Throwable cause) {
		super(cause);
	}

	public Route53Exception(String message, Throwable cause) {
		super(message, cause);
	}
}
