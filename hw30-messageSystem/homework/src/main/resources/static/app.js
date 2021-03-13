const ID = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
const USERS = {};

function updateUserTable() {
  const systemMessageContainer = document.getElementById('systemMessageContainer');
  systemMessageContainer.innerText = 'Загрузка...';

  const stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.send('/app/getUsers.' + ID, {});
    stompClient.subscribe('/topic/users.' + ID, (userMsg) => {
      fillUsers(userMsg.body);
      rebuildUserTable();
      systemMessageContainer.innerText = 'Готово!';
    });
  });
}

function fillUsers(usersString) {
  const oneOrMoreUser = JSON.parse(usersString);
  if(oneOrMoreUser instanceof Array){
    oneOrMoreUser.forEach(user => {
      if(checkUser(user)) {
        USERS[user.login] = user;
      } else {
        printError('Is not user: ' + user);
      }
    });
  } else if(checkUser(oneOrMoreUser)) {
    USERS[oneOrMoreUser.login] = oneOrMoreUser;
  } else {
    printError('Is not user: ' + oneOrMoreUser);
  }
}

function rebuildUserTable() {
  const tableRef = document.getElementById('userTable');
  clearTable(tableRef);
  Object.values(USERS).forEach((user) => {
    addRow(tableRef, user);
  })
}

function clearTable(tableRef) {
  while(tableRef.hasChildNodes())
  {
     tableRef.removeChild(tableRef.firstChild);
  }
}

function addRow(tableRef, user) {
  const newRow = tableRef.insertRow(-1);
  if(checkUser(user)){
    newRow.insertCell(0).innerText = user.login
    newRow.insertCell(1).innerText = user.name
    newRow.insertCell(2).innerText = user.password
    newRow.insertCell(3).innerText = user.role
  } else {
    printError('Is not user: ' + user);
  }
}

function checkUser(user) {
  return (user.login && user.name && user.password && user.role) != undefined;
};

function printError(errorMessage) {
  console.log("printError");
  const errorContainer = document.getElementById('errorContainer').innerText = errorMessage;
}

// Get user
function getUserByLogin(login, dataContainerName) {
  const stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.send("/app/getUser." + ID, {}, login);
    stompClient.subscribe('/topic/getUser.' + ID, (userMsg) => fillDataContainer(dataContainerName, userMsg));
  });
}

function getUserByUserLoginTextBox(userLoginTextBox, dataContainerName) {
  const login = document.getElementById(userLoginTextBox).value;
  getUserByLogin(login, dataContainerName);
}

function getRandomUser(dataContainerName) {
  const stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.send("/app/getRandomUser." + ID, {});
    stompClient.subscribe('/topic/getUser.' + ID, (userMsg) => fillDataContainer(dataContainerName, userMsg));
  });
}

function fillDataContainer(dataContainerName, userMsg) {
  const userStr = userMsg.body;
  document.getElementById(dataContainerName).innerText = userStr == "null" ? "User not found!" : "User: " + userStr;
}

// Save user
function submitUser(){
  const login = document.getElementById('user-login').value;
  const name = document.getElementById('user-name').value;
  const password = document.getElementById('user-password').value;
  const role = document.getElementById('user-role').value;

  const stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
  stompClient.connect({}, (frame) => {
    stompClient.send("/app/saveUser." + ID, {}, JSON.stringify({login: login, name: name, password: password, role: role}));
    stompClient.subscribe('/topic/savedUser.' + ID, (user) => document.getElementById('savedUserContainer').innerText = "User saved: " + user.body)
  });
}