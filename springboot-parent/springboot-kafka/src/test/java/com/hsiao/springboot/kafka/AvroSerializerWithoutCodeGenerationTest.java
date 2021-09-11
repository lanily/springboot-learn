package com.hsiao.springboot.kafka;


import java.io.File;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.junit.Test;

/**
 *
 * 通过不生成代码的方式使用avro序列化User对象
 *
 * @projectName springboot-parent
 * @title: AvroSerializerWithoutCodeGenerationTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroSerializerWithoutCodeGenerationTest {

    @Test
    public void serialize() throws IOException {

        String avscFilePath =
                AvroSerializerWithoutCodeGenerationTest.class.getClassLoader()
                        .getResource("user.avsc").getPath();
        Schema schema = new Schema.Parser().parse(new File(avscFilePath));

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("id", 6);
        user1.put("name", "Tony");
        user1.put("age", 18);
        user1.put("address", "beijing");

        GenericRecord user2 = new GenericData.Record(schema);
        user1.put("id", 7);
        user2.put("name", "Ben");
        user2.put("age", 23);
        user2.put("address", "shanghai");

        File file = new File("user2.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(
                datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();
    }
}

