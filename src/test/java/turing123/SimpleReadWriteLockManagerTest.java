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
	
	/**
	 * For two threads trying to acquire the read locks on the same key, both succeed.
	 */
	@Test
	public void testAcquireReadLocksOnSameKey() throws InterruptedException {
		final String key = "key";
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
	
	/**
	 * For two threads trying to respectively acquire the read and write locks on the same key, only one succeeds.
	 */
	@Test
	public void testAcquireReadWriteLocksOnSameKey() throws InterruptedException {
		final String key = "key";
		final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireReadLock(key);
				threadsCompleted.add(1);				
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireWriteLock(key);
				threadsCompleted.add(2);				
			}
		};
		
		Thread t1 = new Thread( runnable1 );
		Thread t2 = new Thread( runnable2 );
		
		t1.start();
		t2.start();
		t1.join(3000);
		t2.join(3000);
		// only one thread can acquire the read or write lock for the key
		Assert.assertEquals(1, threadsCompleted.size());
	}
	
	/**
	 * For two threads trying to respectively acquire the read and write locks on the same key, 
	 * if each of them release the key, then both succeed.
	 */
	@Test
	public void testAcquireAndReleaseReadWriteLocksOnSameKey() throws InterruptedException {
		final String key = "key";
		final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireReadLock(key);
				threadsCompleted.add(1);
				lockmanager.releaseReadLock(key);
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireWriteLock(key);
				threadsCompleted.add(2);
				lockmanager.releaseWriteLock(key);
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
	 * For two threads trying to respectively acquire the read and write locks on two different keys, both succeed.
	 */
	@Test
	public void testAcquireReadWriteLocksOnDifferentKeys() throws InterruptedException {
		final String key1 = "key1";
		final String key2 = "key2";
		final ArrayList<Integer> threadsCompleted = new ArrayList<Integer>();
		
		Runnable runnable1 = new Runnable() {
			public void run() {
				lockmanager.acquireReadLock(key1);
				threadsCompleted.add(1);				
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				lockmanager.acquireWriteLock(key2);
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
