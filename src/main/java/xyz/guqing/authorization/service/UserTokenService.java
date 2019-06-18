package xyz.guqing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.UserToken;
import xyz.guqing.authorization.entity.UserTokenExample;
import xyz.guqing.authorization.mapper.UserTokenMapper;

import java.util.Date;
import java.util.List;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenMapper userTokenMapper;

    public void save(UserToken token){
        //保存之前先查询是否存在
        UserToken model = this.findByUsername(token.getUsername());
        if(model == null){
            token.setCreateTime(new Date());
            token.setModifyTime(new Date());
            userTokenMapper.insertSelective(token);
        }else {
            //如果存在就更新
            this.update(token);
        }
    }

    public int update(UserToken token){
        token.setModifyTime(new Date());
        UserTokenExample example = new UserTokenExample();
        UserTokenExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(token.getUsername());
        return userTokenMapper.updateByExampleSelective(token,example);
    }

    public UserToken findByUsername(String username){
        UserTokenExample example = new UserTokenExample();
        UserTokenExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<UserToken> tokenList = userTokenMapper.selectByExample(example);
        if(tokenList != null){
            return tokenList.get(0);
        }
        return null;
    }
}
