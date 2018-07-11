package com.example.spark.service;


import com.example.spark.Utils.UserUtils;
import com.example.spark.api.dto.request.AddMoneyDTO;
import com.example.spark.api.dto.request.LogInDTO;
import com.example.spark.api.dto.request.SendMoneyDTO;
import com.example.spark.api.dto.request.UserRequestDTO;
import com.example.spark.api.dto.response.UserBankDTO;
import com.example.spark.api.dto.response.UserResponseDTO;
import com.example.spark.api.dto.response.UserWalletDTO;
import com.example.spark.api.errorhandler.UserException;
import com.example.spark.dao.UserDAO;
import com.example.spark.dataobject.UserBankDO;
import com.example.spark.dataobject.UserDO;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import org.apache.commons.codec.binary.Hex;

@Service
public class UserService {

    @Autowired
    private UserDAO userdao;

    public void setUserdao(UserDAO userdao) {
        this.userdao = userdao;
    }

    @Transactional
    public UserResponseDTO insertUser(UserRequestDTO userdto)
    {
        UserDO userDO = new UserDO(userdto);
        try{userDO.setId(userdao.getUserId(userDO.getPhone()));}catch(RuntimeException re){}//to check phone no. already exist or not
        if(userDO.getId()!=null)
        {throw new UserException(userDO.getPhone()+" already registered ",400);}
        try{userDO.setId(userdao.getUserIdByName(userDO.getUsername()));}catch(RuntimeException re){}//to check username already exist or not
        if(userDO.getId()!=null)
        {throw new UserException(userDO.getUsername()+" already registered ",400);}
        String password = userDO.getPassword();
        String sha256Hex = DigestUtils.sha256Hex(password+userDO.getUsername());
        password = sha256Hex;
        userDO.setPassword(password);
        userDO.setWallet(200);
        userDO = userdao.insertUser(userDO);

        userdto.setWallet(200);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userdto);
        return userResponseDTO;
    }

    @Transactional
    public UserResponseDTO updateUser(UserRequestDTO userdto, String token)
    {
        UserDO userDO = new UserDO(userdto);
        /*try{userDO.setId(userdao.getUserIdByName(userDO.getUsername()));}catch(RuntimeException re){}
        if(userDO.getId()!=null)
        {throw new UserException(userDO.getUsername()+" already registered ",400);}*/
        userDO = userdao.updateUser(userDO, token);
        userDO = userdao.getUserByToken(token);
        userDO.setWallet(userdao.getWallet(token));
        UserResponseDTO userResponseDTO = new UserResponseDTO(userDO);
        return userResponseDTO;
    }

    public void deleteUser(String phone)
    {
        try{
            UserDO userDO = userdao.getUserByPhone(phone);
        }catch(RuntimeException re){
            throw new UserException(phone+" not registered",400);
        }
        userdao.deleteUser(phone);
    }

   @Transactional
   public UserResponseDTO getUser(String phone)
   {
       UserDO userDO = new UserDO();
       try{ userDO = userdao.getUserByPhone(phone); }
       catch(RuntimeException re){ throw new UserException(phone+" not registered",404);}
       UserResponseDTO userdto = new UserResponseDTO();
       userdto.setFirst_name(userDO.getFirst_name());
       userdto.setLast_name(userDO.getLast_name());
       userdto.setPhone(userDO.getPhone());
       userdto.setUsername(userDO.getUsername());
       userdto.setWallet(userDO.getWallet());
       return userdto;

   }

   @Transactional
   public UserWalletDTO getWallet(String token)
   {
       UserDO userDO = new UserDO();
       try{ userDO = userdao.getUserByToken(token); }
       catch(RuntimeException re){ throw new UserException(" Please logIn again ",401);}
       java.sql.Date expDate = userdao.getExpireTime(token);
       java.sql.Date currDate = java.sql.Date.valueOf(LocalDate.from(LocalDate.now()));
       if(currDate.compareTo(expDate)>0)
       {
           throw new UserException("Session timed out, please logIn again", 408);
       }
       UserWalletDTO userWalletDTO = new UserWalletDTO();
       userWalletDTO.setWallet(userdao.getWallet(token));
       return userWalletDTO;
   }

   @Transactional
   public UserBankDTO getBankDetails(String token)
   {
       UserDO userDO = new UserDO();
       try{ userDO = userdao.getUserByToken(token); }
       catch(RuntimeException re){ throw new UserException("please logIn again",401);}
       LocalDate abc = LocalDate.now();
       java.sql.Date date = java.sql.Date.valueOf(abc);
       if((date.compareTo(userdao.getExpireTime(token)))>0)
       {
           throw new UserException("Session Timed Out, please logIn again", 408);
       }
       UserBankDO userBankDO = userdao.getUserBank(userDO);
       UserBankDTO userBankDTO = new UserBankDTO(userBankDO);
       return userBankDTO;
   }

   @Transactional
   public UserWalletDTO addMoney(String token, AddMoneyDTO addMoneyDTO)
   {
       UserDO userDO = new UserDO();
       try{ userDO = userdao.getUserByToken(token); }
       catch(RuntimeException re){ throw new UserException("please logIn again",401);}
       LocalDate abc = LocalDate.now();
       java.sql.Date date = java.sql.Date.valueOf(abc);
       if((date.compareTo(userdao.getExpireTime(token)))>0)
       {
           throw new UserException("Session Timed Out, please logIn again", 408);
       }
       UserBankDO userBankDO = userdao.getUserBank(userDO);
       if(addMoneyDTO.getAmount()<=0){throw new UserException ("Please Enter a vali" +
               "d amount to add", 400);}
       if(addMoneyDTO.getAmount()>userBankDO.getBalance())
       { throw new UserException("not sufficient bank balance", 400);}
       userdao.addMoney(userDO,addMoneyDTO.getAmount(),userBankDO);
       UserWalletDTO userWalletDTO = new UserWalletDTO();
       userWalletDTO.setWallet(userdao.getWallet(token));
       return userWalletDTO;
   }

   @Transactional
   public UserWalletDTO sendMoney(String token, SendMoneyDTO sendMoneyDTO)
   {
       UserDO sender = new UserDO();
       try{ sender = userdao.getUserByToken(token); }
       catch(RuntimeException re){ throw new UserException(" please logIn again ",401);}
       LocalDate abc = LocalDate.now();
       java.sql.Date date = java.sql.Date.valueOf(abc);
       if((date.compareTo(userdao.getExpireTime(token)))>0)
       {
           throw new UserException("Session Timed Out, please logIn again", 408);
       }
       UserDO receiver = new UserDO();
       try{receiver = userdao.getUserByPhone(sendMoneyDTO.getPhone());}
       catch(RuntimeException re){ throw new UserException("receiver account does not exist",400);}
       if(sendMoneyDTO.getAmount()>sender.getWallet())
       {
           float need = sendMoneyDTO.getAmount()-sender.getWallet();
           throw new UserException("Not Sufficient Wallet Money, add " + need + " money more ", 400);
       }
       userdao.sendMoney(sender,sendMoneyDTO.getAmount(),receiver);
       UserWalletDTO userWalletDTO = new UserWalletDTO();
       userWalletDTO.setWallet(userdao.getWallet(token));
       return userWalletDTO;
   }

   @Transactional
   public String logIn(LogInDTO logInDTO)
   {

       UserDO userDO = new UserDO();
       userDO.setId(userdao.getUserIdByName(logInDTO.getUsername()));
       if(userDO.getId()==null)
       {throw new UserException("Username or password is incorrect ",401);}
       String password = logInDTO.getPassword();
       String sha256Hex = DigestUtils.sha256Hex(password+logInDTO.getUsername());
       password = sha256Hex;
       if(password.compareTo(userdao.getPasswordById(userDO.getId()))!=0)
       {
           throw new UserException("Username or password is incorrect ", 401);
       }
       SecureRandom csprng = new SecureRandom();
       Long salt = csprng.nextLong();
       String key = salt.toString();
       UserUtils userUtils = new UserUtils();
       String token = userUtils.hmacSha1(password,key);
       java.sql.Date date = java.sql.Date.valueOf(LocalDate.from(LocalDate.now()).plusDays(1));
       userdao.setToken(key,token,date, userDO.getId());
       return token;
   }

}
