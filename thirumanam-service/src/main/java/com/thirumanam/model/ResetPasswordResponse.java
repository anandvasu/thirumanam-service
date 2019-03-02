package com.thirumanam.model;

public class ResetPasswordResponse extends Response {

	private boolean limitExceed;

	public boolean isLimitExceed() {
		return limitExceed;
	}

	public void setLimitExceed(boolean limitExceed) {
		this.limitExceed = limitExceed;
	}
}
