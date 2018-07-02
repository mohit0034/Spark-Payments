package com.example.spark.dao;

import com.example.spark.dataobject.UserBankDO;
import com.example.spark.dataobject.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate template;

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public UserDO insertUser(UserDO userDO){
        float wallet = 200;
        String sql="insert into user (first_name,last_name,username,phone,password,wallet) values('" + userDO.getFirst_name()+"','"+ userDO.getLast_name()+
                "','" + userDO.getUsername()+"','"+ userDO.getPhone()+"','"+ userDO.getPassword() +"',"+wallet+")";

        template.execute(sql);

        return userDO;
    }

    public UserDO getUserByPhone(String phone){
        String sql="select * from user where phone = ?";
        return template.queryForObject(sql, new Object[]{phone},new BeanPropertyRowMapper<UserDO>(UserDO.class));
    }

    public UserDO updateUser(UserDO userDO, String token)
    {
        String sql = "update user set first_name =?, last_name = ? where token = ?";
        template.update(sql,new Object[]{userDO.getFirst_name(), userDO.getLast_name(), token});
        return userDO;
    }

    public void deleteUser(String phone)
    {
        String sql = "delete from user where phone = '" + phone +"'";
        template.execute(sql);
    }

    public void addMoney(UserDO userDO, float amount, UserBankDO userbank)
    {
        float new_wallet = amount+ userDO.getWallet();
        float new_balance = userbank.getBalance()-amount;
        String sql = "update user set wallet = "+ new_wallet+"where phone = '" + userDO.getPhone()+"'";
        template.execute(sql);
        sql = "update user_accounts set balance = "+new_balance+ "where account_id = " + userbank.getAccount_id()+"";
        template.execute(sql);
    }

    public void sendMoney(UserDO sender, float amount, UserDO receiver)
    {
        float sender_wallet = sender.getWallet()-amount;
        float receiver_wallet = receiver.getWallet()+amount;
        String sql = "update user set wallet = "+ sender_wallet+ "where phone = '" + sender.getPhone()+"'";
        template.execute(sql);
        sql = "update user set wallet = "+ receiver_wallet+ "where phone = '" + receiver.getPhone()+"'";
        template.execute(sql);
    }

    public UserBankDO getUserBank(UserDO userDO){
        String sql="select * from user_accounts where id = ?";
        return template.queryForObject(sql, new Object[]{userDO.getId()},new BeanPropertyRowMapper<UserBankDO>(UserBankDO.class));
    }

    public UserBankDO insertUserAccount(UserBankDO userbank)
    {
        String sql = "insert into user_accounts values (" + userbank.getId()+ ", "+userbank.getAccount_id()+","+ userbank.getBalance()+")";
        return userbank;
    }

    public float getWallet(String token)
    {
        String sql = "select wallet from user where token = ?";
        return template.queryForObject(sql, new Object[]{token}, float.class);
    }

    public Integer getUserId(String phone)
    {
        String sql = "select id from user where phone = ?";
        return template.queryForObject(sql, new Object[]{phone}, Integer.class);
    }

    public Integer getUserIdByName(String name)
    {
        String sql = "select id from user where username = ?";
        return template.queryForObject(sql, new Object[]{name}, Integer.class);
    }

    public String getPasswordByUsername(String name)
    {
        String sql = "select password from user where username = ?";
        return template.queryForObject(sql, new Object[]{name}, String.class);
    }

    public String getPhone(String name)
    {
        String sql = "select phone from user where username = ?";
        return template.queryForObject(sql,new Object[]{name}, String.class);
    }

    public UserDO getUserByToken(String token){
        String sql="select * from user where token = ?";
        return template.queryForObject(sql, new Object[]{token},new BeanPropertyRowMapper<UserDO>(UserDO.class));
    }

    public java.sql.Date getExpireTime(String token){
        String sql = "select expire_time from user where token=?";
        java.sql.Date date =  template.queryForObject(sql, new Object[]{token}, java.sql.Date.class);
        return date;
    }
     public void setToken(String key, String token, java.sql.Date date, String username){
        String sql = "update user set access_key = '"+key+"', token = '" +token+ "', expire_time = '"+ date+"' where username = '"+ username+"'";
        template.execute(sql);
     }

}