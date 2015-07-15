package test.test;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class Main {

	public static void main(String[] args) {
		new Tesseract();
//		@SuppressWarnings("deprecation")
//		Tesseract tesseract = Tesseract.getInstance();
//		try {
//			tesseract.setDatapath(datapath);
//			String dir = new String("C:/123.bmp");
//			File file = new File(dir);
//			System.err.println(file.getAbsolutePath());
//			String s = tesseract.doOCR(file);
//			System.err.println(s);
//		} catch (TesseractException e) {
//			e.printStackTrace();
//		}
		
		
		        File imageFile = new File("C:/123.bmp");
//		        ITesseract instance = new Tesseract(); // JNA Interface Mapping
		         ITesseract instance = new Tesseract1(); // JNA Direct Mapping
		         File tessDataFolder = LoadLibs.extractTessResources("tessdata"); // Maven build only; only English data bundled
		         instance.setDatapath(tessDataFolder.getAbsolutePath());

		        try {
		            String result = instance.doOCR(imageFile);
		            System.out.println(result);
		        } catch (TesseractException e) {
		            System.err.println(e.getMessage());
		        }
	}

}
