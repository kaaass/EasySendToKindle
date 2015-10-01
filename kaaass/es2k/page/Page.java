package kaaass.es2k.page;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

public class Page {
	private List<Object> obj = new ArrayList<Object>();
	private Icon pic;
	private PageEventHandler eventHandler;
	private int i = 0;
	
	/**
	 * Add a button on this page.
	 * @param b - button to add
	 * @return id of button
	 */
	public int add(JButton b) {
		obj.add(b);
		return i++;
	}
	
	/**
	 * Add a label on this page.
	 * @param l - label to add
	 * @return id of label
	 */
	public int add(JLabel l) {
		obj.add(l);
		return i++;
	}
	
	/**
	 * Add a text field on this page.
	 * @param t - text field to add
	 * @return id of text field
	 */
	public int add(JTextField t) {
		obj.add(t);
		return i++;
	}
	
	/**
	 * Add a text area on this page.
	 * @param t - text area to add
	 * @return id of text area
	 */
	public int add(JTextArea t) {
		obj.add(t);
		return i++;
	}
	
	/**
	 * Set bounds for swing.
	 * @param id - id
	 * @param x - like setBounds() in swing
	 * @param y - like setBounds() in swing
	 * @param w - like setBounds() in swing
	 * @param h - like setBounds() in swing
	 */
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
	
	/**
	 * Add action listener for swing.
	 * @param id - id
	 * @param al - like addActionListener() in swing
	 */
	public void addActionListener(int id, ActionListener al) {
		Object o = obj.get(id);
		if (o instanceof JButton) {
			((JButton) obj.get(id)).addActionListener(al);
			return;
		}
		if (o instanceof JTextField) {
			((JTextField) obj.get(id)).addActionListener(al);
			return;
		}
	}
	
	/**
	 * Add mouse listener for swing.
	 * @param id - id
	 * @param al - like addMouseListener() in swing
	 */
	public void addMouseListener(int id, MouseInputListener al) {
		Object o = obj.get(id);
		if (o instanceof JButton) {
			((JButton) obj.get(id)).addMouseListener(al);
			return;
		}
		if (o instanceof JTextField) {
			((JTextField) obj.get(id)).addMouseListener(al);
			return;
		}
	}
	/**
	 * Add mouse motion listener for swing.
	 * @param id - id
	 * @param al - like addMouseMotionListener() in swing
	 */
	public void addMouseMotionListener(int id, MouseInputListener al) {
		Object o = obj.get(id);
		if (o instanceof JButton) {
			((JButton) obj.get(id)).addMouseMotionListener(al);
			return;
		}
		if (o instanceof JTextField) {
			((JTextField) obj.get(id)).addMouseMotionListener(al);
			return;
		}
	}
	
	/**
	 * Add a image for each page.
	 * @param s - the path of image
	 */
	public void setImage(String s) {
		this.pic = new ImageIcon(s);
	}
	
	/**
	 * Add a event handler
	 * @param eh
	 */
	public void setEventHandler(PageEventHandler eh) {
		this.eventHandler = eh;
	}
	
	public List<Object> getList() {
		return obj;
	}
	
	public Icon getImage() {
		return pic;
	}
	
	public PageEventHandler getEventHandler() {
		return eventHandler;
	}
	
	/**
	 * Get frame of this page.
	 * @return frame of this page
	 */
	public Frame getFrame() {
		Frame f = new Frame(){
			private static final long serialVersionUID = 1L;
			
			public JLabel image;
			
			{
				image = new JLabel("");
				image.setIcon(pic);
				this.add(image);
				for (Object o: obj) {
					if (o instanceof JButton) {
						this.add((JButton) o);
					}
					if (o instanceof JLabel) {
						this.add((JLabel) o);
					}
					if (o instanceof JTextField) {
						this.add((JTextField) o);
					}
					if (o instanceof JTextArea) {
						this.add((JTextArea) o);
					}
				}
				this.setSize(410, 230);
				this.setVisible(false);
			}
		};
		return f;
	}
}
