package cn.jxau.soft.dao;

import java.math.BigDecimal;

/**
 * 定义用于计算的bean对象
 * @author gl
 * @version V1.0 2017/7/18
 */
public class CalculatorBean {
	private static CalculatorBean bean;
	
	public static CalculatorBean getInstance() {
		if (bean == null) bean= new CalculatorBean();
		return bean;
	}
	
	/**
	 * 暂时存储的计算结果，其初始化值为0
	 */
	private BigDecimal storedResult = new BigDecimal("0"); 
	/**
	 * 上一次计算操作符
	 */
	private String lastOperator; 
	/**
	 * 上一次，定义为BigDecimal，用于精确计算和小数位保留
	 */
	private BigDecimal lastMemoryResult; 
	/**
	 * 本次计算操作符
	 */
	private String curOperator; 
	/**
	 * 本次的计算结果
	 */
	private BigDecimal curMemorryResult; 
	
	private CalculatorBean() {}

	public String getLastOperator() {
		return lastOperator;
	}

	public void setLastOperator(String lastOperator) {
		this.lastOperator = lastOperator;
	}

	public BigDecimal getLastMemoryResult() {
		return lastMemoryResult;
	}

	public void setLastMemoryResult(BigDecimal lastMemoryResult) {
		this.lastMemoryResult = lastMemoryResult;
	}

	public String getCurOperator() {
		return curOperator;
	}

	public void setCurOperator(String curOperator) {
		this.curOperator = curOperator;
	}

	public BigDecimal getCurMemorryResult() {
		return curMemorryResult;
	}

	public void setCurMemorryResult(BigDecimal curMemorryResult) {
		this.curMemorryResult = curMemorryResult;
	}

	public BigDecimal getStoredResult() {
		return storedResult;
	}

	public void setStoredResult(BigDecimal storedResult) {
		this.storedResult = storedResult;
	}

}
