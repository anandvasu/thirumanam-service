package com.thirumanam.model;

public class ResetPasswordResponse extends Response {

	private boolean limitExceed;
	private boolean codeMatched;

	public boolean isCodeMatched() {
		return codeMatched;
	}

	public void setCodeMatched(boolean codeMatched) {
		this.codeMatched = codeMatched;
	}

	public boolean isLimitExceed() {
		return limitExceed;
	}

	public void setLimitExceed(boolean limitExceed) {
		this.limitExceed = limitExceed;
	}
}