/*
 *  Figgo - http://projeto.figgo.com.br
 *  Copyright (C) 2011 Octahedron - Coletivo Mundo
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
package br.octahedron.figgo.modules.auditor.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import br.octahedron.figgo.util.DateUtil;

/**
 * This class represents an Auditor Record.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@PersistenceCapable
public class Record {

	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long id;
	@Persistent
	private long timestamp;
	@Persistent
	private String userId;
	@Persistent
	private String origModule;
	@Persistent
	private String operation; // REVIEW Should it use an enum here?
	@Persistent
	private String comment;

	/**
	 * Creates a new Record.
	 * 
	 * @param userId
	 *            The id of the user performing the operation
	 * @param origModule
	 *            The origin module that generate this record.
	 * @param operation
	 *            The operation performed.
	 * @param comment
	 *            Any specific comment about the operation performed.
	 */
	public Record(String userId, String origModule, String operation, String comment) {
		this.timestamp = DateUtil.now();
		this.userId = userId;
		this.origModule = origModule;
		this.operation = operation;
		this.comment = comment;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the origModule
	 */
	public String getOrigModule() {
		return origModule;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
}
