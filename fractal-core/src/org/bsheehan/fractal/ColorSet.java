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
	final byte[] ORANGE = {(byte)255, (byte)165, (byte)0};
	final byte[] PURPLE = {(byte)160, (byte)32, (byte)240};

	// arrays for holding individual RGB components of color map
	protected byte colors_rgb[][];

	// the color set arrays have to have enough resolution to map each iteration value of the
	// complex iterated fractal. We store a local copy of the fractal functions range of iteration values
	protected int maxIterations;

	protected ColorSetType colorSetType;

	/** enum for different color mapping alogrithms **/
	public enum ColorSetType {
		COLORMAP_SUNSET,
		COLORMAP_EARTH,
		COLORMAP_GRAY,
		COLORMAP_BINARY,
		COLORMAP_BLACK
	}

	public static List<ColorInfo> getColorInfos(){
		List<ColorInfo> colors = new ArrayList<ColorInfo>();

		colors.add(new ColorInfo(ColorSetType.COLORMAP_SUNSET.ordinal(), "Sunset", "todo"));
		colors.add(new ColorInfo(ColorSetType.COLORMAP_EARTH.ordinal(), "Earth", "todo"));
		colors.add(new ColorInfo(ColorSetType.COLORMAP_GRAY.ordinal(), "Gray", "todo"));
		colors.add(new ColorInfo(ColorSetType.COLORMAP_BINARY.ordinal(), "Binary", "todo"));
		//colors.add(new ColorInfo(ColorSetType.COLORMAP_BLACK.ordinal(), "Black", "todo"));

		return colors;
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


		switch (colorSetType) {
			//black random white random, black
		case COLORMAP_GRAY: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			// first 7 ramps contribute total 254 exponential levels of color
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);

			// following 8 ramps each contribute 256 linear levels each to color map
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);
			controlColors.add(BLACK);
			controlColors.add(WHITE);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_BINARY: {

			for (int i = 0; i < maxIterations-1; ++i) {
				if (i%2==0) {
					colors_rgb[i][0] = (byte) (0xFF);
					colors_rgb[i][1] = (byte) (0xFF);
					colors_rgb[i][2] = (byte) (0xFF);
				} else {
					colors_rgb[i][0] = (byte) (0x0);
					colors_rgb[i][1] = (byte) (0x0);
					colors_rgb[i][2] = (byte) (0x0);
				}
			}
		}
		break;
			case COLORMAP_BLACK: {
				colors_rgb[maxIterations-1][0] = (byte) (0x0);
				colors_rgb[maxIterations-1][1] = (byte) (0x0);
				colors_rgb[maxIterations-1][2] = (byte) (0x0);

				for (int i = 0; i < maxIterations-1; ++i) {
					colors_rgb[i][0] = (byte) (0xFF);
					colors_rgb[i][1] = (byte) (0xFF);
					colors_rgb[i][2] = (byte) (0xFF);

				}
			}
			break;
			case COLORMAP_EARTH: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			// first 7 ramps contribute total 254 exponential levels of color
			controlColors.add(WHITE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);
			controlColors.add(BROWN);
			controlColors.add(ORANGE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);

			// following 8 ramps each contribute 256 linear levels each to color map
			controlColors.add(WHITE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);
			controlColors.add(BROWN);
			controlColors.add(ORANGE);
			controlColors.add(DARK_BLUE);
			controlColors.add(DARK_GREEN);
			controlColors.add(BROWN);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
		case COLORMAP_SUNSET: {
			List<byte[]> controlColors = new ArrayList<byte[]>();
			// first 7 ramps contribute total 254 exponential levels of color

			controlColors.add(DARK_BLUE);
			controlColors.add(BLUE);
			controlColors.add(CYAN);
			controlColors.add(PURPLE);
			controlColors.add(RED);
			controlColors.add(ORANGE);
			controlColors.add(YELLOW);


			// following 8 ramps each contribute 256 linear levels each to color map
			controlColors.add(DARK_BLUE);
			controlColors.add(BLUE);
			controlColors.add(CYAN);
			controlColors.add(PURPLE);
			controlColors.add(RED);
			controlColors.add(ORANGE);
			controlColors.add(YELLOW);
			controlColors.add(WHITE);

			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
		}
		break;
//		case COLORMAP_RANDOM: {
//			List<byte[]> controlColors = new ArrayList<byte[]>();
//
//			int maxSpan = 256;
//
//			// first 7 ramps contribute total 254 exponential levels of color
//			byte[] color = createRandomColor(null, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//
//			// following 8 ramps each contribute 256 linear levels each to color map
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//			color = createRandomColor(color, maxSpan);
//			controlColors.add(color);
//
//			createExponentialMap(controlColors, colors_rgb, maxIterations / controlColors.size());
//		}
//		break;

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
		int start = 0;
		int span = 2; //exponential start
		for (int i = 0; i < controlColors.size() - 1; ++i) {
			final byte startColor[] = controlColors.get(i);
			final byte endColor[] = controlColors.get(i+1);
			if (span > 256)
				span =256; // keep within RGB byte range (256)

//			if (start >= maxIterations) {
//				System.err.println("Exceeded max iterations on interp");
//				return;
//			}

			int range = start+span;

			if (range > maxIterations)
				range=maxIterations;

			interpColors(startColor, endColor, start, range, colors);
			start = start + span;
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
//			if (i >= maxIterations) {
//				System.err.println("Exceeded max iterations on interp");
//				return;
//			}

			colors[i][0] = (byte) (red1 + strideR);
			strideR += stepR;
			colors[i][1] = (byte) (green1 + strideG);
			strideG += stepG;
			colors[i][2] = (byte) (blue1 + strideB);
			strideB += stepB;
		}
	}
}
