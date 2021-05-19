function getUsers(valueControlName) {
  const valueControl = document.getElementById(valueControlName);
  const userDataContainer = document.getElementById('userDataContainer');
  const fullUrl = url + (valueControl? (encodeURIComponent(valueControl.value)) : '');
  fetch(fullUrl)
    .then(response => {
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.indexOf("application/json") !== -1) {
        return response.json();
      } else {
        return "User " + valueControl.value + " not found!";
      }
    }).then(result => userDataContainer.innerText = JSON.stringify(result, null, 2));
}

function addReport(taskId) {
  const progressContainer = document.getElementById('add-report_progress_' + taskId);
  const commentContainer = document.getElementById('add-report_comment_' + taskId);

  sendRequest('POST', '/api/reports/', {taskId: taskId, progress: progressContainer.value, comment: commentContainer.value})
    .then(
      response => {
        console.log("response: ", response);
        success(taskId, response);
      }
    )
    .catch(
      error => {
        fail(error);
      }
    );
}

function fail(response) {
  const errorContainer = document.getElementById('apiErrorMessage');
  errorContainer.innerText = response;
}

function success(taskId, response) {
  $('#apiErrorMessage').text("");

  // add report
  let div = document.createElement('div');
  div.innerHTML =
      '<small id="creation-date" class="text-muted">creationDate</small>' +
      '<div id="progress">progress</div>' +
      '<div id="comment">comment</div>';
  div.id = 'task_reports_content_' + response.taskId + '_' + response.id;
  div.className = 'card card-body mt-2';
  div.querySelector('#creation-date').innerText = response.creationDate;
  div.querySelector('#progress').innerText = response.progress + '%';
  div.querySelector('#comment').innerText = response.comment;
  console.log("div:", div);
  $('#task_reports_content_' + taskId +'>br').before(div);
  // set progress
  $('#task_progress_value_' + taskId).text(response.progress + '%');
  const taskProgressBar = $('#task_progress_bar_' + taskId);
  taskProgressBar.width(response.progress + '%');
  if(response.progress == 100){
    taskProgressBar.addClass('bg-success').removeClass('bg-warning');
  } else {
    taskProgressBar.addClass('bg-warning').removeClass('bg-success');
  }
  // clear inputs
  $('#add-report_progress_' + taskId).val("");
  $('#add-report_comment_' + taskId).val("");
}

function addTask(creatorId) {
  console.log("creatorId: ", creatorId)

  sendRequest(
    'POST',
    '/api/tasks/',
    {
      performerId: $('#add-task_performer').val(),
      description: $('#add-task_description').val(),
      expectedResult: $('#add-task_expected_result').val(),
      expectedDueDate: $('#add-task_expected_due_date').val()
    }
  ).then( response => {
      console.log("addTask::response:", response);
      location.reload();
    }
  ).catch(error => fail(error));
}

async function sendRequest(method = 'GET', url = '', data = {}) {
  const response = await fetch(url, {
    method: method,
    mode: 'same-origin',
    credentials: 'same-origin',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data)
  });
  if (response.redirected && response.url.match('/login$')) {
    throw "You are not logged in!";
  }
  const statusOk = await response.ok;
  if (!statusOk) {
    throw await response.text()
  }
  return await response.json();
}