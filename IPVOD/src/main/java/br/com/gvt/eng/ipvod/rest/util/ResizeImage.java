package br.com.gvt.eng.ipvod.rest.util;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class ResizeImage {
	public static void main(String[] args) throws IOException {
		ResizeImage resizeImage = new ResizeImage();
//		resizeImage.resize("C:\\Users\\GVT\\Documents\\Pics\\1.jpg");
		resizeImage.resize("C:\\img\\14822.jpg");
	}
	
	public static int DEFAULT_WIDTH = 140;
	public static int DEFAULT_HEIGH = 205;
	
	public void resize(String filePath) throws IOException {
		BufferedImage image = ImageIO.read(new File(filePath));
		BufferedImage scaled = getScaledInstance(image, DEFAULT_WIDTH, DEFAULT_HEIGH, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		writeJPG(scaled, new FileOutputStream(filePath), 0.85f);
	}
	
	public void resize(File file) throws IOException {
		if (file.getPath().split("\\.").length <= 1 || !file.getPath().split("\\.")[1].equals("jpg")) {
			System.out.println("Formato inválido: " + file.getPath());
			return;
		}
		BufferedImage image = ImageIO.read(new File(file.getPath()));
		if (image == null) {
			System.out.println("Erro Image IO:" + file.getPath());
			return;
		}
		BufferedImage scaled = getScaledInstance(image, DEFAULT_WIDTH, DEFAULT_HEIGH, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		writeJPG(scaled, new FileOutputStream(file.getPath()), 0.85f);
	}

	public BufferedImage getScaledInstance(BufferedImage img,
			int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			w = img.getWidth();
			h = img.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}

		//diminuir a imagem diversas vezes para não perder qualidade
		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			ret.flush();
			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	public void writeJPG(BufferedImage bufferedImage, OutputStream outputStream, float quality) throws IOException {
		Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter imageWriter = iterator.next();
		ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
		imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imageWriteParam.setCompressionQuality(quality);
		ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream);
		imageWriter.setOutput(imageOutputStream);
		IIOImage iioimage = new IIOImage(bufferedImage, null, null);
		System.out.println("imageWriter.write");
		imageWriter.write(null, iioimage, imageWriteParam);
		System.out.println("flush and close");
		imageOutputStream.flush();
		imageOutputStream.close();
		outputStream.flush();
		outputStream.close();
	}
}
