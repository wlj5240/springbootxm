package com.itopener.demo.shardingjdbc.conditions;

import com.itopener.demo.shardingjdbc.model.OrderItem;

/**
 * @desc OrderItem查询条件辅助类
 * @author fuwei.deng
 * @date 2017-12-15 18:47:07
 */
public class OrderItemCondition extends OrderItem {

	/** SerialVersionUID*/
	private static final long serialVersionUID = 3354783832754148705L;

	/** 分页查询--页数*/
	private int page;

	/** 分页查询--每页数量*/
	private int size;

	/** 排序*/
	private String orderBy;

	public OrderItemCondition setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPage() {
		return page;
	}

	public OrderItemCondition setSize(int size) {
		this.size = size;
		return this;
	}

	public int getSize() {
		return size;
	}

	public OrderItemCondition setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public String getOrderBy() {
		return orderBy;
	}
}