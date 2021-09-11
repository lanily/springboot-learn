package com.hsiao.springboot.page.service;


import com.hsiao.springboot.page.dao.BookRepository;
import com.hsiao.springboot.page.entity.Book;
import com.hsiao.springboot.page.entity.BookQuery;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: BookServiceImpl
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */
public class BookServiceImpl implements BookService {

    @Resource
    BookRepository bookRepository;

    @Override
    public Page<Book> findBookNoCriteria(Integer page, Integer size) {
        if (page <= 0) {
            page = 1;
        }
        // spring boot 2.0 以前，2.0版本也适用，但是2.0版本推荐使用上面的方式
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        // spring boot 2.0推荐写法
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> findBookCriteria(Integer page, Integer size, final BookQuery bookQuery) {
        if (page <= 0) {
            page = 1;
        // spring boot 2.0 以前，2.0版本也适用，但是2.0版本推荐使用上面的方式
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        }
//        Pageable pageRequest = PageRequest.of(page - 1, size);
        // spring boot 2.0推荐写法
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "id");
        Page<Book> bookPage = bookRepository.findAll(new Specification<Book>() {
            /**
             * 构造断言
             * @param root 实体对象引用
             * @param query 规则查询对象
             * @param criteriaBuilder 规则构建对象
             * @return 断言
             */
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                    CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != bookQuery.getName() && !"".equals(bookQuery.getName())) {
                    list.add(criteriaBuilder
                            .equal(root.get("name").as(String.class), bookQuery.getName()));
                }
                if (null != bookQuery.getIsbn() && !"".equals(bookQuery.getIsbn())) {
                    list.add(criteriaBuilder
                            .equal(root.get("isbn").as(String.class), bookQuery.getIsbn()));
                }
                if (null != bookQuery.getAuthor() && !"".equals(bookQuery.getAuthor())) {
                    list.add(criteriaBuilder
                            .equal(root.get("author").as(String.class), bookQuery.getAuthor()));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return bookPage;
    }

    /*@Override
public Page<Book> findBookCriteria(Integer page, Integer size, final BookQuery bookQuery) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<Book> bookPage = bookRepository.findAll(new Specification<Book>(){
@Override
public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate p1 = criteriaBuilder.equal(root.get("name").as(String.class), bookQuery.getName());
        Predicate p2 = criteriaBuilder.equal(root.get("isbn").as(String.class), bookQuery.getIsbn());
        Predicate p3 = criteriaBuilder.equal(root.get("author").as(String.class), bookQuery.getAuthor());
        query.where(criteriaBuilder.and(p1,p2,p3));
        return query.getRestriction();
        }
        },pageable);
        return bookPage;
        }
*/

}




