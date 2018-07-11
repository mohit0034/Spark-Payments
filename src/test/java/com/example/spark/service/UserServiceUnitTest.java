package com.example.spark.service;

import com.example.spark.api.dto.request.AddMoneyDTO;
import com.example.spark.api.dto.request.LogInDTO;
import com.example.spark.api.dto.request.SendMoneyDTO;
import com.example.spark.api.errorhandler.UserException;
import com.example.spark.dao.UserDAO;
import com.example.spark.dataobject.UserBankDO;
import com.example.spark.dataobject.UserDO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {

    private UserService userservice;
    private static final String username = "gargmohit";
    private static final String password = "1234";
    private static final String token = "doesnotexist";
    private UserDAO userdao;
    LogInDTO logInDTO = new LogInDTO();

    @Before
    public void setUp(){
        userservice = new UserService();
        userdao = mock(UserDAO.class);
        userservice.setUserdao(userdao);
        logInDTO.setUsername(username);
        logInDTO.setPassword(password);
    }

    private AddMoneyDTO buildAddMoneyDTO(float amount) {
        AddMoneyDTO addMoneyDTO = new AddMoneyDTO();
        addMoneyDTO.setAmount(amount);
        return addMoneyDTO;
    }

    private UserBankDO buildUserBankDO (float amount) {
        UserBankDO userBankDO = new UserBankDO();
        userBankDO.setBalance(amount);
        return userBankDO;
    }

    private SendMoneyDTO buildSendMoneyDTO (float amount)
    {
        SendMoneyDTO sendMoneyDTO = new SendMoneyDTO();
        sendMoneyDTO.setAmount(amount);
        return sendMoneyDTO;
    }

    private UserDO buildUserDO (float amount){
        UserDO userDO = new UserDO();
        userDO.setWallet(amount);
        return userDO;
    }

    @Test
    public void logInTest() {

        when(userdao.getUserIdByName(anyString())).thenReturn(null);
        try {
            userservice.logIn(logInDTO);
        } catch (UserException ue) {
            assertEquals("test for non-existing user", 401, ue.getError_code());
        }
    }

    @Test
    public void addMoneyIsValidMoneyTest(){
        when(userdao.getUserByToken(anyString())).thenReturn(null);
        LocalDate abc = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf(abc);
        when(userdao.getExpireTime(anyString())).thenReturn(date);
        when(userdao.getUserBank(any())).thenReturn(null);
        try{
            userservice.addMoney(token,buildAddMoneyDTO(-10));
        }catch (UserException ue){
            assertEquals("test for negative amount",400, ue.getError_code());
        }
    }

    @Test
    public void addMoneyIsEnoughMoneyTest(){
        when(userdao.getUserByToken(anyString())).thenReturn(null);
        LocalDate abc = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf(abc);
        when(userdao.getExpireTime(anyString())).thenReturn(date);
        when(userdao.getUserBank(any())).thenReturn(buildUserBankDO(500));
        try{
            userservice.addMoney(token,buildAddMoneyDTO(1000000));
        }catch (UserException ue){
            assertEquals("test for not enough balance in bank",400, ue.getError_code());
        }
    }

    @Test
    public void addMoneyIsTokenValidTest(){
        when(userdao.getUserByToken(anyString())).thenReturn(null);
        LocalDate abc = LocalDate.now().minusDays(1);
        java.sql.Date date = java.sql.Date.valueOf(abc);
        when(userdao.getExpireTime(anyString())).thenReturn(date);
        try{
            userservice.addMoney(token,null);
        }catch (UserException ue){
            assertEquals("test for expiry token",408, ue.getError_code());
        }
    }

    @Test
    public void sendMoneyIsTokenValidTest (){
        when(userdao.getUserByToken(anyString())).thenReturn(null);
        LocalDate abc = LocalDate.now().minusDays(1);
        java.sql.Date date = java.sql.Date.valueOf(abc);
        when(userdao.getExpireTime(anyString())).thenReturn(date);
        try{
            userservice.sendMoney(token,null);
        }catch (UserException ue){
            assertEquals("test for expiry token",408, ue.getError_code());
        }
    }

    @Test
    public void sendMoneyIsEnoughMoney(){

        when(userdao.getUserByToken(anyString())).thenReturn(buildUserDO(500));
        when(userdao.getUserByPhone(anyString())).thenReturn(null);
        LocalDate abc = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf(abc);
        when(userdao.getExpireTime(anyString())).thenReturn(date);
        try{
            userservice.sendMoney(token,buildSendMoneyDTO(1000000));
        }catch (UserException ue){
            assertEquals("test for not enough money to send",400, ue.getError_code());
        }
    }

}
