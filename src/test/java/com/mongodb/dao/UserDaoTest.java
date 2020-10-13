package com.mongodb.dao;

import com.mongodb.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Test
    public void testSaveUser() throws Exception {
        User user=new User();
        user.setId(2l);
        user.setUserName("Tom");
        user.setPassWord("123");
        userDao.saveUser(user);
    }
    @Test
    public void findUserByUserName(){
       User user= userDao.findUserByUserName("Tom");
       System.out.println("用户是："+user);
    }
    @Test
    public void updateUser(){
        User user=new User();
        user.setId(2l);
        user.setUserName("Tom");
        user.setPassWord("122");
        long count=userDao.updateUser(user);
        System.out.println("符合条件文档数："+count);
    }
    @Test
    public void deleteUserById(){
        userDao.deleteUserById(2l);
    }
}
