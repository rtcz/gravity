package sk.uniba.gravity;

public class Scale {

	private double scale;

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

	/**
	 * Scale up to original.
	 * 
	 * model * (original / model) = original
	 * 
	 * @return original / model
	 */
	public double up() {
		return 1 / scale;
	}

	/**
	 * Scale down to model.
	 *
	 * original * (model / original) = model
	 * 
	 * @return model / original
	 */
	public double down() {
		return scale;
	}
}
