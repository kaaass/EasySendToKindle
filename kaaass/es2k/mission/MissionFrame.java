package kaaass.es2k.mission;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import kaaass.es2k.Main;

public class MissionFrame extends JFrame {
	private static final long serialVersionUID = -3203885338701322372L;
	
	public static Vector<String> cN = new Vector<String>();
	public static Vector<Vector<String>> data = new Vector<Vector<String>>();
	
	public static JScrollPane sp;
	public static JTable table;
	public static TableColumnModel tableCM;
	public static JButton pauseBtn;
	public static JButton deleteBtn;
	public static JButton infoBtn;
	public static JButton startBtn;
	public static JLabel lable0;
	public static JComboBox<?> combo0;
	
	public JMenuBar mb;
	public JMenu fileMenu;
	public JMenuItem fmClose;
	public JMenuItem mmDelete;
	
	public MissionFrame () {
		cN.add("任务码");
		cN.add("任务名");
		cN.add("任务类型");
		cN.add("任务状态");
		table = new JTable(data, cN){
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
		table.setRowSelectionAllowed(true);
		sp = new JScrollPane(table);
		pauseBtn = new JButton("暂停");
		deleteBtn = new JButton("清空");
		infoBtn = new JButton("详细信息");
		startBtn = new JButton("重试任务");
		lable0 = new JLabel("所有任务完成后：");
		String[] action = {"无", "关闭程序", "关机", "注销", "重启"};
		combo0 = new JComboBox<String>(action);
		initMenu();
		initBounds();
		initListener();
		this.add(sp);
		this.add(pauseBtn);
		this.add(deleteBtn);
		this.add(infoBtn);
		this.add(startBtn);
		this.add(lable0);
		this.add(combo0);
		this.setLayout(null);
		this.setJMenuBar(mb);
		this.setSize(560, 402);
		this.setLocationRelativeTo(null);
		this.setTitle("任务视图");
		this.setResizable(false);
		this.setVisible(false);
	}
	
	public void initMenu () {
		mb = new JMenuBar();
		fileMenu = new JMenu("文件");
		fmClose = new JMenuItem("关闭");
		mmDelete = new JMenuItem("清空任务");
		fileMenu.add(mmDelete);
		fileMenu.addSeparator();
		fileMenu.add(fmClose);
		mb.add(fileMenu);
	}
	
	public void initBounds () {
		sp.setBounds(2, 0, 552, 326);
		lable0.setBounds(2, 326, 105, 20);
		combo0.setBounds(104, 326, 80, 20);
		infoBtn.setBounds(186, 326, 90, 20);
		startBtn.setBounds(278, 326, 90, 20);
		pauseBtn.setBounds(370, 326, 90, 20);
		deleteBtn.setBounds(462, 326, 90, 20);
	}
	
	public void initListener () {
		infoBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if (row < 0) {
					return;
				}
				int i = Integer.valueOf(data.get(row).get(0));
				if (table.getSelectedRow() == 0 && !Main.missionManager.todoList.isEmpty()) {
					JOptionPane.showMessageDialog(null, Main.missionManager
							.mList.get(Main.missionManager.todoList.get(0))
							.getDesc(), "详细信息", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, Main.missionManager
							.mList.get(i).getDesc(), "详细信息", 
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		startBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Main.missionManager.runMission();
			}
		});
		pauseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (Main.missionManager.running) {
					if (pauseBtn.getText().equals("暂停")) {
						pauseBtn.setText("恢复");
						Main.missionManager.pause();
					} else {
						pauseBtn.setText("暂停");
						Main.missionManager.resume();
					}
				}
			}
		});
		deleteBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Main.missionManager.clear();
				redraw();
			}
		});
		fmClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		mmDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Main.missionManager.clear();
				redraw();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public void show () {
		super.show();
		redraw();
	}
	
	public void redraw () {
		data.clear();
		for (int i = Main.missionManager.mList.size() - 1; i >= 0; i--) {
			IMission m = Main.missionManager.mList.get(i);
			if (m != null) {
				Vector<String> v1 = new Vector<String>();
				v1.add(String.valueOf(m.id));
				v1.add(m.getTitle());
				v1.add(m.getTypeName());
				v1.add(m.getStates());
				if (Main.missionManager.todoList.indexOf(m.id) >= 0) {
					data.add(0, v1);
				} else {
					data.add(v1);
				}
			}
		}
		table.updateUI();
	}
}
