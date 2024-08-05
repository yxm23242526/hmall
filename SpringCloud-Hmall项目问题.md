# SpringCloud-Hmall项目问题

Q：报错提示如下

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'globalTransactionScanner' defined in class path resource [io/seata/spring/boot/autoconfigure/SeataAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [io.seata.spring.annotation.GlobalTransactionScanner]: Factory method 'globalTransactionScanner' threw exception; nested exception is java.lang.ExceptionInInitializerError
  at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:658) ~[spring-beans-5.3.3.jar:5.3.3]
  at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:638) ~[spring-beans-5.3.3.jar:5.3.3]
```

A：

```
在jvm启动参数上添加--add-opens=java.base/java.lang=ALL-UNNAMED
```

----

Q：Nacos权限问题

A：

```
config_info和his_config_info的encrypted_data_key的Not NUll不勾选
```



Q：seata的application.yaml文件连接数据库失败

A：

```
由于docker的mysql名字叫cloud，所以连接时需要修改成
url: jdbc:mysql://cloud:3306/seata?rewriteBatchedStatements=true&serverTimezone=UTC
而不是cloud
```

