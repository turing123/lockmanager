package turing123;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import turing123.lockmanager.SimpleReadWriteLockManager;

public class SimpleReadWriteLockManagerTest {
	private SimpleReadWriteLockManager<String> lockmanager;

	@Before
	public void initLockManager() {
		this.lockmanager = new SimpleReadWriteLockManager<String>();
	}

	@Test
	public void testAcquireReadLocksOnSameKey() throws InterruptedException {
		final String key = "key1";
		final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireReadLock(key);
				threadsCompleted.add(1);				
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireReadLock(key);
				threadsCompleted.add(2);				
			}
		};
		
		Thread t1 = new Thread( runnable1 );
		Thread t2 = new Thread( runnable2 );
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		Assert.assertEquals(2, threadsCompleted.size());
	}
}
