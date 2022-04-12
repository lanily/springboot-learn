/**
 * ws_client.js
 * @version 1.0
 * @author lb
 */

WSClient = function (_opts) {
  if (_opts.uri) {
    this.uri = _opts.uri;
    this.webSocket = new WebSocket(this.uri);
  } else {
    throw "服务端地址uri不能为空！";
  }

  dataType = _opts.dataType ? _opts.dataType : "text";

  this.msgPool = new Array();

  if (typeof _opts.msgEvent == "function") {
    this.msgPool.push(_opts.msgEvent);
  }
  if (typeof _opts.open == "function") {
    this.open = _opts.open;
  }
  if (typeof _opts.close == "function") {
    this.close = _opts.close;
  }
  if (typeof _opts.send == "function") {
    this.send = _opts.send;
  }
  if (typeof _opts.error == "function") {
    this.error = _opts.error;
  }

  var self = this;
  this.webSocket.onopen = function (evt) {
    self.open(evt)
  };
  this.webSocket.onclose = function (evt) {
    self.close(evt)
  };
  this.webSocket.onerror = function (evt) {
    self.error(evt)
  };
  this.webSocket.onmessage = function (evt) {
    self.msgEvent(evt)
  };

}

WSClient.fn = WSClient.prototype = {
  constructor: WSClient,
  uri: null,
  webSocket: null,	//WebSocket对象
  dataType: "text",
  msgPool: null,		//消息接收时的委托函数池
  calbacks: {},		//发送消息的回调函数对象

  open: function (evt) {
  },
  close: function (evt) {
  },
  error: function (evt) {
  }
}

WSClient.fn.msgEvent = function (evt) {
  if (!evt.data) {
    return;
  }
  if (evt.data == "404") {
    throw "路径不匹配或内部方法调用错误";
    return;
  }
  var data = JSON.parse(evt.data);

  if (data.stamp) {
    if (typeof this.calbacks[data.stamp] == "function") {
      this.calbacks[data.stamp](data.data);
      delete this.calbacks[data.stamp];
    }

    //	广播事件
    if (data.stamp == "broadcast") {
      console.log("broadcase--->" + data);
    }
  }

  for (var i in this.msgPool) {
    this.msgPool[i](data);
  }
}

WSClient.fn.bindMsgEvent = function (msgEvent) {
  if (typeof msgEvent == "function") {
    this.msgPool.push(msgEvent);
  }
  return this.msgPool;
}

WSClient.fn.state = function () {
  if (this.webSocket) {
    return this.webSocket.readyState;
  }
}

/*
 * url:访问接口
 * params:参数（js对象或字符串）
 */

WSClient.fn.post = function (url, data, success) {
  this.send(
      {uri: url, data: data, callback: success, method: 'POST', type: 'form'})
}

WSClient.fn.postContent = function (url, data, success) {
  this.send({
    uri: url,
    data: data,
    callback: success,
    method: 'POST',
    type: 'content'
  })
}

/**
 * 发送请求
 * @param params { uri: 请求地址，method：请求方式，type：参数类型(form or content)，callback：回调方法 }
 * @returns {True or False}
 */
WSClient.fn.send = function (params) {
  if (this.webSocket == null || this.webSocket.readyState != 1) {
    return false;
  }

  var body = {"method": "POST", "type": "form"};
  Object.assign(body, params);
  if (!body.uri) {
    console.error("params error");
    return false;
  }

  if (typeof body.callback == 'function') {
    body.stamp = new Date().getTime();
    this.calbacks[body.stamp] = body.callback;
    delete body.callback;
  }

  this.webSocket.send(JSON.stringify(body));
  return this;
}

// 初始化
// var wsClient = new WSClient({
// 	"uri" : "ws://localhost:8082/msg/main"
// });
