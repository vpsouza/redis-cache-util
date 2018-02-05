package me.viniciuspiedade.rediscacheutil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class CacheNameResolverTest {

	private static final String CACHE_NAME = "TEST_CACHE";
	private static final String APP_NAME = "order";
	private static final String PROFILE_NAME = "test";
	private static final String FULL_CACHE_NAME = APP_NAME + ":" + PROFILE_NAME + ":cache." + CACHE_NAME;

	private CacheNameResolver cacheNameResolver;
	
	@Mock
	private CacheManager cacheManager;
	
	@Mock
	CacheOperationInvocationContext<?> cacheOpContext;
	
	@Before
	public void setup() {
		cacheNameResolver = new CacheNameResolver(CACHE_NAME, APP_NAME, PROFILE_NAME, cacheManager);
	}

	@Test
	public void shouldSuccefullyResolveCacheName() {
		when(cacheManager.getCache(FULL_CACHE_NAME)).thenReturn(createCache());
		
		Collection<? extends Cache> caches = cacheNameResolver.resolveCaches(cacheOpContext);
		
		assertEquals(1, caches.size());
	}
	
	private Cache createCache() {
		return new Cache() {

			@Override
			public String getName() {
				return null;
			}

			@Override
			public Object getNativeCache() {
				return null;
			}

			@Override
			public ValueWrapper get(Object key) {
				return null;
			}

			@Override
			public <T> T get(Object key, Class<T> type) {
				return null;
			}

			@Override
			public <T> T get(Object key, Callable<T> valueLoader) {
				return null;
			}

			@Override
			public void put(Object key, Object value) {
				
			}

			@Override
			public ValueWrapper putIfAbsent(Object key, Object value) {
				return null;
			}

			@Override
			public void evict(Object key) {
				
			}

			@Override
			public void clear() {
				
			}
			
		};
	}
	
}
