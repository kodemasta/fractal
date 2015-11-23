package org.bsheehan.fractal;

import java.awt.*;
import java.nio.ByteBuffer;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name FractalFactory
 * @description This creates and updates fractal instances for use in texture mapping. 
 */
public class FractalFactory {

	/**
	 * Create an instance of a fractal generator class
	 * @return
	 */
	static public IFractal createRandomFractal() {
		final IFractal fractal = new Fractal(IteratedFunctionFactory.createRandomFunction());		
		return fractal;
	}
	
	static public IFractal createFractal(IteratedFunctionFactory.FractalType type) {
		final IFractal fractal = new Fractal(IteratedFunctionFactory.createIteratedFunction(type));		
		return fractal;
	}

	/**
	 * For a given fractal, try various zoom in and region crops to
	 * locate a visually stunning section of the iterated complex system.
	 * 
	 * @param fractal - input fractal to zoom in on
	 * @return
	 */
	static public void makeItCool(IFractal fractal, boolean centered, int w, int h) {
		do {
			do {
				fractal.getFractalFunction().setRandomRegion(centered);
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
					// if we interrupt the sleeping thread an
					// exception will get us here.
					return;
				}
			} while (!fractal.isInterestingAtAll(w, h));
			fractal.generate();
		} while (!fractal.isCoolEnough());

	}

	/**
	 * Once a cool fractal region has been located using makeItCool, a full color mapped
	 * fractal can be generated using this call.
	 * 
	 * @param fractal to generate
	 */
	static public void generateFullFractal(IFractal fractal) {

		// update the resolution and generate
		//fractal.setDims(dim, dim);
		fractal.generate();
		// now that we have a cool region, lets assign a cool colormap
		fractal.setRandomColorSet();
		fractal.assignColors();

	}
	
	static public void generateSolidBitmap(IFractal fractal, int dim, Color c) {

		// update the resolution and generate
		fractal.setDims(dim, dim);
		fractal.assignColors();
	}
	
	static public ByteBuffer convertToBitmap(IFractal fractal){
		ByteBuffer buffer = (ByteBuffer)fractal.getBufferColors();
		buffer.rewind();
		//byte[] bytes = new byte[fractal.getWidth() * fractal.getHeight() * 4];
		//buffer.get(bytes);
		//Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, fractal.getWidth() * fractal.getHeight() * 4);
		//Bitmap bitmap = Bitmap.createBitmap(fractal.getWidth(), fractal.getHeight(), Config.ARGB_8888);
		//bitmap.copyPixelsFromBuffer(buffer);
		
		return buffer;
	}
}
