package $group__.$modId__.client.gui.themes;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumTheme implements ITheme<EnumTheme> {
	/* SECTION enums */
	NONE;


	/* SECTION methods */

	@SuppressWarnings("EmptyMethod")
	@Override
	public String toString() { return super.toString(); }


	@Override
	public EnumTheme toImmutable() { return this; }

	@Override
	public boolean isImmutable() { return true; }
}
