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
package br.octahedron.figgo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import br.octahedron.cotopaxi.i18n.I18NTemplateInterceptor;

/**
 * A {@link ResponseInterceptor} that adds the properties related to i18n
 * 
 * @author Vítor Avelino
 */
public class FiggoI18nResponseInterceptor extends I18NTemplateInterceptor {
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.i18n.I18NTemplateInterceptor#numberFormat(java.util.Locale)
	 */
	@Override
	public NumberFormat numberFormat(Locale lc) {
		return createNumberFormat(lc);
	}
	
	private static NumberFormat createNumberFormat(Locale lc) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(lc);
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		return new DecimalFormat("#,##0.00", symbols);
	}
}
