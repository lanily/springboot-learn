// 设置 STOMP 客户端
var stomp = null;
// 设置 WebSocket 进入端点
var SOCKET_ENDPOINT = "/ws";
// 设置订阅消息的请求地址
var SUBSCRIBE = "/topic/notice";
// 设置服务器端点，访问服务器中哪个接口
var SEND_ENDPOINT = "/app/notice";


function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  // $("#send").prop("disabled", !connected);

  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $('#content').val('');
  $("#responses").html("");
}

/* 订阅信息 */
function subscribe(){
  // 执行订阅消息
  stomp.subscribe(SUBSCRIBE, function (response) {
    log(response, 'table-success');
  });
}

/* 进行连接 */
function connect() {
  // stomp = webstomp.over(new SockJS('/websocket-sockjs-stomp'));
  //连接服务端提供的通信接口，连接以后才可以订阅广播消息和个人消息，注意这里用的是http而不是原生WebSocket的ws
  // 设置 SOCKET，建立连接对象
  // var socket = new SockJS('http://127.0.0.1:8081/websocket');
  var socket = new SockJS(SOCKET_ENDPOINT);
  // 配置 STOMP 客户端, 获取STOMP子协议的客户端对象
  stomp = Stomp.over(socket);
  // STOMP 客户端连接
  const token = "Bearer eyJhbGciOiJIUzUxMiJ9";
  let headers = {
    'Authorization': token,
  };
  stomp.connect(headers, function (frame) {
    setConnected(true);
    console.log('Client connected: ' + frame);
    subscribe();
  });
}

/* 断开连接 */
function disconnect() {
  if (stomp !== null) {
    stomp.disconnect(function () {
      setConnected(false);
      console.log("Client disconnected");
      alert("断开连接");
    });
    stomp = null;
  }
}

/* 发送消息并指定目标地址（这里设置的目标地址为自身订阅消息的地址，当然也可以设置为其它地址） */
function send() {
  // const output = $("#output").val();
  // 设置发送的内容
  const content = $("#content").val();
  // console.log("Client sends: " + output);
  console.log("Client sends: " + content);
  // 设置待发送的消息内容
  var msg = {
    code: '001',
    from: 'admin',
    to: 'all',
    content: content
  };
  // 定义客户端的认证信息,按需求配置
  // const token = getToken();
  const token = "Bearer eyJhbGciOiJIUzUxMiJ9";
  let headers = {
    'token': token,
  };
  // 发送消息
  stomp.send(SEND_ENDPOINT, headers, JSON.stringify(msg));
}

function log(response, clazz) {
  // const input = response.body;
  const message = JSON.parse(response.body).content;
  console.log("Client received: " + message);
  $("#responses").append(
      "<tr class='" + clazz + "'><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
  $("#send").click(function () {
    send();
  });
});
