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

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.commons.eventbus.EventBus;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author vitoravelino
 *
 */
public class BlobServlet extends HttpServlet {

	private static final long serialVersionUID = -315053284649272497L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	private UserService userService = UserServiceFactory.getUserService();

	private static final Logger logger = Logger.getLogger(BlobServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String key = parseKey(req.getRequestURI());
		BlobKey blobKey = new BlobKey(key);
		blobstoreService.serve(blobKey, res);
	}
	

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.info("Uploading avatar to blobstore");
		
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("file");
		UploadTypeEnum uploadType = UploadTypeEnum.valueOf(parseType(req.getRequestURI()));
		
		if (blobKey == null) {
			redirectToFailUrl(res, uploadType);
		} else {
			redirectToSuccessUrl(res, uploadType, blobKey.getKeyString());
		}
	}

	/**
	 * @param requestURI
	 * @return
	 */
	private String parseKey(String requestURI) {
		return requestURI.substring(requestURI.lastIndexOf('/')+1);
	}

	/**
	 * @param requestURI
	 * @return
	 */
	private String parseType(String requestURI) {
		String partialPath = requestURI.substring(0, requestURI.lastIndexOf('/')); // /blob/user
		return partialPath.substring(partialPath.lastIndexOf('/')+1).toUpperCase();
	}

	/**
	 * @param res
	 * @param uploadType
	 * @throws IOException 
	 */
	private void redirectToFailUrl(HttpServletResponse res, UploadTypeEnum uploadType) throws IOException {
		switch (uploadType) {
		case USER:
			res.sendRedirect(uploadType.getFailUrl());
			break;

		default:
			res.sendRedirect("/404");
			break;
		}
	}
	
	/**
	 * @param res
	 * @param uploadType
	 * @throws IOException 
	 */
	private void redirectToSuccessUrl(HttpServletResponse res, UploadTypeEnum uploadType, String blobKey) throws IOException {
		switch (uploadType) {
		case USER:
			EventBus.publish(new UploadEvent(uploadType, userService.getCurrentUser().getEmail(), blobKey));
			res.sendRedirect(uploadType.getSuccessUrl());
			break;

		default:
			res.sendRedirect("/404");
			break;
		}
	}
}

