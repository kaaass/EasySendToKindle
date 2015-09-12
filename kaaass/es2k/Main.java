package kaaass.es2k;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumnModel;

import kaaass.es2k.crashreport.ErrorUtil;
import kaaass.es2k.file.FileUtil;
import kaaass.es2k.mail.MailUtil;
import kaaass.es2k.mail.MailUtil.Result;

public class Main extends JFrame {
	private static final long serialVersionUID = 5727395420329762298L;
	
	public static boolean otherM = false;
	public static Vector<String> cN = new Vector<String>();
	public static Vector<Vector<String>> data = new Vector<Vector<String>>();
	public static String lastDir = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
	
	public static JLabel des0;
	public static JScrollPane sp;
	public static JTable table;
	public static TableColumnModel tableCM;
	public static JTextField fileP;
	public static JButton cBtn;
	public static JButton fBtn;
	public static JCheckBox des1;
	public static JComboBox<?> comboF;
	public static JLabel des2;
	public static JButton sBtn;

	public JMenuBar mb;
	public JPopupMenu popup;
	public JMenu fileMenu;
	public JMenu helpMenu;
	public JMenu aboutMenu;
	public JMenuItem fmSend;
	public JMenuItem fmOption;
	public JMenuItem fmClose;
	public JMenuItem fmDebug;
	public JMenuItem fmHelp;
	public JMenuItem fmAbout;
	public JMenuItem fmSend2;
	public JMenuItem fmDel;

	public Main() {
		cN.add("文件名");
		cN.add("路径");
		cN.add("大小");
		cN.add("状态");
		des0 = new JLabel("推送文件列表:");
		table = new JTable(data, cN){
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
		table.setRowSelectionAllowed(true);
		sp = new JScrollPane(table);
		fileP = new JTextField();
		fileP.setEditable(false);
		fileP.setForeground(Color.DARK_GRAY);
		fileP.setText("...");
		cBtn = new JButton("选择文件");
		fBtn = new JButton("选择文件夹");
		des1 = new JCheckBox("自动将pdf文件转为");
		String[] format = { ".txt", ".mobi" };
		comboF = new JComboBox<Object>(format);
		comboF.enableInputMethods(false);
		des2 = new JLabel(String.format("状态:%s", "准备完毕"));
		sBtn = new JButton("推送全部");
		menuInit();
		btnLis();
		this.setLayout(null);
		this.add(des0);
		this.add(sp);
		this.add(fileP);
		this.add(cBtn);
		this.add(fBtn);
		this.add(des1);
		this.add(comboF);
		this.add(des2);
		this.add(sBtn);
	}
	
	private void menuInit() {
		mb = new JMenuBar();
		fileMenu = new JMenu("文件");
		helpMenu = new JMenu("帮助");
		aboutMenu = new JMenu("关于");
		popup = new JPopupMenu();
		fmSend = new JMenuItem("推送");
		fmOption = new JMenuItem("选项");
		fmClose = new JMenuItem("关闭");
		fmDebug = new JMenuItem("反馈错误");
		fmHelp = new JMenuItem("帮助");
		fmAbout = new JMenuItem("关于");
		fmSend2 = new JMenuItem("推送");
		fmDel = new JMenuItem("删除文件");
		menuListener();
		fileMenu.add(fmSend);
		fileMenu.add(fmOption);
		fileMenu.addSeparator();
		fileMenu.add(fmClose);
		helpMenu.add(fmDebug);
		helpMenu.addSeparator();
		helpMenu.add(fmHelp);
		aboutMenu.add(fmAbout);
		popup.add(fmSend2);
		popup.addSeparator();
		popup.add(fmDel);
		mb.add(fileMenu);
		mb.add(helpMenu);
		mb.add(aboutMenu);
		this.setJMenuBar(mb);
	}
	
	private void menuListener() {
		fmSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				int[] tem = new int[data.size()];
				for (int i = 0; i < tem.length; i++) {
					tem[i] = i;
				}
				if (tem.length <= 0) {
					JOptionPane.showMessageDialog(null, "列表空！", "错误",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				send(tem);
			}
		});
		fmOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				otherM = JOptionPane.showConfirmDialog(null,
						"是否使用备用源推送？(重启程序恢复，不推荐使用，仅作备用源)", "选项",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}
		});
		fmClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		fmDebug.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				MailUtil mail = new MailUtil(false);
				if (!(new File("CrashReport/").isDirectory())) {
					JOptionPane.showMessageDialog(null, "未查询到错误记录！", "错误",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String[] a = new String[3];
				while (true) {
					a[0] = JOptionPane.showInputDialog("请输入您的称呼：");
					if (a[0] == null) {
						return;
					} else if (!a[0].equals("")) {
						break;
					}
				}
				while (true) {
					a[1] = JOptionPane.showInputDialog("请输入您的邮箱地址：");
					if (a[1] == null) {
						return;
					} else if (!a[1].equals("")) {
						break;
					}
				}
				while (true) {
					a[2] = JOptionPane.showInputDialog("请输入问题描述：");
					if (a[2] == null) {
						return;
					} else if (!a[2].equals("")) {
						break;
					}
				}
				if (JOptionPane.showConfirmDialog(null,
						"点击确定反馈，可能需要等待一段时间。请勿关闭程序防止反馈失败。", "提示",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					Result r = mail.sendDebugInfo(a[0], a[1], a[2]);
					if (!r.isSuccess()) {
						(new ErrorUtil(r)).dealWithResult();
					} else {
						JOptionPane.showMessageDialog(null, "反馈成功！开发者将会尽快跟您取得联系。");
					}
				}
			}
		});
		fmHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(null, "如何使用："
						+ "\n注：请先将程序推送邮箱设置为认可邮箱！方法见下一条。"
						+ "\n1.点击选择按钮选择您的电子书"
						+ "\n2.选择完后，确认书目显示在列表内"
						+ "\n3.点击推送按钮，书籍会自动推送到您的Kindle上");
				JOptionPane.showMessageDialog(null, "如何设置推送邮箱："
						+ "\n1.打开亚马逊官网"
						+ "\n2.打开我的账户->管理我的内容和设备->设置"
						+ "\n3.在“已认可的发件人电子邮箱列表”下点击“添加认可的电子邮箱”"
						+ "\n4.输入推送邮箱(默认为:es2kindle@163.com)"
						+ "\n5.(备用为:es2kindle@sina.com)");
				JOptionPane.showMessageDialog(null, "如何解决错误："
						+ "\n1.用文本文档打开同目录下的mail.properties文件"
						+ "\n2.根据提示修改项目"
						+ "\n3.Kindle推送邮箱可以在您的“Kindle->设置->设备选项->个性化您的Kindle”中找到"
						+ "\n4.若还不能解决，请通过菜单->帮助->反馈错误来通知开发者以解决。");
			}
		});
		fmAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JOptionPane.showMessageDialog(null, "关于"
						+ "\n程序名:Easy Send To Kindle"
						+ "\n作者:KAAAsS"
						+ "\n版本:1.0.2.1509_Alpha"
						+ "\n建立于Java 1.8"
						+ "\n感谢网易、新浪邮箱的服务支持。", "关于", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		fmSend2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				send(table.getSelectedRows());
			}
		});
		fmDel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				int[] tem = table.getSelectedRows();
				if (tem.length > 0) {
					for (int i = 0; i < tem.length; i++) {
						data.remove(tem[i]);
					}
					table.updateUI();
				}
				
			}
		});
		MouseInputListener mil = new MouseInputListener(){
			@Override
			public void mouseReleased(MouseEvent e) {
				processEvent(e);   
				if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 && !e.isControlDown() && !e.isShiftDown()) {
					popup.show(table, e.getX(), e.getY());
                } 
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}
			
		};
		table.addMouseListener(mil);
		table.addMouseMotionListener(mil);
	}
	
	private void btnLis() {
		cBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				JFileChooser c = new JFileChooser();
				FileFilter ff = new FileFilter(){
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}
						return f.getName().endsWith(".doc") ||
								f.getName().endsWith(".docx") ||
								f.getName().endsWith(".rtf") ||
								f.getName().endsWith(".txt") ||
								f.getName().endsWith(".mobi") ||
								f.getName().endsWith(".pdf");
					} 
					public String getDescription(){
						return "Kindle支持文件";
					}
				};
				c.setFileFilter(ff);
				c.setMultiSelectionEnabled(false);
				c.setCurrentDirectory(new File(lastDir));
				c.setDialogTitle("请选择推送文件...");
				c.setApproveButtonText("加入列表");
				c.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File f = c.getSelectedFile();
					lastDir = f.getAbsolutePath().substring(0, f.getAbsolutePath()
							.length() - f.getName().length());
					if (ff.accept(f)) {
						if (f.length() > 31457280) {
							JOptionPane.showMessageDialog(null, "单文件大小不能大于30MB！", "错误",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						addListItem(f, SendType.READY);
						fileP.setText(f.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(null, "请选择支持的文件！(*.doc、"
								+ "*.docx、*.rtf、*.txt、*.mobi、*.pdf)", "错误",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		fBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser c = new JFileChooser();
				c.setMultiSelectionEnabled(false);
				c.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				c.setDialogTitle("请选择推送文件所在的文件夹...");
				c.setApproveButtonText("批量加入");
				c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileP.setText(c.getSelectedFile().getAbsolutePath());
					File[] f = c.getSelectedFile().listFiles();
					for (int i = 0; i < f.length; i++) {
						if (f[i].getName().endsWith(".doc") ||
								f[i].getName().endsWith(".docx") ||
								f[i].getName().endsWith(".rtf") ||
								f[i].getName().endsWith(".txt") ||
								f[i].getName().endsWith(".mobi") ||
								f[i].getName().endsWith(".pdf")) {
							if (f[i].length() > 31457280) {
								break;
							}
							addListItem(f[i], SendType.READY);
						}
					}
				}
			}
		});
		sBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				clear();
				int[] tem = new int[data.size()];
				for (int i = 0; i < tem.length; i++) {
					tem[i] = i;
				}
				if (tem.length <= 0) {
					JOptionPane.showMessageDialog(null, "列表空！", "错误",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				send(tem);
			}
		});
		comboF.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (comboF.getSelectedIndex() == 1) {
					JOptionPane.showMessageDialog(null, "转*.mobi由于种种原因不对外开放。", "提示",
							JOptionPane.WARNING_MESSAGE);
					comboF.setSelectedIndex(0);
				}
			}
		});
		new DropTarget (this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void drop (DropTargetDropEvent dtde) {
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						List<File> list = (List<File>)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
						for(File file: list) {
							if (file.getName().endsWith(".doc") ||
									file.getName().endsWith(".docx") ||
									file.getName().endsWith(".rtf") ||
									file.getName().endsWith(".txt") ||
									file.getName().endsWith(".mobi") ||
									file.getName().endsWith(".pdf")) {
								if (file.length() > 31457280) {
									break;
								}
								addListItem(file, SendType.READY);
							}
						}
						dtde.dropComplete(true);
					} else {
                    dtde.rejectDrop();
					}
				} catch (Exception e) {
					e.printStackTrace();
					(new ErrorUtil(e)).dealWithException();
				}
			}
		});
	}
	
	private static void boundsInit() {
		des0.setBounds(2, 0, 90, 20);
		sp.setBounds(2, 20, 432, 273);
		fileP.setBounds(2, 295, 238, 20);
		cBtn.setBounds(242, 295, 90, 20);
		fBtn.setBounds(334, 295, 100, 20);
		des1.setBounds(2, 317, 135, 20);
		comboF.setBounds(137, 318, 60, 18);
		des2.setBounds(200, 316, 150, 20);
		sBtn.setBounds(344, 316, 90, 20);
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		boundsInit();
		m.setSize(442, 392);
		m.setLocationRelativeTo(null);
		m.setVisible(true);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m.setTitle("Easy Send To Kindle");
		m.setResizable(false);
		FileUtil.loadFile();
	}
	
	public static void addListItem(File file, SendType type) {
		Vector<String> v = new Vector<String>();
		v.add(file.getName());
		v.add(file.getAbsolutePath());
		v.add(bytes2kb(file.length()));
		v.add(type.toString());
		data.add(v);
		table.updateUI();
	}
	
	public void send (int[] item) {
		float total = 0F;
		int last = 0;
		if (item.length <= 0) {
			JOptionPane.showMessageDialog(null, "请选择至少一项！", "错误",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		des2.setText(String.format("状态:%s", "正在推送"));
		for (int i = 0; i < item.length; i++) {
			total += kb2mb(data.get(item[i]).get(2));
			if (total >= 30F) {
				total = 0F;
				String[] file = new String[i - last];
				for (int ii = 0; ii < file.length; ii++) {
					file[ii] = data.get(ii + last).get(1);
					if (file[ii].endsWith(".pdf") && des1.isSelected()) {
						switch(comboF.getSelectedIndex()){
						case 0:
							try {
								FileUtil.pdf2txt(file[ii]);
							} catch (Exception e) {
								e.printStackTrace();
								(new ErrorUtil(e)).dealWithException();
							}
							file[ii] = file[ii].substring(0, file[ii].length() - 4) + ".txt";
							break;
						}
					}
					data.get(ii + last).add(3, SendType.SENDING.toString());
					table.updateUI();
				}
				last = i;
				MailUtil mail = null;
				Result result = null;
				mail = new MailUtil(false);
				result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
				if (!result.isSuccess()) {
					(new ErrorUtil(result)).dealWithResult(); 
					for (int ii = 0; ii < file.length; ii++) {
						data.get(ii + last).add(3, SendType.ERROR.toString());
						table.updateUI();
					}
				} else {
					System.out.println("Send ok!");
					for (int ii = 0; ii < file.length; ii++) {
						data.get(ii + last).add(3, SendType.OK.toString());
						table.updateUI();
					}
				}
			}
		}
		String[] file = new String[item.length - last];
		for (int ii = 0; ii < file.length; ii++) {
			file[ii] = data.get(ii + last).get(1);
			data.get(ii + last).add(3, SendType.SENDING.toString());
			table.updateUI();
		}
		MailUtil mail = null;
		Result result = null;
		mail = new MailUtil(false);
		result = mail.send("Kindle推送邮件", "<p>本邮件是程序自动推送的邮件，请勿回复，谢谢！</p>", file);
		if (!result.isSuccess()) {
			(new ErrorUtil(result)).dealWithResult(); 
			for (int ii = 0; ii < file.length; ii++) {
				data.get(ii + last).add(3, SendType.ERROR.toString());
				table.updateUI();
			}
		} else {
			System.out.println("Send ok!");
			for (int ii = 0; ii < file.length; ii++) {
				data.get(ii + last).add(3, SendType.OK.toString());
				table.updateUI();
			}
		}
		des2.setText(String.format("状态:%s", "完毕"));
	}
	
	public void clear () {
		int i = 0;
		while(true){
			if (i > data.size() - 1) {
				break;
			}
			if (data.get(i).get(3).equals(SendType.OK.toString())) {
				data.remove(i);
				i = 0;
			}
			i++;
		}
		table.updateUI();
	}
	
	public static String bytes2kb(long bytes) {  
        BigDecimal filesize = new BigDecimal(bytes);  
        BigDecimal megabyte = new BigDecimal(1024 * 1024);  
        float returnValue = filesize.divide(megabyte, 4, BigDecimal.ROUND_UP)  
                .floatValue();  
        if (returnValue > 1)  
            return (returnValue + " MB");  
        BigDecimal kilobyte = new BigDecimal(1024);  
        returnValue = filesize.divide(kilobyte, 4, BigDecimal.ROUND_UP)  
                .floatValue();  
        return (returnValue + " KB");  
    }
	
	public static float kb2mb(String kb) {  
		if (kb.endsWith(" KB")) {
			float f = Float.valueOf(kb.substring(0, kb.length() - 3));
			BigDecimal filesize = new BigDecimal(f);  
			BigDecimal megabyte = new BigDecimal(1024);  
			float returnValue = filesize.divide(megabyte, 7, BigDecimal.ROUND_UP)  
					.floatValue();   
			return (returnValue); 
		}
		return Float.valueOf(kb.substring(0, kb.length() - 3));
    }
	
	enum SendType {
		READY(0), SENDING(1), OK(2), ERROR(3);
		
		private int index;
		
		SendType (int index) {
			this.index = index;
		}
		
		public String toString () {
			switch(this.index){
			case 0:
				return "准备完毕";
			case 1:
				return "推送中";
			case 2:
				return "推送成功";
			case 3:
				return "推送失败";
			}
			return "";
		}
		
		public static SendType toEnum(String str) {
			switch(str){
			case "准备完毕":
				return SendType.READY;
			case "推送中":
				return SendType.SENDING;
			case "推送成功":
				return SendType.OK;
			case "推送失败":
				return SendType.ERROR;
			}
			return null;
		}
	}
}
