package com.hsiao.springboot.kafka;


import com.hsiao.springboot.kafka.model.User;
import java.io.File;
import java.io.IOException;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

/**
 *
 * 使用 avro 对 com.avro.example.User 类的对象进行序列化
 *
 * @projectName springboot-parent
 * @title: AvroSerializerTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        User user1 = new User();
        user1.setId(1);
        user1.setName("Tom");
        user1.setAge(8);
        user1.setAddress("shenzhen");

        User user2 = new User(2, "Jack", 18, "shanghai");

        User user3 = User.newBuilder()
                .setId(3)
                .setName("Harry")
                .setAge(28)
                .setAddress("beijing")
                .build();

        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), new File("users.avro"));
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();
    }
}

