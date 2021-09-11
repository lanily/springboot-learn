package com.hsiao.springboot.kafka.avro;


import com.hsiao.springboot.kafka.model.User;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: AvroKafkaDemo
 * @description: TODO
 * @author xiao
 * @create 2021/3/7
 * @since 1.0.0
 */
public class AvroKafkaDemo {

    public static void main(String[] args) {

        Producer<User> producer = new Producer<>();
        Consumer<User> consumer = new Consumer<>();

        System.out.println("Please input 'send', 'receive', or 'exit'");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.next();

            switch (input) {
                case "send":
                    producer.sendData(Topic.USER,
                            new User(3, "Yanbin", 18, "Address: " + new Random().nextInt()));
                    break;
                case "receive":
                    List<User> users = consumer.receive(Topic.USER);
                    if (users.isEmpty()) {
                        System.out.println("Received nothing");
                    } else {
                        users.forEach(user -> System.out.println("Received user: " + user));
                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please input 'send', 'receive', or 'exit'");
            }
        }
    }
}

