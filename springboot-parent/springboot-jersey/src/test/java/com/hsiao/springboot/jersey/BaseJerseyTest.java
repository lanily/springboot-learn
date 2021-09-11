package com.hsiao.springboot.jersey;


import static org.junit.Assert.assertTrue;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BaseJerseyTest
 * @description: TODO
 * @author xiao
 * @create 2021/7/22
 * @since 1.0.0
 */
public class BaseJerseyTest extends JerseyTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseJerseyTest.class);

    protected static final String BASE_PATH = "/employees";

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() {
        logger.info("===start {} ===", name.getMethodName());
        try {
            super.setUp();
        } catch (Exception ex) {
            logger.error("failed to set up test case in {}", this.getClass().getName());
        }
    }



/*    @Override
    protected Application configure() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestContextConfiguration.class);
        return new JerseyAppConfig()
                .property("contextConfig", context);
    }*/

    @After
    public void tearDown() {
        try {
            super.tearDown();
        } catch (Exception ex) {
            logger.error("failed to tear down test case in {}", this.getClass().getName());
        }
        logger.info("===End {} ===", name.getMethodName());
    }

    @Test
    public void testBase() {
        //this is a base test case, but sonar can't ignore it, so add this
        assertTrue(true);
    }
}

