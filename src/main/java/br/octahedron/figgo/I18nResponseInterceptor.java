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

import br.octahedron.commons.util.CurrencyFormatter;
import br.octahedron.cotopaxi.CotopaxiProperty;
import br.octahedron.cotopaxi.interceptor.TemplateInterceptor;
import br.octahedron.cotopaxi.view.response.RenderableResponse;

/**
 * A {@link ResponseInterceptor} that adds the properties related to i18n
 * 
 * @author Vítor Avelino
 */
public class I18nResponseInterceptor extends TemplateInterceptor {
	
	private CurrencyFormatter currencyFormatter;
	
	public I18nResponseInterceptor() {
		super();
		this.currencyFormatter = new CurrencyFormatter(CotopaxiProperty.getProperty("LOCALE"));
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.TemplateInterceptor#preRender(br.octahedron.cotopaxi.view.response.RenderableResponse)
	 */
	@Override
	public void preRender(RenderableResponse response) {
		response.addOutput("currencyFormatter", currencyFormatter);
		// response.addOutput("dictionary", dictionary);
	}

}
