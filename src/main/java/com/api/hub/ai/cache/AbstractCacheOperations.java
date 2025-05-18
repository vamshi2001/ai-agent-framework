package com.api.hub.ai.cache;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.api.hub.ai.cache.impl.CacheRefresher;
import com.api.hub.ai.cache.impl.InMemoryCache;

import jakarta.annotation.PostConstruct;

public abstract class AbstractCacheOperations<K,V> extends InMemoryCache<K,V> {

	@Autowired
	protected CacheRefresher refresher;
	
	@Value("${cache.syncOnChange:false}")
	protected boolean syncOnChange;
	
	protected Queue<K> keysToUpdate = new LinkedBlockingQueue<K>();
	
	protected long lastRefreshTime = 0;
	
	@PostConstruct
	public void intit() {
		refresher.registerCache(this);
		source();
	}
	
	@Override
	public synchronized boolean refresh() {
		while(!keysToUpdate.isEmpty()) {
		
			K key = keysToUpdate.poll();
			sink(key);
		}
		source();
		lastRefreshTime = System.currentTimeMillis();
		return true;
	}

	@Override
	public void notifyCacheHandler(K key) {
		
		if(syncOnChange) {
			sink(key);
		}else {
			keysToUpdate.add(key);
		}
	}
	
	@Override
	public long getLastRefreshTime() {
		
		return lastRefreshTime;
	}
}
