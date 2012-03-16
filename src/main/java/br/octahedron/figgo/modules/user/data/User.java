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
package br.octahedron.figgo.modules.user.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Erick Moreno
 * 
 */
@PersistenceCapable
public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 3496196911059199158L;

	@Persistent
	@PrimaryKey
	private String userId;
	@Persistent
	private String name;
	@Persistent
	private String nameLowerCase;
	@Persistent
	private String phoneNumber;
	@Persistent
	private String avatarKey;
	@Persistent
	private String shortDescription;
	@Persistent
	private String description;

	public User(String userId, String name, String phoneNumber, String shortDescription) {
		this.userId = userId.toLowerCase();
		this.name = name;
		this.nameLowerCase = this.name.toLowerCase();
		this.phoneNumber = phoneNumber;
		this.shortDescription = shortDescription;
		this.description = shortDescription;
	}

	/**
	 * @return the short description
	 */
	public String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * @param shortDescription
	 *            the short description to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	/**
	 * @return the description
	 */
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

	/**
	 * @return the name
	 */
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

	/**
	 * @return the phone number
	 */
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

	/**
	 * @return the avatar key
	 */
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

	/**
	 * @return the user id
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(User o) {
		int comparation = this.nameLowerCase.compareTo(o.nameLowerCase);
		return (comparation != 0) ? comparation : this.userId.toLowerCase().compareTo(o.userId.toLowerCase());
	}

}
