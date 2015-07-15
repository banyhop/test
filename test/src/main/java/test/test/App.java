package test.test;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.javaocr.ImageFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacpp.BytePointer;

import cn.easyproject.easyocr.EasyOCR;
import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;

public class App {
  public static void main( String[] args ) throws UnsupportedEncodingException {
//	TessBaseAPI api = new TessBaseAPI();
    
//	File file = new File("111.bmp");
//    
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    BufferedImage img = null;
//    String ext = FilenameUtils.getExtension(file.getName());
//	try {
//		img = ImageIO.read(file);
//		ImageIO.write(img, ext, baos);
//	} catch (IOException e) {
//		System.err.println("Reading file or writing byte[] failed.");
//		e.printStackTrace();
//	}
//    
//	byte[] imageInByte = baos.toByteArray(); //
    
//	  EasyOCR e = new EasyOCR("");
//	  System.err.println(e.discernAndAutoCleanImage("scan2.jpg", null)); //shit
   
	  try {
		RecognizeManager rm = new RecognizeManager();
		List<String> langs = new ArrayList<String>();
		langs.add("eng");
		langs.add("rus");
		rm.recognize(langs, RecognizeManager.SIMPLE_BIN, 1);
	} catch (RecognizerExeption e) {
		e.printStackTrace();
	}
    
//    BytePointer outText = null;
//    //TODO определить язык
//    String language = "ENG"; //getLanguage(file);
//    if (api.Init(".", language) != 0) {
//        System.err.println("Could not initialize tesseract.");
//        System.exit(1);
//    }
//    
//    PIX im = new PIX(new BytePointer(imageInByte));
//    
//    api.SetImage(imageInByte, img.getWidth(), img.getHeight(), 2, 2*img.getWidth());
//    
////    PIX image = pixRead(imageInByte);
//   // pixReadStream(input, 1);
//    api.SetImage(im);
//    
//    //TODO кодировка
//    outText = api.GetUTF8Text();
//    String string = outText.getString("UTF8");
////    assertTrue(!string.isEmpty());
//    System.out.println("OCR output:\n" + string);
//
//    // Destroy used object and release memory
//    api.End();
//    outText.deallocate();
//    pixDestroy(im);

  }
}
