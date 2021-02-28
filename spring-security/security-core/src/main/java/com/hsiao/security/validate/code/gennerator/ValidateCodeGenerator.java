package com.hsiao.security.validate.code.gennerator;

import org.springframework.web.context.request.ServletWebRequest;
import com.hsiao.security.validate.code.ValidateCode;


public interface ValidateCodeGenerator {
	
    ValidateCode generate(ServletWebRequest request);

}
