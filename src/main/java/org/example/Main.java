package org.example;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{

		System.out.println("150 - normal quality");
		System.out.println("300 - cool quality");
		System.out.println("600 - premium quality");
		System.out.println();
		System.out.print("Enter DPI: ");

		Scanner scanner = new Scanner(System.in);
		int dpi = scanner.nextInt();
		System.out.println();

		List<Thread> savers = new ArrayList<>();
		long currentTimeMillis = System.currentTimeMillis();

		File dir = new File(".");
		File[] filesList = dir.listFiles();
		if(filesList != null)
		{
			for(File file : filesList)
			{
				if(file.isFile() && getFileExtension(file.getName()).equalsIgnoreCase("pdf"))
				{
					Saver saver = new Saver(file, dpi);
					Thread thread = new Thread(saver);
					thread.start();

					savers.add(thread);
				}
			}
		}

		while(true)
		{
			boolean allDead = true;

			for(Thread thread : savers)
			{
				if(thread.isAlive()) allDead = false;
			}

			//Thread.sleep(1000);
			if(allDead) break;
		}


		long diff = System.currentTimeMillis() - currentTimeMillis;

		System.out.println();
		System.out.print("Time spent: " + diff / 1000f + " seconds");
		scanner.nextLine();
		scanner.nextLine();
	}

	private static String getFileExtension(String fileName)
	{
		if(fileName == null || fileName.equals("")) return "undefined";
		int dotIndex = fileName.lastIndexOf(".");
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}