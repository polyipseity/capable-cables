package $group__.client.ui.mvvm.views.events;

import $group__.client.ui.mvvm.core.structures.IUIDataMouseButtonClick;
import $group__.client.ui.mvvm.views.core.events.IUIEventMouse;
import $group__.client.ui.mvvm.views.core.events.IUIEventTarget;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public class UIEventMouse extends UIEvent implements IUIEventMouse {
	protected final IUIDataMouseButtonClick data;
	@Nullable
	protected final IUIEventTarget relatedTarget;

	public UIEventMouse(ResourceLocation type, boolean canBubble, IUIEventTarget target, IUIDataMouseButtonClick data, @Nullable IUIEventTarget relatedTarget) {
		super(type, canBubble, target);
		this.data = data;
		this.relatedTarget = relatedTarget;
	}

	@Override
	public IUIDataMouseButtonClick getData() { return data; }

	@Override
	public Optional<IUIEventTarget> getRelatedTarget() { return Optional.ofNullable(relatedTarget); }

}
