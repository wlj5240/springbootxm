package com.itopener.zuul.ratelimiter.spring.boot.common.entity;

/**  
 * @author fuwei.deng
 * @date 2018年2月1日 下午3:49:33
 * @version 1.0.0
 */
public class ZuulIdEntity extends LimiterEntity {

	/** */
	private static final long serialVersionUID = 7026780267624790202L;
	
	/** zuul路由配置的id*/
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
