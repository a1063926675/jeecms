package com.jeecms.common.exception;

import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;

/**
 * Description: 定时任务错误信息类，用于支持定时任务错误的处理。
 * @author: tom
 * @date:   2019年3月8日 下午4:27:33   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class JobExceptionInfo implements ExceptionInfo {

	/**定时任务错误的代码。 */
	private String code = SysOtherErrorCodeEnum.JOB_ERROR.getCode();

	/**定时任务错误对应的默认提示信息。 */
	private String defaultMessage = SysOtherErrorCodeEnum.JOB_ERROR.getDefaultMessage();

	/**定时任务错误对应的原始提示信息。 */
	private String originalMessage= SysOtherErrorCodeEnum.JOB_ERROR.getDefaultMessage();

	/** 当前请求的URL。 */
	private String requestUrl;

	/** 默认的转向（重定向）的URL，默认为空。 */
	private String defaultRedirectUrl = "";

	/**定时任务错误对应的响应数据。 */
	private Object data = new Object();

	/**
	 * Description: 通过非法参数异常的默认提示信息、响应数据构建一个非法参数异常信息对象。
	 *
	 * @param defaultMessage
	 *           定时任务错误的默认提示信息。
	 * 
	 * @param data
	 *           定时任务错误的响应数据。
	 */
	public JobExceptionInfo(String defaultMessage, Object data) {
		this.defaultMessage = defaultMessage;
		this.data = data;
	}
	
	public JobExceptionInfo(String defaultMessage, String code) {
		this.defaultMessage = defaultMessage;
		this.code = code;
	}
	
	public JobExceptionInfo() {
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDefaultMessage() {
		return defaultMessage;
	}

	@Override
	public String getOriginalMessage() {
		return originalMessage;
	}

	@Override
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}

	@Override
	public String getRequestUrl() {
		return requestUrl;
	}

	@Override
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	@Override
	public String getDefaultRedirectUrl() {
		return defaultRedirectUrl;
	}

	@Override
	public void setDefaultRedirectUrl(String defaultRedirectUrl) {
		this.defaultRedirectUrl = defaultRedirectUrl;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

}
