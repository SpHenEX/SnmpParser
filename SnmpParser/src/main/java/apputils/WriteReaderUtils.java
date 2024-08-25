/**
 *
 */
package apputils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

/**
 * @author jose.gomez.j
 */
public class WriteReaderUtils {

	WriteReaderUtils() {
		// default constructor
	}

	public static final java.util.logging.Logger logTerm = java.util.logging.Logger
			.getLogger(WriteReaderUtils.class.getName());

	public static void WriteFile(File outFile, String data) {
		try {
			FileWriter myWriter = new FileWriter(outFile, true);
			myWriter.write(data);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String fileName, String data) throws IOException {
		try (FileWriter fstream = new FileWriter(fileName, true); BufferedWriter bw = new BufferedWriter(fstream);) {
			String line2 = data.replaceAll("(\\[42D.+\\[42D)", "\n");
			bw.write(line2 + "\n");
			bw.flush();
		} catch (Exception e) {
			logTerm.log(Level.WARNING, e.getMessage());
		}
	}

	public static void writeBufferedFile(String fileName, String data, boolean append) throws IOException {
		FileWriter fw = new FileWriter(new File(fileName), append);
		try (BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(data);
			bw.flush();
		} catch (Exception e) {
			logTerm.log(Level.WARNING, e.getMessage());
		}
	}

	/**
	 * Read full lines from files
	 * 
	 * @param file path
	 * 
	 */
	public static synchronized String readFileBuffer(String file) {
		String fileEscape = "";
		if (file != null) {
			fileEscape = file.replace("\\", "/");
		}

		File f = new File(fileEscape);
		byte[] buffer = new byte[(int) f.length()];
		try (FileInputStream fis = new FileInputStream(f); DataInputStream is = new DataInputStream(fis);) {
			is.readFully(buffer);
			return new String(buffer);
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * Read full lines from files
	 *
	 * @param file path
	 * 
	 */
	public static String readFile(String file) {
		try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);) {
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static String readFile2(String file) {
		try {
			file = "file:///" + file.replace("\\", "/");
			InputStream is = new URL(file).openStream();
			String stringData = IOUtils.toString(is, StandardCharsets.UTF_8);
			is.close();
			return stringData;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Leer Arhivos en disponibilidad de Buffer
	 * <p>
	 * Leer con el juego de caracteres adecuado (codificación). Este puede ir null
	 * por defecto 'charsetName'
	 * </p>
	 *      *
	 * <p>
	 * No ignorar el salto de línea al final del archivo.
	 * </p>
	 * <p>
	 * No perder el CPU y la memoria mediante la adición de objetos String en un
	 * bucle (utilizar un StringBuffer o un ArrayList <String> en su lugar).
	 * </p>
	 * <p>
	 * No perder la memoria (por línea-buffering o de doble búfer)
	 * </p>
	 * 
	 * @param fileName
	 * @param charsetName
	 * @return
	 * @throws java.io.IOException
	 */
	public static String readFileAsString(String fileName) throws java.io.IOException {
		String jarPath = new File(WriteReaderUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath())
				.getPath();
		jarPath = jarPath.replace("\\", "/");
		String dirPath = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/";

		try (java.io.InputStream is = new java.io.FileInputStream(dirPath + fileName);) {
			final int bufsize = 4096;
			int available = is.available();
			byte[] data = new byte[available < bufsize ? bufsize : available];
			int used = 0;
			while (true) {
				if (data.length - used < bufsize) {
					byte[] newData = new byte[data.length << 1];
					System.arraycopy(data, 0, newData, 0, used);
					data = newData;
				}
				int got = is.read(data, used, data.length - used);
				if (got <= 0)
					break;
				used += got;
			}
			return new String(data, 0, used);
		}
	}

	public static String readFromInputStream(String file) throws IOException {
		InputStream inputStream = WriteReaderUtils.class.getResourceAsStream(file);
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

}