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
package br.octahedron.figgo.util;

import static br.octahedron.cotopaxi.CotopaxiProperty.getCharset;
import static java.net.URLDecoder.decode;

/**
 * 
 * @author Danilo Queiroz
 */
public class SafeStringConverter extends br.octahedron.cotopaxi.controller.converter.SafeStringConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.converter.SafeStringConverter#convert(java.lang.String)
	 */
	@Override
	public String convert(String input) {
		try {
			return decode(super.convert(input), getCharset().name());
		} catch (Exception e) {
			return null;
		}
	}
}
