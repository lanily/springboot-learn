package com.hsiao.security.validate.code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hsiao.security.properties.SecurityProperties;
import com.hsiao.security.sender.DefaultSmsCodeSender;
import com.hsiao.security.sender.SmsCodeSender;
import com.hsiao.security.validate.code.gennerator.ImageCodeGenerator;
import com.hsiao.security.validate.code.gennerator.ValidateCodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ValidateCodeBeanConfig {
	@Autowired
    private SecurityProperties securityProperties;
	
	// 定义图片验证码生成器
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        log.info("init default imageValidateCodeGenerator");
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    // 定义短信验证码发送器
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
    	log.info("init default smsCodeSender");
        return new DefaultSmsCodeSender();
    }
}
