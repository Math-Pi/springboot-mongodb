# SpringBoot的学习笔记(十一)：MongoDB 的使用

## MongoDB 简介

- MongoDB 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。
- **MongoDB 是由数据库（database）、集合（collection）、文档对象（document）三个层次组成。**
- MongoDB 的适合对大量或者无固定格式的数据进行存储，比如：日志、缓存等。
- MongoDB 将数据存储为一个文档，数据结构由键值(key=>value)对组成。MongoDB 文档类似于 JSON 对象。字段值可以包含其他文档，数组及文档数组。

## 一、pom.xml引入Maven依赖

```xml
<dependencies>
	<dependency> 
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-data-mongodb</artifactId>
	</dependency> 
</dependencies>
```

## 二、application.properties 配置mongodb

```properties
spring.data.mongodb.uri=mongodb://name:pass@localhost:27017/test
```

## 三、实体类

```java
public class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String userName;
        private String passWord;
      //省略getter、setter方法
}
```

## 四、UserDao接口

```java
public interface UserDao {
    void saveUser(User user);
    User findUserByUserName(String userName);
    long updateUser(User user);
    void deleteUserById(Long id);
}
```

## 五、接口实现类

```java
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
        Query query=new Query(Criteria.where("id").is(user.getId()));
        Update update= new Update().set("userName", user.getUserName()).set("passWord", user.getPassWord());
        //修改符合条件第一条记录
        UpdateResult result =mongoTemplate.updateFirst(query,update,User.class);
        //修改符合条件的所有
        // mongoTemplate.updateMulti(query,update,User.class);
        if(result!=null)
            return result.getMatchedCount();
        else
            return 0;
    }
    //删除文档对象
    public void deleteUserById(Long id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,User.class);
    }
}
```

## 六、测试类

```java
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
        user.setUserName("Jenny");
        user.setPassWord("122");
        userDao.updateUser(user);
    }
    @Test
    public void deleteUserById(){
        userDao.deleteUserById(2l);
    }
}
```

## 七、 MongoDB查看结果

切换到 MongoDB 目录的 bin 目录打开命令行

1.连接上 MongoDB

```cmd
mongo
```

2、切换到 test 库

```cmd
user test
```

3、查询 user 集合数据

```
db.user.find()
```

## 八、MongoTemplate

### 常用方法
```java
//查询User文档的全部数据
mongoTemplate.findAll(User.class); 
//查询User文档id为id的数据
mongoTemplate.findById(<id>, User.class);
//根据query内的查询条件查询    
mongoTemplate.find(query, User.class);
//修改
mongoTemplate.upsert(query, update, User.class);
//删除
mongoTemplate.remove(query, User.class);
//新增
mongoTemplate.insert(user);
```

- **mongoTemplate.updateFirst()**：修改符合条件第一条记录。
- **mongoTemplate.updateMulti()**：修改符合条件的所有。
- **mongoTemplate.upsert()**：修改符合条件时如果不存在则添加。