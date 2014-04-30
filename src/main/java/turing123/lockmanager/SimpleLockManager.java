package turing123.lockmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is a simple implementation of LockManager.
 * 
 * It uses a hash map to store the pairs of key and locks.
 * There is no limitation to the storage size.
 * 
 */
public class SimpleLockManager<E> implements LockManager<E> {
	 
  /**
   * The lock pool containing the locks managed for the keys.
   */
  private Map<E, ManagedNode> lockPool = new HashMap<E, ManagedNode>();
  
  /**
   * Acquire the lock for the specified key.
   */
  public void acquireLock(E key) {
    
    ManagedNode node;
    synchronized (this) {
      node = lockPool.get(key);
      if (node == null) {
        node = new ManagedNode();
        lockPool.put(key, node);                
      }      
      node.increaseLockCount();      
    }
    
    node.getLock().lock();
  }
  
  /**
   * Release the lock for the specified key.
   */
  public void releaseLock(E key) {
    synchronized(this) {
      ManagedNode node = lockPool.get(key);
      if (node == null) {
        return;
      }
      if (node.getLockCount() == 0) {
        throw new RuntimeException("the lock count is already zero, so the read release is illegal for the key: " + key.toString());
      }
      node.getLock().unlock();
      
      node.decreaseLockCount();      

      if (node.getLockCount() == 0) {
        lockPool.remove(key);
      }
    }
  }
  
  
  /**
   * The node corresponding to a key in the lock pool.
   * 
   */
  static private class ManagedNode{
	ReentrantLock lock = new ReentrantLock();
    int lockCount = 0;
    
    ManagedNode(){
    }

    Lock getLock() {
      return lock;
    }
       
    void increaseLockCount() {
      this.lockCount ++;
    }
    
    void decreaseLockCount() {
      this.lockCount --;
    }
    
    int getLockCount() {
      return this.lockCount;
    }
  }
}