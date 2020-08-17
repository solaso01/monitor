package com.sjzy.data.monitor.common.error;


public interface CommonError {
	
	public int getErrCode();
	
	public String getErrMsg();
	
	public CommonError setErrMsg(String errMsg);

}
