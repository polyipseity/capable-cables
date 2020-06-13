package $group__.client.gui.utilities.builders;

import $group__.client.gui.components.GuiColorNull;
import $group__.client.gui.components.GuiDrawable;
import $group__.client.gui.themes.GuiThemedNull;
import $group__.client.gui.themes.ITheme;
import $group__.client.gui.themes.IThemed;
import $group__.client.gui.traits.IColored;
import $group__.utilities.builders.BuilderStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.function.Function;

import static $group__.utilities.builders.BuilderDefaults.peekDefault;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.specific.Optionals.optionalNonnull;
import static com.google.common.collect.Maps.immutableEntry;
import static java.util.function.Function.identity;

@SideOnly(Side.CLIENT)
public class BuilderGuiDrawable<T extends BuilderGuiDrawable<T, V, N, C, TH>, V extends GuiDrawable<V, N, C, TH>,
		N extends Number, C, TH extends ITheme<TH>> extends BuilderStructure<T, V> {
	public static final Map.Entry<Class<IColored<?>>, String> KEY_DEFAULT_COLORED =
			immutableEntry(castUncheckedUnboxedNonnull(IColored.class), "colored");
	public static final Map.Entry<Class<IThemed<?>>, String> KEY_DEFAULT_THEMED =
			immutableEntry(castUncheckedUnboxedNonnull(IThemed.class), "themed");


	public IColored<C> colored = castUncheckedUnboxedNonnull(optionalNonnull(peekDefault(KEY_DEFAULT_COLORED),
			identity(), GuiColorNull::getInstance));
	public IThemed<TH> themed = castUncheckedUnboxedNonnull(optionalNonnull(peekDefault(KEY_DEFAULT_THEMED),
			identity(), GuiThemedNull::getInstance));


	public BuilderGuiDrawable(Function<? super T, ? extends V> constructor) { super(constructor); }

	public BuilderGuiDrawable(BuilderGuiDrawable<T, V, N, C, TH> copy) {
		super(copy);
		colored = copy.colored;
		themed = copy.themed;
	}


	public T setColored(IColored<C> colored) {
		this.colored = colored;
		return castUncheckedUnboxedNonnull(this);
	}

	public T setThemed(IThemed<TH> themed) {
		this.themed = themed;
		return castUncheckedUnboxedNonnull(this);
	}
}
