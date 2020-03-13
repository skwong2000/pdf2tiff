package converter.PDF2TIFF;

import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDF2TIFF_Converter {
	
	private static int default_dpi = 300;
	
	/**
	 * Converts PDF to multi-page TIFF file with 300dpi
	 * @param pdfInputPath Complete input PDF file path.
	 * @param tiffOutputPath Complete output TIFF file path.
	 * @throws IOException Thrown if either of the input paths are incorrect
	 */
	public static void convert(String pdfInputPath, String tiffOutputPath) throws IOException {
		
		//Load the PDF
		try(PDDocument pdf = PDDocument.load(new File(pdfInputPath))){
			
			//Initialize PDF renderer
			PDFRenderer ren = new PDFRenderer(pdf);
			
			//Setup Image Writer
			ImageWriter writer = ImageIO.getImageWritersBySuffix("tiff").next();
			writer.setOutput(ImageIO.createImageOutputStream(new File(tiffOutputPath)));
			
			//Setup Image Writer Parameters
			ImageWriteParam params = writer.getDefaultWriteParam();
			params.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
			
			//Prepare the Image Writer
			writer.prepareWriteSequence(null);
			
			//Writer pages to the image writer
			for(int page=0; page<pdf.getNumberOfPages(); page++) {
				writer.writeToSequence(new IIOImage(ren.renderImageWithDPI(page, default_dpi), null, null), params);
			}
			
			//End Writer Sequence
			writer.endWriteSequence();
		}
	}
	
	/**
	 * Converts PDF to multi-page TIFF file with given dpi. Default dpi of 300 usually works for any OCR tool like Tesseract.
	 * @param pdfInputPath Complete input PDF file path.
	 * @param tiffOutputPath Complete output TIFF file path.
	 * @param dpi Resolution of the output page in the tiff
	 * @throws IOException 
	 */
	public static void convert(String pdfInputPath, String tiffOutputPath, int dpi) throws IOException {
		PDF2TIFF_Converter.default_dpi = dpi;
		
		convert(pdfInputPath, tiffOutputPath);
		
		PDF2TIFF_Converter.default_dpi = 300;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("Program Start...");
			String pdfFile = "C:\\projects\\clive\\eclipse-workspace\\pdf2tiff\\PDF2TIFF\\temp\\efx.pdf";
			String tifFile = "C:\\projects\\clive\\eclipse-workspace\\pdf2tiff\\PDF2TIFF\\temp\\efx.tif";			
			PDF2TIFF_Converter.convert(pdfFile, tifFile);
			System.out.println("Tif generated successfully.");			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
