/*
 *  Figgo - http://www.figgo.com.br
 *  Copyright (C) 2011  FESL
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
package br.figgo;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

/**
 * Ferramenta para inserção de transações em um determinado banco.
 * 
 * Parâmetros de uso: [dominio] [arquivo|diretório]
 * 
 * dominio - indica o dominio no qual as transações deverão ser inseridas.
 * 
 * arquivo - arquivo com os dados conforme descritos a seguir. O arquivo deve
 * ter como nome origemUserEmail-destUserEmail. Examplo: o arquivo
 * dpenna.queiroz@gmail.com-demo@figgo.com.br contém transações de
 * dpenna.queiroz para a conta do banco 'demo'.
 * 
 * É possível também que este segundo parâmetro seja um diretório com diversos
 * arquivos com a nomeclatura descrita acima.
 * 
 * Em cada arquivo os dados devem estar da seguinte forma:
 * 
 * Uma transação por linha; cada transação na forma
 * "dd/mm/yyyy::comentário::valor". Caso o valor possua casas decimais, deverá
 * ser usado '.' como separador. Exemplo: 100.01
 * 
 * Exemplo: 28/02/2012::Pagamento pela ferramenta de migração::154.90
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class UploadBankData {

	private static DateFormat df;
	private static DatastoreService ds;
	private static int entities = 0;

	private static boolean isDebug() {
		return parseBoolean(getProperty("dbg", "false"));
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Parâmetros: <dominio> <arquivo|diretório> ");
			System.exit(42);
		}
		String domain = args[0];
		String path = args[1];
		RemoteApiInstaller installer = null;
		try {
			if (!isDebug()) {
				installer = installRemoteApi();
				NamespaceManager.set(domain);
			}
			init();
			process(path);
		} catch (Exception e) {
			System.err.printf("Ocorreu um erro. \nEntrada: %s - Error: %s\n", Arrays.toString(args), e.getMessage());
			e.printStackTrace();
		} finally {
			if (installer != null)
				installer.uninstall();
		}
		System.out.printf("%d transações adicionadas ao banco do coletivo %s\n", entities, domain);
	}

	private static void init() {
		df = new SimpleDateFormat("dd/MM/yyyy");
		df.setTimeZone(TimeZone.getTimeZone("America/Recife"));

		ds = DatastoreServiceFactory.getDatastoreService();
	}

	private static RemoteApiInstaller installRemoteApi() throws IOException {
		String username = System.console().readLine("Admin username: ");
		String password = new String(System.console().readPassword("Admin password: "));

		RemoteApiOptions options = new RemoteApiOptions().server("figgo-octa.appspot.com", 443).credentials(username, password);
		RemoteApiInstaller installer = new RemoteApiInstaller();
		installer.install(options);
		return installer;
	}

	private static void process(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			for (File fc : f.listFiles()) {
				try {
					uploadFile(fc);
				} catch (Exception e) {
					System.err.printf("Erro processando arquivo %s : %s\n", fc.getName(), e.getMessage());
				}
			}
		} else {
			uploadFile(f);
		}
	}

	private static void uploadFile(File file) {
		try {
			String[] fileName = file.getName().split("-");
			String orig = fileName[0];
			String dest = fileName[1];

			int line = 0;
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				line++;
				String read = scan.nextLine();
				try {
					if (!read.startsWith("#")) {
						insertTransaction(read, orig, dest);
					}
				} catch (Exception e) {
					System.err.printf("Erro lendo linha %d do arquivo %s: %s\n", line, file.getName(), e.getMessage());
					System.err.printf(" Origem %s ; Destino: %s ; Original: %s\n", orig, dest, read);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.err.printf("Erro processando arquivo %s : %s\n", file.getName(), e.getMessage());
		}
	}

	private static void insertTransaction(String read, String orig, String dest) throws ParseException {
		Scanner scan = new Scanner(read);
		scan.useDelimiter("::");
		String field = scan.next().trim();
		long time = df.parse(field).getTime();
		String comment = scan.next().trim();
		Float value = new Float(scan.next().trim());
		if (!isDebug()) {
			Entity e = new Entity("BankTransaction");
			e.setProperty("accountDest", dest);
			e.setProperty("accountOrig", orig);
			e.setProperty("timestamp", time);
			e.setProperty("comment", comment);
			e.setProperty("amount", value);
			e.setProperty("type", "PAYMENT");
			ds.put(e);
		}
		entities++;
	}
}
