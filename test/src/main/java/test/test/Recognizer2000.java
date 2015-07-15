package test.test;

import static net.sourceforge.tess4j.ITessAPI.TRUE;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Russian;
import org.languagetool.rules.RuleMatch;

import test.test.preprocessing.binarization.Otsu;
import test.test.preprocessing.binarization.Sauvola;
import test.test.preprocessing.filter.BlobSizeFilter;

import com.recognition.software.jdeskew.ImageDeskew;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import net.sourceforge.lept4j.Box;
import net.sourceforge.lept4j.Boxa;
import net.sourceforge.lept4j.Leptonica1;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.ImageIOHelper;
import static net.sourceforge.lept4j.ILeptonica.L_CLONE;

public class Recognizer2000 {

	TessBaseAPI handle;
    
	public Recognizer2000() {
		super();
		this.handle = TessAPI1.TessBaseAPICreate();
	}
	/**
	 * бинаризаци€++
	 * разбиение на блоки++
	 * определение €зыка+-
	 * распознование++
	 * подбор по словарю
	 * верификаци€ 
	 * @throws RecognizerExeption 
	 */
	public String recognize(String filePath, String langs, int binarization_type, int quality) throws RecognizerExeption {
		String result = "";
		try {
		File file = new File(filePath);
		BufferedImage image = getImage(binarization_type, file);
		ByteBuffer buf = ImageIOHelper.convertImageData(image);
		int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPIInit2(handle, "", langs, quality);
        TessAPI1.TessBaseAPISetPageSegMode(handle, TessPageSegMode.PSM_AUTO);
//        TessAPI1.TessBaseAPIAdaptToWordStr(handle, TessAPI1., wordstr)
        TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
		PointerByReference pixa = null;
		PointerByReference blockids = null;
		Boxa boxes = TessAPI1.TessBaseAPIGetComponentImages(handle, TessPageIteratorLevel.RIL_PARA, TRUE, pixa, blockids);
		int boxCount = Leptonica1.boxaGetCount(boxes);
		result = checkWithDict(boxes, boxCount);
		} catch (IOException e) {
			throw new RecognizerExeption(e.getCause());
		}
		return result;
	}
	
	private BufferedImage getImage(int binarization_type, File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		image = binarization(image, binarization_type);
		image = deskew(image);
		return image;
	}
	
	private String checkWithDict(Boxa boxes, int boxCount) {
		StringBuilder result = new StringBuilder();
		JLanguageTool langTool = new JLanguageTool(new Russian());
		List<RuleMatch> matches = null;
		for (int i = 0; i < boxCount; i++) {
			Box box = Leptonica1.boxaGetBox(boxes, i, L_CLONE);
			if (box == null) {
				continue;
			}
			TessAPI1.TessBaseAPISetRectangle(handle, box.x, box.y, box.w, box.h);
//			System.err.println(TessAPI1.TessBaseAPIGetInitLanguagesAsString(handle));
			Pointer utf8Text = TessAPI1.TessBaseAPIGetUTF8Text(handle);
//			TessAPI1.TessResultIteratorWordIsFromDictionary(handle);
			String ocrResult = utf8Text.getString(0);
			
			TessAPI1.TessDeleteText(utf8Text);
			int conf = TessAPI1.TessBaseAPIMeanTextConf(handle);
			System.out.print(String.format("Box[%d]: x=%d, y=%d, w=%d, h=%d, confidence: %d, text: %s",i, box.x, box.y, box.w, box.h, conf,ocrResult));
			
//			matches = checkGrammar(langTool, ocrResult); //нужен исправленный стринг на выходе
			result.append(ocrResult);
		}

		PointerByReference pRef = new PointerByReference();
//		pRef.setValue(pix.getPointer());
		Leptonica1.pixDestroy(pRef);
		
		return result.toString();
	}
	
	
	private List<RuleMatch> checkGrammar(JLanguageTool langTool, String ocrResult) throws IOException {
		List<RuleMatch> matches = null;
		matches = langTool.check(ocrResult);
		for (RuleMatch match : matches) {
			System.out.println("Potential error at line " +
					match.getLine() + ", column " +
					match.getColumn() + ": " + match.getMessage());
			System.out.println("Suggested correction: " +
					match.getSuggestedReplacements());
//				if(!match.getSuggestedReplacements().isEmpty()) {
//					ocrResult = match.getSuggestedReplacements().get(0);
//				}
		}
		return matches;
	}
	
	private BufferedImage deskew(BufferedImage image) {
		ImageDeskew id = new ImageDeskew(image);
        double imageSkewAngle = id.getSkewAngle();
        if ((imageSkewAngle > 0.05 || imageSkewAngle < -(0.05))) {
        	image = ImageHelper.rotateImage(image, -imageSkewAngle);
        }
		return image;
	}
	
	private BufferedImage binarization(BufferedImage image, int bin) throws IOException {
		FileOutputStream out = null;
		try {
			RescaleOp rescaleOp = new RescaleOp(1.2f, 2.4f, null);
			rescaleOp.filter(image, image);
			switch (bin) {
			case 1:
				image = otsu(image);
				break;
			case 2:
				image = sauvola(image);
				break;
			case 3:
				image = simple(image);
				break;
			default:
				break;
			}
			final BlobSizeFilter filter = new BlobSizeFilter(0, 5000);
			filter.filter(image);
			File file = new File("shitOut.bmp"); //
			out = new FileOutputStream(file);   // TODO clear
			ImageIO.write(image, "bmp", out);  //
		} finally {
			IOUtils.closeQuietly(out);
		}
		return image;
	}
	
	private BufferedImage simple(BufferedImage image) {
		image = ImageHelper.convertImageToBinary(image);
		return image;
	}
	
	private BufferedImage sauvola(BufferedImage image) {
		Sauvola s = new Sauvola(10, 1, 1); //TODO radius
		image = s.binarize(image);
		return image;
	}
	
	private BufferedImage otsu(BufferedImage image) {
		Otsu o = new Otsu();
		image = o.binarize(image);
		return image;
	}
	
}
