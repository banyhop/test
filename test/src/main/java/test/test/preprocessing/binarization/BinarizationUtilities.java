package test.test.preprocessing.binarization;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class BinarizationUtilities {
    private static final ColorConvertOp RGB_TO_GRAYSCALE = new ColorConvertOp(
            ColorSpace.getInstance(ColorSpace.CS_sRGB),
            ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public static BufferedImage imageToGrayscale(BufferedImage image) {
        final BufferedImage grayscale;

        // return the buffered image as-is, if it is binary already
        switch (image.getType()) {
		case BufferedImage.TYPE_BYTE_BINARY:
			return image;
		case BufferedImage.TYPE_3BYTE_BGR:
			break;
		case BufferedImage.TYPE_4BYTE_ABGR:
			break;
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
			break;
		case BufferedImage.TYPE_BYTE_GRAY:
			break;
		case BufferedImage.TYPE_BYTE_INDEXED:
			break;
		case BufferedImage.TYPE_CUSTOM:
			break;
		case BufferedImage.TYPE_INT_ARGB:
			break;
		case BufferedImage.TYPE_INT_ARGB_PRE:
			break;
		case BufferedImage.TYPE_INT_BGR:
			break;
		case BufferedImage.TYPE_INT_RGB:
			break;
		case BufferedImage.TYPE_USHORT_555_RGB:
			break;
		case BufferedImage.TYPE_USHORT_565_RGB:
			break;
		case BufferedImage.TYPE_USHORT_GRAY:
			break;
		default:
			break;
		}
        if (image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
            return image;
        } else if (image.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            grayscale = image;
        } else if (image.getType() == BufferedImage.TYPE_INT_RGB
                || image.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
            grayscale = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_BYTE_GRAY);

            // convert rgb image to grayscale
            RGB_TO_GRAYSCALE.filter(image, grayscale);
        } else {
            throw new IllegalArgumentException(String.format(
                    "illegal color space: %s",
                    image.getColorModel().getColorSpace().getType()));
        }

        return grayscale;
    }
}
