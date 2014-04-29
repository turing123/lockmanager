LockManager
===========
LockManager is a Java concurrency utility designed for such scenario: you have several or many "resources" to manage
in your program; and for each "resource", there are several logics distributed in your program to access it, and the
accesses form the "race condition".

LockManager is an elegant solution for such scenario by centrally managing the locks associated with the resources.

> **Note:** The "resources" may be natural in your program, or may be something of some abstractions in your program.

> **Note:** LockManager is designed for using in a single process, and if you need some kind of distributed lock management,
you need some other solutions such as [Apache ZooKeeper]: http://zookeeper.apache.org/.

Examples
===========
```java
SimpleReadWriteLockManager<MyResource> lockmanager = new SimpleReadWriteLockManager<MyResource>();
```
in one thread:
```java
lockmanager.acquireReadLock(my_resource);

// do some stuff with the resource

lockmanager.releaseReadLock(my_resource);
```
and in other thread:
```java
lockmanager.acquireWriteLock(my_resource);

// do some stuff with the resource

lockmanager.releaseWriteLock(my_resource);
```
And please refer to the unit testing cases for more examples.
