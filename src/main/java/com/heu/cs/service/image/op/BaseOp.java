package com.heu.cs.service.image.op;

import com.heu.cs.service.image.ClientConfig;
import com.heu.cs.service.image.http.AbstractImageHttpClient;
import com.heu.cs.service.image.sign.Credentials;

/**
 * @author chengwu 
 * 封装目录和文件共同的操作
 */
public abstract class BaseOp {
	// 配置信息
	protected ClientConfig config;
	// 鉴权信息
	protected Credentials cred;
	// http请求发送对象
	protected AbstractImageHttpClient httpClient;

	public BaseOp(ClientConfig config, Credentials cred, AbstractImageHttpClient httpClient) {
		super();
		this.config = config;
		this.cred = cred;
		this.httpClient = httpClient;
	}
	
	public void setConfig(ClientConfig config) {
		this.config = config;
	}

	public void setCred(Credentials cred) {
		this.cred = cred;
	}

	public void setHttpClient(AbstractImageHttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
