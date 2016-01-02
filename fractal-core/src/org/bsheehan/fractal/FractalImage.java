package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.complex.Complex;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name FractalImage
 * @description This class wraps the complete fractal state, including the
 * fractal function, color set, array of iteration values, and color pixel buffer.
 * 
 * This is implements IFractalImage which specifies services required to be a fractal generator class.
 */
public class FractalImage implements IFractalImage {

	// holds the RGB color values that represent the visual fractal
	private ByteBuffer rgbBuffer;

	// buffer for collecting each pixel's generated iteration value
	private short[][] iterationBuffer;

	// function that represents the mathematical fractal iteration function
	private IterableFractal iterableFractal;

	// color set used to map from iteration buffer values to pixelBuffer RGB  values
	protected ColorSet rgbColorSet;
	
	private Complex c = new Complex(0, 0);

	// this is used by algorithms to determine if a particular fractal region
	// has interesting visual heuristics
	protected int iterationHistogram[];
	protected int iterationHistogramTotal = 0;

	// width and height of fractal in screen space
	private int screenWidth, screenHeight;

	// ARGB pixel format size
	final private int kNumColorSpaceComponents = 3;
	
	private float screenAspectRatio = 1.0f;

	/**
	 * Constructor
	 * @param iterableFractal
	 */
	public FractalImage(IterableFractal iterableFractal) {
		this.iterableFractal = iterableFractal;
		this.iterationHistogram = new int[this.iterableFractal.getInfo().config.getMaxIterations()];
	}

	/** 
	 * Set resolution for generated fractal image.
	 */
	public void setDims(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.rgbBuffer = ByteBuffer.allocate(width * height * this.kNumColorSpaceComponents);

		this.iterationBuffer = new short[width][];
		for (int i = 0; i < width; ++i){
			this.iterationBuffer[i] = new short[height];
		}
	}	

	/**
	 *  Called after a cool fractal region has been iterated at full resolution.
	 *  Map the iteration values to actual color RGB values for display.
	 *  Based on 3 byte GBR format
	 */
	public void assignColors() {


		this.rgbBuffer.clear();
		byte colorMap[][] = this.rgbColorSet.getColors();
		for (int i = 0 ; i < this.screenWidth; i++){
			for (int j = 0; j < this.screenHeight; j++) {

// histogram normalization method
//				float hue = 0.0f;
//				for (int k = 0; k < this.iterationBuffer[i][j]; k++)
//				{
//					hue += iterationHistogram[k] / (float)iterationHistogramTotal; // Must be floating-point division.
//				}
//				final int index = (int)(hue*(float)this.iterableFractal.getConfig().getMaxIterations());

				// basic color setting
				int index = this.iterationBuffer[i][j];
				this.rgbBuffer.put(colorMap[index][2]);
				this.rgbBuffer.put(colorMap[index][1]);
				this.rgbBuffer.put(colorMap[index][0]);
			}
		}
	}

	/**
	 * Iterate over fractal iteration function to create buffer of iteration values
	 * per pixel. 
	 */
	public boolean generate(boolean julia) {
		return generate(this.rgbBuffer, this.screenWidth, this.screenHeight, julia);
	}

	private boolean generate(ByteBuffer buffer, int width, int height, boolean julia) {
		final double kConvertPixelToRealAxis = (double)this.iterableFractal.getInfo().config.getFractalRegion().getWidth()
		/ width;
		final double kConvertPixelToImagAxis = (double)this.iterableFractal.getInfo().config.getFractalRegion().getHeight()
		/ height;

		//final int maxIterations = this.iterableFractal.getInfo().config.getMaxIterations();
		final Rectangle.Double fractalRegion = this.iterableFractal.getInfo().config.getFractalRegion();
		double minY = fractalRegion.getMinY();
		double minX = fractalRegion.getMinX();
		//iterationHistogramTotal = 0;
		//for (int i = 0; i < maxIterations; ++i)
		//	this.iterationHistogram[i] = 0;
		// determine number of iterations for each fractal pixel on complex
		// plane.
		// outer loop iterates over imaginary axis of specified region


		Complex c = new Complex(this.iterableFractal.getInfo().config.zConstant);
		Complex z = new Complex(this.iterableFractal.getInfo().config.zOrigin);

		if (!julia) {
			// mandelbrot iteration
			for (int pixelY = 0; pixelY < height; pixelY++) {
				// convert pixel y coordinate to imaginary component of zConstant, cy
				c.i = kConvertPixelToImagAxis * pixelY + minY; //top
				// inner loop iterates over real axis of specified region
				for (int pixelX = 0; pixelX < width; pixelX++) {
					// convert pixel x coordinate to real component of zConstant, cx
					c.r = kConvertPixelToRealAxis * pixelX + minX; //left
					z.setValues(this.iterableFractal.getInfo().config.zOrigin);
					this.iterationBuffer[pixelY][pixelX] = this.iterableFractal.iterate(z, c);

					//this.iterationHistogram[numIterations]++;
					//iterationHistogramTotal++;

				}
			}
		} else {

			// julia iteration
			for (int pixelY = 0; pixelY < height; pixelY++) {
				// convert pixel y coordinate to imaginary component of zConstant, cy
				z.i = kConvertPixelToImagAxis * pixelY + minY; //top
				// inner loop iterates over real axis of specified region
				for (int pixelX = 0; pixelX < width; pixelX++) {
					// convert pixel x coordinate to real component of zConstant, cx
					z.r = kConvertPixelToRealAxis * pixelX + minX; //left
					c.setValues(this.iterableFractal.getInfo().config.zConstant);
					this.iterationBuffer[pixelY][pixelX] = this.iterableFractal.iterate(z, c);

					//this.iterationHistogram[numIterations]++;
					//iterationHistogramTotal++;
				}
			}
		}

//		for (int i = 0; i < maxIterations; ++i)
//			if (this.iterationHistogram[i] > 0)
//				System.out.println("histogram " + i + " " +this.iterationHistogram[i]);

		return true;
	}

	public void setColorSet(ColorSet colorSet) {
		this.rgbColorSet = colorSet;
	}

	public ByteBuffer getBufferColors() {
		return this.rgbBuffer;
	}

	public int getWidth() {
		return this.screenWidth;
	}

	public int getHeight() {
		return this.screenHeight;
	}



	@Override
	public IterableFractal getIterableFractal() {
		return this.iterableFractal;
	}


}
