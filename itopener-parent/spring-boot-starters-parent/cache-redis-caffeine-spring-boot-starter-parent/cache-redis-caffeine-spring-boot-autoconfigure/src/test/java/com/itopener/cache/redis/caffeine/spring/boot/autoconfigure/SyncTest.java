package com.itopener.cache.redis.caffeine.spring.boot.autoconfigure;

/**  
 * @author fuwei.deng
 * @date 2018年2月11日 上午9:59:30
 * @version 1.0.0
 */
public class SyncTest {
	
	public static void main(String[] args) throws InterruptedException {
		new SyncTest().test();
		Thread.sleep(50000);
	}

	public void test() throws InterruptedException {
		for(int i=0; i<10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
//						new SyncTest().exec("123-" + new Random().nextInt());
						new SyncTest().exec(new User("fuwei"));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		}
	}
	
	public void exec(Object key) throws InterruptedException {
		synchronized (key) {
			System.out.println(key);
			Thread.sleep(5000);
		}
	}
	@Override
	public String toString() {
		return super.toString();
	}
	
	class User{
		
		private String name;

		public User(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
		
	}
}
