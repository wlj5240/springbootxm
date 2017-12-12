package com.itopener.framework.base;

/**
 * @author fuwei.deng
 * @date 2017年12月11日 下午1:44:55
 * @version 1.0.0
 */
public class BaseModel implements BaseCondition {
	
	/** */
	private static final long serialVersionUID = 2375431423530024690L;
	
	/** 主键*/
	private long id;

	/** 分页查询--页数*/
	private int page;

	/** 分页查询--每页数量*/
	private int size;

	/** 排序*/
	private String orderBy;

	public long getId() {
		return id;
	}

	public BaseModel setId(long id) {
		this.id = id;
		return this;
	}

	@Override
	public int getPage() {
		return page;
	}

	@Override
	public BaseModel setPage(int page) {
		this.page = page;
		return this;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public BaseModel setSize(int size) {
		this.size = size;
		return this;
	}

	@Override
	public String getOrderBy() {
		return orderBy;
	}

	@Override
	public BaseModel setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

}
