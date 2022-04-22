package com.hsiao.springboot.websocket.stomp.util;


import com.hsiao.springboot.websocket.stomp.model.WebSocketUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Jwt工具类
 * 注意点:
 * 1、生成的token, 是可以通过base64进行解密出明文信息
 * 2、base64进行解密出明文信息，修改再进行编码，则会解密失败
 * 3、无法作废已颁布的token，除非改秘钥
 * JSON WEB TOKEN 结构组成：
 * (1)Header(头部)：包含加密算法，通常直接使用 HMAC SHA256
 * (2)Payload(负载)：存放有效信息，比如消息体、签发者、过期时间、签发时间等
 * (3)Signature(签名)：由header(base64后的)+payload(base64后的)+secret(秘钥)三部分组合，然后通过head中声明的算法进行加密
 * @projectName springboot-parent
 * @title: JwtUtils
 * @description: TODO
 * @author xiao
 * @create 2022/4/19
 * @since 1.0.0
 */
public class JwtUtils {

    /**
     * 过期时间，一天
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    /**密钥*/
    public static final String APP_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(APP_SECRET.getBytes());
        /*
        使用len的第一个len字节构造来自给定字节数组的key ，从offset开始。
        构成密钥的字节是key[offset]和key[offset+len-1]之间的字节。
        参数
            key - 密钥的密钥材料。 将复制以offset开头的数组的第一个len字节，以防止后续修改。
            offset - 密钥材料开始的 key中的偏移量。
            len - 密钥材料的长度。
            algorithm - 与给定密钥材料关联的密钥算法的名称。 AES是一种对称加密算法
         */
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 获取token方法 :
     *           userId 是要存到token的用户信息， 如有需要可以添加更多
     * @param userId
     * @return
     */
    public static String getToken(String userId) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("userId", userId);

        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //jwt编号:随机产生
                .setId(UUID.randomUUID().toString())
                //设置jwt主题
                .setSubject("test")
                //签发者信息
                .setIssuer("xiao")
                //签发时间
                .setIssuedAt(new Date(currentTime))
                //过期时间戳
                .setExpiration(new Date(currentTime + EXPIRE))
//                .claim("company", "hsbc")
                //自定义
                .addClaims(claimMaps)
                // 签名算法以及密匙
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .compact();
    }

    /**
     * 获取token中的claims信息
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token);
    }

    /**
     * 获取payload body信息(指的是token中Payload部分)
     * @param token
     * @return Claims 是Map
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public static JwsHeader getClaimsHeader(String token) {
        try {
            return getJws(token).getHeader();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public static String getClaimsSignature(String token) {
        try {
            return getJws(token).getSignature();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }


    /**
     *
     * 检查token
     *      1. 检查token的完整性和有效期
     *      2. 检查失败会报错
     *      3. 检查成功返回token的play load内容
     */
    public static Claims checkToken(String token) {
        try {
            Claims claims = getClaimsBody(token);
            if (claims == null) {
                throw new RuntimeException("token解析失败");
            }
            return claims;
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("token已经失效");
        } catch (Exception e) {
            throw new RuntimeException("token解析失败");
        }
    }

    /**
     * 判断token是否存在与有效
     * @param token
     * @return
     */
//    public static boolean checkToken(String token) {
//        if(StringUtils.isEmpty(jwtToken)) return false;
//        try {
//            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    public static WebSocketUser parseToken(String token) {
        Claims claims = getClaimsBody(token);
        String name = (String) claims.get("userId");
        return new WebSocketUser(name);
    }


    public static void main(String[] args) {
        /*
         *  元 : 起始
         * 元数据 : 用于描述数据的数据
         * */
        // x数据
//        String header = "eyJhbGciOiJIUzI1NiJ9";
//        String header = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9";
//        byte[] xx = Base64.getUrlDecoder().decode(header);
//        //{"alg":"HS256"}
//        String s = new String(xx, StandardCharsets.UTF_8);
//        System.out.println("s1: "  + s);

//        byte[] yy = Base64.getUrlDecoder().decode("eyJqdGkiOiIxZjQwYmVkOC03YjQ5LTRkYmQtYjAyNS02YTM4M2U5ZjM5ZTkiLCJpYXQiOjE2MTgxNTU2MTAsImlzcyI6ImhlaW1hIiwiZXhwIjoxNjE4MTU5MjEwLCJpZCI6MX0");
//        //{"jti":"1f40bed8-7b49-4dbd-b025-6a383e9f39e9","iat":1618155610,"iss":"heima","exp":1618159210,"id":1}
//        String s2 = new String(yy);
//        System.out.println("s2: " + s2);


//        String payload = "eyJqdGkiOiJmMmFiZjYzOC1hN2QxLTQwYzEtODdhZC0yNjg3NmI3NDE5OWUiLCJpYXQiOjE2MzIwMzc1ODQsImlzcyI6ImhlaW1hIiwiZXhwIjoxNjMyMDQxMTg0LCJ1c2VySWQiOjF9";
//        byte[] zz = Base64.getUrlDecoder().decode(payload);
//        {"jti":"f2abf638-a7d1-40c1-87ad-26876b74199e","iat":1632037584,"iss":"heima","exp":1632041184,"userId":1}
//        String s3 = new String(zz);
//        System.out.println("s3: " + s3);

//        userId = 1
//        String token = getToken(1);
//        System.out.println(token);
//        Claims claims = checkToken(token);

//        byte[] ou = Base64.getUrlDecoder().decode("");
//        {"jti":"f2abf638-a7d1-40c1-87ad-26876b74199e","iat":1632037584,"iss":"heima","exp":1632041184,"userId":1}
//        String s4 = new String(ou).replace("\r\n", ""), "utf-8");
//        System.out.println("s4: " + ou);

        String token = getToken("admin");
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjMWU0YTdmMC0xMzVhLTQ0NmEtYjVjZC03NjQxNzk1MGNiNzAiLCJzdWIiOiLmtYvor5UiLCJpc3MiOiJ4aWFvIiwiaWF0IjoxNjUwNTM3OTY3LCJleHAiOjE2NTA2MjQzNjcsInVzZXJJZCI6ImFkbWluIn0.6Kfzgbdo77SQMpNrim2CHYInACPS12a45lLPYvVe31Y
        System.out.println(token);

//        String[] parts = token.split("\\.", 0);
//        for(String part : parts){
//            byte[] bytes = Base64.getUrlDecoder().decode(part);
//            String decodedString = new String(bytes, StandardCharsets.UTF_8);
//            System.out.println("Decoded: " + decodedString);
//        }
    }

}
