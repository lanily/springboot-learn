> 原项目地址 [sb-jpa-batch-insert-demo](https://github.com/Cepr0/sb-jpa-batch-insert-demo)

> 原文地址 [how-to-do-bulk-multi-row-inserts-with-jparepository](https://stackoverflow.com/questions/50772230/how-to-do-bulk-multi-row-inserts-with-jparepository)

使用 Spring Data JPA 在应用程序中批量插入的示例

1. 将 `spring.jpa.properties.hibernate.jdbc.batch_size` 选项设置为您需要的值。
2. 使用 `repo` 的 `saveAll()` 方法插入的实体集合。

运行此应用程序并查看日志：

```
2019-06-16 01:05:08.367  INFO 98762 --- [           main] jdbc.sqlonly                             : batching 5 statements: 1: insert into application$model (number, id) values (0, '<byte[]>') 
2: insert into application$model (number, id) values (1, '<byte[]>') 3: insert into application$model 
(number, id) values (2, '<byte[]>') 4: insert into application$model (number, id) values (3, 
'<byte[]>') 5: insert into application$model (number, id) values (4, '<byte[]>') 

2019-06-16 01:05:08.372  INFO 98762 --- [           main] jdbc.sqlonly                             : batching 5 statements: 1: insert into application$model (number, id) values (5, '<byte[]>') 
2: insert into application$model (number, id) values (6, '<byte[]>') 3: insert into application$model 
(number, id) values (7, '<byte[]>') 4: insert into application$model (number, id) values (8, 
'<byte[]>') 5: insert into application$model (number, id) values (9, '<byte[]>') 
```


[示例代码库](https://github.com/AnghelLeonard/Hibernate-SpringBoot/tree/master/HibernateSpringBootBatchInsertsJpaRepository)

## [#](https://ifzm.cn/views/java/springboot-data-jpa-shu-ju-pi-liang-xie-ru-huo-geng-xin-pei-zhi.html#批处理)批处理

默认情况下，100 条数据插入，将导致 100 次 Insert 语句的执行，但在数据量较大的情况下，情况很糟糕，因为会引起 N 次的数据库交互操作。

我们可通过一些配置使其支持批处理，实现批量插入的一种方法是使用`SimpleJpaRepository#saveAll(Iterable< S> entities)`方法。在这里，我们使用 MySQL 执行此操作。推荐的批量大小在 5 到 30 之间。

一些关键点：

1. 在 `application.properties`,集 `spring.jpa.properties.hibernate.jdbc.batch_size`
2. 在 `application.properties`,集 `spring.jpa.properties.hibernate.generate_statistics`（只是为了检查将批处理工作）
3. 在 `application.properties`,带有的 JDBC URL 中 `rewriteBatchedStatements=true` （针对 MySQL 的优化）
4. 在 `application.properties` 组 JDBC URL 与 `cachePrepStmts=true`（启用缓存，如果您决定设置是很有用的 `prepStmtCacheSize`， `prepStmtCacheSqlLimit` 等为好;如果没有这个设置缓存被禁用）
5. 在 `application.properties` 带有 JDBC URL 的情况下 `useServerPrepStmts=true`（通过这种方式，您可以切换到服务器端准备好的语句（可能会大大提高性能））
6. 在实体中，使用[分配的生成器](https://vladmihalcea.com/how-to-combine-the-hibernate-assigned-generator-with-a-sequence-or-an-identity-column/)，因为 MySQL `IDENTITY` 将导致插入批处理被禁用
7. 在实体中，添加带有注释的属性， `@Version` 以避免 `SELECT` 在批处理之前被过度激发（还可以防止多请求事务中的更新丢失）。预算外资金 `SELECTs` 使用的效果 `merge()`，而不是 `persist()`。在后台，`saveAll()`利用 `save()`,其在非新的实体的情况下（实体有标识），将调用 `merge()`，指示 `Hibernate` 来火一个 `SELECT` 声明，以确保存在具有相同标识符的数据库没有记录。
8. 请注意传递 `saveAll()`给不“淹没”持久性上下文的插入次数。通常，`EntityManager` 应该不时地刷新和清除，但是在 `saveAll()`执行过程中，您根本无法做到这一点，因此，如果 `saveAll()`列表中包含大量数据，则所有这些数据都将到达持久性上下文（一级缓存） ），并且会一直保留到刷新时间。使用相对少量的数据应该可以。
9. 该 `saveAll()` 方法返回一个 `List<S>` 包含持久实体的；每个持久实体都添加到该列表中；如果您只是不需要它， `List` 那么它就一无所有
10. 要获取大量数据，请 `saveAll()` 每批次调用 一次并将批次大小设置为 5 到 30。


https://www.baeldung.com/jpa-hibernate-batch-insert-update
