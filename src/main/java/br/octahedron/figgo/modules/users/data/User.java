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
package br.octahedron.figgo.modules.users.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 * 
 */
@PersistenceCapable
public class User implements Serializable, UserView {

	private static final long serialVersionUID = 3496196911059199158L;

	@Persistent
	@PrimaryKey
	private String userId;
	@Persistent
	private String name;
	@SuppressWarnings("unused")
	@Persistent
	private String nameLowerCase;
	@Persistent
	private String phoneNumber;
	@Persistent
	private String avatarKey;
	@Persistent
	private String description;

	public User(String userId, String name, String phoneNumber, String description) {
		this.userId = userId.toLowerCase();
		this.name = name;
		this.nameLowerCase = this.name.toLowerCase();
		this.phoneNumber = phoneNumber;
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.users.data.UserView#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.users.data.UserView#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.nameLowerCase = this.name.toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.users.data.UserView#getPhoneNumber()
	 */
	@Override
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.users.data.UserView#getAvatar()
	 */
	@Override
	public String getAvatarKey() {
		return this.avatarKey;
	}

	/**
	 * @param avatarKey
	 *            the avatar to set
	 */
	public void setAvatarKey(String avatarKey) {
		this.avatarKey = avatarKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.users.data.UserView#getUserId()
	 */
	@Override
	public String getUserId() {
		return this.userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + this.name + " " + this.userId + "]";
	}

}
