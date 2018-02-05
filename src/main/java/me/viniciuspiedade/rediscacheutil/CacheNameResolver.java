package me.viniciuspiedade.rediscacheutil;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;

public class CacheNameResolver extends AbstractCacheResolver {

	private String cacheName;
	
	public CacheNameResolver(String cacheName, String applicationName, String profileName, CacheManager cacheManager) {
		this.cacheName = buildCacheName(cacheName, applicationName, profileName);
		this.setCacheManager(cacheManager);
	}
	
	private static String buildCacheName(String cacheName, String applicationName, String profileName) {
		return applicationName + ":" + profileName + ":cache." + cacheName;
	}

	@Override
	protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		return Arrays.asList(cacheName);
	}

}
