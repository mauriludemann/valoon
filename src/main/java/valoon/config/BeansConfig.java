package valoon.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import valoon.utils.JacksonJsonMapper;

import java.util.TimeZone;

@Configuration
public class BeansConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return (jackson2ObjectMapperBuilder) -> {
            jackson2ObjectMapperBuilder
                    .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .timeZone(TimeZone.getDefault())
                    .featuresToEnable(
                            DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                            DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
                    );
        };
    }

    @Bean
    public JacksonJsonMapper jacksonJsonMapper() {
        return new JacksonJsonMapper();
    }
}
