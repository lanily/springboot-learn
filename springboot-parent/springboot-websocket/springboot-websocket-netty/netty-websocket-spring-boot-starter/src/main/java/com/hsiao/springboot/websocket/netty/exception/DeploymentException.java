package com.hsiao.springboot.websocket.netty.exception;

/**
 * 〈一句话功能简述〉<br>
 *
 * @projectName springboot-websocket
 * @title: DeploymentException
 * @description: TODO
 * @author xiao
 * @create 2022/4/24
 * @since 1.0.0
 */
public class DeploymentException extends Exception {

  private static final long serialVersionUID = 1L;

  public DeploymentException(String message) {
    super(message);
  }

  public DeploymentException(String message, Throwable cause) {
    super(message, cause);
  }
}
