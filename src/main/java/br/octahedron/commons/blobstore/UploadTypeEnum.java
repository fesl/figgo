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

/**
 * @author vitoravelino
 *
 */
public enum UploadTypeEnum {
	USER("/dashboard", "/user/upload");

	private String successUrl;
	
	private String failUrl;
	
	private UploadTypeEnum(String successUrl, String failUrl) {
		this.successUrl = successUrl;
		this.failUrl = failUrl;
	}

	public String getFailUrl() {
		return failUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}
	
}
