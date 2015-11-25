package sk.uniba.gravity;

public class Scale {

	private double scale;
	private double zoom = 1;

	/**
	 * The ratio of the linear dimension of the model to the same dimension of
	 * the original.
	 * 
	 * @param scale
	 *            model / original
	 */
	public Scale(double scale) {
		this.scale = scale;
	}

	public void zoom(double zoom) {
		this.zoom *= zoom;
	}

	public double getZoom() {
		return zoom;
	}

	public double getScale() {
		return scale;
	}

	/**
	 * Scale up to original.
	 * 
	 * model * (original / model) = original
	 * 
	 * @return original / model
	 */
	public double up() {
		return 1 / (scale * zoom);
	}

	/**
	 * Scale down to model.
	 *
	 * original * (model / original) = model
	 * 
	 * @return model / original
	 */
	public double down() {
		return scale * zoom;
	}
}
