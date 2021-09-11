    -- 判断数据表是否存在，存在则删除
    DROP TABLE IF EXISTS tb_book;

    -- 创建“用户信息”数据表
    CREATE TABLE IF NOT EXISTS tb_book
    (
        id INT AUTO_INCREMENT PRIMARY KEY COMMENT '书籍编号',
        name VARCHAR(50) NOT NULL COMMENT '书籍名',
        isbn VARCHAR(50) NOT NULL COMMENT '国际标准书号',
        author VARCHAR(50) NOT NULL COMMENT '作者',
        create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
        ) COMMENT = '书籍信息表';

    -- 添加数据
    INSERT INTO tb_book(name,isbn, author) VALUES
    ('Spring实战','0118', 'Bob'),
    ('Netty实战','0119', 'Alex'),
    ('Hibernate实战','0120', 'Brian'),
    ('Docker实战','0121', 'Jerry'),
    ('Java核心技术','0122', 'Tom'),
    ('Hadoop实战','0123', 'Olivia'),
    ('Spark实战','0124', 'Ella'),
    ('Storm实战','0125', 'Lily'),
    ('Flink实战','0126', 'John'),
    ('MySQL实战','0127', 'Luke'),
    ('Mybatis实战','0128', 'Kevin'),
    ('JVM实战','0129', 'Charles'),
    ('SpringBoot实战','0130', 'Albert'),
    ('Redis实战','0131', 'Nick'),
    ('Mongodb实战','0132', 'Paul'),
    ('Nginx实战','0133', 'Fred'),
    ('Kafka实战','0134', 'Jack');