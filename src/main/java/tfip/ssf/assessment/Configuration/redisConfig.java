package tfip.ssf.assessment.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}") // add this line to read the Redis password from application.properties
    private String redisPassword;

    @Value("${spring.redis.database}")
    private int redisDatabse;

    @Bean("my-redis")
    public RedisTemplate<String, String> createRedisTemplate() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
                redisHost, redisPort);
        
        // add this block to set the Redis password
        if (redisPassword != null && !redisPassword.isEmpty()) {
            RedisPassword redisAuth = RedisPassword.of(redisPassword);
            config.setPassword(redisAuth);
        }

        JedisClientConfiguration jedisClient = JedisClientConfiguration
                .builder().build();
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    };

}
