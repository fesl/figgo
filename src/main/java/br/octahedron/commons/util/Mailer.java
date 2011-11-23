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
package br.octahedron.commons.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.octahedron.cotopaxi.CotopaxiProperty;
import br.octahedron.util.Log;

/**
 * @author vitoravelino
 *
 */
public class Mailer {

	private static final Log log = new Log(Mailer.class);
	
	public static void send(String name, String from, String subject, String message) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from, name));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(CotopaxiProperty.getProperty("MAIL_TO"), "Figgo"));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		} catch (Exception e) {
			log.warning("Cannot send mail from %s (%s) with '%s' as subject and '%s' as the message", name, from, subject, message);
		}
	}

}
