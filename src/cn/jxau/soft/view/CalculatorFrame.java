package cn.jxau.soft.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import cn.jxau.soft.service.CalculatorService;

/**
 * 定义计算器主界面
 * @author gl
 * @version V1.0 2017/7/18
 */
public class CalculatorFrame extends JFrame {
	private static final long serialVersionUID = 643953261268968528L;
	private final int WIDTH = 250; //定义窗体宽度
	private final int HEIGHT = 320; //定义窗体长度
	
	private final Font middleFont = new Font("Seria", Font.BOLD, 15);
	
	private JPanel topPane = new JPanel(); //定义放置菜单  和   标记 的面板
	
	private JMenuBar topMenuBar = new JMenuBar(); //定义窗体菜单栏条
	private JMenu menu = new JMenu("菜单"); //定义窗体菜单栏
	private JMenuItem helpMenuItem = new JMenuItem("帮助"); //定义窗体子菜单栏
	
	private JPanel labsPane = new JPanel(); //定义放置 表达式显示标记 和 输入显示标记 的面板
	private final JLabel expLab = new JLabel("");
	
	private final JLabel inputLab = new JLabel("");
	
	
	private JPanel btnsPane = new JPanel(); //定义放置功能按钮的面板
	private JButton[] btns = new JButton[28];
	/**
	 * 0:MC 1:MR, 2:MS, 3:M+, 4:M-
	 * 5:←  6:CE  7:C   8:±   9:√
	 * 10:'7' 11:'8' 12:'9' 13:/ 14:%
	 * 15:'4' 16:'5' 17:'6' 18:* 19:⅟x
	 * 20:'1' 21:'2' 22:'3' 23:- 24:=
	 * 25:'0' 26:. 27:+
	 */
	private String[] btnSign = new String[] {
		"MC", "MR", "MS", "M+", "M-", 
		"←", "CE", "C", "±", "√", 
		"7", "8", "9", "/", "%", 
		"4", "5", "6", "*", "⅟x", 
		"1", "2", "3", "-", "=", 
		"0",      ".", "+"
	};
	
	/**
	 * CalculatorFrame的无参构造方法
	 */
	public CalculatorFrame() {
		this.setTitle("简易计算器");
		this.setIconImage(new ImageIcon("calculator.jpg").getImage());
		this.setBounds(new Rectangle(getLeftTopPoint(), new Dimension(WIDTH, HEIGHT)));
		this.setLayout(new BorderLayout());
		
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		
		topPane.setLayout(new BorderLayout());
		menu.add(helpMenuItem);
		topMenuBar.add(menu);
		topPane.add(topMenuBar, BorderLayout.NORTH);

		topPane.add(labsPane);
		labsPane.setLayout(new BorderLayout());
		labsPane.setPreferredSize(new Dimension(0, 60));
		labsPane.setBorder(BorderFactory.createLineBorder(Color.gray));
		expLab.setFont(middleFont);
		expLab.setHorizontalAlignment(SwingUtilities.RIGHT);
		labsPane.add(expLab);
		inputLab.setFont(middleFont);
		inputLab.setHorizontalAlignment(SwingUtilities.RIGHT);
		labsPane.add(inputLab, BorderLayout.SOUTH);
		
		GridBagLayout baglayout = new GridBagLayout();
		btnsPane.setLayout(baglayout);
		GridBagConstraints c = baglayout.getConstraints(btnsPane);
		
		for (int i=0; i<btns.length; i++) {
			btns[i] = new JButton(btnSign[i]);
			
			c.ipadx=0; c.ipady=10;
			
			if (! (i<7 || i==8 || i==9 || i==19)) { //增大单字符的水平宽度
				c.ipadx=5;
			}
			
			if (i==24) { //添加等于号
				c.ipady=42; addComponent(btnsPane, btns[i], c, i%5, (i+1)/5-1, 1, 2);
			} else if ((i+1)%5==0) { //添加数字'0'
				addComponent(btnsPane, btns[i], c, i%5, (i+1)/5-1, 1, 1);
			} else if (i==25)  { //添加小数点'.'
				c.ipadx=50; addComponent(btnsPane, btns[i], c, i%5, (i+1)/5, 2, 1); 
			} else if (i>=26) { //添加加号
				addComponent(btnsPane, btns[i], c, i%5+1, (i+1)/5, 1, 1);
			} else { //添加其他
				addComponent(btnsPane, btns[i], c, i%5, (i+1)/5, 1, 1);
			}
		}
	
		contentPane.setLayout(new BorderLayout());
		contentPane.add(topPane, BorderLayout.NORTH);
		contentPane.add(btnsPane);
		
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addListener();
	}
	
	/**
	 * 为按钮添加监听事件
	 */
	private void addListener() {
		final CalculatorService service = CalculatorService.getInstance();
		
		/**
		 * 0:MC 1:MR, 2:MS, 3:M+, 4:M-
		 * 5:←  6:CE  7:C   8:±   9:√
		 * 10:'7' 11:'8' 12:'9' 13:/ 14:%
		 * 15:'4' 16:'5' 17:'6' 18:* 19:⅟x
		 * 20:'1' 21:'2' 22:'3' 23:- 24:=
		 * 25:'0' 26:. 27:+
		 */
		/**
		 * MC
		 */
		btns[0].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.clearStoredNum();
			}
		});
		
		/**
		 * MR
		 */
		btns[1].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				inputLab.setText(service.readStoredNum().toString());
			}
		});
		
		/**
		 * MS
		 */
		btns[2].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.reStoredNum(new BigDecimal(inputLab.getText()));
			}
		});
		
		/**
		 * M+
		 */
		btns[3].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.storedNumPlus(new BigDecimal(inputLab.getText()));
			}
		});
		
		/**
		 * M-
		 */
		btns[4].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.storedNumSubstract(new BigDecimal(inputLab.getText()));
			}
		});
		
		/**
		 * ←
		 */
		btns[5].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.backspace(inputLab);
			}
		});
		
		/**
		 * CE
		 */
		btns[6].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.clearInputResult(inputLab);
			}
		});
		
		/**
		 * C
		 */
		btns[7].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.clearResultAndOperator(inputLab, expLab);
			}
		});
		
		/**
		 * ±
		 */
		btns[8].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.addOrRemoveMinus(inputLab);
			}
		});
		
		/**
		 * √
		 */
		btns[9].addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				service.sqrt(inputLab, expLab);
			}
		});
		
		/**
		 * '7'
		 */
		btns[10].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("7", inputLab);
			}
		});
		
		/**
		 * '8'
		 */
		btns[11].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("8", inputLab);
			}
		});
		
		/**
		 * '9'
		 */
		btns[12].addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("9", inputLab);
			}
		});
		
		/**
		 * /
		 */
		btns[13].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("/", inputLab, expLab);
			}
		});
		
		/**
		 * %
		 */
		btns[14].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("%", inputLab, expLab);
			}
		});
		
		/**
		 * '4'
		 */
		btns[15].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("4", inputLab);
			}
		});
		
		/**
		 * '5'
		 */
		btns[16].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("5", inputLab);
			}
		});
		
		/**
		 * '6'
		 */
		btns[17].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("6", inputLab);
			}
		});
		
		/**
		 * *
		 */
		btns[18].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("*", inputLab, expLab);
			}
		});
		
		/**
		 * ⅟x
		 */
		btns[19].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.onePartX(inputLab, expLab);
			}
		});
		
		/**
		 * '1'
		 */
		btns[20].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("1", inputLab);
			}
		});
		
		/**
		 * '2'
		 */
		btns[21].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("2", inputLab);
			}
		});
		
		/**
		 * '3'
		 */
		btns[22].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed("3", inputLab);
			}
		});
		
		/**
		 * -
		 */
		btns[23].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("-", inputLab, expLab);
			}
		});
		
		/**
		 * =
		 */
		btns[24].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("=", inputLab, expLab);
			}
		});
		
		/**
		 * '0'，特殊正则验证
		 */
		btns[25].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.zeronNumPressed("0", inputLab);
			}
		});
		
		/**
		 * .
		 */
		btns[26].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.numAndPointPressed(".", inputLab);
			}
		});
		
		/**
		 * +
		 */
		btns[27].addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				service.operatorPressed("+", inputLab, expLab);
			}
		});
		
	}

	/**
	 * 获得基于屏幕垂直水平居中的左上定位点
	 * @return 左上定位点
	 */
	private Point getLeftTopPoint() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		return new Point((int) (d.getWidth()-WIDTH)/2, (int) (d.getHeight()-HEIGHT)/2);
	}
	
	/**
	 * 向父组件中添加组件
	 * @param container 父组件
	 * @param item 要添加的组件
	 * @param c 父组件的布局类型约束
	 * @param x 左上点的x轴坐标
	 * @param y 左上点的y轴坐标
	 * @param width 组件在x上要占的格数
	 * @param height 组件在以y上要占的格数
	 */
	private void addComponent(JComponent container, JComponent item, 
			GridBagConstraints c, int x, int y, int width, int height) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		container.add(item, c);
	}
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CalculatorFrame();
			}
		});
	}
	
}
