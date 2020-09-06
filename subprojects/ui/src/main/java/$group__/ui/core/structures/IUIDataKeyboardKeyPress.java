package $group__.ui.core.structures;

import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.ICloneable;
import $group__.utilities.interfaces.ICopyable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.function.Function;

@Immutable
public interface IUIDataKeyboardKeyPress
		extends ICloneable, ICopyable {
	ImmutableList<Function<? super IUIDataKeyboardKeyPress, ?>> OBJECT_VARIABLES = ImmutableList.of(
			IUIDataKeyboardKeyPress::getKey, IUIDataKeyboardKeyPress::getScanCode, IUIDataKeyboardKeyPress::getModifiers, IUIDataKeyboardKeyPress::getTimestampMills);
	ImmutableMap<String, Function<? super IUIDataKeyboardKeyPress, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.stitchIterables(OBJECT_VARIABLES.size(),
			ImmutableList.of("key", "scanCode", "modifiers", "timestampMills"),
			OBJECT_VARIABLES));

	int getKey();

	int getScanCode();

	int getModifiers();

	long getTimestampMills();

	IUIDataKeyboardKeyPress recreate();
}
