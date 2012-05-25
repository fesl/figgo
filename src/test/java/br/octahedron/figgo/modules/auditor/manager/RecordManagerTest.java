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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class RecordManagerTest {
	
	private RecordManager manager = new RecordManager();

	@Before
	public void setUp() {
		// TODO
	}

	@Test
	public void createRecordTest() {
		this.manager.createRecord("test@example.com", "TestModule", "test", "it works!");
	}

}
