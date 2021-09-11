package com.hsiao.springboot.kafka;


import com.hsiao.springboot.kafka.util.AvroUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AvroUtilTest
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroUtilTest {

    @Test
    public void test() throws IOException {
        String schema = "{" +
                "   \"type\": \"record\"," +
                "   \"name\": \"user\"," +
                "   \"fields\": [" +
                "       {" +
                "           \"name\": \"name\"," +
                "           \"type\": [\"null\", \"string\"]" +
                "       }," +
                "       {" +
                "           \"name\": \"age\"," +
                "           \"type\": [\"null\", \"int\"]" +
                "       }," +
                "       {" +
                "           \"name\": \"gender\"," +
                "           \"type\": [\"null\", \"boolean\"]" +
                "       }," +
                "       {" +
                "           \"name\": \"info\"," +
                "           \"type\": [\"null\", \"bytes\"]" +
                "       }," +
                "       {" +
                "           \"name\": \"info2\"," +
                "           \"type\": [\"null\", \"string\"]" +
                "       }" +
                "   ]" +
                "}";
        Map<String, Object> data = new HashMap<>();
        data.put("name", "xiaoming");
        data.put("age", 18);
        data.put("gender", true);
        data.put("info", ByteBuffer.wrap(new byte[]{1, 2, 3}));
        data.put("info2", null);
        byte[] bytes = AvroUtil.mapToByte(schema, data);
        GenericRecord record = AvroUtil.byteToRecord(schema, bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println(record);
    }
}

