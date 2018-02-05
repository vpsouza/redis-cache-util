package me.viniciuspiedade.rediscacheutil.annotation;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ProfilesExcludedCondition implements Condition {

	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return !Optional.ofNullable(context.getEnvironment()).isPresent()
				|| !Optional.ofNullable(metadata.getAllAnnotationAttributes(ProfilesExcluded.class.getName()))
						.map(elm -> elm.get("value").stream().map(item -> (String[]) item).collect(Collectors.toList()))
						.orElseGet(() -> Collections.emptyList())
						.stream()
							.flatMap(elm -> Arrays.asList(elm).stream())
							.anyMatch(elm -> context.getEnvironment().acceptsProfiles(elm));
	}
}