package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.awt;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.graphics.MinecraftSurfaceData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sun.awt.HeadlessToolkit;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.im.InputMethodHighlight;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Deprecated
public final class MinecraftToolkit
		extends HeadlessToolkit {
	private static final Supplier<@Nonnull MinecraftToolkit> INSTANCE = Suppliers.memoize(MinecraftToolkit::new);

	private MinecraftToolkit() {
		super(Toolkit.getDefaultToolkit());
	}

	public static MinecraftToolkit getInstance() {
		return INSTANCE.get();
	}

	@Override
	public Dimension getScreenSize() throws HeadlessException {
		return MinecraftSurfaceData.getInstance().getBounds().getSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight highlight) throws HeadlessException {
		return super.mapInputMethodHighlight(highlight);
	}
}