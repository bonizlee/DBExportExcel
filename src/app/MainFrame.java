package app;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	
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
	
	JTextField textPfDb;
	JTextField textHbDb;

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
		pbutton.add(btnQueryPF);

		pfPanel.add(plabel);
		pfPanel.add(pDB);
		pfPanel.add(pbutton);
		// ---排放统计结束---
		
		//---黄标车统计开始--
		JPanel hlabel = new JPanel();
		hlabel.add(new JLabel("统计黄标车情况"));
		JPanel hDB = new JPanel();
		hDB.add(new JLabel("请输入黄标统计数据库日期"));
		textHbDb = new JTextField(20);
		hDB.add(textHbDb);
		hbPanel.add(hlabel);
		hbPanel.add(hDB);

		//---淘汰清单
		JPanel ttPanel = new JPanel();
		ttPanel.add(new JLabel("文件名："));
		JTextField textTTFile = new JTextField("黄标车淘汰数据清单");
		btnQueryTT = new JButton("导出已淘汰清单");
		ttPanel.add(textTTFile);
		ttPanel.add(btnQueryTT);		
		hbPanel.add(ttPanel);
		
		//-----M状态清单
		JPanel mPanel = new JPanel();
		mPanel.add(new JLabel("文件名："));
		JTextField textMFile = new JTextField("黄标车强制注销未淘汰数据清单");
		btnQueryM = new JButton("导出强制注销未淘汰清单");
		mPanel.add(textMFile);
		mPanel.add(btnQueryM);		
		hbPanel.add(mPanel);
		
		//---剩余黄标车
		JPanel syPanel = new JPanel();
		syPanel.add(new JLabel("文件名："));
		JTextField textSyFile = new JTextField("剩余黄标车数据清单");
		btnQuerySY = new JButton("导出剩余黄标车清单");
		syPanel.add(textSyFile);
		syPanel.add(btnQuerySY);		
		hbPanel.add(syPanel);
		
		//---黄标车统计结束--

		//----状态栏，备用
		JPanel pstatus = new JPanel();
		pstatus.add(status1);
		pstatus.add(status2);
		pstatus.add(status3);
		//---状态栏
		
		btnQueryPF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!check(textPfDb))
					return;
				String dbname = textPfDb.getText();
				String[] selcity;
				selcity = getSelectCities();
				Thread t = new Thread(new QueryThread(QueryType.PF,dbname, selcity, null));
				t.start();
			}
		});
		
		btnQueryTT.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!check(textHbDb))
					return;
				String dbname=textHbDb.getText();
				String[] selcity;
				selcity = getSelectCities();
				String filename=String.format("截止至%s_%s", dbname,textTTFile.getText());				
				Thread t = new Thread(new QueryThread(QueryType.TT,dbname, selcity, filename));
				t.start();
			}
		});
		
		btnQueryM.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!check(textHbDb))
					return;
				String dbname=textHbDb.getText();
				String[] selcity;
				selcity = getSelectCities();
				String filename=String.format("截止至%s_%s", dbname,textMFile.getText());				
				Thread t = new Thread(new QueryThread(QueryType.M,dbname, selcity, filename));
				t.start();				
			}
		});
		
		btnQuerySY.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!check(textHbDb))
					return;
				String dbname=textHbDb.getText();
				String[] selcity;
				selcity = getSelectCities();
				String filename=String.format("截止至%s_%s", dbname,textSyFile.getText());				
				Thread t = new Thread(new QueryThread(QueryType.SY,dbname, selcity, filename));
				t.start();
				
			}
		});

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
		if(textfield==null||textfield.getText().equals(""))
			return false;
		else
			return true;
	}
}
