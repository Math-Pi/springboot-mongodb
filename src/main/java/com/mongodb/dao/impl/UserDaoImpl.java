package com.mongodb.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.dao.UserDao;
import com.mongodb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    //增加文档对象
    @Override
    public void saveUser(User user) {
        mongoTemplate.save(user);
    }

    //根据用户名查询文档对象
    @Override
    public User findUserByUserName(String userName) {
        Query query=new Query(Criteria.where("userName").is(userName));
        User user =  mongoTemplate.findOne(query , User.class);
        return user;
    }
     //更新文档对象
    @Override
    public long updateUser(User user) {
        Query query=new Query(Criteria.where("userName").is(user.getUserName()));
        Update update= new Update().set("passWord", user.getPassWord());
        //修改符合条件第一条记录
        UpdateResult result = mongoTemplate.updateFirst(query,update,User.class);
        //修改符合条件时如果不存在则添加
        //UpdateResult result =mongoTemplate.upsert(query,update,User.class);
        //修改符合条件的所有
        //UpdateResult result= mongoTemplate.updateMulti(query,update,User.class);
        if(result!=null)
            return result.getMatchedCount();//符合条件数
        else
            return 0;
    }
    //删除文档对象
    public void deleteUserById(Long id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,User.class);
    }
}
