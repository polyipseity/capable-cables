package $group__.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static $group__.utilities.helpers.specific.MapsExtension.MAP_MAKER_SINGLE_THREAD;
import static $group__.utilities.helpers.specific.Optionals.unboxOptional;

public enum GuiHandler implements IGuiHandler {
	INSTANCE;


	public static final ConcurrentMap<Integer, IGuiHandler> GUI_MAP = MAP_MAKER_SINGLE_THREAD.makeMap();
	protected static final GuiHandlerFunctional DEFAULT = (side, id, player, world, x, y, z) -> {
		throw rejectArguments(side, id, player, world, x, y, z);
	};


	public int registerGui(GuiHandlerFunctional handler) { return registerGui((IGuiHandler) handler); }

	public int registerGui(IGuiHandler handler) {
		int r = GUI_MAP.size();
		GUI_MAP.put(r, handler);
		return r;
	}


	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getServerGuiElement(id, player, world, x, y, z); }

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return GUI_MAP.getOrDefault(id, DEFAULT).getClientGuiElement(id, player, world, x, y, z); }


	@FunctionalInterface
	public interface GuiHandlerFunctional extends IGuiHandler {
		@Nullable
		@Override
		default Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(tryGetGuiElement(Side.SERVER, id, player, world, x, y, z)); }

		@Nullable
		@Override
		default Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) { return unboxOptional(tryGetGuiElement(Side.CLIENT, id, player, world, x, y, z)); }


		Optional<Object> tryGetGuiElement(Side side, int id, EntityPlayer player, World world, int x, int y, int z);
	}
}
