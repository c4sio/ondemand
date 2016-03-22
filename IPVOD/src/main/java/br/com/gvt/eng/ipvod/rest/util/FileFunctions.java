package br.com.gvt.eng.ipvod.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author GVT
 * 
 */
public class FileFunctions {

	/**
	 * @param file
	 * @param packageId
	 * @throws IOException
	 */
	public void copyJPGFilesToCluster(File file)
			throws IOException {
		Session session = null;
		ChannelSftp sftpChannel = null;
		JSch jsch = null;
		FileInputStream fi = null;
		try {
			if (!file.exists())
				throw new RuntimeException("Error. Local file not found");

			// Lista os hosts para que o arquivo seja enviado
			for (String host : IpvodFileUtil.getScpHost()) {

				System.out.println("Copiando arquivo de imagem " + file.getName()
						+ " - path " + IpvodFileUtil.getPathClusterImg() + " - host "
						+ host);

				jsch = new JSch();
				session = jsch.getSession(IpvodFileUtil.getScpUser().toString(),
						host.toString(), IpvodFileUtil.getScpPort());
				session.setPassword(IpvodFileUtil.getScpPassword());
				session.setConfig("StrictHostKeyChecking", "no");
				session.connect();

				Channel channel = session.openChannel("sftp");
				channel.connect();
				sftpChannel = (ChannelSftp) channel;
				sftpChannel.cd(IpvodFileUtil.getPathClusterImg());

				fi = new FileInputStream(file);
				sftpChannel.put(fi, file.getName());

				if (sftpChannel != null) {
					sftpChannel.disconnect();
				}
				if (session != null) {
					session.disconnect();
				}
				if (fi != null) {
					fi.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sftpChannel != null) {
				sftpChannel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
			if (fi != null) {
				fi.close();
			}
		}
	}

	/**
	 * @param file
	 * @param error
	 */
	public void createFileError(File file, List<String> error) {
		File dir = new File(file.getParent());
		File arq = new File(dir, file.getParentFile().getName() + ".error");
		try {
			arq.createNewFile();
			FileWriter fileWriter = new FileWriter(arq, false);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			for (String string : error) {
				printWriter.println(string.toString());
			}

			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
