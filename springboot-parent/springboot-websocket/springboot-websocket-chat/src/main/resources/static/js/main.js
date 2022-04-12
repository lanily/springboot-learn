'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
  '#2196F3', '#32c787', '#00BCD4', '#ff5652',
  '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
  username = document.querySelector('#name').value.trim();
  if (username) {
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');
    // can't connection with follow
    // var socket = new SockJS("ws://localhost:8081/websocket");
    // 为了与建立在TCP socket的STOMP-broker连接，使用Stomp.overTCP(host, port)方法。
    // var client = Stomp.overTCP('localhost', 8081);

    // 为了与建立在Web Socket的STOMP broker连接，使用Stomp.overWS(url)方法。
    // var stompClient = Stomp.overWS('ws://localhost:8081/websocket');

    var socket = new SockJS('/websocket', null, { timeout: 15000});
    stompClient = Stomp.over(socket);


    stompClient.connect({}, onConnected, onError);
  }
  event.preventDefault();

/*  var url="ws://localhost:8080/stomp"
  var client = Stomp.client(url);
  client.subscribe('/topic/message', function(message){
    console.log('subscribe topic callback:'+message.body);
  });
  var connect_callback = function(frame) {
    var payload = JSON.stringify({'message':'greeting to stomp broker!'});
    client.send("/app/message",{},payload);
  };
  var error_callback = function(error) {
    alert(error.headers.message);
  };
  var headers = {
    login: '',
    passcode: '',
    // additional header
    'client-id': 'stomp-client-id'
  };
  client.connect(headers, connect_callback, error_callback);
  */
}

function onConnected() {
  // Subscribe to the Public Topic
  stompClient.subscribe('/topic/public', onMessageReceived);
  // Tell your username to the server
  stompClient.send("/app/chat.register",
      {},
      JSON.stringify({sender: username, type: 'JOIN'})
  )
  connectingElement.classList.add('hidden');
}

function onError(error) {
  connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
  connectingElement.style.color = 'red';
}

function sendMessage(event) {
  var messageContent = messageInput.value.trim();

  if (messageContent && stompClient) {
    var chatMessage = {
      sender: username,
      content: messageInput.value,
      type: 'CHAT'
    };

    stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
  }
  event.preventDefault();
}

function onMessageReceived(payload) {
  var message = JSON.parse(payload.body);
  var messageElement = document.createElement('li');
  if (message.type === 'JOIN') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' joined!';
  } else if (message.type === 'LEAVE') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' left!';
  } else {
    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(message.sender[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(message.sender);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
  }

  var textElement = document.createElement('p');
  var messageText = document.createTextNode(message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
  var hash = 0;
  for (var i = 0; i < messageSender.length; i++) {
    hash = 31 * hash + messageSender.charCodeAt(i);
  }

  var index = Math.abs(hash % colors.length);
  return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
