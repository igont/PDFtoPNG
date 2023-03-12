package org.example;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main
{

	static String src = "C:\\Users\\Igor\\Desktop\\СГП Гонтаренко.pdf";
	static String dest = "C:\\Users\\Igor\\Desktop\\СГП Гонтаренко.png";

	public static void main(String[] args) throws IOException
	{
		Document pdfDocument = new Document(src);

		// Loop through all the pages of PDF file
		for(int pageCount = 1; pageCount <= pdfDocument.getPages().size(); pageCount++)
		{
			// Create stream object to save the output image
			try(java.io.OutputStream imageStream = new java.io.FileOutputStream(src.replace(".pdf","_" + pageCount + ".png")))
			{
				// Create Resolution object
				Resolution resolution = new Resolution(100000);
				// Create PngDevice object with particular resolution
				PngDevice pngDevice = new PngDevice(resolution);
				// Convert a particular page and save the image to stream
				pngDevice.process(pdfDocument.getPages().get_Item(pageCount), imageStream);
			}
		}
	}
}