package $group__.client.gui.core.structures;

import $group__.utilities.interfaces.ICopyable;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;
import java.util.function.DoubleConsumer;

@OnlyIn(Dist.CLIENT)
@Immutable
public final class GuiConstraint implements IGuiConstraint, ICopyable {
	protected final Rectangle2D rectangleMin;
	protected final Rectangle2D rectangleMax;

	public GuiConstraint(Rectangle2D rectangleMin, Rectangle2D rectangleMax) {
		this.rectangleMin = (Rectangle2D) rectangleMin.clone();
		this.rectangleMax = (Rectangle2D) rectangleMax.clone();
		correct(getRectangleMax(), getRectangleMin());
	}

	protected static void correct(Rectangle2D min, Rectangle2D max) {
		correct(min.getX(), max.getX(),
				d -> min.setRect(d, min.getY(), min.getWidth(), min.getHeight()),
				d -> max.setRect(d, max.getY(), max.getWidth(), max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), d, min.getWidth(), min.getHeight()),
				d -> max.setRect(max.getX(), d, max.getWidth(), max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), min.getY(), d, min.getHeight()),
				d -> max.setRect(max.getX(), max.getY(), d, max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), min.getY(), min.getWidth(), d),
				d -> max.setRect(max.getX(), max.getY(), max.getWidth(), d));
	}

	protected Rectangle2D getRectangleMax() { return rectangleMax; }

	protected Rectangle2D getRectangleMin() { return rectangleMin; }

	protected static void correct(double min, double max, DoubleConsumer minSetter, DoubleConsumer maxSetter) {
		if (!IGuiConstraint.isNull(min) && !IGuiConstraint.isNull(max) && min > max) {
			minSetter.accept(max);
			maxSetter.accept(min);
		}
	}

	@Override
	public GuiConstraint copy() { return new GuiConstraint(getRectangleMin(), getRectangleMax()); }

	@Override
	public Rectangle2D getRectangleMinView() { return (Rectangle2D) getRectangleMin().clone(); }

	@Override
	public Rectangle2D getRectangleMaxView() { return (Rectangle2D) getRectangleMax().clone(); }
}
