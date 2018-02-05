package me.viniciuspiedade.rediscacheutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.viniciuspiedade.rediscacheutil.GenericRedisTemplate.GenericRedisSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenericRedisTemplateTest {

	@Mock private RedisConnectionFactory redisConnectionFactory;
	@Mock private ObjectMapper mockMapper;
	
	private GenericRedisTemplate<TestPojo> restTemplate;
	private GenericRedisSerializer<TestPojo> successRedisSerializer;
	private GenericRedisSerializer<TestPojo> failRedisSerializer;
	
	private ObjectMapper mapper;
	private TestPojo toBeSerializedPojo;
	
	@Before
	public void setup() {
		toBeSerializedPojo = new TestPojo(1, "description", LocalDateTime.now());

		restTemplate = new GenericRedisTemplate<TestPojo>(redisConnectionFactory, TestPojo.class);
		
		mapper = getObjectMapper();
		successRedisSerializer = new GenericRedisSerializer<>(TestPojo.class, mapper);
		
		failRedisSerializer = new GenericRedisSerializer<>(TestPojo.class, mockMapper);
	}
	
	private ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
	}
	
	@Test
	public void shouldCreateGenericRedisTemplate() {
		assertNotNull(restTemplate.getSerializer());
	}
	
	@Test
	public void shouldSuccefullySerializePojo() throws JsonProcessingException {
		String strExpected = mapper.writeValueAsString(toBeSerializedPojo);
		
		assertEquals(true, strExpected.equals(new String(successRedisSerializer.serialize(toBeSerializedPojo))));
	}
	
	@Test
	public void shouldSuccefullyDeerializePojo() throws JsonProcessingException {
		byte[] serialized = mapper.writeValueAsString(toBeSerializedPojo).getBytes();
		
		assertEquals(true, toBeSerializedPojo.equals(successRedisSerializer.deserialize(serialized)));
	}
	
	@Test(expected=SerializationException.class)
	public void shouldThrowJsonProcessingExceptionWhenSerializerErrorOccurs() throws SerializationException, JsonProcessingException {
		when(mockMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
		
		failRedisSerializer.serialize(new TestPojo());
	}
	
	@Test(expected=SerializationException.class)
	public void shouldThrowJsonProcessingExceptionWhenDeserializerErrorOccurs() throws SerializationException, JsonParseException, JsonMappingException, IOException {
		byte[] emptyByteArray = new byte[] {};
		when(mockMapper.readValue(emptyByteArray, TestPojo.class)).thenThrow(JsonProcessingException.class);
		
		failRedisSerializer.deserialize(emptyByteArray);
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor
	private static class TestPojo implements Serializable {
		private static final long serialVersionUID = -7743857223032599929L;
		
		private Integer id;
		private String description;
		private LocalDateTime registrationDate;
	}
}
