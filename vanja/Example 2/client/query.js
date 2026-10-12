let stompClient;
let isLoaded = false;
let socket;
const serverUrl = "ws://localhost:8082/socket";

function fetchData() {
    const container = document.getElementById("data");
    container.innerHTML = "Fetching data...";

    if (!stompClient) {
        initializeWebSocketConnection();
    } else {
        stompClient.disconnect(function () {
            stompClient = undefined;
            initializeWebSocketConnection();
        });
    }
}

function onConnect(frame) {
    console.log("Connection opened")
    isLoaded = true;
    const name = document.getElementById('measurements').value;
    const type = document.querySelector('input[name="type"]:checked').value;
    subscribe(name, type);
};

function onStompError(frame) {
    console.log('Broker reported error: ' + frame.headers['message']);
    console.log('Additional details: ' + frame.body);
};

function initializeWebSocketConnection() {
    stompClient = new Stomp.client(`${serverUrl}`);
    stompClient.connect({}, onConnect, onStompError)
}

function subscribe(name, type) {
    if (isLoaded) {
        stompClient.subscribe(`/data/${type}/${name}`, message => {
            handleResult(message);
        });
    }
}

function handleResult(message) {
    if (message.body) {
        let messageResult = JSON.parse(message.body);
        showData(messageResult.data);
    }
}

function showData(data) {
    const container = document.getElementById("data");
    container.innerHTML = "";
    const ol = document.createElement("ol");
    for (let item of data) {
        const li = document.createElement("li");
        const text = document.createTextNode(`${item.name} | ${item.timestamp} | ${item.value}`);
        li.appendChild(text);
        ol.appendChild(li);
    }
    container.appendChild(ol);
}