package com.aliasi.crf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;



public class download {

	public static void main(String[] arg)  {
		downloadPDF("http://www.dline.info/jmpt/fulltext/v1n2/2.pdf", "elena mugellini");
		System.out.println("Done");		
	}

	public static void downloadPDF(String path, String name){
		URL url = null;
		try {
			url = new URL(path);
			InputStream in = url.openStream();
			FileOutputStream fos = new FileOutputStream(new File(name+".pdf"));
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from
			                                // connection
			while ((length = in.read(buffer)) > -1) {
			    fos.write(buffer, 0, length);
			}
			fos.close();
			in.close();
			System.out.println("Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(path);
			e.printStackTrace();
		}		
	}
	
	
}
