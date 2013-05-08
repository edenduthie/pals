package pals.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pals.database.FileDAO;
import pals.entity.PalsFile;

@Service
public class FileService
{
    @Autowired FileDAO photoDAO;
    int BUFFER = 1024;
    
    public PalsFile load(InputStream in) throws IOException
    {
    	PalsFile photo = create(in);
    	photoDAO.put(photo);
    	return photo;
    }
    
    public PalsFile create(InputStream in) throws IOException
    {
    	PalsFile photo = new PalsFile();
    	List<Byte> byteArray = new ArrayList<Byte>();
    	byte[] buffer = new byte[BUFFER];
    	while( in.read(buffer,0,BUFFER) != -1 )
    	{
    		for( byte b : buffer ) byteArray.add(b);
    	}
    	byte[] picture = new byte[byteArray.size()];
    	int i=0;
    	for( Byte b : byteArray )
    	{
    		picture[i++] = b.byteValue(); 
    	}
    	photo.setPicture(picture);
    	return photo;
    }
    
    public void resize(PalsFile photo, Integer width, Integer height, String type) throws IOException
    {
    	BufferedImage bi = ImageIO.read(photo.retrieveInputStream());
    	Double originalRatio = new Double(bi.getHeight()) / new Double(bi.getWidth());
    	if( bi.getWidth() < width ) width = bi.getWidth();
    	if( bi.getHeight() < height ) height = bi.getHeight();
    	Double newRatio = new Double(height) / new Double(width);
    	if( newRatio > originalRatio ) height = (int) (width*originalRatio);
    	else if( newRatio < originalRatio ) width = (int) (height/originalRatio);
    	BufferedImage resized = getScaledInstance(bi,width,height,RenderingHints.VALUE_INTERPOLATION_BILINEAR,true);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ImageIO.write( resized,type, baos );
    	photo.setType(type);
    	baos.flush();
    	photo.setPicture(baos.toByteArray());
    	baos.close();
    }
    
	/**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth,
			int targetHeight, Object hint, boolean higherQuality) 
	{
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}
}
