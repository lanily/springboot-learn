package com.hsiao.springboot.kafka;


import java.io.File;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

/**
 *
 * AvroDeSerializerWithoutCodeGenerationTest
 *
 * @projectName springboot-parent
 * @title: AvroDeSerializerWithoutCodeGenerationTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroDeSerializerWithoutCodeGenerationTest {

    public void deserialize() throws IOException {
        String avscFilePath =
                AvroDeSerializerWithoutCodeGenerationTest.class.getClassLoader()
                        .getResource("user.avsc").getPath();
        Schema schema = new Schema.Parser().parse(new File(avscFilePath));
        File file = new File("user2.avro");
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file,
                datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}

