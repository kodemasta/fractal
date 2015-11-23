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
	protected int iterationHistogram[];

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
		this.iterationHistogram = new int[maxIterations];
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
	 */
	public void assignColors() {
		this.rgbBuffer.clear();
		for (int i = 0 ; i < this.screenWidth; i++){
			for (int j = 0; j < this.screenHeight; j++) {
				final int index = this.iterationBuffer[i][j];
				// TODO eventually may use this component value for alpha blending effects
				this.rgbBuffer.put(this.rgbColorSet.getRed(index));
				this.rgbBuffer.put(this.rgbColorSet.getGreen(index));
				this.rgbBuffer.put(this.rgbColorSet.getBlue(index));
				//this.rgbBuffer.put((byte) 255);

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
		final float kConvertPixelToRealAxis = (float)this.fractalIterationFunction.getFractalRegion().getWidth()
		/ width;
		final float kConvertPixelToImagAxis = (float)this.fractalIterationFunction.getFractalRegion().getHeight()
		/ height;

		final int maxIterations = this.fractalIterationFunction.getMaxIterations();
		final Rectangle.Float fractalRegion = this.fractalIterationFunction.getFractalRegion();
		for (int i = 0; i < maxIterations; ++i)
			this.iterationHistogram[i] = 0;
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
				this.iterationHistogram[numIterations]++;
				this.iterationBuffer[pixelX][pixelY] = numIterations;
			}
		}
		return true;
	}

	/**
	 * Sample specific pnts to detect if there is enough variation in the fractal
	 * generated to be considered for full resolution rendering.
	 * 
	 * Each corner pixel is compared to the center pixel iteration value. Only when
	 * each comparison exceeds a selected tolerance is true returned.
	 */
	public boolean isInterestingAtAll(int width, int height) {
		// This param can be optimized to determine which amount of iteration value differences
		// should be allowed to be considered "interesting"
		final int tolerance = this.fractalIterationFunction.getMaxIterations()/50;

		final float kConvertPixelToRealAxis = (float)this.fractalIterationFunction.getFractalRegion().getWidth()
		/ width;
		final float kConvertPixelToImagAxis = (float) this.fractalIterationFunction.getFractalRegion().getHeight()
		/ height;

		final float top = (float)this.fractalIterationFunction.getFractalRegion().getMaxY(); //top
		final float left = (float)this.fractalIterationFunction.getFractalRegion().getMinX(); //left
		
		//
		final int NUM_SAMPLES_X = 3;
		final int NUM_SAMPLES_Y = 3;
		
		//short samples[] = new short[NUM_SAMPLES_X * NUM_SAMPLES_Y];
		int incX = (width-1)/(NUM_SAMPLES_X-1);
		int incY = (height-1)/(NUM_SAMPLES_Y-1);
		short max = Short.MIN_VALUE;
		short min = Short.MAX_VALUE;
		short rowMax = Short.MIN_VALUE;
		short rowMin = Short.MAX_VALUE;
		
		for (int i = 0; i < NUM_SAMPLES_X; ++i) {
			rowMax = Short.MIN_VALUE;
			rowMin = Short.MAX_VALUE;
			int pixelX = i*incX;
			c.r = kConvertPixelToRealAxis * pixelX + left;
			for (int j = 0; j < NUM_SAMPLES_Y; ++j) {
				int pixelY = i*incY;
				c.i = kConvertPixelToImagAxis * pixelY + top;
				short iters = this.fractalIterationFunction.iterate(c);
				if (iters < min){
					min = iters;
					rowMin = iters;
				}
				else if (iters > max){
					max = iters;
					rowMax = iters;
				}
			}
			//if (rowMax-rowMin > 0 && rowMax-rowMin < 2)
			//	return false;
		}
		
		//if (max-min < 2)
		//+	return false;

/*		// Test corners
		// pixel 1
		int pixelX = 0;
		int pixelY = 0;
		c.i = kConvertPixelToImagAxis * pixelY + top;
		c.r = kConvertPixelToRealAxis * pixelX + left;
		final short numIterations_0_0 = this.fractalIterationFunction.iterate(c);

		// pixel 2
		pixelY = height-1;
		c.i = kConvertPixelToImagAxis * pixelY + top;
		c.r = kConvertPixelToRealAxis * pixelX + left;
		final short numIterations_0_h = this.fractalIterationFunction.iterate(c);

		// pixel 3
		pixelX = width-1;
		c.i = kConvertPixelToImagAxis * pixelY + top;
		c.r = kConvertPixelToRealAxis * pixelX + left;
		final short numIterations_w_h = this.fractalIterationFunction.iterate(c);

		// pixel 4
		pixelY = 0;
		c.i = kConvertPixelToImagAxis * pixelY + top;
		c.r  = kConvertPixelToRealAxis * pixelX + left;
		final short numIterations_w_0 = this.fractalIterationFunction.iterate(c);

		// pixel 5 (center)
		pixelX = (width-1)/2;
		pixelY = (height-1)/2;
		c.i = kConvertPixelToImagAxis * pixelY + this.fractalIterationFunction.getFractalRegion().top;
		c.r = kConvertPixelToRealAxis * pixelX + this.fractalIterationFunction.getFractalRegion().left;
		final short numIterations_c_c = this.fractalIterationFunction.iterate(c);
*/
/*		if (Math.abs(numIterations_0_0 - numIterations_c_c) < tolerance &&
				Math.abs(numIterations_w_0 - numIterations_c_c) < tolerance) {
			return false;
		} else if (Math.abs(numIterations_w_0 - numIterations_c_c) < tolerance &&
				Math.abs(numIterations_w_h - numIterations_c_c) < tolerance) {
			return false;
		} else if (Math.abs(numIterations_0_h - numIterations_c_c) < tolerance &&
				Math.abs(numIterations_w_h - numIterations_c_c) < tolerance) {
			return false;
		} else if (Math.abs(numIterations_0_h - numIterations_c_c) < tolerance &&
				Math.abs(numIterations_0_0 - numIterations_c_c) < tolerance) {
			return false;
		} */
		return true;
	}

	/**
	 * If isInterestingAtAll passes, then we do further processing to determinie
	 * if the low resolution fractal region is truly cool.
	 */
	public boolean isCoolEnough() {
		final int totalPixels = this.rgbBuffer.capacity() / this.kNumColorSpaceComponents ;

		// if any one iteration count is more than configured percent of fractal, bail.
		final int threshold = (int)(totalPixels * this.fractalIterationFunction.getFractalConfig().singleIterMaxThreshold);
		int nonZeroCnt = 0;
		for (int i = 0; i < this.fractalIterationFunction.getMaxIterations(); ++i) {
			if (this.iterationHistogram[i] >= threshold) {
				return false;
			}
			if (this.iterationHistogram[i] > 0)
				nonZeroCnt++;
		}

		// if number of non zero is too small then bail
		final int itersMinNonZeroThreshold = this.fractalIterationFunction.getFractalConfig().itersMinNonZeroThreshold;
		if (nonZeroCnt < itersMinNonZeroThreshold)
			return false;

		return true;
	}

	/**
	 *  Create a RGB color palette to be used for mapping the iteration
	 *  values to RGB pixel values.
	 */
	public void setRandomColorSet() {
		this.rgbColorSet.setRandomColorSet(this.iterationHistogram);
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

//	@Override
//	public void assignColor(int color) {
//		this.rgbBuffer.rewind();
//		for (int i = 0 ; i < this.screenWidth; i++){
//			for (int j = 0; j < this.screenHeight; j++) {
//				final int index = this.iterationBuffer[i][j];
//				// TODO eventually may use this component value for alpha blending effects
//				//this.rgbBuffer.put((byte)Color.);
//				//this.rgbBuffer.put((byte)Color.green(color));
//				//this.rgbBuffer.put((byte)Color.blue(color));
//				//this.rgbBuffer.put((byte) 255);
//
//				this.rgbBuffer.put(index,color);
//
//			}
//		}
//	}
}
