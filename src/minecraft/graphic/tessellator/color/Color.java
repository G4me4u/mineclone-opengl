package minecraft.graphic.tessellator.color;

import minecraft.math.LinMath;

/**
 * @author Christian
 */
public final class Color {

	// Color definitions are from G4mEngine. See the below link for more information:
	// https://github.com/G4me4u/g4mengine/blob/master/src/com/g4mesoft/graphic/GColor.java
	
	/* REDS */
	public static final Color INDIAN_RED             = new Color(0xFFCD5C5C);
	public static final Color LIGHT_CORAL            = new Color(0xFFF08080);
	public static final Color SALMON                 = new Color(0xFFFA8072);
	public static final Color DARK_SALMON            = new Color(0xFFE9967A);
	public static final Color LIGHT_SALMON           = new Color(0xFFFFA07A);
	public static final Color CRIMSON_SALMON         = new Color(0xFFDC143C);
	public static final Color RED                    = new Color(0xFFFF0000);
	public static final Color FIRE_BRICK             = new Color(0xFFB22222);
	public static final Color DARK_RED               = new Color(0xFF8B0000);

	/* PINKS */
	public static final Color PINK                   = new Color(0xFFFFC0CB);
	public static final Color LIGHT_PINK             = new Color(0xFFFFB6C1);
	public static final Color HOT_PINK               = new Color(0xFFFF69B4);
	public static final Color DEEP_PINK              = new Color(0xFFFF1493);
	public static final Color MEDIUM_VIOLET_RED      = new Color(0xFFC71585);
	public static final Color PALE_VIOLET_RED        = new Color(0xFFDB7093);
	
	/* ORANGES */
	public static final Color CORAL                  = new Color(0xFFFF7F50);
	public static final Color TOMATO                 = new Color(0xFFFF6347);
	public static final Color ORANGE_RED             = new Color(0xFFFF4500);
	public static final Color DARK_ORANGE            = new Color(0xFFFF8C00);
	public static final Color ORANGE                 = new Color(0xFFFFA500);
	
	/* YELLOWS */
	public static final Color GOLD                   = new Color(0xFFFFD700);
	public static final Color YELLOW                 = new Color(0xFFFFFF00);
	public static final Color LIGHT_YELLOW           = new Color(0xFFFFFFE0);
	public static final Color LEMON_CHIFFON          = new Color(0xFFFFFACD);
	public static final Color LIGHT_GOLDENROD_YELLOW = new Color(0xFFFAFAD2);
	public static final Color PAPAYA_WHIP            = new Color(0xFFFFEFD5);
	public static final Color MOCCASIN               = new Color(0xFFFFE4B5);
	public static final Color PEACH_PUFF             = new Color(0xFFFFDAB9);
	public static final Color PALE_GOLDENROD         = new Color(0xFFEEE8AA);
	public static final Color KHAKI                  = new Color(0xFFF0E68C);
	public static final Color DARK_KHAKI             = new Color(0xFFBDB76B);
	
	/* PURPLES */
	public static final Color LAVENDER               = new Color(0xFFE6E6FA);
	public static final Color THISTLE                = new Color(0xFFD8BFD8);
	public static final Color PLUM                   = new Color(0xFFDDA0DD);
	public static final Color VIOLET                 = new Color(0xFFEE82EE);
	public static final Color ORCHID                 = new Color(0xFFDA70D6);
	public static final Color FUCHSIA                = new Color(0xFFFF00FF);
	public static final Color MAGENTA                = FUCHSIA;
	public static final Color MEDIUM_ORCHID          = new Color(0xFFBA55D3);
	public static final Color MEDIUM_PURPLE          = new Color(0xFF9370DB);
	public static final Color BLUE_VIOLET            = new Color(0xFF8A2BE2);
	public static final Color DARK_VIOLET            = new Color(0xFF9400D3);
	public static final Color DARK_ORCHID            = new Color(0xFF9932CC);
	public static final Color DARK_MAGENTA           = new Color(0xFF8B008B);
	public static final Color PURPLE                 = new Color(0xFF800080);
	public static final Color REB_PURPLE             = new Color(0xFF663399);
	public static final Color INDIGO                 = new Color(0xFF4B0082);
	public static final Color MEDIUM_SLATE_BLUE      = new Color(0xFF7B68EE);
	public static final Color SLATE_BLUE             = new Color(0xFF6A5ACD);
	public static final Color DARK_SLATE_BLUE        = new Color(0xFF483D8B);
	
	/* GREENS */
	public static final Color GREEN_YELLOW           = new Color(0xFFADFF2F);
	public static final Color CHARTREUSE             = new Color(0xFF7FFF00);
	public static final Color LAWN_GREEN             = new Color(0xFF7CFC00);
	public static final Color LIME                   = new Color(0xFF00FF00);
	public static final Color LIME_GREEN             = new Color(0xFF32CD32);
	public static final Color PALE_GREEN             = new Color(0xFF98FB98);
	public static final Color LIGHT_GREEN            = new Color(0xFF90EE90);
	public static final Color MEDIUM_SPRING_GREEN    = new Color(0xFF00FA9A);
	public static final Color SPRING_GREEN           = new Color(0xFF00FF7F);
	public static final Color MEDIUM_SEA_GREEN       = new Color(0xFF3CB371);
	public static final Color SEA_GREEN              = new Color(0xFF2E8B57);
	public static final Color FOREST_GREEN           = new Color(0xFF228B22);
	public static final Color GREEN                  = new Color(0xFF008000);
	public static final Color DARK_GREEN             = new Color(0xFF006400);
	public static final Color YELLOW_GREEN           = new Color(0xFF9ACD32);
	public static final Color OLIVE_DRAB             = new Color(0xFF6B8E23);
	public static final Color OLIVE                  = new Color(0xFF808000);
	public static final Color DARK_OLIVE_GREEN       = new Color(0xFF556B2F);
	public static final Color MEDIUM_AQUAMARINE      = new Color(0xFF66CDAA);
	public static final Color DARK_SEA_GREEN         = new Color(0xFF8FBC8F);
	public static final Color LIGHT_SEA_GREEN        = new Color(0xFF20B2AA);
	public static final Color DARK_CYAN              = new Color(0xFF008B8B);
	public static final Color TEAL                   = new Color(0xFF008080);
	
	/* BLUES / CYANS */
	public static final Color AQUA                   = new Color(0xFF00FFFF);
	public static final Color CYAN                   = AQUA;
	public static final Color LIGHT_CYAN             = new Color(0xFFE0FFFF);
	public static final Color PALE_TURQUOISE         = new Color(0xFFAFEEEE);
	public static final Color AQUAMARINE             = new Color(0xFF7FFFD4);
	public static final Color TURQUOISE              = new Color(0xFF40E0D0);
	public static final Color MEDIUM_TURQUOISE       = new Color(0xFF48D1CC);
	public static final Color DARK_TURQUOISE         = new Color(0xFF00CED1);
	public static final Color CADET_BLUE             = new Color(0xFF5F9EA0);
	public static final Color STEEL_BLUE             = new Color(0xFF4682B4);
	public static final Color LIGHT_STEEL_BLUE       = new Color(0xFFB0C4DE);
	public static final Color POWDER_BLUE            = new Color(0xFFB0E0E6);
	public static final Color LIGHT_BLUE             = new Color(0xFFADD8E6);
	public static final Color SKY_BLUE               = new Color(0xFF87CEEB);
	public static final Color LIGHT_SKY_BLUE         = new Color(0xFF87CEFA);
	public static final Color DEEP_SKY_BLUE          = new Color(0xFF00BFFF);
	public static final Color DODGER_BLUE            = new Color(0xFF1E90FF);
	public static final Color CORNFLOWER_BLUE        = new Color(0xFF6495ED);
	public static final Color ROYAL_BLUE             = new Color(0xFF4169E1);
	public static final Color BLUE                   = new Color(0xFF0000FF);
	public static final Color MEDIUM_BLUE            = new Color(0xFF0000CD);
	public static final Color JENIFER_BLUE           = new Color(0xFF06066D);
	public static final Color DARK_BLUE              = new Color(0xFF00008B);
	public static final Color NAVY                   = new Color(0xFF000080);
	public static final Color MIDNIGHT_BLUE          = new Color(0xFF191970);
	
	/* BROWNS */
	public static final Color CORNSILK               = new Color(0xFFFFF8DC);
	public static final Color BLANCHED_ALMOND        = new Color(0xFFFFEBCD);
	public static final Color BISQUE                 = new Color(0xFFFFE4C4);
	public static final Color NAVAJO_WHITE           = new Color(0xFFFFDEAD);
	public static final Color WHEAT                  = new Color(0xFFF5DEB3);
	public static final Color BURLY_WOOD             = new Color(0xFFDEB887);
	public static final Color TAN                    = new Color(0xFFD2B48C);
	public static final Color ROSY_BROWN             = new Color(0xFFBC8F8F);
	public static final Color SANDY_BROWN            = new Color(0xFFF4A460);
	public static final Color GOLDENROD              = new Color(0xFFDAA520);
	public static final Color DARK_GOLDENROD         = new Color(0xFFB8860B);
	public static final Color PERU                   = new Color(0xFFCD853F);
	public static final Color CHOCOLATE              = new Color(0xFFD2691E);
	public static final Color SADDLE_BROWN           = new Color(0xFF8B4513);
	public static final Color SIENNA                 = new Color(0xFFA0522D);
	public static final Color BROWN                  = new Color(0xFFA52A2A);
	public static final Color MAROON                 = new Color(0xFF800000);
	
	/* WHITES */
	public static final Color WHITE                  = new Color(0xFFFFFFFF);
	public static final Color SNOW                   = new Color(0xFFFFFAFA);
	public static final Color HONEYDEW               = new Color(0xFFF0FFF0);
	public static final Color MINT_CREAM             = new Color(0xFFF5FFFA);
	public static final Color AZURE                  = new Color(0xFFF0FFFF);
	public static final Color ALICE_BLUE             = new Color(0xFFF0F8FF);
	public static final Color GHOST_WHITE            = new Color(0xFFF8F8FF);
	public static final Color WHITE_SMOKE            = new Color(0xFFF5F5F5);
	public static final Color SEASHELL               = new Color(0xFFFFF5EE);
	public static final Color BEIGE                  = new Color(0xFFF5F5DC);
	public static final Color OLD_LACE               = new Color(0xFFFDF5E6);
	public static final Color FLORAL_WHITE           = new Color(0xFFFFFAF0);
	public static final Color IVORY                  = new Color(0xFFFFFFF0);
	public static final Color ANTIQUE_WHITE          = new Color(0xFFFAEBD7);
	public static final Color LINEN                  = new Color(0xFFFAF0E6);
	public static final Color LAVENDER_BLUSH         = new Color(0xFFFFF0F5);
	public static final Color MISTY_ROSE             = new Color(0xFFFFE4E1);
	
	/* GREYS */
	public static final Color GAINSBORO              = new Color(0xFFDCDCDC);
	public static final Color LIGHT_GRAY             = new Color(0xFFD3D3D3);
	public static final Color LIGHT_GREY             = LIGHT_GRAY;
	public static final Color SILVER                 = new Color(0xFFC0C0C0);
	public static final Color DARK_GRAY              = new Color(0xFFA9A9A9);
	public static final Color DARK_GREY              = DARK_GRAY;
	public static final Color GRAY                   = new Color(0xFF808080);
	public static final Color GREY                   = GRAY;
	public static final Color DIM_GRAY               = new Color(0xFF696969);
	public static final Color DIM_GREY               = DIM_GRAY;
	public static final Color LIGHT_SLATE_GRAY       = new Color(0xFF778899);
	public static final Color LIGHT_SLATE_GREY       = LIGHT_SLATE_GRAY;
	public static final Color SLATE_GRAY             = new Color(0xFF708090);
	public static final Color SLATE_GREY             = SLATE_GRAY;
	public static final Color DARK_SLATE_GRAY        = new Color(0xFF2F4F4F);
	public static final Color DARK_SLATE_GREY        = DARK_SLATE_GRAY;
	public static final Color BLACK                  = new Color(0xFF000000);
	
	/* FULLY TRANSPARENT */
	public static final Color TRANSPARENT            = new Color(0x00000000);
	
	private final int alpha;
	private final int red;
	private final int green;
	private final int blue;

	public Color(float red, float green, float blue) {
		this(255, deNormalize(red), deNormalize(green), deNormalize(blue));
	}

	public Color(float alpha, float red, float green, float blue) {
		this(deNormalize(alpha), deNormalize(red), deNormalize(green), deNormalize(blue));
	}

	public Color(int red, int green, int blue) {
		this(255, red, green, blue);
	}

	public Color(int alpha, int red, int green, int blue) {
		validateARGB(alpha, red, green, blue);
		
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(int argb) {
		this(argb, true);
	}
	
	public Color(int argb, boolean hasAlpha) {
		alpha = hasAlpha ? ((argb >>> 24) & 0xFF) : 0xFF;
		red   = (argb >>> 16) & 0xFF;
		green = (argb >>>  8) & 0xFF;
		blue  = (argb >>>  0) & 0xFF;
	}
	
	private void validateARGB(int alpha, int red, int green, int blue) {
		if ((alpha & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid alpha value, must be 0-255: " + alpha);
		if ((red & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid red value, must be 0-255: " + red);
		if ((green & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid green value, must be 0-255: " + green);
		if ((blue & (~0xFF)) != 0)
			throw new IllegalArgumentException("Invalid blue value, must be 0-255: " + blue);
	}
	
	private static int deNormalize(float value) {
		return LinMath.clamp((int)(value * 255.0f), 0, 255);
	}

	private static float normalize(int value) {
		return value / 255.0f;
	}
	
	public Color withAlpha(float alphaN) {
		return new Color(deNormalize(alphaN), red, green, blue);
	}

	public Color withRed(float redN) {
		return new Color(alpha, deNormalize(redN), green, blue);
	}

	public Color withGreen(float greenN) {
		return new Color(alpha, red, deNormalize(greenN), blue);
	}
	
	public Color withBlue(float blueN) {
		return new Color(alpha, red, green, deNormalize(blueN));
	}

	public Color withAlpha(int alpha) {
		return new Color(alpha, red, green, blue);
	}

	public Color withRed(int red) {
		return new Color(alpha, red, green, blue);
	}

	public Color withGreen(int green) {
		return new Color(alpha, red, green, blue);
	}
	
	public Color withBlue(int blue) {
		return new Color(alpha, red, green, blue);
	}
	
	public Color interpolate(Color other, float t) {
		int a = alpha + (int)(t * (other.alpha - alpha));
		int r = red   + (int)(t * (other.red   - red  ));
		int g = green + (int)(t * (other.green - green));
		int b = blue  + (int)(t * (other.blue  - blue ));
		return new Color(a, r, g, b);
	}
	
	public float getAlphaN() {
		return normalize(alpha);
	}
	
	public float getRedN() {
		return normalize(red);
	}
	
	public float getGreenN() {
		return normalize(green);
	}
	
	public float getBlueN() {
		return normalize(blue);
	}

	public int getAlpha() {
		return alpha;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	public int getARGB() {
		return ((alpha << 24) | (red << 16) | (green << 8) | blue);
	}

	@Override
	public int hashCode() {
		return getARGB();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Color))
			return false;
		return getARGB() == ((Color)other).getARGB();
	}
}
