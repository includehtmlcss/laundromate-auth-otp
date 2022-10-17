package com.example.demo;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//@Service("twilio")
//public class TwilioSmsSender implements SmsSender {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);
//    private final TwilioConfiguration twilioConfiguration;
//    private Map<String,String> otpMap;
//    @Autowired
//    public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
//        this.twilioConfiguration = twilioConfiguration;
//        otpMap=new HashMap<>();
//    }
//    @Override
//    public void sendSms(SmsRequest smsRequest) {
//        if (isPhoneNumberValid(smsRequest.getPhoneNumber())) {
//            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
//            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
//            String message = smsRequest.getMessage();
//            MessageCreator creator = Message.creator(to, from, message);
//            creator.create();
//            LOGGER.info("Send sms {}", smsRequest);
//        } else {
//            throw new IllegalArgumentException(
//                    "Phone number [" + smsRequest.getPhoneNumber() + "] is not a valid number"
//            );
//        }
//    }
//    private boolean isPhoneNumberValid(String phoneNumber) {
//
//        return true;
//    }
//}

@Service("twilio")
public class TwilioSmsSender implements SmsSender {
    @Autowired
    private TwilioConfiguration twilioConfiguration;
    Map<String,String> otpMap=new HashMap<>();
    public void sendSms(SmsRequest smsRequest)
    {
        PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
        String otp = generateOTP();
        String otpMessage = "Dear customer, your OTP is #" + otp + "#.";
        Message message = Message.creator(to, from, otpMessage).create();
        otpMap.put(smsRequest.getPhoneNumber(),otp);
    }
    public String validateOTP(String userInputOtp,String phoneNumber)
    {
        if(userInputOtp.equals(otpMap.get(phoneNumber)))
        {
            return "Valid Otp";
        }
        else
        {
            return "Invalid Otp";
        }
    }
    private String generateOTP()
    {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}