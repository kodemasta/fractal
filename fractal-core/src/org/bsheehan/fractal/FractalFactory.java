package org.bsheehan.fractal;


import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name FractalFactory
 * @description This creates and updates fractal instances for use in texture mapping. 
 */
public class FractalFactory {

	static public List<FractalInfo> getFractals() {
		List<FractalInfo> fractals = new ArrayList<FractalInfo>();
		fractals.add(new MandelbrotQuarticJuliaFunction().fractalInfo);
		fractals.add(new MandelbrotQuarticFunction().fractalInfo);
		fractals.add(new MandelbrotCubicJuliaFunction().fractalInfo);
		fractals.add(new MandelbrotCubicFunction().fractalInfo);
		fractals.add(new MandelbrotJuliaFunction().fractalInfo);
		fractals.add(new MandelbrotFunction().fractalInfo);
		return fractals;
	}

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
	 * Once a cool fractal region has been located using makeItCool, a full color mapped
	 * fractal can be generated using this call.
	 * 
	 * @param fractal to generate
	 */
	static public void generateFullFractal(IFractal fractal) {

		// update the resolution and generate
		//fractal.setDims(dim, dim);
		//fractal.generate();
		// now that we have a cool region, lets assign a cool colormap
		//fractal.setRandomColorSet();
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
