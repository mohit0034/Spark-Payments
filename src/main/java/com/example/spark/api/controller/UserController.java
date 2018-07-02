package com.example.spark.api.controller;


import com.example.spark.api.dto.request.LogInDTO;
import com.example.spark.api.errorhandler.ErrorResponse;
import com.example.spark.api.dto.request.AddMoneyDTO;
import com.example.spark.api.dto.request.SendMoneyDTO;
import com.example.spark.api.dto.request.UserRequestDTO;
import com.example.spark.api.dto.response.UserBankDTO;
import com.example.spark.api.dto.response.UserResponseDTO;
import com.example.spark.api.dto.response.UserWalletDTO;
import com.example.spark.api.errorhandler.UserException;
import com.example.spark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired

    private UserService userservice;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public UserResponseDTO insertUser(@RequestBody UserRequestDTO userdto) {
        UserResponseDTO userResponsedto = userservice.insertUser(userdto);
        return userResponsedto;
    }

    @GetMapping(value = "/{phone}")
    public UserResponseDTO getUser(@PathVariable("phone") String phone)
    {
            return userservice.getUser(phone);
    }

    @RequestMapping(value = "/{phone}", method = RequestMethod.DELETE)
    public ResponseEntity<ErrorResponse> deleteUser(@PathVariable("phone") String phone){
            userservice.deleteUser(phone);
            ErrorResponse error = new ErrorResponse();
            error.setMessage("User with phone "+ phone+" deleted Successfully");
            return new ResponseEntity<ErrorResponse>(error,HttpStatus.OK);
    }

    @RequestMapping(value="/{phone}", method = RequestMethod.PUT)
    public UserResponseDTO updateUser(@RequestBody UserRequestDTO userdto)
    {
        UserResponseDTO userResponseDTO = userservice.updateUser(userdto);
        return userResponseDTO;
    }

    @RequestMapping(value="/wallet", method = RequestMethod.GET)
    public UserWalletDTO getWallet(@RequestHeader("token") String token){
        UserWalletDTO userWalletDTO = userservice.getWallet(token);
        return userWalletDTO;
    }
    @RequestMapping(value="/bank", method = RequestMethod.GET)
    public UserBankDTO getBankDetails(@RequestHeader("token") String token){
        UserBankDTO userBankDTO = userservice.getBankDetails(token);
        return userBankDTO;
    }
    @RequestMapping(value = "/add_money", method = RequestMethod.PUT)
    public UserWalletDTO addMoney(@RequestHeader("token") String token, @RequestBody AddMoneyDTO addMoneyDTO){
        UserWalletDTO userWalletDTO = userservice.addMoney(token,addMoneyDTO);
        return userWalletDTO;
    }
    @RequestMapping(value = "/send_money", method = RequestMethod.PUT)
    public UserWalletDTO sendMoney(@RequestHeader("token") String token, @RequestBody SendMoneyDTO sendMoneyDTO){
        UserWalletDTO userWalletDTO = userservice.sendMoney(token,sendMoneyDTO);
        return userWalletDTO;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity logIn(@RequestBody LogInDTO logInDTO){
        String token = userservice.logIn(logInDTO);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token" , token);
        return new ResponseEntity(responseHeaders,HttpStatus.OK);
    }
}
