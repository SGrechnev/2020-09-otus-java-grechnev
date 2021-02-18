<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Пользователи</title>
    <link rel="icon" href="favicon.svg">
    <script>
        function createUser(method) {
            const login = document.getElementById('userLogin').value;
            const name = document.getElementById('userName').value;
            const password = document.getElementById('userPassword').value;
            const role = document.getElementById('userRole').value;

            (async () => {
              const rawResponse = await fetch('api/user', {
                method: method,
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({login: login, name: name, password: password, role: role}),
                credentials: "same-origin"
              });
              const content = await rawResponse.json();
              console.log(content);
              if (content["status"] == "SUCCESS"){
                location.reload();
              } else {
                document.getElementById("apiErrorMessage").innerText = content["message"];
              }
            })();
        }
    </script>
</head>

<body>
<h4>Создать пользователя</h4>
<input type="text" id="userLogin" value="johnwick" placeholder="Введите логин пользователя">
<input type="text" id="userName" value="John Wick" placeholder="Введите имя пользователя">
<input type="password" id="userPassword" value="<script>alert(1);</script>" placeholder="Введите пароль пользователя">
<select id="userRole">
    <option value="USER">Пользователь</option>
    <option value="ADMIN">Администратор</option>
</select>

<button onclick="createUser('POST')">Создать</button>
<button onclick="createUser('PUT')">Изменить</button>

<pre id="apiErrorMessage" color="#880000"></pre>

<h4>Пользователи</h4>
<table style="width: 600px" border="1">
    <thead>
        <tr bgcolor="#dddddd">
            <td style="width: 100px">Имя</td>
            <td style="width: 100px">Логин</td>
            <td style="width: 100px">Пароль</td>
            <td style="width: 150px">Роль</td>
        </tr>
    </thead>
    <tbody>
        <#list allUsers as user>
            <tr>
                <td>${user.name}</td>
                <td>${user.login}</td>
                <td>${user.password}</td>
                <td>${user.role}</td>
            </tr>
        </#list>
    </tbody>
</table>
</body>
</html>