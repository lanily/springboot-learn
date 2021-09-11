package com.hsiao.springboot.test.entity;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: User
 * @description: TODO
 * @author xiao
 * @create 2021/4/11
 * @since 1.0.0
 */
@Entity
@Data
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    private String password;

    @CreationTimestamp
    private Date createDate;

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public final boolean finalMethod(){
        return true;
    }
}

