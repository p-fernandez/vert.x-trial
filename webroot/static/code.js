const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {
  serviceList.forEach(service => {
    var li = document.createElement("li");

    var div = document.createElement("div");
    div.appendChild(document.createTextNode(service.url + ' : ' + service.status));
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
        console.log(evt);
        evt.preventDefault();
        console.log(evt.target);
        let id = evt.target.id;
        fetch('/service/' + id, {
            method: 'delete',
            headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
            }
        }).then(res=> location.reload());
    });
  }
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    fetch('/service', {
      method: 'post',
      headers: {
        'Accept': 'application/json, text/plain, */*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({url:urlName})
    }).then(res=> location.reload());
}