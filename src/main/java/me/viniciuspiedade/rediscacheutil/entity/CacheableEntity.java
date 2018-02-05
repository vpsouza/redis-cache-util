package me.viniciuspiedade.rediscacheutil.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface CacheableEntity {

	@JsonIgnore
	Serializable getCacheableKey();
}
