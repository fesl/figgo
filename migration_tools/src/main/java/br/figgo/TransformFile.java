package br.figgo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class TransformFile {

	private static int lineNumber;
	private static List<String> fileContent;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Uso: <dominio> <arquivo|diretório>");
			System.exit(42);
		}

		File f = new File(args[0]);
		if (f.isDirectory()) {
			for (File file : f.listFiles()) {
				try {
					lineNumber = 0;
					fileContent = new ArrayList<String>();
					processFile(file);
					writeFile(file);
				} catch (Exception e) {
					System.err.printf("Erro processando arquivo %s:%d - %s\n", file.getName(), lineNumber, e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private static void processFile(File file) throws FileNotFoundException {
		Scanner line = new Scanner(file);
		while (line.hasNextLine()) {
			lineNumber++;
			processLine(line.nextLine());
		}
	}

	private static void processLine(String line) {
		if (line != null && !line.trim().isEmpty()) {
			Scanner scan = new Scanner(line);
			scan.useDelimiter(",");

			String tk = scan.next();
			if (!tk.isEmpty()) {
				String date = tk;
				String comment = getToken(scan, ',');
				String value = getToken(scan, '.');
				fileContent.add(String.format("%s::%s::%s", date, comment, value));
			}
		}
	}

	private static String getToken(Scanner scan, char c) {
		String buf = scan.next();
		while (buf.startsWith("\"") && !buf.endsWith("\"")) {
			buf += c;
			buf += scan.next();
		}
		return buf.replaceAll("\"", "").trim();
	}

	private static void writeFile(File file) throws IOException {
		file.delete();
		file.createNewFile();
		FileWriter w = new FileWriter(file);
		for (String line : fileContent) {
			w.write(line);
			w.write('\n');
		}
		w.close();
	}
}
