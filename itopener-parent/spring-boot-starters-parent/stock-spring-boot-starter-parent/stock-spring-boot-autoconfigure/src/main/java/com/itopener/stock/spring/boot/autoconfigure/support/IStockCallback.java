package com.itopener.stock.spring.boot.autoconfigure.support;

/**  
 * @author fuwei.deng
 * @date 2018年2月6日 下午5:39:49
 * @version 1.0.0
 */
public interface IStockCallback {

	public long getStock(String stock);
}
