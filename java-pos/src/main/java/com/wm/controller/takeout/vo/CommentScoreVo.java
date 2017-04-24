package com.wm.controller.takeout.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CommentScoreVo {

	private int total;
	private double score;

	public int getTotal() {
		return total;
	}

	public void setTotal(BigInteger total) {
		this.total = total.intValue();
	}

	public double getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score.doubleValue();
	}

}
