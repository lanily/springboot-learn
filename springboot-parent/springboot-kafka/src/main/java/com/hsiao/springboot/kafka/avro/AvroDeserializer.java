package com.hsiao.springboot.kafka.avro;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import lombok.SneakyThrows;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

/**
 *
 * Avro 对象的反序列化类 AvroDeserializer
 *
 * @projectName springboot-parent
 * @title: AvroDeserializer
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @SneakyThrows
    @Override
    public T deserialize(String topic, byte[] data) {

        DatumReader<T> userDatumReader = new SpecificDatumReader<>(
                Topic.matchFor(topic).topicType.getSchema());
        BinaryDecoder binaryEncoder = DecoderFactory
                .get().directBinaryDecoder(new ByteArrayInputStream(data), null);
        try {
            return userDatumReader.read(null, binaryEncoder);
        } catch (IOException e) {
            throw new DeserializationException(e.getMessage());
        }
    }

    @Override
    public void close() {

    }
}

