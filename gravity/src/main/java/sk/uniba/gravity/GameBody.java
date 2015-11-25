package sk.uniba.gravity;

public class GameBody extends Body {

	private boolean selected;

	private double deltaTime;

	public GameBody() {}

	public GameBody(String name) {
		setName(name);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isNew() {
		return deltaTime == 0;
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}

}
