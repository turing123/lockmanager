package turing123.lockmanager;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

/**
 * This class is a simple implementation of ReadWriteLockManager.
 * 
 * It uses a hash map to store the pairs of key and read-write-locks.
 * There is no limitation to the storage size.
 * 
 */
public class SimpleReadWriteLockManager<E> implements ReadWriteLockManager<E> {
  
  /**
   * The lock pool containing the locks managed for the keys.
   */
  private Map<E, ManagedNode> lockPool = new HashMap<E, ManagedNode>();
  
  /**
   * Acquire the read lock for the specified key.
   */
  public void acquireReadLock(E key) {
    
    ManagedNode node;
    synchronized (this) {
      node = lockPool.get(key);
      if (node == null) {
        node = new ManagedNode();
        lockPool.put(key, node);                
      }      
      node.increaseLockCount();      
    }
    
    node.getReadLock().lock();
  }
  
  /**
   * Release the read lock for the specified key.
   */
  public void releaseReadLock(E key) {
    synchronized(this) {
      ManagedNode node = lockPool.get(key);
      if (node == null) {
        return;
      }
      if (node.getLockCount() == 0) {
        throw new RuntimeException("the lock count is already zero, so the read release is illegal for the key: " + key.toString());
      }
      node.getReadLock().unlock();
      
      node.decreaseLockCount();      

      if (node.getLockCount() == 0) {
        lockPool.remove(key);
      }
    }
  }
  
  /**
   * Acquire the write lock for the specified key.
   */
  public void acquireWriteLock(E key) {
    ManagedNode node;
    synchronized (this) {
      node = lockPool.get(key);
      if (node == null) {
        node = new ManagedNode();
        lockPool.put(key, node);        
      }
      node.increaseLockCount();
    }
    
    node.getWriteLock().lock();
  }
  
  /**
   * Release the write lock for the specified key.
   */
  public void releaseWriteLock(E key) {
    synchronized(this) {
      ManagedNode node = lockPool.get(key);
      if (node == null) {
        return;
      }
      if (node.getLockCount() == 0) {
        throw new RuntimeException("the lock count is already zero, so the write release is illegal for the key: " + key.toString());
      }
      
      node.getWriteLock().unlock();
            
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
    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    int lockCount = 0;
    
    ManagedNode(){
    }

    Lock getReadLock() {
      return rwLock.readLock();
    }
    
    Lock getWriteLock() {
      return rwLock.writeLock();
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


