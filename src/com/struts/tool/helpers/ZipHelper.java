package com.struts.tool.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

	public void zip(File[] files, File outputFile) throws IOException {

		if (files != null && files.length > 0) {
			ZipOutputStream out = new ZipOutputStream(
						new FileOutputStream(outputFile));
			Stack<File> parentDirs = new Stack<File>();
			zipFiles(parentDirs, files, out);
			out.close();
		}
	}

	private void zipFiles(Stack<File> parentDirs, File[] files,
				ZipOutputStream out) throws IOException {
		byte[] buf = new byte[1024];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				//se a entrad é um diretório, empilha o diretório e
				//chama o mesmo método recursivamente
				parentDirs.push(files[i]);
				zipFiles(parentDirs, files[i].listFiles(), out);

				//após processar as entradas do diretório, desempilha
				parentDirs.pop();
			} else {
				FileInputStream in = new FileInputStream(files[i]);

				//itera sobre os itens da pilha para montar o caminho
				//completo do arquivo
				String path = "";
				for(File parentDir : parentDirs) {
					path += parentDir.getName() + "/";
				}

				//grava os dados no arquivo zip
				out.putNextEntry(new ZipEntry(path + files[i].getName()));

				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				out.closeEntry();
				in.close();
			}
		}
	}

	public void unzip(File zipFile, File dir) throws IOException {
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[1024];

		try {
			// cria diretório informado, caso não exista
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!dir.exists() || !dir.isDirectory()) {
				throw new IOException("O diretório " + dir.getName() +
				" não é um diretório válido");
			}

			zip = new ZipFile(zipFile);
			Enumeration e = zip.entries();
			while (e.hasMoreElements()) {
				ZipEntry entrada = (ZipEntry) e.nextElement();
				arquivo = new File(dir, entrada.getName());

				// se for diretório inexistente, cria a estrutura e pula
				// pra próxima entrada
				if (entrada.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
				}

				// se a estrutura de diretórios não existe, cria
				if (!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				try {
					// lê o arquivo do zip e grava em disco
					is = zip.getInputStream(entrada);
					os = new FileOutputStream(arquivo);
					int bytesLidos = 0;
					if (is == null) {
						throw new ZipException("Erro ao ler a entrada do zip: " +
									entrada.getName());
					}
					while ((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
					if (os != null) {
						try {
							os.close();
						} catch (Exception ex) {
						}
					}
				}
			}
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
				}
			}
		}
	}
}