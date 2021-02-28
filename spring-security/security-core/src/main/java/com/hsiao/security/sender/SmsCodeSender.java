package com.hsiao.security.sender;

public interface SmsCodeSender {
    void send(String mobile, String code);
}
