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
package br.octahedron.straight.modules.admin.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.appengine.repackaged.org.apache.commons.codec.binary.Base64;

/**
 * @author Danilo Penna Queiroz
 */
public class Route53Util {
	
	private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String MAC_ALGORITHM = "HmacSHA1";
	
	protected static String sign(String content, String key) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKey skey = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
		Mac mac = Mac.getInstance(MAC_ALGORITHM);
		mac.init(skey);
		mac.update(content.getBytes());
		return new String(Base64.encodeBase64(mac.doFinal()));
	}
	
	 

}
