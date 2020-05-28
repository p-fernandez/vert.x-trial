function disableButtons() {
  let buttons = document.querySelectorAll('button');
  for (var i = 0; i < buttons.length; i++) {
    buttons[i].disabled = true;
  }
}

function enableButtons() {
  let buttons = document.querySelectorAll('button');
  for (var i = 0; i < buttons.length; i++) {
    buttons[i].disabled = false;
  }
}

const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {
  serviceList.forEach(service => {
    var li = document.createElement("li");

    var div = document.createElement("div");
    div.appendChild(document.createTextNode(service.url + ' : ' + service.name + ' : ' + service.status));
    var button = document.createElement("button");
    button.innerHTML = 'DELETE';
    button.id = service.id;
    button.className = "remove-service";
    div.appendChild(button);
    li.appendChild(div);
    listContainer.appendChild(li);
  });

  const deleteButtons = document.querySelectorAll('.remove-service');
  for (var i = 0; i < deleteButtons.length; i++) {
    deleteButtons[i].addEventListener('click', function(evt) {
        evt.preventDefault();
        disableButtons();

        let id = evt.target.id;
        fetch('/service/' + id, {
            method: 'delete',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        })
        .then(res=> location.reload())
        .catch(() => enableButtons());
    });
  }
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    disableButtons();

    let url = document.querySelector('#service-url').value;
    let name = document.querySelector('#service-name').value || null;
    fetch('/service', {
      method: 'post',
      headers: {
        'Accept': 'application/json, text/plain, */*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({url, name})
    })
    .then(res=> location.reload())
    .catch(() => enableButtons());

}