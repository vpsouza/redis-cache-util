package me.viniciuspiedade.rediscacheutil;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.viniciuspiedade.rediscacheutil.entity.CacheableEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class CacheKeyGeneratorTest {

	private static final String APP_NAME = "spider-sense";
	private static final String PROFILE_NAME = "test";
	private static final String CACHE_KEY_PREFIX = APP_NAME + ":" + PROFILE_NAME + ":cache.alerts.";
	
	Method methodForGenerator;
	
	private CacheKeyGenerator generator;
	
	@Before
	public void setup() throws NoSuchMethodException, SecurityException {
		methodForGenerator = CacheKeyGeneratorTest.class.getMethod("setup");
		generator = new CacheKeyGenerator(APP_NAME, PROFILE_NAME);
	}
	
	@Test
	public void shouldGenerateKeyWhenCacheableEntityParameterIsPresent() {
		String key = "String";
		CacheableEntity cacheableEntity = new CacheableEntity() {
			@Override
			public Serializable getCacheableKey() {
				return key;
			}
		};
		
		assertEquals(true, CACHE_KEY_PREFIX.concat(key).equals(generator.generate(null, null, cacheableEntity)));
	}
	
	@Test
	public void shouldGenerateKeyWhenNonEntityParametersIsPresent() {
		Integer key1 = 1;
		String key2 = "StrKey";
		Boolean key3 = false;
		String cacheKeyName = new StringBuilder()
				.append(CACHE_KEY_PREFIX)
				.append(key1)
				.append(".")
				.append(key2)
				.append(".")
				.append(key3).toString();
		
		assertEquals(true, cacheKeyName.equals(generator.generate(null,  null, key1, key2, key3)));
	}
	
	@Test
	public void shouldGenerateKeyWithoutParameters() {
		assertEquals(true, CACHE_KEY_PREFIX.concat(methodForGenerator.getName()).equals(generator.generate(null, methodForGenerator)));
	}
}
