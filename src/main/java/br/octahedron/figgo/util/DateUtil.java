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
package br.octahedron.figgo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitoravelino
 * 
 */
public class DateUtil {

	private static Map<String, SimpleDateFormat> formatters = new HashMap<String, SimpleDateFormat>();
	public static String LONG = "dd/MM/yy HH:mm:ss";
	public static String SHORT = "dd/MM/yy";

	static {
		formatters.put(LONG, new SimpleDateFormat(LONG));
		formatters.put(SHORT, new SimpleDateFormat(SHORT));
	}

	public static String format(Date date) {
		return format(date, LONG);
	}

	public static String format(Date date, String pattern) {
		DateFormat formatter = formatters.get(pattern);
		if (formatter == null) {
			formatter = formatters.put(pattern, new SimpleDateFormat(pattern));
		}
		return formatter.format(date);
	}

	public static Date parse(String date) {
		return parse(date, LONG);
	}

	public static Date parse(String date, String pattern) {
		try {
			DateFormat formatter = formatters.get(pattern);
			if (formatter == null) {
				formatter = formatters.put(pattern, new SimpleDateFormat(pattern));
			}
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a {@link Date} with the first day of the current month.
	 */
	public static Date getFirstDateOfCurrentMonth() {
		return parse("01/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR),
				br.octahedron.figgo.util.DateUtil.SHORT);
	}
}
