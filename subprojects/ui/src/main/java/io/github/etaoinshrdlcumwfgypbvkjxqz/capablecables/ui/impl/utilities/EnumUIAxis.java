package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Point2D;

@Immutable
public enum EnumUIAxis {
	X {
		@Override
		public double getCoordinate(Point2D point) { return point.getX(); }
	},
	Y {
		@Override
		public double getCoordinate(Point2D point) { return point.getY(); }
	};

	public abstract double getCoordinate(Point2D point);
}