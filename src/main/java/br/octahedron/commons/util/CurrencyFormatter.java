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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Vítor Avelino
 * 
 */
public class CurrencyFormatter {

	private final NumberFormat numberFormatter;
	private final DecimalFormatSymbols symbols;

	public CurrencyFormatter(Locale locale) {
		this.symbols = new DecimalFormatSymbols(locale);
		this.symbols.setDecimalSeparator(',');
		this.symbols.setGroupingSeparator('.');
		this.numberFormatter = new DecimalFormat("#,##0.00", symbols);
	}

	public CurrencyFormatter(String locale) {
		this(parseLocale(locale));
	}
	
	// needed cause overloading constructor call must be the first
	private static Locale parseLocale(String locale) {
		String[] localeSplitted = locale.split("-"); 
		return new Locale(localeSplitted[0], localeSplitted[1]);
	}
	

	public String format(BigDecimal value) {
		return this.numberFormatter.format(value);
	}
	
	public String format(long value) {
		return this.numberFormatter.format(value);
	}
	
	public String format(Object value) {
		return this.numberFormatter.format(value);
	}

}
