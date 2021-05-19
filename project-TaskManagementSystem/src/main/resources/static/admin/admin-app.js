function addUser() {
  const username = document.getElementById('add-user_username').value;
  const fullname = document.getElementById('add-user_fullname').value;
  const role = document.getElementById('add-user_role').value;

  const errorContainer = document.getElementById('apiErrorMessage');

  postData('/api/users/', {username: username, fullname: fullname, role: role})
    .then(response => {
      console.log(response);
      location.reload();
    });
}

async function postData(url = '', data = {}) {
  const response = await fetch(url, {
    method: 'POST',
    mode: 'same-origin',
    credentials: 'same-origin',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data)
  });
  return response.json();
}