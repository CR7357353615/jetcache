package com.lho.cache.jetcache.services;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.CacheResultCode;
import com.alicp.jetcache.anno.*;
import com.lho.cache.jetcache.models.User;
import com.lho.cache.jetcache.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lihao on 2018/9/20.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 缓存实例，存储在远程
//    @CreateCache(expire = 100)
//    private Cache<Integer, User> userCache;

    // 缓存实例，存储在内存和远程两个端，内存限制50个
    @CreateCache(name = "UserService.userCache", expire = 100, cacheType = CacheType.BOTH, localLimit = 50)
    private Cache<Integer, User> userCache;

    @Cached(name="UserService.users", expire = 100)
    public List<User> users() {
        Iterable<User> users =  userRepository.findAll();
        List<User> userList = new ArrayList<>();
        Iterator<User> userIterator = users.iterator();
        while (userIterator.hasNext()) {
            User user = userIterator.next();
//            userCache.put(user.getId(), user);
            userList.add(user);
        }
        return userList;
    }

    // 获取用户
    @Cached(name="UserService.users", expire = 100, key="#userId")
    public User getUserById(Integer userId) {
//        CacheGetResult<User> userCacheResilt = userCache.GET(userId);
//        User user = validCacheGet(userCacheResilt);
//        if (null == user) {
//            return userRepository.findOne(userId);
//        }
//        return userCache.get(userId);
        return userRepository.findOne(userId);
    }

    // 更新用户
    @CacheUpdate(name="UserService.users", key="#user.id", value="#user")
    public void updateUser(User user) {
        User user1 = userRepository.findOne(user.getId());
        if (null == user1) {
            throw new EntityNotFoundException("E_NOT_FOUND_USER");
        }
        userRepository.save(user);
    }

    // 删除用户
    @CacheInvalidate(name="UserService.users", key="#id")
    public void removeUserById(Integer id) {
        User user = userRepository.findOne(id);
        if (null == user) {
            throw new EntityNotFoundException("E_NOT_FOUND_USER");
        }
        userRepository.delete(id);
    }

    // 新增用户
    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    private <T> T validCacheGet(CacheGetResult<T> result) {
        if(result.isSuccess() ){
            return (T) result.getValue();
        } else if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
            System.out.println("cache miss:" + result);
        } else if(result.getResultCode() == CacheResultCode.EXPIRED) {
            System.out.println("cache expired:" + result.getMessage());
        } else {
            System.out.println("cache get error:" + result.getMessage());
        }
        return null;
    }
}
