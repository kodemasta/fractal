package org.bsheehan.fractal;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name Fractal
 * @description This class wraps the complete fractal state, including the
 * fractal function, color set, array of iteration values, and color pixel buffer.
 * 
 * This is implements IFractal which specifies services required to be a fractal generator class.
 */
public class Fractal implements IFractal {

	// holds the RGB color values that represent the visual fractal
	private ByteBuffer rgbBuffer;

	// buffer for collecting each pixel's generated iteration value
	private short[][] iterationBuffer;

	// function that represents the mathematical fractal iteration function
	private IIteratedFunction fractalIterationFunction;

	// color set used to map from iteration buffer values to pixelBuffer RGB  values
	protected ColorSet rgbColorSet;
	
	private Complex c = new Complex(0, 0);

	// this is used by algorithms to determine if a particular fractal region
	// has interesting visual heuristics
	//protected int iterationHistogram[];

	// width and height of fractal in screen space
	private int screenWidth, screenHeight;

	// ARGB pixel format size
	final private int kNumColorSpaceComponents = 3;
	
	private float screenAspectRatio = 1.0f;

	/**
	 * Constructor
	 * @param fractalFunction
	 */
	public Fractal(IIteratedFunction fractalFunction) {
		this.fractalIterationFunction = fractalFunction;
		final short maxIterations = this.fractalIterationFunction.getMaxIterations();
		//this.iterationHistogram = new int[maxIterations];
		this.rgbColorSet = new ColorSet(maxIterations);
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
		for (int i = 0 ; i < this.screenWidth; i++){
			for (int j = 0; j < this.screenHeight; j++) {
				final int index = this.iterationBuffer[i][j];
				this.rgbBuffer.put(this.rgbColorSet.getBlue(index));
				this.rgbBuffer.put(this.rgbColorSet.getGreen(index));
				this.rgbBuffer.put(this.rgbColorSet.getRed(index));
			}
		}
	}

	/**
	 * Iterate over fractal iteration function to create buffer of iteration values
	 * per pixel. 
	 */
	public boolean generate() {
		return generate(this.rgbBuffer, this.screenWidth, this.screenHeight);
	}

	private boolean generate(ByteBuffer buffer, int width, int height) {
		final double kConvertPixelToRealAxis = (double)this.fractalIterationFunction.getFractalRegion().getWidth()
		/ width;
		final double kConvertPixelToImagAxis = (double)this.fractalIterationFunction.getFractalRegion().getHeight()
		/ height;

		final int maxIterations = this.fractalIterationFunction.getMaxIterations();
		final Rectangle.Double fractalRegion = this.fractalIterationFunction.getFractalRegion();
//		for (int i = 0; i < maxIterations; ++i)
//			this.iterationHistogram[i] = 0;
		// determine number of iterations for each fractal pixel on complex
		// plane.
		// outer loop iterates over imaginary axis of specified region
		for (int pixelY = 0; pixelY < height; pixelY++) {
			// convert pixel y coordinate to imaginary component of c, cy
			c.i = kConvertPixelToImagAxis * pixelY
			+ fractalRegion.getMinY(); //top
			// inner loop iterates over real axis of specified region
			for (int pixelX = 0; pixelX < width; pixelX++) {
				// convert pixel x coordinate to real component of c, cx
				c.r = kConvertPixelToRealAxis * pixelX
				+ fractalRegion.getMinX(); //left
				final short numIterations = this.fractalIterationFunction.iterate(c);
//				this.iterationHistogram[numIterations]++;
				this.iterationBuffer[pixelY][pixelX] = numIterations;
			}
		}

//		for (int i = 0; i < maxIterations; ++i)
//			if (this.iterationHistogram[i] > 0)
//				System.out.println("histogram " + i + " " +this.iterationHistogram[i]);

		return true;
	}



	/**
	 *  Create a RGB color palette to be used for mapping the iteration
	 *  values to RGB pixel values.
	 */
	public void setRandomColorSet() {
		//this.rgbColorSet.setRandomColorSet(this.iterationHistogram);
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
	public void setFractalFunction(IIteratedFunction fractalFunction) {
		this.fractalIterationFunction = fractalFunction;
		this.fractalIterationFunction.setScale(this.screenAspectRatio);
	}

	@Override
	public IIteratedFunction getFractalFunction() {
		return this.fractalIterationFunction;
	}

	@Override
	public void setScale(float scale) {
		screenAspectRatio = scale;
	}
}
