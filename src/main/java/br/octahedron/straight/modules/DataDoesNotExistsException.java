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
package br.octahedron.straight.modules;

/**
 * Indicates that a data doesn't exists
 * 
 * @author Danilo Queiroz
 */
public class DataDoesNotExistsException extends RuntimeException {

	private static final long serialVersionUID = -3152533253588504515L;

	public DataDoesNotExistsException() { }

	public DataDoesNotExistsException(String message) {
		super(message);
	}

	public DataDoesNotExistsException(Throwable cause) {
		super(cause);
	}

	public DataDoesNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
