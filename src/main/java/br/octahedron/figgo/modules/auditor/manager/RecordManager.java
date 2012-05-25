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
package br.octahedron.figgo.modules.auditor.manager;

import br.octahedron.figgo.modules.auditor.data.RecordDAO;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class RecordManager {
	
	private RecordDAO recordDAO = new RecordDAO();
	
	/**
	 * @param recordDAO the recordDAO to set
	 */
	public void setRecordDAO(RecordDAO recordDAO) {
		this.recordDAO = recordDAO;
	}

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 */
	public void createRecord(String string, String string2, String string3, String string4) {
		// TODO Auto-generated method stub
		
	}

}
