package com.hsiao.springboot.transaction;

import com.hsiao.springboot.transaction.entity.Account;
import com.hsiao.springboot.transaction.service.IsolationAccountService;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 隔离级别测试使用多线程模拟并发事务操作<br/>
 * 1、读未提交隔离级别测试用例：readUncommittedTest<br/>
 * 2、读提交隔离级别测试用例：readCommittedTest<br/>
 * 3、可重复读隔离级别测试用例：repeatableReadTest<br/>
 * 4、串行化隔离级别测试用例：serializableTest隔离级别测试<br/>
 *
 * @author hsiao
 * @date 2019/1/24
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class IsolationTest {

    ThreadPoolExecutor executor = null;
    @Autowired
    IsolationAccountService isolationAccountService;

    @Before
    public void init() {
        executor = new ThreadPoolExecutor(4, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(16));
    }

    @Test
    public void readUncommittedTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.readUncommittedTest();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                isolationAccountService.getReadUncommittedAllList();
                latch.countDown();
            }
        });
        latch.await();
        System.out.println("事务结束后再次读取");
        List<Account> beforeList = isolationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        executor.shutdown();
    }

    @Test
    public void readCommittedTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.readCommittedTest();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.getReadCommittedAllList();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        latch.await();
        System.out.println("事务结束后再次读取");
        List<Account> beforeList = isolationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        executor.shutdown();
    }

    @Test
    public void repeatableReadTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.repeatableReadTest();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.getRepeatableAllList();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        latch.await();
        System.out.println("事务结束后再次读取");
        List<Account> beforeList = isolationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        executor.shutdown();
    }

    @Test
    public void serializableTest() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.serializableTest();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    isolationAccountService.getSerializableAllList();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }
        });
        latch.await();
        System.out.println("事务结束后再次读取");
        List<Account> beforeList = isolationAccountService.getAllList();
        for (Account account : beforeList) {
            System.out.println(account);
        }
        executor.shutdown();
    }
}
