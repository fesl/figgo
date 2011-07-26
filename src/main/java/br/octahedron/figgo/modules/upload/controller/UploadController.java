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
package br.octahedron.figgo.modules.upload.controller;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.blobstore.BlobstoreController;
import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.cotopaxi.inject.Inject;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * @author Vítor Avelino
 *
 */
@AuthenticationRequired
public class UploadController extends BlobstoreController {

	@Inject
	private EventBus eventBus;
	
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public void getServeBlob() {
		serve(in("key"));
	}

	public void getUserUpload() {
		out("uploadUrl", uploadUrl("/user/upload"));
		success("user/upload.vm");
	}

	public void postUserUpload() {
		BlobKey blobKey = blobKey("file");
		if (blobKey != null) {
			this.eventBus.publish(new UserUploadEvent(currentUser(), blobKey.getKeyString()));
			redirect("/");
		} else {
			redirect("/user/upload");
		}
	}
	
	public void getDomainUpload() {
		out("uploadUrl", uploadUrl("/domain/upload"));
		success("domain/upload.vm");
	}

	public void postDomainUpload() {
		BlobKey blobKey = blobKey("file");
		if (blobKey != null) {
			this.eventBus.publish(new DomainUploadEvent(subDomain(), blobKey.getKeyString()));
			redirect("/");
		} else {
			redirect("/user/upload");
		}
	}
	
}
