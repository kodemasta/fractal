package org.bsheehan.fractal;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name ColorSet
 * @description This class provides RGB color palette support for mapping fractal iteration escape values 
 * to RGB pixel colors for use as a texture map.
 * 
 **/
public class ColorSet {

	/** enum for different color mapping alogrithms **/
	public enum ColorSetType {
		/** **/
		RANDOM_COLORMAP_ONE, 
		/** **/
		RANDOM_COLORMAP_TWO, 
		/** **/
		RANDOM_COLORMAP_THREE, 
		/** **/
		RANDOM_COLORMAP_FOUR, 
		/** **/
		RANDOM_COLORMAP_FIVE, 
		/** **/
		RANDOM_COLORMAP_SIX
	}

	/** 
	 * Constructor
	 * @param maxIterations
	 */
	public ColorSet(int maxIterations) {
		this.maxIterations = maxIterations;
		this.colors_red = new byte[maxIterations];
		this.colors_green = new byte[maxIterations];
		this.colors_blue = new byte[maxIterations];
		for (int i = 0; i < maxIterations; i++) {
			this.colors_red[i] = 0;
			this.colors_green[i] = 0;
			this.colors_blue[i] = 0;
		}
	}

	/**
	 * For a given histogram, create a color mapping. The histogram can optioinally be used
	 * to create an optimized color mapping.
	 * @param histogram
	 */
	public void setRandomColorSet(int[] histogram) {
		final int colorSetIndex = (int) (Math.random() * ColorSetType.values().length);
		final ColorSetType colorSetType = ColorSetType.values()[colorSetIndex];
		setColorSet(colorSetType);
	}

	/**
	 * Return red part of color mapping
	 * @param iterations
	 * @return
	 */
	public byte getRed(int iterations) {
		return this.colors_red[iterations];
	}

	/**
	 * Return blue part of color mapping
	 * @param iterations
	 * @return
	 */
	public byte getBlue(int iterations) {
		return this.colors_blue[iterations];
	}
	/**
	 * Return green part of color mapping
	 * @param iterations
	 * @return
	 */
	public byte getGreen(int iterations) {
		return this.colors_green[iterations];
	}

	/**
	 * Select a particular colormap style to generate. Each mapping spans the
	 * iteration range of the iterated complex fractal so there are unique colors for
	 * each iteration escape level. Several randomly generated styles are available.
	 * TODO: perhaps make this more easily extensible with subclass or delegation.
	 * @param colorSetType
	 */
	private void setColorSet(ColorSetType colorSetType) {
		final int length1 = 16;
		final int length2 = 32;
		final int length3 = 64;
		final int length4 = this.maxIterations - length1 - length2 - length3;
		final int[] lengths = { length1, length2, length3, length4 };

		switch (colorSetType) {
		case RANDOM_COLORMAP_ONE: {

			final int[] colorStart = { 0, 0, 0 };
			final int[] color2 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color3 = { 255, 255, 255 };
			final int[] color4 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] colorEnd = { 0, 0, 0 };

			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_TWO: {

			final int[] colorStart = { 255, 255, 255 };
			final int[] color2 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color3 = { 0, 0, 0 };
			final int[] color4 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] colorEnd = { 255, 255, 255 };

			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_THREE: {

			final int[] colorStart = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color2 = { 255, 255, 255 };
			final int[] color3 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color4 = { 0, 0, 0 };
			final int[] colorEnd = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_FOUR: {
			final int[] colorStart = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color2 = { 0, 0, 0 };
			final int[] color3 = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[] color4 = { 255, 255, 255 };
			final int[] colorEnd = { (int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255) };
			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_FIVE: {
			final int[] colorStart = { 0, 0, 0 };
			final int[] color2 = { 255, 255, 255 };
			final int[] color3 = { 0, 0, 0 };
			final int[] color4 = { 255, 255, 255 };
			final int[] colorEnd = { 0, 0, 0 };
			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_SIX: {
			final int[] colorStart = { 255, 255, 255 };
			final int[] color2 = { 0, 0, 0 };
			final int[] color3 = { 255, 255, 255 };
			final int[] color4 = { 0, 0, 0 };
			final int[] colorEnd = { 255, 255, 255 };
			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			createColorMap(colors, lengths);
		}
		break;

		}
	}

	private void createColorMap(int[][] colors, int[] lengths) {
		int start = 0;
		for (int i = 0; i < colors.length - 1; ++i) {
			final int startColor[] = colors[i];
			final int endColor[] = colors[i + 1];
			interpColors(startColor, endColor, start, start + lengths[i]);
			start = start + lengths[i];
		}

	}

	private void interpColors(int startColor[], int endColor[], int startIndex,
			int endIndex) {
		final int steps = endIndex - startIndex - 1;
		final int red1 = startColor[0];
		final int green1 = startColor[1];
		final int blue1 = startColor[2];

		final int red2 = endColor[0];
		final int green2 = endColor[1];
		final int blue2 = endColor[2];

		final int stepR = (red2 - red1) / steps;
		int strideR = 0;
		final int stepG = (green2 - green1) / steps;
		int strideG = 0;
		final int stepB = (blue2 - blue1) / steps;
		int strideB = 0;
		for (int i = startIndex; i < endIndex; ++i) {
			this.colors_red[i] = (byte) (red1 + strideR);
			strideR += stepR;
			this.colors_green[i] = (byte) (green1 + strideG);
			strideG += stepG;
			this.colors_blue[i] = (byte) (blue1 + strideB);
			strideB += stepB;
		}
	}

	// arrays for holding individual RGB components of color map
	protected byte colors_red[];
	protected byte colors_green[];
	protected byte colors_blue[];

	// the color set arrays have to have enough resolution to map each iteration value of the
	// complex iterated fractal. We store a local copy of the fractal functions range of iteration values
	protected int maxIterations;

}
