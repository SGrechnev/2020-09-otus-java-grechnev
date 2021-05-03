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

  postData('/api/reports/', {taskId: taskId, progress: progressContainer.value, comment: commentContainer.value})
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
  const errorContainer = document.getElementById('apiErrorMessage');
  errorContainer.innerText = "";

  const progressContainer = document.getElementById('add-report_progress_' + taskId);
  const commentContainer = document.getElementById('add-report_comment_' + taskId);
  const brElem = document.getElementById('task_reports_content_' + taskId).getElementsByTagName('br')[0];
  const taskProgressValue = document.getElementById('task_progress_value_' + taskId);
  const taskProgressBar = document.getElementById('task_progress_bar_' + taskId);

  // add report
  let div = document.createElement('div');
  div.className = "card card-body";
  div.innerText = JSON.stringify(response);
  brElem.before(div);
  // set progress
  taskProgressValue.innerText = response.progress + "%";
  taskProgressBar.style.width = response.progress + "%";
  if(response.progress == 100){
    taskProgressBar.className = taskProgressBar.className.replaceAll("bg-warning", "bg-success");
  } else {
    taskProgressBar.className = taskProgressBar.className.replaceAll("bg-success", "bg-warning");
  }
  // clear inputs
  progressContainer.value = "";
  commentContainer.value = "";
}

function addTask(creatorId) {
  console.log("creatorId: ", creatorId)
  const performerId = document.getElementById('add-task_performer');
  const description = document.getElementById('add-task_description');
  const expectedResult = document.getElementById('add-task_expected_result');
  const expectedDueDate = document.getElementById('add-task_expected_due_date');

  const errorContainer = document.getElementById('apiErrorMessage');

  postData('/api/tasks/', {performerId: performerId.value, description: description.value, expectedResult: expectedResult.value, expectedDueDate: expectedDueDate.value})
    .then(
      response => {
        console.log("addTask::response:", response);
        let div = document.createElement('div');
        div.className = "card card-body";
        div.innerText = JSON.stringify(response);
        errorContainer.append(div);
        performerId.value = '';
        description.value = '';
        expectedResult.value = '';
        expectedDueDate.value = '';
      }
    ).catch(error => fail(error));
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
  const statusOk = await response.ok;
  if (!statusOk) {
    const responseText = await response.text();
    throw new Error(responseText);
  }
  return await response.json();
}