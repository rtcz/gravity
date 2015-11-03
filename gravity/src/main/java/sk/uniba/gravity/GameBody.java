package sk.uniba.gravity;

public class GameBody extends Body {

	private boolean selected;

	private boolean isColliding;

	public GameBody() {}

	public GameBody(String name) {
		setName(name);
	}

	public boolean isColliding() {
		return isColliding;
	}

	public void setColliding(boolean isColliding) {
		this.isColliding = isColliding;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

}
