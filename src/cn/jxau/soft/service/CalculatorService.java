package cn.jxau.soft.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cn.jxau.soft.dao.CalculatorBean;


/**
 * 定义封装计算逻辑业务的对象
 * @author gl
 * @version V1.0 2017/7/18
 */
public class CalculatorService {
	private static CalculatorService service;
	
	public static CalculatorService getInstance() {
		if (service == null) service = new CalculatorService();
		return service;
	}
	
	private CalculatorBean bean = CalculatorBean.getInstance(); //定义CalculatorBean单例对象
	private final StringBuffer exp = new StringBuffer(); //定义存放表达式的StringBuffer对象
	private final StringBuffer input = new StringBuffer(); //定义存放输入数字的StringBuffer对象
	
	private String sqrtExp = "sqrt({0})";
	private String reciprocExp = "reciproc({0})";
	private DecimalFormat decimalFormat = new DecimalFormat("#.###############");
	private boolean flag = true;
	private boolean reInputed = false;
	
	private CalculatorService() {}
	
	
	/**
	 * 用于封装为输入数添加或清除负号的业务逻辑方法
	 * @param inputLab 当前输入label
	 */
	public void addOrRemoveMinus(JLabel inputLab) {
		String inputNum = inputLab.getText();
		inputLab.setText(new BigDecimal(inputNum).negate().toString());
	}
	
	/**
	 * 用于封装开根号的业务逻辑方法
	 * @param inputLab 当前输入label
	 * @param expLab 表达式显示label
	 */
	public void sqrt(JLabel inputLab, JLabel expLab) {
		String inputStr = inputLab.getText();
		String expStr = expLab.getText();
		
		expStr = removeSqrtAndReciproc(expStr);
		
		try {
			if (("".equals(expStr) && !"".equals(inputStr)) ||
					(expStr.matches("(\\d*\\.?\\d*(\\+|\\-|\\*|\\/|%|=))+")	&& !"".equals(inputStr))) 
			{ //表达式label中 表达式文本为 ""(空串)时 或者 表达式label中 表达式文本为 (12+, 12.32-, 12+45-, 12+56.025-56+, ...)这种形式时
				exp.append(MessageFormat.format(sqrtExp, inputStr));
				bean.setCurMemorryResult(new BigDecimal(inputStr));
				inputLab.setText(decimalFormat.format(
						Math.sqrt(new BigDecimal(inputStr).doubleValue()))
						);
				expLab.setText(exp.toString());
				
				flag = false;
			}
		} catch (ArithmeticException ae) {
			clearResultAndOperator(inputLab, expLab);
			JOptionPane.showMessageDialog(null, ae.getMessage());
			return;
		}
	}
	
	/**
	 * 用于封装1/x号的业务逻辑方法
	 * @param inputLab 当前输入label
	 * @param expLab 表达式显示label
	 */
	public void onePartX(JLabel inputLab, JLabel expLab) {
		String inputStr = inputLab.getText();
		String expStr = expLab.getText();
		
		expStr = removeSqrtAndReciproc(expStr);
		
		try {
			if (("".equals(expStr) && !"".equals(inputStr)) ||
					(expStr.matches("(\\d*\\.?\\d*(\\+|\\-|\\*|\\/|%|=))+")	&& !"".equals(inputStr))) 
			{ //表达时label中 表达式文本为 ""(空串)时 或者 表达时label中 表达式文本为 (12+, 12.32-, 12+45-, 12+56.025-56+, ...)这种形式时
				exp.append(MessageFormat.format(reciprocExp, inputStr));
				bean.setCurMemorryResult(new BigDecimal("1").divide(new BigDecimal(inputStr), 10, RoundingMode.HALF_UP));
				inputLab.setText(bean.getCurMemorryResult().toString());
				expLab.setText(exp.toString());
				
				flag = false;
			}
		} catch (ArithmeticException ae) {
			clearResultAndOperator(inputLab, expLab);
			JOptionPane.showMessageDialog(null, ae.getMessage());
			return;
		}
	}
	
	/**
	 * 用于封装清除所有结果和操作符键的业务逻辑方法
	 * @param value 按钮上的文本
	 * @param inputLab 输入label
	 */
	public void clearResultAndOperator(JLabel inputLab, JLabel expLab) {
		input.delete(0, input.length());
		inputLab.setText("0");
		exp.delete(0, exp.length());
		expLab.setText("");
		
		bean.setLastMemoryResult(new BigDecimal("0"));
		bean.setLastOperator("");
	}
	
	
	/**
	 * 用于封装清除输入结果键的业务逻辑方法
	 * @param inputLab 当前输入label
	 */ 
	public void clearInputResult(JLabel inputLab) {
		inputLab.setText("0");
		input.delete(0, input.length());
	}
	
	/**
	 * 用于封装回退键的业务逻辑方法
	 * @param inputLab 当前输入label
	 */
	public void backspace(JLabel inputLab) {
		String inputNum = inputLab.getText();
		if (inputNum.length() > 1) {
			if (inputNum.length() == 2 && inputNum.matches("\\-\\d")) {
				inputLab.setText("0");
				input.delete(0, 2);
			} else {
				inputLab.setText(inputNum.substring(0, inputNum.length()-1));
				input.delete(0, input.length());
				input.append(inputNum.substring(0, inputNum.length()-1));
			}
		} else if (inputNum.length() == 1 && ! "0".equals(inputNum)) {
			inputLab.setText("0");
			input.delete(0, 1);
		}
	}
	
	/**
	 * 用于封装清除缓存数据键的业务逻辑方法
	 */
	public void clearStoredNum() {
		bean.setStoredResult(new BigDecimal("0"));
	}
	
	/**
	 * 用于封装读取缓存数据键的业务逻辑方法
	 * 
	 */
	public BigDecimal readStoredNum() {
		return bean.getStoredResult();
	}
	
	/**
	 * 用于封装缓存键的业务逻辑方法
	 * 将当前输入数据缓存到CalculatorBean中的storedNum属性中
	 * @param storedNum 要缓存的storedNum
	 */
	public void reStoredNum(BigDecimal storedNum) {
		bean.setStoredResult(storedNum);
	}
	
	/**
	 * 用于封装缓存相加键的业务逻辑方法
	 * 将缓存数据加上输入数据，再将该数据缓存
	 * @param inputNum 要和缓存num相加的数
	 */
	public void storedNumPlus(BigDecimal inputNum) {
		bean.setStoredResult(bean.getStoredResult().add(inputNum));
	}
	
	/**
	 * 用于封装缓存相减键的业务逻辑方法
	 * 将缓存数据减去输入数据，再将该数据缓存
	 * @param inputNum 要和缓存num相减的数
	 */
	public void storedNumSubstract(BigDecimal inputNum) {
		bean.setStoredResult(bean.getStoredResult().subtract(inputNum));
	}
	
	/**
	 * 输入非0数字 或者 . 时的正则验证
	 * @param value 按钮上的文本
	 * @param inputLab 输入label
	 */
	public void numAndPointPressed(String value, JLabel inputLab) {
		String appendStr = input.toString() + value;
		if (appendStr.matches("\\d*\\.?\\d*")) {
			inputLab.setText(appendStr);
			input.append(value);
			
			reInputed = true;
		}
	}

	/**
	 * 输入数字0 时的正则验证
	 * @param value 按钮上的文本
	 * @param inputLab 输入label
	 */
	public void zeronNumPressed(String value, JLabel inputLab) {
		String appendStr = input.toString() + "0";
		if ((appendStr.matches("\\d?")&&!appendStr.matches("00"))
				|| (appendStr.matches("\\d+.")&&appendStr.matches("00.")) 
					|| appendStr.matches("\\d+.\\d+")&& !appendStr.matches("00.\\d+")) {
			inputLab.setText(appendStr);
			input.append(value);
			
			reInputed = true;
		}		
	}
	
	/**
	 * 算术运算符按钮时的验证
	 * @param value 按钮上的文本
	 * @param inputLab 输入label
	 * @param expLab 表达式label
	 */
	public void operatorPressed(String value, JLabel inputLab, JLabel expLab) {
		String expStr = expLab.getText();
		String inputStr = inputLab.getText();
		
		expStr = removeSqrtAndReciproc(expStr);
		
		if ("".equals(expStr) && !"".equals(inputStr)) { //若表达时label中 表达式文本为 ""(空串)时
			exp.append(inputStr);
			exp.append(value);
			
			bean.setCurMemorryResult(new BigDecimal(inputStr));
			clearInputResult(inputLab);

			expLab.setText(exp.toString());
		} else if(expStr.matches("(\\d*\\.?\\d*(\\+|\\-|\\*|\\/|%|=))+") 
					&& !"".equals(inputStr)) { //若表达时label中 表达式文本为 (12+, 12.32-, 12+45-, 12+56.025-56+, ...)这种形式时
			if (inputStr.equals(bean.getCurMemorryResult().toString()) && ! reInputed) return;

			if (flag) exp.append(inputStr);
			exp.append(value);
			
			flag = true;
			bean.setLastMemoryResult(bean.getCurMemorryResult());
			try {
				if ("+".equals(bean.getLastOperator())) {
					bean.setCurMemorryResult(bean.getLastMemoryResult().add(new BigDecimal(inputStr)));
				} else if ("-".equals(bean.getLastOperator())) {
					bean.setCurMemorryResult(bean.getLastMemoryResult().subtract(new BigDecimal(inputStr)));
				} else if ("*".equals(bean.getLastOperator())) {
					bean.setCurMemorryResult(bean.getLastMemoryResult().multiply(new BigDecimal(inputStr)));
				} else if ("/".equals(bean.getLastOperator())) {
					bean.setCurMemorryResult(bean.getLastMemoryResult().divide(new BigDecimal(inputStr), 10, RoundingMode.HALF_UP));
				} else if ("%".equals(bean.getLastOperator())) {
					bean.setCurMemorryResult(bean.getLastMemoryResult().remainder(new BigDecimal(inputStr)));
				} 
				
				reInputed = false;
				
				if ("=".equals(value)) {
					clearResultAndOperator(inputLab, expLab);					
					inputLab.setText(bean.getCurMemorryResult().toString());
					return;
				}
			} catch(ArithmeticException ae) {
				clearResultAndOperator(inputLab, expLab);
				JOptionPane.showMessageDialog(null, ae.getMessage());
				return;
			}
			clearInputResult(inputLab);
			
			expLab.setText(exp.toString());
			inputLab.setText(bean.getCurMemorryResult().toString());
		}
		
		bean.setLastOperator(value);
	}

	/**
	 * 移除表达式中sqrt({0})和reciproc({0})
	 * @param expStr 表达式
	 */
	private String removeSqrtAndReciproc(String expStr) {
		if (expStr.indexOf("sqrt") != -1) {
			expStr = expStr.replaceAll("sqrt\\(\\d+\\.?\\d?\\)", "");
		}
		if (expStr.indexOf("reciproc") != -1) {
			expStr = expStr.replaceAll("reciproc\\(\\d+\\.?\\d?\\)", "");
		}
		
		return expStr;
	}
	
}
