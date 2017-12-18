package com.itopener.demo.shardingjdbc.conditions;

import com.itopener.demo.shardingjdbc.model.Order;

/**
 * @desc Order查询条件辅助类
 * @author fuwei.deng
 * @date 2017-12-15 18:47:07
 */
public class OrderCondition extends Order {

	/** SerialVersionUID*/
	private static final long serialVersionUID = 8865569014344600944L;

	/** 分页查询--页数*/
	private int page;

	/** 分页查询--每页数量*/
	private int size;

	/** 排序*/
	private String orderBy;

	public OrderCondition setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPage() {
		return page;
	}

	public OrderCondition setSize(int size) {
		this.size = size;
		return this;
	}

	public int getSize() {
		return size;
	}

	public OrderCondition setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public String getOrderBy() {
		return orderBy;
	}
}