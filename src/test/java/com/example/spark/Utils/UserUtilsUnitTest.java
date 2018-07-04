package com.example.spark.Utils;

import com.example.spark.Utils.UserUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UserUtilsUnitTest {
    UserUtils userUtils;
    final static String msg = "password";
    final static String key = "123";
    @Before
    public void setData() {
        userUtils = new UserUtils();
    }
    @Test
    public void checkHmac() {
        final String actualToken = userUtils.hmacSha1(msg,key);
        final String expectedToken="5b432a96fcc4be540d7bd2fb1e164954963992af45fc9bf572d7f1233d67fce7";
        assertEquals("expected token should equal to the token generated",expectedToken,actualToken);
    }

}
