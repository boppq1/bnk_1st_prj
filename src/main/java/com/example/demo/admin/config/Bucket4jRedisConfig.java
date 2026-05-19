//package com.example.demo.admin.config;
//
//import java.time.Duration;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//
//import io.github.bucket4j.distributed.proxy.ProxyManager;
//import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
//import io.lettuce.core.RedisClient;
//import io.lettuce.core.api.StatefulRedisConnection;
//import io.lettuce.core.codec.ByteArrayCodec;
//import io.lettuce.core.codec.RedisCodec;
//import io.lettuce.core.codec.StringCodec;
//
//@Configuration
//public class Bucket4jRedisConfig {
//	
//	@Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        // 기본 캐시 설정 (유효기간 10분 설정 예시, 필요시 변경 가능)
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)); 
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
//	
//	@Bean
//	public ProxyManager<String> lettuceProxyManager(RedisConnectionFactory connectionFactory) {
//		
//		// RedisConnectionFactory(스프링의 기본 Redis 공장 설정)를 LettuceConnectionFactory(성능 좋은 레디스 라이브러리)로 캐스팅
//		LettuceConnectionFactory lcf = (LettuceConnectionFactory) connectionFactory;
//		
//		// 팩토리 내부에서 사용하는 RedisClient 원본을 꺼냄 (Lettuce 가동 위한 원본 레디스 클라이언트 엔진(RedisClient) 추출)
//		RedisClient rc = (RedisClient) lcf.getNativeClient();
//		
//		// Key는 String, Value는 byte[]를 사용하는 하이브리드 코덱 정의 (Redis와 데이터 주고 받을 때 Key는 String, Value는 이진 데이터로 다루겠다는 규격 약속)
//		RedisCodec<String, byte[]> stringAndBytesCodec = RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE);
//		
//		// 정의한 코덱을 넣어 <String, byte[]> 커넥션을 안전하게 오픈 (위에서 만든 규격대로 대화할 수 있는 통로(connection) 오픈)
//		StatefulRedisConnection<String, byte[]> connection = rc.connect(stringAndBytesCodec);
//		
//		// Bucket4j(처리율 제한 라이브러리)가 해당 통로를 통해 Redis 명령어를 날릴 수 있도록 래핑
//		return LettuceBasedProxyManager.builderFor(connection).build();
//	}
//}
