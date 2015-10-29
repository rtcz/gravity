package sk.uniba.gravity.game.ui;

import java.awt.Color;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class SpeedSlider extends JSlider {// implements ChangeListener {

	private static final long serialVersionUID = 5032016481596276173L;
	
	//private GameManager manager;

	public SpeedSlider() {//GameManager manager) {
		super();
		//this.manager = manager;
		
		setMinimum(0);
		//setMaximum(8);
		setMaximum(6);
		setValue(0);
		
		setOpaque(false);
		setForeground(Color.WHITE);
		setPaintTrack(true);

		setSnapToTicks(true);
		setMajorTickSpacing(1);
		setPaintTicks(true);

		setToolTipText("Game speed multiplier");
		setLabelTable(createLabelTable());
		setPaintLabels(true);
		
		//addChangeListener(this);
	}

	private Dictionary<Integer, JLabel> createLabelTable() {
		Dictionary<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("1"));
		labelTable.put(1, new JLabel("10"));
		labelTable.put(2, new JLabel("100"));
		labelTable.put(3, new JLabel("1k"));
		labelTable.put(4, new JLabel("10k"));
		labelTable.put(5, new JLabel("100k"));
		labelTable.put(6, new JLabel("1m"));
//		labelTable.put(7, new JLabel("10m"));
//		labelTable.put(8, new JLabel("100m"));
		
		Enumeration<JLabel> labels = labelTable.elements();
		
		while (labels.hasMoreElements()) {
			JLabel label = labels.nextElement();
			label.setForeground(Color.WHITE);
		}
		return labelTable;
	}

//	@Override
//	public void stateChanged(ChangeEvent e) {
//		manager.setSpeedMultiplier(Math.pow(10, getValue()));
//	}
}
