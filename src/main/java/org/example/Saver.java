package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Saver implements Runnable
{
	private File pdfFile;
	private static int dpi;

	public Saver(File file, int dpi)
	{
		pdfFile = file;
		this.dpi = dpi;
	}

	@Override
	public void run()
	{
		convert(pdfFile);
	}

	private static void convert(File sourceFile)
	{
		try
		{
			String destinationDir = sourceFile.getName().replace(".pdf", "") + "/"; // converted images from pdf document are saved here

			File destinationFile = new File(destinationDir);


			if(sourceFile.exists())
			{
				PDDocument document = PDDocument.load(sourceFile);
				PDFRenderer pdfRenderer = new PDFRenderer(document);

				if(!destinationFile.exists() && document.getNumberOfPages() > 1)
				{
					boolean fileCreated = destinationFile.mkdir();
					if(fileCreated) System.out.println("Folder Created: " + destinationFile.getAbsolutePath());
				} else
				{
					destinationDir = sourceFile.getParentFile().getName();
				}

				String fileName = sourceFile.getName().replace(".pdf", "");
				int pageNumber = 1;

				List<Thread> threads = new ArrayList<>();

				for(int page = 0; page < document.getNumberOfPages(); ++page)
				{
					PNGSaver saver = new PNGSaver(destinationDir, pdfRenderer, fileName, pageNumber, page, dpi);
					Thread thread = new Thread(saver);
					thread.start();
					threads.add(thread);

					pageNumber++;
				}

				while(true)
				{
					boolean allDead = true;

					for(Thread thread : threads)
					{
						if(thread.isAlive()) allDead = false;
					}
					if(allDead) break;
				}

				document.close();
			} else
			{
				System.err.println(sourceFile.getName() + " File not exists");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
