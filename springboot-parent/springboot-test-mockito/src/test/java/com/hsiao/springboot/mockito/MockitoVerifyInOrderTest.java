package com.hsiao.springboot.mockito;


import static org.mockito.Mockito.inOrder;

import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-parent
 * @title: MockitoVerifyInOrderTest
 * @description: TODO
 * @author xiao
 * @create 2021/9/5
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class MockitoVerifyInOrderTest {

    @Mock
    List<String> mockList;

    @Test
    public void verify_OrderedInvocationsOnMock() {
        mockList.size();
        mockList.add("add a parameter");
        mockList.clear();

        InOrder inOrder = inOrder(mockList);
        inOrder.verify(mockList).size();
        inOrder.verify(mockList).add("add a parameter");
        inOrder.verify(mockList).clear();
    }
}

