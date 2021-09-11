package com.hsiao.springboot.page.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: Book
 * @description: TODO
 * @author xiao
 * @create 2021/3/28
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="tb_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "isbn")
    private String isbn;

    @Column(nullable = false, name = "author")
    private String author;
}

