package com.itopener.demo.shardingjdbc.conditions;

import com.itopener.demo.shardingjdbc.model.SysIp;

/**
 * SysIp查询条件辅助类
 * @author fuwei.deng
 * @date 2017年12月18日 下午1:24:51
 * @version 1.0.0
 */
public class SysIpCondition extends SysIp {

	/** SerialVersionUID*/
	private static final long serialVersionUID = 8865569014344600944L;

	/** 分页查询--页数*/
	private int page;

	/** 分页查询--每页数量*/
	private int size;

	/** 排序*/
	private String orderBy;

	public SysIpCondition setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPage() {
		return page;
	}

	public SysIpCondition setSize(int size) {
		this.size = size;
		return this;
	}

	public int getSize() {
		return size;
	}

	public SysIpCondition setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public String getOrderBy() {
		return orderBy;
	}
}