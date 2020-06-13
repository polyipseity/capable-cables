package $group__.utilities.helpers.specific;

import java.awt.*;
import java.util.regex.Matcher;

import static $group__.utilities.helpers.specific.Patterns.HASH_PATTERN;

public enum Colors {
	/* MARK empty */;


	@SuppressWarnings("unused")
	public static final Color
			WHITE = Color.WHITE,
			LIGHT_GRAY = Color.LIGHT_GRAY,
			GRAY = Color.GRAY,
			DARK_GRAY = Color.DARK_GRAY,
			BLACK = Color.BLACK,
			RED = Color.RED,
			PINK = Color.PINK,
			ORANGE = Color.ORANGE,
			YELLOW = Color.YELLOW,
			GREEN = Color.GREEN,
			MAGENTA = Color.MAGENTA,
			CYAN = Color.CYAN,
			BLUE = Color.BLUE,
			COLORLESS = newColor("#00000000");


	public static Color newColor(String s) {
		return new Color(Integer.parseInt(HASH_PATTERN.matcher(s).replaceAll(Matcher.quoteReplacement("")), RADIX_HEX), true);
	}
}
