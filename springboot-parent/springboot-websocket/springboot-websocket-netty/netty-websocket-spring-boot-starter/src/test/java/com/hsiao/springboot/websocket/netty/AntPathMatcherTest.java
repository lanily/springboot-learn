package com.hsiao.springboot.websocket.netty;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 *
 * https://cloud.tencent.com/developer/article/1840091
 *
 * @projectName springboot-parent
 * @title: AntPathMatcherTest
 * @description: TODO
 * @author xiao
 * @create 2022/4/25
 * @since 1.0.0
 */
public class AntPathMatcherTest {

    private static final PathMatcher MATCHER = new AntPathMatcher();


    private static void match(int index, PathMatcher matcher, String pattern, String reqPath) {
        boolean match = matcher.match(pattern, reqPath);
        System.out.println(index + "\tmatch结果：" + pattern + "\t" + (match ? "【成功】" : "【失败】") + "\t" + reqPath);
    }

    /**
     * 对PathMatcher最常用的match方法、extractUriTemplateVariables方法做简易封装，主要为了输出日志，方便控制台里对应着查看。
     * @param matcher
     * @param pattern
     * @param reqPath
     */
    private static void extractUriTemplateVariables(PathMatcher matcher, String pattern, String reqPath) {
        Map<String, String> variablesMap = matcher.extractUriTemplateVariables(pattern, reqPath);
        System.out.println("extractUriTemplateVariables结果：" + variablesMap + "\t" + pattern + "\t" + reqPath);
    }

    /**
     *
     * ?：匹配任意单字符
     * 因为是匹配单字符，所以一般“夹杂”在某个path片段内容中间
     *
     * =======匹配任意单字符=======
     * 1	match结果：/api/your?atman	【失败】	/api/youratman
     * 2	match结果：/api/your?atman	【成功】	/api/yourBatman
     * 3	match结果：/api/your?atman	【失败】	/api/yourBatman/address
     * 4	match结果：/api/your?atman	【失败】	/api/yourBBBatman
     *
     * 关注点：
     *
     * 1. ?表示匹配精确的1个字符，所以0个不行（如结果1）
     * 2. 即使?匹配成功，但“多余”部分和pattern并不匹配最终结果也会是false（如结果3,4）
     */
    @Test
    public void test1() {
        System.out.println("=======测试?：匹配任意单个字符=======");
        String pattern = "/api/your?atman";

        match(1, MATCHER, pattern, "/api/youratman");
        match(2, MATCHER, pattern, "/api/yourBatman");
        match(3, MATCHER, pattern, "/api/yourBatman/address");
        match(4, MATCHER, pattern, "/api/yourBBBatman");
    }


    /**
     *
     * *：匹配任意数量的字符
     * 因为是匹配任意数量的字符，所以一般使用*来代表URL的一个层级
     *
     * 关注点：
     *
     * 1. 路径的//间必须有内容（即使是个空串）才能被*匹配到
     * 2. *只能匹配具体某一层的路径内容
     *
     * =======*:匹配任意数量的字符=======
     */
    //=======*:匹配任意数量的字符=======
    //1	match结果：/api/*/yourbatman	【失败】	/api//yourbatman
    //2	match结果：/api/*/yourbatman	【成功】	/api/ /yourbatman
    //3	match结果：/api/*/yourbatman	【失败】	/api/yourbatman
    //4	match结果：/api/*/yourbatman	【成功】	/api/v1v2v3/yourbatman
    @Test
    public void test2() {
        System.out.println("=======*:匹配任意数量的字符=======");
        String pattern = "/api/*/yourbatman";

        match(1, MATCHER, pattern, "/api//yourbatman");
        match(2, MATCHER, pattern, "/api/ /yourbatman");
        match(2, MATCHER, pattern, "/api/yourbatman");
        match(3, MATCHER, pattern, "/api/v1v2v3/yourbatman");
    }


    /**
     *
     * **：匹配任意层级的路径/目录
     * 匹配任意层级的路径/目录，这对URL这种类型字符串及其友好。
     *
     * =======**:匹配任意层级的路径/目录=======
     *  1	match结果：/api/yourbatman/**	【成功】	/api/yourbatman
     *  2	match结果：/api/yourbatman/**	【成功】	/api/yourbatman/
     *  3	match结果：/api/yourbatman/**	【成功】	/api/yourbatman/address
     *  4	match结果：/api/yourbatman/**	【成功】	/api/yourbatman/a/b/c
     */
    @Test
    public void test3() {
        System.out.println("=======**:匹配任意层级的路径/目录=======");
        String pattern = "/api/yourbatman/**";


        match(1, MATCHER, pattern, "/api/yourbatman");
        match(2, MATCHER, pattern, "/api/yourbatman/");
        match(3, MATCHER, pattern, "/api/yourbatman/address");
        match(4, MATCHER, pattern, "/api/yourbatman/a/b/c");
    }


    /**
     * 关注点：
     *
     * 1. **的匹配“能力”非常的强，几乎可以匹配一切：任意层级、任意层级里的任意“东西”
     * 2. **在AntPathMatcher里即可使用在路径中间，也可用在末尾
     * 3. **其实不仅可以放在末尾，还可放在中间。
     *
     */
//    =======**:匹配任意层级的路径/目录=======
//    1	match结果：/api/**/yourbatman	【成功】	/api/yourbatman
//    2	match结果：/api/**/yourbatman	【成功】	/api//yourbatman
//    3	match结果：/api/**/yourbatman	【成功】	/api/a/b/c/yourbatman
    @Test
    public void test4() {
        System.out.println("=======**:匹配任意层级的路径/目录=======");
        String pattern = "/api/**/yourbatman";

        match(1, MATCHER, pattern, "/api/yourbatman");
        match(2, MATCHER, pattern, "/api//yourbatman");
        match(3, MATCHER, pattern, "/api/a/b/c/yourbatman");
    }


    /**
     * ======={pathVariable:可选的正则表达式}=======
     * 1	match结果：/api/yourbatman/{age}	【成功】	/api/yourbatman/10
     * 2	match结果：/api/yourbatman/{age}	【成功】	/api/yourbatman/Ten
     * extractUriTemplateVariables结果：{age=10}  /api/yourbatman/{age}	/api/yourbatman/10
     * extractUriTemplateVariables结果：{age=Ten} /api/yourbatman/{age}	/api/yourbatman/Ten
     */
    @Test
    public void test5() {
        System.out.println("======={pathVariable:可选的正则表达式}=======");
        String pattern = "/api/yourbatman/{age}";

        match(1, MATCHER, pattern, "/api/yourbatman/10");
        match(2, MATCHER, pattern, "/api/yourbatman/Ten");

        // 打印提取到的内容
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/10");
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/Ten");
    }


    /**
     *
     * 可能你能察觉到，age是int类型，不应该匹配到Ten这个值呀。这个时候我们就可以结合正则表达式来做进一步约束啦。
     *
     *
     * 关注点：
     *
     * 1. 该匹配方式可以结合正则表达式一起使用对具体值做约束，但正则表示式是可选的
     * 2. 只有匹配成功了，才能调用extractUriTemplateVariables(...)方法，否则抛出异常
     *
     * ======={pathVariable:可选的正则表达式}=======
     * 1	match结果：/api/yourbatman/{age:[0-9]*}	【成功】	/api/yourbatman/10
     * 2	match结果：/api/yourbatman/{age:[0-9]*}	【失败】	/api/yourbatman/Ten
     * extractUriTemplateVariables结果：{age=10}	/api/yourbatman/{age:[0-9]*}	/api/yourbatman/10
     * java.lang.IllegalStateException: Pattern "/api/yourbatman/{age:[0-9]*}" is not a match for "/api/yourbatman/Ten"
     */
    @Test
    public void test6() {
        System.out.println("======={pathVariable:可选的正则表达式}=======");
        String pattern = "/api/yourbatman/{age:[0-9]*}";

        match(1, MATCHER, pattern, "/api/yourbatman/10");
        match(2, MATCHER, pattern, "/api/yourbatman/Ten");

        // 打印提取到的内容
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/10");
        extractUriTemplateVariables(MATCHER, pattern, "/api/yourbatman/Ten");
    }


    /**
     * isPattern()方法
     *
     * 关注点：
     *
     * 1. 只要含有? * ** {xxx}这种特殊字符的字符串都属于模式
     *
     * false
     * true
     * true
     * true
     */
    @Test
    public void test7() {
        System.out.println("=======isPattern方法=======");

        System.out.println(MATCHER.isPattern("/api/yourbatman"));
        System.out.println(MATCHER.isPattern("/api/your?atman"));
        System.out.println(MATCHER.isPattern("/api/*/yourBatman"));
        System.out.println(MATCHER.isPattern("/api/yourBatman/**"));
    }


    /**
     *
     * matchStart()方法
     * 它和match方法非常像，区别为：
     *
     * match：要求全路径完全匹配
     * matchStart：模式部分匹配上，然后其它部分（若还有）是空路径即可
     *
     * 关注点：
     *
     * 1. 请对比结果，看出和match方法的差异性
     * 2. matchStart方法的使用场景少之又少，即使在代码量巨大的Spring体系中，也只有唯一使用处：PathMatchingResourcePatternResolver#doRetrieveMatchingFiles
     *
     *
     * =======matchStart方法=======
     * match方法结果：true
     * match方法结果：false
     * match方法结果：false
     * matchStart方法结果：true
     * matchStart方法结果：true
     * matchStart方法结果：false
     */
    @Test
    public void test8() {
        System.out.println("=======matchStart方法=======");
        String pattern = "/api/?";

        System.out.println("match方法结果：" + MATCHER.match(pattern, "/api/y"));
        System.out.println("match方法结果：" + MATCHER.match(pattern, "/api//"));
        System.out.println("match方法结果：" + MATCHER.match(pattern, "/api"));
        System.out.println("matchStart方法结果：" + MATCHER.matchStart(pattern, "/api//"));
        System.out.println("matchStart方法结果：" + MATCHER.matchStart(pattern, "/api"));
        System.out.println("matchStart方法结果：" + MATCHER.matchStart(pattern, "/api///a/"));

    }


    /**
     *
     * extractPathWithinPattern()方法
     * 该方法通过一个实际的模式来确定路径的哪个部分是动态匹配的，换句话讲：该方法用户提取出动态匹配的那部分
     *
     * 说明：该方法永远不可能返回null
     *
     * 关注点：
     *
     * 1. 该方法和extractUriTemplateVariables()不一样，即使匹配不成功也能够返回参与匹配的那部分，有种“重在参与”的赶脚
     *
     *
     * =======extractPathWithinPattern方法=======
     * 是否匹配成功：false，提起结果：yourbatman/address
     * 是否匹配成功：true，提起结果：index.html
     */
    @Test
    public void test9() {
        System.out.println("=======extractPathWithinPattern方法=======");
        String pattern = "/api/*.html";

        System.out.println("是否匹配成功：" + MATCHER.match(pattern, "/api/yourbatman/address")
                + "，提取结果：" + MATCHER.extractPathWithinPattern(pattern, "/api/yourbatman/address"));
        System.out.println("是否匹配成功：" + MATCHER.match(pattern, "/api/index.html")
                + "，提取结果：" + MATCHER.extractPathWithinPattern(pattern, "/api/index.html"));
    }


    /**
     * 下面再看个复杂点pattern情况（pattern里具有多个模式）表现如何：
     *
     *
     * 关注点：
     *
     * 1. 该方法会返回所有参与匹配的片段，即使这匹配不成功
     * 2. 若有多个模式（如本例中的**和*），返回的片段不会出现跳跃现象（只截掉前面的非pattern匹配部分，中间若出现非pattern匹配部分是不动的）
     *
     *
     * =======extractPathWithinPattern方法=======
     * 是否匹配成功：false，提取结果：yourbatman/address
     * 是否匹配成功：true，提取结果：yourbatman/index.html/temp
     */
    @Test
    public void test10() {
        System.out.println("=======extractPathWithinPattern方法=======");
        String pattern = "/api/**/yourbatman/*.html/temp";

        System.out.println("是否匹配成功：" + MATCHER.match(pattern, "/api/yourbatman/address")
                + "，提取结果：" + MATCHER.extractPathWithinPattern(pattern, "/api/yourbatman/address"));
        System.out.println("是否匹配成功：" + MATCHER.match(pattern, "/api/yourbatman/index.html/temp")
                + "，提取结果：" + MATCHER.extractPathWithinPattern(pattern, "/api/yourbatman/index.html/temp"));
    }


    /**
     *
     * getPatternComparator()方法
     * 此方法用于返回一个Comparator<String>比较器，用于对多个path之间进行排序。目的：让更具体的 path出现在最前面，也就是所谓的精确匹配优先原则（也叫最长匹配规则（has more characters））。
     *
     * 关注点：
     *
     * 1. 该方法拥有一个入参，作用为：用于判断是否是精确匹配，也就是用于确定精确值的界限的（根据此界限值进行排序）
     * 2. 越精确的匹配在越前面。其中路径的匹配原则是从左至右（也就是说左边越早出现精确匹配，分值越高）
     *
     *
     */
//    =======getPatternComparator方法=======
//    排序前：[/api/**/index.html, /api/yourbatman/*.html, /api/**/*.html, /api/yourbatman/index.html]
//    排序后：[/api/yourbatman/index.html, /api/yourbatman/*.html, /api/**/index.html, /api/**/*.html]
    @Test
    public void test11() {
        System.out.println("=======getPatternComparator方法=======");
        List<String> patterns = Arrays.asList(
                "/api/**/index.html",
                "/api/yourbatman/*.html",
                "/api/**/*.html",
                "/api/yourbatman/index.html"
        );
        System.out.println("排序前：" + patterns);

        Comparator<String> patternComparator = MATCHER.getPatternComparator("/api/yourbatman/index.html");
        Collections.sort(patterns, patternComparator);
        System.out.println("排序后：" + patterns);
    }













}
