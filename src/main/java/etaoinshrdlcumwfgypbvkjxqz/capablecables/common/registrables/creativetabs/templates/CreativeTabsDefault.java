package etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.creativetabs.templates;

import net.minecraft.creativetab.CreativeTabs;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.utilities.RegistryHelper.getNamespacedUnlocalizedNameForRegistry;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.MOD_ID;

public abstract class CreativeTabsDefault extends CreativeTabs {
    public CreativeTabsDefault(String label) { super(getNamespacedUnlocalizedNameForRegistry(MOD_ID, label)); }
    @SuppressWarnings("unused")
    public CreativeTabsDefault(int index, String label) { super(index, getNamespacedUnlocalizedNameForRegistry(MOD_ID, label)); }
}
