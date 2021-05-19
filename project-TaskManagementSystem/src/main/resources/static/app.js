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
  $('.toast-body>p').text(response);
  showToast({
    title: 'Ошибка',
    content: response,
    type: 'error',
    delay: 3000
  })
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

  showToast({
      title: 'Готово!',
      content: 'Отчёт успешно добавлен',
      type: 'success',
      delay: 3000
    })
}

function addTask(creatorId) {
  console.log("creatorId: ", creatorId);

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

function deleteTask() {
  var taskId = $('#delete-task-id').val();
  console.log('delete task ', taskId);
  sendRequest('DELETE', '/api/tasks/' + taskId)
    .then(response => {
        if(response == true) {
          console.log("TODO: delete task");
          $('#task_'+taskId).slideUp(500, function() { $(this).remove(); });
          // $('#task_'+taskId).hide('slow', function(){ $('#task_'+taskId).remove(); });
          // show success modal
        } else {
          console.log("TODO: don't delete task");
          // show error modal
        }
      }
    );
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

function addModalListener(modalId) {
  $('#deleteConfirmation').on('show.bs.modal', function (event) {
    // Button that triggered the modal
    var button = $(event.relatedTarget);
    // Extract info from data-* attributes
    var taskId = button.data('task-id');
    var modal = $(this)

    modal.find('.modal-title').text('Вы уверены, что хотите удалить задачу ' + taskId + '?')
    modal.find('.modal-body input').val(taskId)
  })
}

function showToast(options) {
  var color;
  switch (options.type) {
    case 'error':
      color = '#dc3545'; break;
    case 'warning':
      color = '#ffc107'; break;
    case 'success':
      color = '#1e7e34'; break;
    default:
      color = '#0062cc'; break;
  }

  let toast = createElementFromHTML(
      '<div class="toast fade hide" role="alert" aria-live="assertive" aria-atomic="true" data-delay="3000">' +
        '<div class="toast-header">' +
          '<svg class="bd-placeholder-img rounded mr-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg" role="img" aria-label=" :  " preserveAspectRatio="xMidYMid slice" focusable="false"><rect width="100%" height="100%" fill="' + color + '"></rect></svg>' +
          '<div class="flex-grow-1 mt-1 mb-1">' +
            '<strong> Ошибка </strong>' +
          '</div>' +
          '<button type="button" data-dismiss="toast" aria-label="Close" class="ml-2 mb-1 close" style="position: fixed; right: 10px;">' +
            '<span aria-hidden="true">×</span>' +
          '</button>' +
        '</div>' +
        '<div class="toast-body">' +
          '<p>Message</p>' +
        '</div>' +
      '</div>');
  toast.setAttribute('data-delay', options.delay ?? 3000);
  toast.querySelector('.toast-header>div>strong').innerText = options.title ?? 'Уведомление';
  toast.querySelector('.toast-body>p').innerText = options.content ?? '';
  $('#toasts').append(toast);
  $('.toast').on('hidden.bs.toast', function() {$(this).remove();});
  (new bootstrap.Toast(toast)).show();
}

function createElementFromHTML(htmlString) {
  var div = document.createElement('div');
  div.innerHTML = htmlString.trim();

  // Change this to div.childNodes to support multiple top-level nodes
  return div.firstChild;
}