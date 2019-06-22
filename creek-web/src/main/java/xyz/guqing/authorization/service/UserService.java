package xyz.guqing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.model.User;
import xyz.guqing.authorization.entity.model.UserExample;
import xyz.guqing.authorization.mapper.UserMapper;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserByUsername(String username){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList != null && userList.size() == 1){
            return userList.get(0);
        }
        return null;
    }
}
