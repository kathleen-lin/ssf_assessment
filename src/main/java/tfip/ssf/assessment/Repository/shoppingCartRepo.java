package tfip.ssf.assessment.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class shoppingCartRepo {

    @Autowired @Qualifier("my-redis")
	private RedisTemplate<String, String> template;

    
	
}
