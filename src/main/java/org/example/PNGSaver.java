package org.example;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PNGSaver implements Runnable
{

	String destinationDir;
	PDFRenderer pdfRenderer;
	String fileName;
	int pageNumber;
	int page;
	int dpi;
	int count = 0;

	public PNGSaver(String destinationDir, PDFRenderer pdfRenderer, String fileName, int pageNumber, int page, int dpi)
	{
		this.destinationDir = destinationDir;
		this.pdfRenderer = pdfRenderer;
		this.fileName = fileName;
		this.pageNumber = pageNumber;
		this.page = page;
		this.dpi = dpi;
	}

	@Override
	public void run()
	{
		try
		{
			printPage();
		}
		catch(IOException|InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void printPage() throws IOException, InterruptedException
	{
		try
		{
			if(Objects.equals(destinationDir, ".")) destinationDir = "";
			BufferedImage image = pdfRenderer.renderImageWithDPI(page, dpi, ImageType.RGB);
			File outputFile = new File(destinationDir + fileName + "_" + dpi + "DPI_page" + pageNumber + ".png");
			ImageIO.write(image, "png", outputFile);

			System.out.println("Converted Images are saved at -> " + outputFile.getAbsolutePath());
		}
		catch(IllegalStateException e)
		{
			printPage();
		}
		catch(OutOfMemoryError e)
		{
			count++;
			if(count >= 10) return;
			Thread.sleep(7000);
			printPage();
		}
	}
}
