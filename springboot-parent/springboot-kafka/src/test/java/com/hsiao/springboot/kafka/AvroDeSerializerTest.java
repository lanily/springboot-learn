package com.hsiao.springboot.kafka;


import com.hsiao.springboot.kafka.model.User;
import java.io.File;
import java.io.IOException;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.Test;

/**
 *
 *
 *  解析 avro 序列化后的对象
 * @projectName springboot-parent
 * @title: AvroDeSerializerTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroDeSerializerTest {

    @Test
    public void testDeserialize() throws IOException {
        DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<User>(new File("users.avro"),
                userDatumReader);
        User user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}

