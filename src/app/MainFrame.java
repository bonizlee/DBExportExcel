package app;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1729233823098776690L;
	static final String[] cities = CityCode.getCities();

	JLabel status1 = new JLabel();
	JLabel status2 = new JLabel();
	JLabel status3 = new JLabel();

	JList<String> jCity;
	JCheckBox chkSummary;
	JButton btnQueryPF;
	JButton btnQueryTT;
	JButton btnQueryM;
	JButton btnQuerySY;
	JButton btnQueryIN;

	JTextField textPfDb;
	JTextField textHbDb;
	JTextField textMFile;
	JTextField textTTFile;
	JTextField textSyFile;

	public MainFrame() {
		Container cp = getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));//
		setTitle("导出Excel工具");

		JSplitPane jsp = new JSplitPane();// 创建分隔面板
		cp.add(jsp);
		JTabbedPane jtp = new JTabbedPane();// 创建选项面板

		JPanel hbPanel = new JPanel(new VFlowLayout());// 黄标统计
		JPanel pfPanel = new JPanel(new VFlowLayout());// 排放统计
		jtp.addTab("黄标统计", hbPanel);
		jtp.addTab("排放统计", pfPanel);

		jsp.setRightComponent(jtp);

		// ---Cities----
		JPanel pPf = new JPanel(new VFlowLayout());
		jCity = new JList<String>(CityCode.getCities());
		jCity.setFixedCellWidth(100);
		pPf.add(new JLabel("请选择一个或多个地市"));
		pPf.add(jCity);
		chkSummary = new JCheckBox("查询全省汇总数据");
		pPf.add(chkSummary);
		jsp.setLeftComponent(pPf);
		// ---Cities---

		// ----排放统计开始---
		JPanel plabel = new JPanel();
		plabel.add(new JLabel("统计排放数值"));
		JPanel pDB = new JPanel();
		pDB.add(new JLabel("请输入排放统计数据库日期"));
		textPfDb = new JTextField(20);
		pDB.add(textPfDb);

		JPanel pbutton = new JPanel();
		btnQueryPF = new JButton("导出排放数据");
		btnQueryPF.setActionCommand(QueryType.PF.toString());
		btnQueryPF.addActionListener(this);
		pbutton.add(btnQueryPF);
		
		
		btnQueryIN=new JButton("迁入类排放数据");
		btnQueryIN.setActionCommand(QueryType.IN.toString());
		btnQueryIN.addActionListener(this);
		pbutton.add(btnQueryIN);

		pfPanel.add(plabel);
		pfPanel.add(pDB);
		pfPanel.add(pbutton);
		// ---排放统计结束---

		// ---黄标车统计开始--
		JPanel hlabel = new JPanel();
		hlabel.add(new JLabel("统计黄标车情况"));
		JPanel hDB = new JPanel();
		hDB.add(new JLabel("请输入黄标统计数据库日期"));
		textHbDb = new JTextField(20);
		hDB.add(textHbDb);
		hbPanel.add(hlabel);
		hbPanel.add(hDB);

		// ---淘汰清单
		JPanel ttPanel = new JPanel();
		ttPanel.add(new JLabel("文件名："));
		textTTFile = new JTextField("黄标车淘汰数据清单");
		btnQueryTT = new JButton("导出已淘汰清单");
		btnQueryTT.setActionCommand(QueryType.TT.toString());
		btnQueryTT.addActionListener(this);
		ttPanel.add(textTTFile);
		ttPanel.add(btnQueryTT);
		hbPanel.add(ttPanel);

		// -----M状态清单
		JPanel mPanel = new JPanel();
		mPanel.add(new JLabel("文件名："));
		textMFile = new JTextField("黄标车强制注销未淘汰数据清单");
		btnQueryM = new JButton("导出强制注销未淘汰清单");
		btnQueryM.setActionCommand(QueryType.M.toString());
		btnQueryM.addActionListener(this);
		mPanel.add(textMFile);
		mPanel.add(btnQueryM);
		hbPanel.add(mPanel);

		// ---剩余黄标车
		JPanel syPanel = new JPanel();
		syPanel.add(new JLabel("文件名："));
		textSyFile = new JTextField("剩余黄标车数据清单");
		btnQuerySY = new JButton("导出剩余黄标车清单");
		btnQuerySY.setActionCommand(QueryType.SY.toString());
		btnQuerySY.addActionListener(this);
		syPanel.add(textSyFile);
		syPanel.add(btnQuerySY);
		hbPanel.add(syPanel);

		// ---黄标车统计结束--

		// ----状态栏，备用
		JPanel pstatus = new JPanel();
		pstatus.add(status1);
		pstatus.add(status2);
		pstatus.add(status3);
		// ---状态栏
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocation(600, 250);
		setVisible(true);
	}

	protected String[] getSelectCities() {
		String[] selcity;
		int[] selindex = jCity.getSelectedIndices();
		if (chkSummary.isSelected()) {
			selcity = new String[] { "Summary" };
		} else {
			selcity = new String[selindex.length];
			for (int i = 0; i < selindex.length; i++) {
				selcity[i] = CityCode.getPlate(cities[selindex[i]]);
			}
		}
		return selcity;
	}

	private boolean check(JTextField textfield) {
		if (textfield == null || textfield.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "请填入数据库日期", "错误", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (jCity.getSelectedIndices().length == 0 && !chkSummary.isSelected()) {
			JOptionPane.showMessageDialog(this, "请选择地市或勾选汇总", "错误", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String filename = "";
		QueryType querytype = QueryType.getType(e.getActionCommand());
		String dbname = "";
		
		String[] selcity = getSelectCities();
		switch (querytype) {
		case PF:
			if(!check(textPfDb)) return;
			dbname = textPfDb.getText();
			filename=String.format("截止至%s排放分类统计", dbname);
			break;
		case TT:
			if (!check(textHbDb)) return;
			dbname = textHbDb.getText();
			filename = String.format("截止至%s_%s", dbname, textTTFile.getText());
			break;
		case SY:
			if (!check(textHbDb)) return;
			dbname = textHbDb.getText();
			filename = String.format("截止至%s_%s", dbname, textSyFile.getText());
			break;
		case M:
			if (!check(textHbDb)) return;
			dbname = textHbDb.getText();
			filename = String.format("截止至%s_%s", dbname, textMFile.getText());
			break;			
		case IN:
			if(!check(textPfDb)) return;
			dbname = textPfDb.getText();
			filename=String.format("截止至%s迁入按排放分类统计", dbname);
			break;
		default:
			return;			
		}
		Thread t = new Thread(new QueryThread(querytype, dbname, selcity, filename));
		t.start();
	}
}
