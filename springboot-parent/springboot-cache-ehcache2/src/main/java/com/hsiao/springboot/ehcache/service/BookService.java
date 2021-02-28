/**
 * Copyright (C), 2015-2020, XXX有限公司 FileName: BookService Author: xiao Date: 2020/10/24 12:29
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
package com.hsiao.springboot.ehcache.service;

import com.hsiao.springboot.ehcache.entity.Book;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @projectName springboot-parent
 * @title: BookService
 * @description: TODO
 * @author xiao
 * @create 2020/10/24
 * @since 1.0.0
 */
@Slf4j
@Service
public class BookService {

  private Map<Integer, Book> dataMap =
      new HashMap<Integer, Book>() {
        {
          for (int i = 1; i < 100; i++) {
            Book book = new Book(i, "name" + i, "author" + i);
            put(i, book);
          }
        }
      };


  /**
   *
   * 获取数据
   * @Cacheable：配置了findByName函数的返回值将被加入缓存。同时在查询时，会先从缓存中获取，若不存在才再发起对数据库的访问。
   *
   * <p>该注解主要有下面几个参数：
   *
   * <p>value、cacheNames：两个等同的参数（cacheNames为Spring 4新增，作为value的别名），用于指定缓存存储的集合名。
   * 由于Spring 4中新增了@CacheConfig，因此在Spring 3中原本必须有的value属性，也成为非必需项了
   * key：缓存对象存储在Map集合中的key值，非必需，缺省按照函数的所有参数组合作为key值，
   * 若自己配置需使用SpEL表达式，比如：@Cacheable(key = “#p0”)：使用函数第一个参数作为缓存的key值
   * condition：缓存对象的条件，非必需，也需使用SpEL表达式，只有满足表达式条件的内容才会被缓存，比如：@Cacheable(key = “#p0”, condition = “#p0.length() < 3”)，
   * 表示只有当第一个参数的长度小于3的时候才会被缓存
   * unless：另外一个缓存条件参数，非必需，需使用SpEL表达式。它不同于condition参数的地方在于它的判断时机，该条件是在函数被调用之后才做判断的，所以它可以通过对result进行判断。
   * keyGenerator：用于指定key生成器，非必需。若需要指定一个自定义的key生成器，
   * 我们需要去实现org.springframework.cache.interceptor.KeyGenerator接口，并使用该参数来指定。需要注意的是：该参数与key是互斥的
   * cacheManager：用于指定使用哪个缓存管理器，非必需。只有当有多个时才需要使用
   * cacheResolver：用于指定使用那个缓存解析器，非必需。
   * 需通过org.springframework.cache.interceptor.CacheResolver接口来实现自己的缓存解析器，并用该参数指定。
   *
   * @param id
   * @return
   */
  //  @Cacheable(value = "cache", key = "'Book:' + #id")
  @Cacheable(value = "book", key = "'Book:' + #result.id")
  public Book get(int id) {
    log.info("通过id{}查询获取", id);
    return dataMap.get(id);
  }

  /**
   * 更新数据
   *@CachePut：主要针对方法配置，能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用 。简单来说就是用户更新缓存数据。但需要注意的是该注解的value 和 key 必须与要更新的缓存相同，也就是与@Cacheable 相同。
   * @param id
   * @param book
   * @return
   */
  @CachePut(value = "book", key = "'Book:' + #id")
  public Book set(int id, Book book) {
    log.info("更新id{}数据", id);
    dataMap.put(id, book);
    return book;
  }

  /**
   * 删除数据
   *
   * @CacheEvict：配置于函数上，通常用在删除方法上，用来从缓存中移除相应数据。除了同@Cacheable一样的参数之外，它还有下面两个参数：
   *
   * <p>allEntries：非必需，默认为false。当为true时，会移除所有数据。
   * 如：@CachEvict(value=”testcache”,allEntries=true)
   * beforeInvocation：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。
   * 如：@CachEvict(value=”testcache”，beforeInvocation=true)
   *
   * @param id
   */
  //清除一条缓存，key为要清空的数据
  @CacheEvict(value = "book", key = "'Book:' + #id")
  public void del(int id) {
    log.info("删除id{}数据", id);
    dataMap.remove(id);
  }

    // 方法调用后清空所有缓存
    @CacheEvict(value="book",allEntries=true)
    public void delAll() {
//        dataMap.clear();
    }

    //方法调用前清空所有缓存
    @CacheEvict(value="book",beforeInvocation=true)
    public void deleteAll() {
        dataMap.clear();
    }

//    @CacheEvict(value = "book", key = "#book.id")
    @CacheEvict(value = "book", key = "'Book:' + #book.id")
//    @CachePut(value = "book", key = "'bBook:' + #book.id") //测试发现只将结果清除，key未清除，导致查询继续使用缓存但结果为空？？？？
    public void update(Book book) {
        dataMap.put(book.getId(), book);
    }
}
