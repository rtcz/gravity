package sk.uniba.gravity;

import java.text.DecimalFormat;

public class Length {

	private double length;
	private boolean convertUnits;
	private boolean displayUnits;
	
	/**
	 * @param length in meters
	 */
	public Length(double length) {
		this.length = length;
		this.displayUnits = true;
		this.convertUnits = true;
	}
	
	public double getLength() {
		return length;
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
		double dLength = length;
		
		if (convertUnits) {
			if (dLength >= 1e3 && dLength < 1e9) {
				dLength *= 1e-3; 
				unit = "km";
			} else if (dLength >= 1e9) {
				dLength *= 1e-9; 
				unit = "mil. km";
			}
		}
		
		DecimalFormat format = new DecimalFormat(",##0.000");
		if (displayUnits) {
			return format.format(dLength) + " " + unit;
		}
		return format.format(dLength);
	}
}
