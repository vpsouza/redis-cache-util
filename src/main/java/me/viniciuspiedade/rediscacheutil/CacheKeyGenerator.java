package me.viniciuspiedade.rediscacheutil;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;

import me.viniciuspiedade.rediscacheutil.entity.CacheableEntity;

public class CacheKeyGenerator implements KeyGenerator{

	private String applicationName;
	private String profile;
	
	public CacheKeyGenerator(String applicationName, String profile) {
		this.applicationName = applicationName;
		this.profile = profile;
	}
	
	public Object generate(Object target, Method method, Object... params) {
		final StringBuilder sb = new StringBuilder();
        
        sb.append(applicationName)
        .append(":").append(profile).append(":cache.alerts.");
        
        if(params.length > 0) {
	        	if(params[0] instanceof CacheableEntity) {
	    			sb.append(((CacheableEntity)params[0]).getCacheableKey());
	    		} else {
	    			sb.append(Stream.of(params).filter(elm -> !(elm instanceof Pageable)).map(p -> p.toString()).collect(Collectors.joining(".")));
	    		}
        } else {
        		sb.append(method.getName());
        }
         
        return sb.toString();
	}

}
