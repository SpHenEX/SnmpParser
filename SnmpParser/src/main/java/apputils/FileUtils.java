package apputils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Copia los archivos de config a la caperta actual, de modo que en la apliacion
 * web siempre exista el archivo de config cuando se realiza la consulta
 * 
 * @author EOY4268A
 * @since 2020-11-11
 *
 */
public class FileUtils {

	FileUtils() {
		// default constructor
	}

	/**
	 * @param baseName
	 */
	public static void syncFiles(String baseName) {
		System.out.println("copiando baseName: " + baseName);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DATE, -1);

		String currDate = dateFormat.format(cal1.getTime());
		String prevDate = dateFormat.format(cal2.getTime());

		try {
			File prevFilePath = new File(baseName.replace(currDate, prevDate));
			File currFilePath = new File(baseName);

			if (!currFilePath.exists()) {
				try {
					currFilePath.createNewFile();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			try {
				copyFile(prevFilePath, currFilePath);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void copyFile(File srcFile, File destFile) throws IOException {

		InputStream oInStream = new FileInputStream(srcFile);
		OutputStream oOutStream = new FileOutputStream(destFile);

		// Transfer bytes from in to out
		byte[] oBytes = new byte[1024];
		int nLength;

		BufferedInputStream oBuffInputStream = new BufferedInputStream(oInStream);
		while ((nLength = oBuffInputStream.read(oBytes)) > 0) {
			oOutStream.write(oBytes, 0, nLength);
		}
		oInStream.close();
		oOutStream.close();
	}

	public static void writeFile(String file, String data, boolean append) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, append)));
			out.print(data + "\r");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * retorna la lista de archivos en un directorio, sin contar subdirectorios
	 * 
	 * @param path ruta del directorio
	 * @return List<String> lista de nombres de directorios encontrados
	 */
	public static List<String> getFiles(String path) {
		List<String> files = null;
		String[] directories = new File(path).list();
		if (directories != null) {
			files = new ArrayList<>();
			for (int i = 0; i < directories.length; i++) {
				if (!new File(path + "/" + directories[i]).isDirectory()) {
					files.add(directories[i]);
				}
			}
			return files;
		}
		System.out.println("No hay archivos en la ruta: " + path);
		return null;
	}

	/**
	 * retorna la lista de sub directorios de un directorio, sin contar archivos
	 * 
	 * @param path ruta del directorio
	 * @return List<String> lista de nombres de directorios encontrados
	 */
	public static List<String> getSubDirectories(String path) {
		List<String> dirs = null;
		String[] directories = new File(path).list();
		if (directories != null) {
			dirs = new ArrayList<>();
			for (int i = 0; i < directories.length; i++) {
				if (new File(path + "/" + directories[i]).isDirectory()) {
					dirs.add(directories[i]);
				}
			}
			return dirs;
		}
		System.out.println("No hay directorios en la ruta: " + path);
		return null;
	}

}