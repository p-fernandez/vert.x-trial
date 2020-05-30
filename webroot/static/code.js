const SERVICES_PATH = '/services';

const listContainer = document.querySelector('#service-list');

function disableButtons() {
  const buttons = document.querySelectorAll('button');
  for (let i = 0; i < buttons.length; i++) {
    buttons[i].disabled = true;
  }
}

function enableButtons() {
  const buttons = document.querySelectorAll('button');
  for (let i = 0; i < buttons.length; i++) {
    buttons[i].disabled = false;
  }
}

function getColor(status) {
  const color = status === 'OK' ? 'green' : (status === 'FAIL' ? 'red' : 'orange');
  return ' color: ' + color + ';';
}

const baseStyle = 'margin: 0 10px 0 10px; padding: 0; width: auto;';

function createBlock(id, key, value) {
  const el = document.createElement('div');
  el.className = `service-${id}-${key}`;
  el.innerHTML = value;
  el.style = baseStyle;
  return el;
}

function createStatusBlock(id, key, value) {
  const el = createBlock(id, key, value);
  el.style = baseStyle + getColor(value) + 'font-weight: bold;';
  return el;
}

function createNode(service) {
  const div = document.createElement('div');
  div.className = 'node-service-' + service.id;
  div.style = 'display: flex;';

  Object.keys(service).map(key => {
     let el = null;
     if (key === 'status') {
       el = createStatusBlock(service.id, key, service[key]);
     }
     if (['url', 'name'].indexOf(key) > -1) {
       el = createBlock(service.id, key, service[key]);
     }
     el && div.appendChild(el);
  });

  const button = createDeleteButton(service.id);
  button.style = 'margin: 0 10px 0 10px; padding: 0; width: auto;';
  div.appendChild(button);

  return div;
}

function addRow(service) {
  const li = document.createElement('li');
  li.className = 'service-' + service.id;

  const div = createNode(service);

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

function findAndReplaceNodes(services) {
  for(let i=0; i < services.length; i+=1) {
    const service = services[i];
    const node = document.querySelector('.service-' + service.id + '-status');
    node.innerHTML = service.status;
    node.style = baseStyle + getColor(service.status) + 'font-weight: bold;';
  }
}

function onDelete(evt) {
  evt.preventDefault();
  disableButtons();

  const id = evt.target.id;
  fetch(SERVICES_PATH + '/' + id, {
     method: 'delete',
     headers: {
         'Accept': 'application/json, text/plain, */*',
         'Content-Type': 'application/json'
     }
  })
  .then((json) => {
    if (!json.error) {
       removeRow(id);
    }
    enableButtons();
  })
  .catch(() => enableButtons());
}

function onSave(evt) {
  disableButtons();

  const url = document.querySelector('#service-url').value;
  const name = document.querySelector('#service-name').value || null;
  fetch(SERVICES_PATH, {
    method: 'post',
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({url, name})
  })
  .then(res => res.json())
  .then(json => {
    if (!json.error) {
      addRowToContainer(json);
    }
    clearInputs();
    enableButtons();
  })
  .catch(() => enableButtons());
}

const servicesRequest = new Request(SERVICES_PATH);
fetch(servicesRequest)
  .then(function(response) { return response.json(); })
  .then(function(serviceList) {
    serviceList.forEach(addRowToContainer);
  });

const saveButton = document.querySelector('#post-service');
saveButton.onclick = onSave;

let ws;
function start(location){
    ws = new WebSocket(location);
    ws.onopen = function (event) {
      const message = JSON.stringify({ type: "handshake", message: "Connecting..."});
      ws.send(message);
    };

    ws.onmessage = function (event = { data: "" }) {
      const data = event.data;
      try {
        const json = JSON.parse(data);
        if (json.type && json.type === "updatedServices") {
          findAndReplaceNodes(json.services);
        }
      } catch (error) {
        console.log(error.message);
        console.error('Malformed JSON string');
        console.error(data);
      }
    }

    ws.onerror = function(event) {
        console.error(event.data);
    }

    ws.onclose = function(event) {
      console.log('Closing...');

      if (event.code != 1000) {
         if (!navigator.onLine) {
           console.error("You are offline. Please connect to the Internet and try again.");
         }
      }

      setTimeout(function(){
        start(location)
      }, 1000);
    };
}

start("ws://localhost:8080/channel");




