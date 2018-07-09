package com.example.spark.dao;

import com.example.spark.dataobject.UserDO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration("classpath*:applicationContextTest.xml")
public class UserDAOTest {

    private static final String username = "testusername";
    private static final String password = "testpassword";
    private static final String phone = "0000000000";
    private static final String first_name = "test";
    private static final String last_name = "lastname";

    @Autowired
    private UserDAO userDAO;

    @Before
    public void setUp(){

    }

 /*   @After
    public void tearDown() {
        userDAO.deleteUser(phone);
    }*/

    private UserDO buildUserDO(String first_name, String last_name, String username, String password, String phone){
        UserDO userDO = new UserDO();
        userDO.setFirst_name(first_name);
        userDO.setLast_name(last_name);
        userDO.setUsername(username);
        userDO.setPassword(password);
        userDO.setPhone(phone);
        return userDO;
    }

    @Test
    public void insertUserTest(){
        UserDO userDOExpected = buildUserDO(first_name,last_name,username,password,phone);
        userDAO.insertUser(userDOExpected);
        int id = userDAO.getUserId(phone);
        UserDO userDOActual = userDAO.getUserById(id);
        //System.out.println(userDOActual.getFirst_name());
        Assert.assertEquals(userDOExpected.getUsername(),userDOActual.getUsername());
        Assert.assertEquals(userDOExpected.getFirst_name(),userDOActual.getFirst_name());
        Assert.assertEquals(userDOExpected.getLast_name(),userDOActual.getLast_name());
        Assert.assertEquals(userDOExpected.getPassword(),userDOActual.getPassword());
        Assert.assertEquals(userDOExpected.getPhone(),userDOActual.getPhone());
    }




}
