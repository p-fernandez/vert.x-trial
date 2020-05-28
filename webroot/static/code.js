const listContainer = document.querySelector('#service-list');

function disableButtons() {
  const buttons = document.querySelectorAll('button');
  for (var i = 0; i < buttons.length; i++) {
    buttons[i].disabled = true;
  }
}

function enableButtons() {
  const buttons = document.querySelectorAll('button');
  for (var i = 0; i < buttons.length; i++) {
    buttons[i].disabled = false;
  }
}

function addRow(service) {
  const li = document.createElement('li');
  li.className = 'service-' + service.id;

  const div = document.createElement('div');
  div.appendChild(document.createTextNode(service.url + ' : ' + service.name + ' : ' + service.status));
  const button = createDeleteButton(service.id);
  div.appendChild(button);

  li.appendChild(div);
  return li;
}

function addRowToContainer(service) {
  const li = addRow(service);
  listContainer.appendChild(li);
}

function removeRow(id) {
  const element = document.querySelector('.service-' + id);
  element.parentNode.removeChild(element);
}

function createDeleteButton(id) {
  const button = document.createElement('button');
  button.innerHTML = 'DELETE';
  button.id = id;
  button.className = 'remove-service';
  button.addEventListener('click', onDelete);
  return button;
}

function clearInputs() {
  const url = document.querySelector('#service-url');
  url.value = '';
  const name = document.querySelector('#service-name');
  name.value = '';
}

function onDelete(evt) {
  evt.preventDefault();
  disableButtons();

  const id = evt.target.id;
  fetch('/service/' + id, {
     method: 'delete',
     headers: {
         'Accept': 'application/json, text/plain, */*',
         'Content-Type': 'application/json'
     }
  })
  .then(() => {
     removeRow(id);
     enableButtons();
  })
  .catch(() => enableButtons());
}

function onSave(evt) {
  disableButtons();

  const url = document.querySelector('#service-url').value;
  const name = document.querySelector('#service-name').value || null;
  fetch('/service', {
    method: 'post',
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({url, name})
  })
  .then(res => res.json())
  .then(json => {
    addRowToContainer(json);
    clearInputs();
    enableButtons();
  })
  .catch(() => enableButtons());
}

const servicesRequest = new Request('/service');
fetch(servicesRequest)
  .then(function(response) { return response.json(); })
  .then(function(serviceList) {
    serviceList.forEach(addRowToContainer);
  });

const saveButton = document.querySelector('#post-service');
saveButton.onclick = onSave;