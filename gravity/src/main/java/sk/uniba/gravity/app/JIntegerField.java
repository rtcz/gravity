package sk.uniba.gravity.app;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JIntegerField extends JTextField {

	private static final long serialVersionUID = -6610067110923047786L;

	public JIntegerField() {
		super();
		setHorizontalAlignment(JTextField.RIGHT);
	}
	
	public int getValue() {
		try {
			return Integer.parseInt(getText());
		} catch (NumberFormatException exc) {
			return 0;
		}
	}

	@Override
	protected Document createDefaultModel() {
		return (Document) new IntegerDocument();
	}

	private static class IntegerDocument extends PlainDocument {

		private static final long serialVersionUID = 1L;

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			char[] chars = str.toCharArray();
			boolean ok = true;

			for (int i = 0; i < chars.length; i++) {

				try {
					Integer.parseInt(String.valueOf(chars[i]));
				} catch (NumberFormatException exc) {
					ok = false;
					break;
				}

			}
			if (ok) {
				super.insertString(offs, new String(chars), a);
			}
		}
	}

}
