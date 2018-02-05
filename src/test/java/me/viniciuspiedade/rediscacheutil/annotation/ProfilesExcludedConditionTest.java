package me.viniciuspiedade.rediscacheutil.annotation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProfilesExcludedConditionTest {

	private ProfilesExcludedCondition condition;
	
	@Mock private ConditionContext context;
	@Mock private AnnotatedTypeMetadata metadata;
	@Mock private Environment environment;
	@Mock private MultiValueMap<String, Object> _getAllAnnotationAttributes;
	
	@Before
	public void setup() {
		when(context.getEnvironment()).thenReturn(environment);
		when(metadata.getAllAnnotationAttributes(anyString())).thenReturn(_getAllAnnotationAttributes);
		
		condition = new ProfilesExcludedCondition();
	}
	
	@Test
	public void shouldReturnFalseWhenProfileNeedToBeExcluded() {
		List<Object> ignoredProfiles = new ArrayList<>();
		ignoredProfiles.add(new String[] {"test", "dev"});
		
		when(_getAllAnnotationAttributes.get("value")).thenReturn(ignoredProfiles);
		when(environment.acceptsProfiles("test")).thenReturn(true);
		
		assertEquals(false, condition.matches(context, metadata));
	}
	
	@Test
	public void shouldReturnTrueWhenProfileDoesNotNeedToBeExcluded() {
		List<Object> ignoredProfiles = new ArrayList<>();
		ignoredProfiles.add(new String[] {"test", "dev"});
		
		when(_getAllAnnotationAttributes.get("value")).thenReturn(ignoredProfiles);
		when(environment.acceptsProfiles(anyString())).thenReturn(false);
		
		assertEquals(true, condition.matches(context, metadata));
	}
	
	@Test
	public void shouldReturnTrueWhenThereIsNoEnvironmentInsideContext() {
		List<Object> ignoredProfiles = new ArrayList<>();
		
		when(context.getEnvironment()).thenReturn(environment);
		when(_getAllAnnotationAttributes.get("value")).thenReturn(ignoredProfiles);
		
		assertEquals(true, condition.matches(context, metadata));
	}
	
}
