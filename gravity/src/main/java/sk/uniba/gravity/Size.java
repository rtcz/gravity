package sk.uniba.gravity;

import java.text.DecimalFormat;

public class Size {

	private double size;
	private boolean convertUnits;
	private boolean displayUnits;
	
	/**
	 * @param size in meters
	 */
	public Size(double size) {
		this.size = size;
		this.displayUnits = true;
		this.convertUnits = true;
	}
	
	public double getLength() {
		return size;
	}
	
	public void convertUnits(boolean convertUnits) {
		this.convertUnits = convertUnits;
	}
	
	public void displayUnits(boolean displayUnits) {
		this.displayUnits = displayUnits;
	}
	
	@Override
	public String toString() {
		String unit = "m";
		double dSize = size;
		
		if (convertUnits) {
			if (dSize >= 1e3 && dSize < 1e9) {
				dSize *= 1e-3; 
				unit = "km";
			} else if (dSize >= 1e9) {
				dSize *= 1e-9; 
				unit = "mil. km";
			}
		}
		
		DecimalFormat format = new DecimalFormat(",##0.000");
		if (displayUnits) {
			return format.format(dSize) + " " + unit;
		}
		return format.format(dSize);
	}
}
