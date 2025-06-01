package com.nowcoder.community.service;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    //依赖注入用户的 Mapper
    @Autowired
    private UserMapper userMapper;
    //根据用户 id 查询用户
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
