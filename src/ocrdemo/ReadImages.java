package ocrdemo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoadLibs;

public class ReadImages {
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage){
	        return (BufferedImage) img;
	    }
	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();
	    // Return the buffered image
	    return bimage;
	}
	
	public static void main(String[] args){
	
		ITesseract image = new Tesseract();
		File tessFolder = LoadLibs.extractTessResources("tessdata");
		image.setDatapath(tessFolder.getAbsolutePath());
		image.setLanguage("eng");
		
		File pathToFile = new File("C:\\Tesseract-OCR\\invoice_1.tif"); 
		try {
			Image origin = ImageIO.read(pathToFile);
			BufferedImage bufferedImg = toBufferedImage(origin);
			BufferedImage textImage = ImageHelper.convertImageToGrayscale(bufferedImg);

			Rectangle rect =  new Rectangle(891,676,500,300);
			String str = image.doOCR(textImage, rect);
			
			System.out.println(str.contains("\n"));
	        System.out.println(str.contains("\\n"));
	        
	        str = str.replaceAll("\\r\\n|\r|\n", " ").replaceAll("(\\s)+", " ").trim();
	        
			System.out.println(str);
			
		}catch(TesseractException e){
			System.out.println("Exception "+e.getMessage());
		}catch(IOException e){
			System.out.println("Exception "+e.getMessage());
		}
		 
	/*	 try {
		File file = new File("C:\\Tesseract-OCR\\invoice_1.tif");
		BufferedImage bufferedImage = ImageIO.read(file);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage.getSubimage(2027, 2848, 330, 330));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

       
            Result result = new MultiFormatReader().decode(bitmap);
            System.out.println(result.getText());
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
        }catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }
        */
	}

}
