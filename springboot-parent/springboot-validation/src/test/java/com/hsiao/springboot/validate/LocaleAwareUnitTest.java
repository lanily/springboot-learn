package com.hsiao.springboot.validate;


import java.util.Locale;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: LocaleAwareUnitTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/12
 * @since 1.0.0
 */
public abstract class LocaleAwareUnitTest {
    private static Locale previousDefault;

    @BeforeClass
    public static void setupLocale() {
        previousDefault = Locale.getDefault();

        Locale.setDefault(Locale.CHINA);
    }

    @AfterClass
    public static void resetLocale() {
        Locale.setDefault(previousDefault);
    }

}

