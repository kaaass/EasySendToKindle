package kaaass.es2k.page;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Page {
	private List<Object> obj = new ArrayList<Object>();
	private int i = 0;
	
	public int add(JButton b) {
		obj.add(b);
		return i++;
	}
	
	public int add(JLabel l) {
		obj.add(l);
		return i++;
	}
	
	public int add(JTextField t) {
		obj.add(t);
		return i++;
	}
	
	public int add(JTextArea t) {
		obj.add(t);
		return i++;
	}
	
	public void setBounds(int id, int x, int y, int w, int h) {
		Object o = obj.get(id);
		if (o instanceof JButton) {
			((JButton) obj.get(id)).setBounds(x, y, w, h);
			return;
		}
		if (o instanceof JLabel) {
			((JLabel) obj.get(id)).setBounds(x, y, w, h);
			return;
		}
		if (o instanceof JTextField) {
			((JTextField) obj.get(id)).setBounds(x, y, w, h);
			return;
		}
		if (o instanceof JTextArea) {
			((JTextArea) obj.get(id)).setBounds(x, y, w, h);
			return;
		}
	}
}
