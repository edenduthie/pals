package pals.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import pals.Configuration;

public class ImageUtil 
{
	public static void createThumbnail(File sourceFile, File thumbnailFile) throws IOException
	{
		BufferedImage largerImage = ImageIO.read(sourceFile);
		BufferedImage thumbnailImage = getScaledInstance(largerImage,
			Integer.parseInt(Configuration.getInstance().IMAGE_THUMB_WIDTH),
			Integer.parseInt(Configuration.getInstance().IMAGE_THUMB_HEIGHT),
			 RenderingHints.VALUE_INTERPOLATION_BILINEAR,
			 true);
		ImageIO.write(thumbnailImage, "png", thumbnailFile);
	}
	
	public static void shrinkImage(File sourceFile, File thumbnailFile, Integer width, Integer height) throws IOException
	{
		BufferedImage largerImage = ImageIO.read(sourceFile);
		BufferedImage thumbnailImage = getScaledInstance(largerImage,width,height,
			 RenderingHints.VALUE_INTERPOLATION_BILINEAR,
			 true);
		ImageIO.write(thumbnailImage, "png", thumbnailFile);
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
	
	public static InputStream writeTextOnImage(InputStream input, String text1, String text2) throws IOException
	{
		BufferedImage img = ImageIO.read(input);
		
		int width = img.getWidth();
		int height = img.getHeight();

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		
		Font fnt=new Font("Arial",Font.BOLD,36);
	    Color fntC = new Color(255,0,0);       
	    g2d.setColor(fntC);
	    g2d.setFont(fnt);

		// draw graphics
		g2d.drawImage(img, 0, 0, null);
		//g2d.drawString(text, 1, 15);
		
		Font font = new Font("Arial", Font.BOLD,36);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(text1, font, frc);
        Rectangle r = layout.getPixelBounds(null,0,0);
        layout.draw(g2d, 130, 255-r.y);
        
        TextLayout layout2 = new TextLayout(text2, font, frc);
        Rectangle r2 = layout2.getPixelBounds(null,0,0);
        layout2.draw(g2d, 130, 285-r.y);

		g2d.dispose();
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "gif", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}
}
