package turing123;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import turing123.lockmanager.SimpleLockManager;

public class SimpleLockManagerTest {
	private SimpleLockManager<String> lockmanager;

	@Before
	public void initLockManager() {
		this.lockmanager = new SimpleLockManager<String>();
	}
	
	/**
	 * For two threads trying to acquire the locks on the same key, one of them succeeds.
	 */
	@Test
	public void testAcquireLocksOnSameKey() throws InterruptedException {
		final String key = "key";
		final Vector<Integer> threadsCompleted = new Vector<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key);
				threadsCompleted.add(1);				
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key);
				threadsCompleted.add(2);				
			}
		};
		
		Thread t1 = new Thread( runnable1 );
		Thread t2 = new Thread( runnable2 );
		
		t1.start();
		t2.start();
		t1.join(3000);
		t2.join(3000);
		// only one thread can acquire the lock for the key
		Assert.assertEquals(1, threadsCompleted.size());
	}
	
	/**
	 * For two threads trying to respectively acquire the locks on the same key, 
	 * if each of them release the key, then both succeed.
	 */
	@Test
	public void testAcquireAndReleaseReadWriteLocksOnSameKey() throws InterruptedException {
		final String key = "key";
		final Vector<Integer> threadsCompleted = new Vector<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key);
				threadsCompleted.add(1);
				lockmanager.releaseLock(key);
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key);
				threadsCompleted.add(2);
				lockmanager.releaseLock(key);
			}
		};
		
		Thread t1 = new Thread( runnable1 );
		Thread t2 = new Thread( runnable2 );
		
		t1.start();
		t2.start();
		t1.join(3000);
		t2.join(3000);
		// both threads can run correctly since each of them release the lock
		Assert.assertEquals(2, threadsCompleted.size());
	}
	
	/**
	 * For two threads trying to respectively acquire the locks on two different keys, both succeed.
	 */
	@Test
	public void testAcquireReadWriteLocksOnDifferentKeys() throws InterruptedException {
		final String key1 = "key1";
		final String key2 = "key2";
		final Vector<Integer> threadsCompleted = new Vector<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key1);
				threadsCompleted.add(1);				
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireLock(key2);
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
