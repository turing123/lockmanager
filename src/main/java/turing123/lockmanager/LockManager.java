package turing123.lockmanager;

/**
 * This class represents the interface for lock_managers.
 * 
 * Any implementation of this interface should guarantee that:
 * (1) the customers of this implementation can acquire and release a lock for 
 * a specified key;
 * (2) the acquisition and release of locks obey to the semantics of java.util.concurrent.locks.ReentrantLock;
 * (3) no memory leak is introduced, that is, the lock objects are correctly managed, they should be
 * constructed and deconstructed at right time.
 * 
 *  From the perspective of customers, following is an example of event sequence when using the lock manager:
 *  (1) customer1 tries to acquire the lock for the key K1;
 *  (2) customer1 acquires the lock successfully;
 *  (3) customer2 tries to acquire the lock for the key K1;
 *  (4) customer2 will be blocked until the lock for K1 is released;
 *  (5) Customer3 tries to acquire the lock for the key K2;
 *  (6) Customer3 acquires the lock successfully;
 *  (7) customer4 tries to acquire the lock for the key K2;
 *  (8) Customer4 will be blocked until the lock for K2 is released;
 *  (9) Customer1 releases the lock for K1;
 *  (10) Customer2 acquires the lock for K1.
 *  (11) Customer3 releases the lock for K2;
 *  (12) Customer4 acquires the lock for K2;
 * 
 * 
 */
public interface LockManager<E> {

  /**
   * Acquire the lock for the specified key.
   * @param key
   */
  void acquireLock(E key);

  /**
   * Release the lock for the specified key.
   * @param key
   */
  void releaseLock(E key);
}
