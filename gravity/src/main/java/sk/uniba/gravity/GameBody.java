package sk.uniba.gravity;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class GameBody extends Body {

	private boolean selected;

	public GameBody(String name) {
		setName(name);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setCenter(Vector2D center) {
		super.setCenter(center);
	}

}
