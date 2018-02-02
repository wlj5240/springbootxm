package com.itopener.zuul.ratelimiter.spring.boot.common.entity;

/**  
 * @author fuwei.deng
 * @date 2018年2月1日 下午3:49:33
 * @version 1.0.0
 */
public class ZuulPathEntity extends LimiterEntity {

	/** */
	private static final long serialVersionUID = 7026780267624790202L;
	
	/** 所属的zuul路由配置的id*/
	private String zuulId;
	
	/** 访问路径*/
	private String path;
	
	public String getZuulId() {
		return zuulId;
	}

	public void setZuulId(String zuulId) {
		this.zuulId = zuulId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
