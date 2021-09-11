package com.hsiao.springboot.test.convert;


import com.hsiao.springboot.test.entity.Credentials;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: CustomCredentialsConverter
 * @description: TODO
 * @author xiao
 * @create 2021/4/22
 * @since 1.0.0
 */
@Component
@ConfigurationPropertiesBinding
public class CustomCredentialsConverter implements Converter<String, Credentials> {

    @Override
    public Credentials convert(String source) {
        String[] data = source.split(",");
        return new Credentials(data[0], data[1]);
    }
}

