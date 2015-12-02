package org.bsheehan.fractal;


import java.util.ArrayList;
import java.util.List;

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

	// arrays for holding individual RGB components of color map
	protected byte colors_rgb[][];

	// the color set arrays have to have enough resolution to map each iteration value of the
	// complex iterated fractal. We store a local copy of the fractal functions range of iteration values
	protected int maxIterations;

	protected ColorSetType colorSetType;

	/** enum for different color mapping alogrithms **/
	public enum ColorSetType {
		COLORMAP_GRAY,
		COLORMAP_EARTH,
		COLORMAP_RANDOM,
		COLORMAP_BLACK,
		COLORMAP_WHITE,
		RANDOM_COLORMAP_FOUR,
		RANDOM_COLORMAP_FIVE,
		RANDOM_COLORMAP_SIX,
		RANDOM_COLORMAP_SEVEN
	}

	/** 
	 * Constructor
	 * @param maxIterations
	 */
	public ColorSet(int maxIterations, ColorSetType colorSetType) {
		this.colorSetType = colorSetType;
		setMaxIterations(maxIterations);
	}

	public void setMaxIterations(int maxIterations) {
		if (this.maxIterations != maxIterations) {
			this.maxIterations = maxIterations;

			this.colors_rgb = new byte[maxIterations][3];
			setColorSet(this.colorSetType); //reset
		}
	}


	/**
	 * Return red part of color mapping
	 * @return
	 */
	public byte[][] getColors() {
		return this.colors_rgb;
	}

	private byte[] createRandomColor(byte previousColor[], int maxSpan){
		byte[] color = { (byte) (Math.random() * 255), (byte) (Math.random() * 255), (byte) (Math.random() * 255) };

		//make sure the new color is sufficiently different from previous color  clor componentrelative to possible span per
		if (previousColor != null){
			int delta = Math.abs(color[0] - previousColor[0]) + Math.abs(color[1] - previousColor[1]) + Math.abs(color[2] - previousColor[2]);
			while (delta < maxSpan/8)
				color = createRandomColor(previousColor, maxSpan);
		}
		return color;
	}
		/**
	 * Select a particular colormap style to generate. Each mapping spans the
	 * iteration range of the iterated complex fractal so there are unique colors for
	 * each iteration escape level. Several randomly generated styles are available.
	 * TODO: perhaps make this more easily extensible with subclass or delegation.
	 * @param colorSetType
	 */
	public void setColorSet(ColorSetType colorSetType) {

		final byte[] WHITE = {(byte)255, (byte)255, (byte)255};
		final byte[] BLACK = {0, 0, 0};
		final byte[] BLUE = {0, 0, (byte)255};
		final byte[] RED = {(byte)255, 0, 0};
		final byte[] GREEN = {0, (byte)255, 0};
		final byte[] CYAN = {0, (byte)255, (byte)255};
		final byte[] MAGENTA = {(byte)255, 0, (byte)255};
		final byte[] YELLOW = {(byte)255, (byte)255, 0};
		final byte[] BROWN = {(byte)150, (byte)100, (byte)50};
		final byte[] DARK_GREEN = {(byte)50, (byte)150, (byte)50};
		final byte[] DARK_BLUE = {(byte)0, (byte)50, (byte)150};

		switch (colorSetType) {
			//black random white random, black
		case COLORMAP_GRAY: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_BLACK: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			controlColors.add(WHITE);
			controlColors.add(BLACK);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_WHITE: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			controlColors.add(BLACK);
			controlColors.add(WHITE);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_EARTH: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			controlColors.add(WHITE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);
			controlColors.add(BROWN);
			controlColors.add(WHITE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);
			controlColors.add(BROWN);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_RANDOM: {
			List<byte[]> controlColors = new ArrayList<byte[]>();

			int maxSpan = maxIterations/8;
			byte[] color = createRandomColor(null, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);
			color = createRandomColor(color, maxSpan);
			controlColors.add(color);

//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());
//			controlColors.add(createRandomColor());

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());

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
			//createLinearMap(colors, lengths);
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
			//createLinearMap(colors, lengths);
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
			//createLinearMap(colors, lengths);
		}
		break;
		case RANDOM_COLORMAP_SEVEN: {
			final int[] colorStart = { 255, 255, 255 };
			final int[] color2 = { 200, 200, 200 };
			final int[] color3 = { 150, 150, 150 };
			final int[] color4 = { 100, 100, 100 };
			final int[] colorEnd = { 50, 50, 50 };
			final int[][] colors = { colorStart, color2, color3, color4,
					colorEnd };
			//createLinearMap(colors, lengths);
		}
		break;

		}
	}

	private void createLinearMap(final List<byte[]> controlColors, byte[][] colors, final int maxSpan) {

		int start = 0;
		for (int i = 0; i < controlColors.size() - 1; ++i) {
			final byte startColor[] = controlColors.get(i);
			final byte endColor[] = controlColors.get(i+1);
			interpColors(startColor, endColor, start, start + maxSpan, colors);

			start = start + maxSpan;
		}
	}

	private void createExponentialMap(final List<byte[]> controlColors, byte[][] colors, final int maxSpan) {

		int accumulate = 0;
		int start = 0;
		int span = 2;
		for (int i = 0; i < controlColors.size() - 1; ++i) {
			final byte startColor[] = controlColors.get(i);
			final byte endColor[] = controlColors.get(i+1);
			if (i == controlColors.size() - 2 )
				span = maxSpan - accumulate;
			span %=256;
			interpColors(startColor, endColor, start, start + span, colors);
			start = start + span;
			accumulate += span;
			span *= 2;

		}
	}

	private void interpColors(final byte startColor[], final byte endColor[], final int startIndex,
							  final int endIndex, byte[][] colors) {
		final int steps = (endIndex - startIndex - 1);
		final int red1 = startColor[0] & 0xFF;
		final int green1 = startColor[1] & 0xFF;;
		final int blue1 = startColor[2] & 0xFF;

		final int red2 = endColor[0] & 0xFF;
		final int green2 = endColor[1] & 0xFF;
		final int blue2 = endColor[2] & 0xFF;

		byte stepR = (byte)((red2 - red1)/steps);
		byte strideR = 0;
		byte stepG = (byte)((green2 - green1)/steps);
		byte strideG = 0;
		byte stepB = (byte)((blue2 - blue1)/steps);
		byte strideB = 0;
		for (int i = startIndex; i < endIndex; ++i) {
			colors[i][0] = (byte) (red1 + strideR);
			strideR += stepR;
			colors[i][1] = (byte) (green1 + strideG);
			strideG += stepG;
			colors[i][2] = (byte) (blue1 + strideB);
			strideB += stepB;
		}


	}



}
