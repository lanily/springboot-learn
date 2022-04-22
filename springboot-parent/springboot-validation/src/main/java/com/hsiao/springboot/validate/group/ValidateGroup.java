package com.hsiao.springboot.validate.group;


import javax.validation.groups.Default;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: ValidateGroup
 * @description: TODO
 * @author xiao
 * @create 2021/9/11
 * @since 1.0.0
 */
public interface ValidateGroup {

    interface RouteValidStart {

    }

    interface RouteValidEnd {

    }

    interface Create extends Default {
    }

    interface Update extends Default{
    }
}

