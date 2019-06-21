package cn.ideal.wf.cache.redis.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisShardInfo;

@Configuration
@ComponentScan("cn.ideal.wf.redis.conf")
//@PropertySource("classpath:application.properties")
public class RedisConfig {
	@Value("${spring.redis.host}")
	String hostName;
	@Value("${spring.redis.port}")
	String port;
	@Value("${spring.redis.password}")
	String password;
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {		
		JedisShardInfo shardInfo = new JedisShardInfo(hostName, port);
		shardInfo.setPassword(password);
		return new JedisConnectionFactory(shardInfo);
	}
	 
	@Bean
	public RedisTemplate<String,Object> redisTemplate() {
		final RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		return template;
	}


}
