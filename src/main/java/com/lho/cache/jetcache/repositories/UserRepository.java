package com.lho.cache.jetcache.repositories;

import com.lho.cache.jetcache.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lihao on 2018/9/20.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer>{

}
