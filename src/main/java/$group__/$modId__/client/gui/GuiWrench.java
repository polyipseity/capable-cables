package $group__.$modId__.client.gui;

import $group__.$modId__.client.gui.components.*;
import $group__.$modId__.client.gui.templates.GuiContainerDefault;
import $group__.$modId__.client.gui.utilities.constructs.IThemed;
import $group__.$modId__.client.gui.utilities.constructs.IThemed.EnumTheme;
import $group__.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.X;
import $group__.$modId__.client.gui.utilities.constructs.NumberRelativeDisplay.Y;
import $group__.$modId__.client.gui.utilities.constructs.XY;
import $group__.$modId__.client.gui.utilities.constructs.polygons.Rectangle;
import $group__.$modId__.common.registrable.items.ItemWrench;
import $group__.$modId__.utilities.helpers.Colors;
import $group__.$modId__.utilities.variables.Globals;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;

@SideOnly(Side.CLIENT)
public class GuiWrench extends GuiContainerDefault<Number> implements IThemed<EnumTheme> {
	/* SECTION static variables */

	@SuppressWarnings({"unchecked", "RedundantSuppression"})
	protected static final GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> TABS = new GuiTabsThemed<>(
			EnumTheme.NONE,
			0,
			new GuiTabs.ITabThemed.Impl<>(
					new GuiRectangleThemedDrawable<>(
							new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -32)), new XY<>(32, 32)),
							new GuiResource<>(
									new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F, -32)), new XY<>(64, 64)),
									Globals.Client.Resources.GUI_WRENCH,
									Globals.Client.Resources.GUI_WRENCH_INFO
							),
							EnumTheme.NONE
					),
					new GuiRectangleThemed<>(
							new Rectangle<>(new XY<>(new X<>(0.1F), new Y<>(0.1F)), new XY<>(new X<>(0.8F), new Y<>(0.8F))),
							Colors.WHITE,
							EnumTheme.NONE
					),
					EnumTheme.NONE
			)
	);


	/* SECTION variables */

	protected GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> tabs;
	protected ItemStack stack;
	protected EnumTheme theme;


	/* SECTION constructors */

	public GuiWrench(Container container, ItemStack stack) { this(container, stack, EnumTheme.NONE, 0); }

	public GuiWrench(Container container, ItemStack stack, EnumTheme theme, int open) {
		super(container);
		if (!(stack.getItem() instanceof ItemWrench)) throw rejectArguments(stack);
		this.stack = stack;
		this.theme = theme;

		tabs = TABS.copy();
		tabs.setOpen(open);
		tabs.setTheme(theme);
	}


	/* SECTION getters & setters */

	public GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> getTabs() { return tabs; }

	public void setTabs(GuiTabsThemed<Number, List<GuiTabs.ITab<Number, ?>>, GuiTabs.ITab<Number, ?>, EnumTheme, ?> tabs) { this.tabs = tabs; }

	public ItemStack getStack() { return stack; }

	public void setStack(ItemStack stack) { this.stack = stack; }

	@Override
	public EnumTheme getTheme() { return theme; }

	@Override
	public void setTheme(EnumTheme theme) {
		tabs.setTheme(theme);
		this.theme = theme;
	}


	/* SECTION methods */

	@Override
	public Optional<Rectangle<Number, ?>> spec() { return tabs.spec(); }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { tabs.draw(mc); }
}
