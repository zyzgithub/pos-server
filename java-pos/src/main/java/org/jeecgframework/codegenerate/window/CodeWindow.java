package org.jeecgframework.codegenerate.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jeecgframework.codegenerate.database.JeecgReadTable;
import org.jeecgframework.codegenerate.generate.CodeGenerate;
import org.jeecgframework.codegenerate.pojo.CreateFileProperty;

@SuppressWarnings(value={"serial", "unused", "rawtypes", "unchecked"})
public class CodeWindow extends JFrame {
	private static String jdField_a_of_type_JavaLangString = "test";
	private static String b = "TestEntity";
	private static String c = "t00_company";
	private static String d = "分公司";
	private static int jdField_a_of_type_Int = 1;
	private static String e = "uuid";
	private static String f = "";
	String[] jdField_a_of_type_ArrayOfJavaLangString = { "uuid", "identity",
			"sequence" };

	public enum CodeType {

		A("", "");
		private CodeType(String label, String code) {
			this.label = label;
			this.code = code;
		}

		private String label;

		private String code;

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}

	public CodeWindow() {
		JPanel localJPanel = new JPanel();
		setContentPane(localJPanel);
		localJPanel.setLayout(new GridLayout(14, 2));
		JLabel localJLabel1 = new JLabel("提示:");
		JLabel localJLabel2 = new JLabel();
		JLabel localJLabel3 = new JLabel("包名（小写）：");
		JTextField localJTextField1 = new JTextField();
		JLabel localJLabel4 = new JLabel("实体类名（首字母大写）：");
		JTextField localJTextField2 = new JTextField();
		JLabel localJLabel5 = new JLabel("表名：");
		JTextField localJTextField3 = new JTextField(20);
		JLabel localJLabel6 = new JLabel("主键生成策略：");
		JComboBox localJComboBox = new JComboBox(
				this.jdField_a_of_type_ArrayOfJavaLangString);
		JLabel localJLabel7 = new JLabel("主键SEQUENCE：(oracle序列名)");
		JTextField localJTextField4 = new JTextField(20);
		JLabel localJLabel8 = new JLabel("功能描述：");
		JTextField localJTextField5 = new JTextField();
		JLabel localJLabel9 = new JLabel("行字段数目：");
		JTextField localJTextField6 = new JTextField();
		localJTextField6.setText(jdField_a_of_type_Int + "");
		ButtonGroup localButtonGroup = new ButtonGroup();
		JRadioButton localJRadioButton1 = new JRadioButton("Table风格(form)");
		localJRadioButton1.setSelected(true);
		JRadioButton localJRadioButton2 = new JRadioButton("Div风格(form)");
		localButtonGroup.add(localJRadioButton1);
		localButtonGroup.add(localJRadioButton2);
		JCheckBox localJCheckBox1 = new JCheckBox("Action");
		localJCheckBox1.setSelected(true);
		JCheckBox localJCheckBox2 = new JCheckBox("Jsp");
		localJCheckBox2.setSelected(true);
		JCheckBox localJCheckBox3 = new JCheckBox("ServiceI");
		localJCheckBox3.setSelected(true);
		JCheckBox localJCheckBox4 = new JCheckBox("ServiceImpl");
		localJCheckBox4.setSelected(true);
		JCheckBox localJCheckBox5 = new JCheckBox("Page");
		localJCheckBox5.setSelected(true);
		JCheckBox localJCheckBox6 = new JCheckBox("Entity");
		localJCheckBox6.setSelected(true);
		localJPanel.add(localJLabel1);
		localJPanel.add(localJLabel2);
		localJPanel.add(localJLabel3);
		localJPanel.add(localJTextField1);
		localJPanel.add(localJLabel4);
		localJPanel.add(localJTextField2);
		localJPanel.add(localJLabel5);
		localJPanel.add(localJTextField3);
		localJPanel.add(localJLabel6);
		localJPanel.add(localJComboBox);
		localJPanel.add(localJLabel7);
		localJPanel.add(localJTextField4);
		localJPanel.add(localJLabel8);
		localJPanel.add(localJTextField5);
		localJPanel.add(localJLabel9);
		localJPanel.add(localJTextField6);
		localJPanel.add(localJCheckBox1);
		localJPanel.add(localJCheckBox2);
		localJPanel.add(localJCheckBox3);
		localJPanel.add(localJCheckBox4);
		localJPanel.add(localJCheckBox5);
		localJPanel.add(localJCheckBox6);
		localJPanel.add(localJRadioButton1);
		localJPanel.add(localJRadioButton2);
		JButton localJButton1 = new JButton("生成");
		localJButton1.addActionListener(new a(this, localJTextField1,
				localJLabel2, localJTextField2, localJTextField3,
				localJTextField5, localJComboBox, localJTextField4,
				localJRadioButton1, localJRadioButton2, localJCheckBox1,
				localJCheckBox2, localJCheckBox3, localJCheckBox4,
				localJCheckBox6));
		JButton localJButton2 = new JButton("退出");
		// localJButton2.addActionListener(new b(this));
		localJPanel.add(localJButton1);
		localJPanel.add(localJButton2);
		setTitle("QDPlat代码生成器[单表模型]-雄志信息");
		setVisible(true);
		setDefaultCloseOperation(3);
		setSize(new Dimension(600, 400));
		setResizable(false);
		setLocationRelativeTo(getOwner());
	}

	public final class a implements ActionListener {
		private CodeWindow paramCodeWindow;
		private JTextField paramPackName;
		private JLabel operateMsg;
		private JTextField paramEntityName;
		private JTextField paramTableName;
		private JTextField paramDescription;
		private JComboBox paramJComboBox;
		private JTextField paramSequence;
		private JRadioButton paramJspTableTemplate;
		private JRadioButton paramJspDivTemplate;
		private JCheckBox actionFlag;
		private JCheckBox paramJCheckBox2;
		private JCheckBox serviceIFlag;
		private JCheckBox serviceImplFlag;
		private JCheckBox entityFlag;

		public a(CodeWindow paramCodeWindow, JTextField paramPackName,
				JLabel paramJLabel, JTextField paramEntityName,
				JTextField paramTableName, JTextField paramDescription,
				JComboBox paramJComboBox, JTextField paramSequence,
				JRadioButton paramJspTableTemplate,
				JRadioButton paramJspDivTemplate, JCheckBox actionFlag,
				JCheckBox paramJCheckBox2, JCheckBox serviceIFlag,
				JCheckBox serviceImplFlag, JCheckBox entityFlag) {
			this.paramCodeWindow = paramCodeWindow;
			this.paramPackName = paramPackName;
			this.operateMsg = paramJLabel;
			this.paramEntityName = paramEntityName;
			this.paramTableName = paramTableName;
			this.paramDescription = paramDescription;
			this.paramJComboBox = paramJComboBox;
			this.paramSequence = paramSequence;
			this.paramJspTableTemplate = paramJspTableTemplate;
			this.paramJspDivTemplate = paramJspDivTemplate;
			this.actionFlag = actionFlag;
			this.paramJCheckBox2 = paramJCheckBox2;
			this.serviceIFlag = serviceIFlag;
			this.serviceImplFlag = serviceImplFlag;
			this.entityFlag = entityFlag;
		}

		// 执行代码生成
		public void actionPerformed(ActionEvent paramActionEvent) {
			// 生成前、必填信息校验
			if ("".equals(paramPackName.getText())) {
				this.operateMsg.setForeground(Color.red);
				this.operateMsg.setText("包名不能为空！");
				return;
			}
			if ("".equals(this.paramEntityName.getText())) {
				this.operateMsg.setForeground(Color.red);
				this.operateMsg.setText("实体类名不能为空！");
				return;
			}
			if ("".equals(this.paramTableName.getText())) {
				this.operateMsg.setForeground(Color.red);
				this.operateMsg.setText("表名不能为空！");
				return;
			}

			String selectedItem = ((String) this.paramJComboBox
					.getSelectedItem());
			if (selectedItem.equals("sequence"))
				if ("".equals(this.paramSequence.getText())) {
					this.operateMsg.setForeground(Color.red);
					this.operateMsg.setText("主键生成策略为sequence时，序列号不能为空！");
					return;
				}

			if ("".equals(this.paramDescription.getText())) {
				this.operateMsg.setForeground(Color.red);
				this.operateMsg.setText("描述不能为空！");
				return;
			}
			CreateFileProperty localCreateFileProperty = new CreateFileProperty();
			if (this.paramJspTableTemplate.isSelected())// jspTableTemplate
				localCreateFileProperty.setJspMode("01");
			if (this.paramJspDivTemplate.isSelected())
				localCreateFileProperty.setJspMode("02");
			if (this.actionFlag.isSelected())
				localCreateFileProperty.setActionFlag(true);
			if (this.paramJCheckBox2.isSelected())
				localCreateFileProperty.setJspFlag(true);
			if (this.serviceIFlag.isSelected())
				localCreateFileProperty.setServiceIFlag(true);
			if (this.serviceImplFlag.isSelected())
				localCreateFileProperty.setServiceImplFlag(true);
			if (this.entityFlag.isSelected())
				localCreateFileProperty.setEntityFlag(true);
			try {
				boolean bool = new JeecgReadTable()
						.checkTableExist(paramTableName.getText());
				if (bool) {
					new CodeGenerate(paramPackName.getText(),
							paramEntityName.getText(),
							paramTableName.getText(),
							paramDescription.getText(),
							localCreateFileProperty, paramSequence.getText(), f)
							.generateToFile();
					this.operateMsg.setForeground(Color.red);
					this.operateMsg.setText("成功生成增删改查->功能：" + e);
				} else {
					this.operateMsg.setForeground(Color.red);
					this.operateMsg.setText("表[" + paramTableName.getText()
							+ "] 在数据库中，不存在");
				}
			} catch (Exception localException) {
				this.operateMsg.setForeground(Color.red);
				this.operateMsg.setText(localException.getMessage());
				localException.printStackTrace();
			}
		}
	}

	public static void main(String[] paramArrayOfString) {
		try {
			new CodeWindow().pack();
		} catch (Exception localException) {
			System.out.println(localException.getMessage());
		}
	}
}

/*
 * Location:
 * E:\Workspace\jeecg-framework-3.2.0.RELEASE\jeecg-v3-simple\WebRoot\WEB
 * -INF\lib\org.jeecgframework.codegenerate.jar Qualified Name:
 * org.jeecgframework.codegenerate.window.CodeWindow JD-Core Version: 0.6.0
 */