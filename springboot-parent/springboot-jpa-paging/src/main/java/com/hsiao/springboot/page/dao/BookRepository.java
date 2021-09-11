package com.hsiao.springboot.page.dao;


import com.hsiao.springboot.page.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookRepository
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */
@Repository("bookRepository")
public interface BookRepository extends JpaRepository<Book, Long>
        , JpaSpecificationExecutor<Book> {


    /**
     * 根据书名，分页查询书籍信息（使用原生SQL语句）
     */
    @Query(value = "SELECT * FROM tb_book WHERE name LIKE %:name%", nativeQuery = true)
    public Page<Book> getBookPageByNameSQL(@Param("name") String name, Pageable pageable);

    /**
     * 根据书名，分页查询书籍信息（使用JPQL语句）
     */
    @Query("SELECT b FROM Book b WHERE b.name LIKE %:name%")
    public Page<Book> getUserPageByNameJPQL(@Param("name") String name, Pageable pageable);

    /**
     * 根据author字段查询book表数据，传入Pageable分页参数，不需要自己写SQL
     * @param author
     * @param pageable
     * @return
     */
    Page<Book> findByAuthor(String author, Pageable pageable);

    /**
     *
     * 根据author和isbn字段查询book表数据，传入Pageable分页参数，不需要自己写SQL
     *
     * 什么时候使用Slice？什么时候使用Page？
     *
     * 答：通过这两个接口的函数定义可以看出，
     * Slice只关心是不是存在下一个分片(分页)，不会去数据库count计算总条数、总页数。所以比较适合大数据量列表的的鼠标或手指滑屏操作，不关心总共有多少页，只关心有没有下一页。
     * Page比较适合传统应用中的table开发，需要知道总页数和总条数
     */
    Slice<Book> findByAuthor(String author, String isbn, Pageable pageable);

}

