package me.viniciuspiedade.rediscacheutil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class GenericRedisTemplate<T> extends RedisTemplate<String, T> {
	
	private RedisSerializer<T> serializer;
	
	public GenericRedisTemplate(RedisConnectionFactory redisConnectionFactory, Class <T> clazz) {
		this.setConnectionFactory(redisConnectionFactory);
		
		this.serializer = new GenericRedisSerializer<T>(clazz, getObjectMapper());
		
        this.setValueSerializer(this.serializer);
        this.setHashValueSerializer(this.serializer);
	}
	
	private ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
	}
	
	public static class GenericRedisSerializer<T> implements RedisSerializer<T> {
		private Class<T> classTemplate;
		private ObjectMapper mapper;
		
		public GenericRedisSerializer(Class<T> classTemplate, ObjectMapper mapper) {
			this.classTemplate = classTemplate;
			this.mapper = mapper;
		}
		
		public byte[] serialize(T t) throws SerializationException {
			try {
				return this.mapper.writeValueAsString(t).getBytes();
			} catch (JsonProcessingException e) {
				throw new SerializationException("Redis Template Serialization failure", e);
			}
		}

		public T deserialize(byte[] bytes) throws SerializationException {
			try {
				
				ObjectMapper objectMapper = this.mapper;
				T readValue = objectMapper.readValue(bytes, this.classTemplate);
				return readValue;
			} catch (IOException e) {
				throw new SerializationException("Redis Template Serialization failure", e);
			}
		}
	}

	public RedisSerializer<T> getSerializer() {
		return serializer;
	}
}
