const stompClient = new StompJs.Client({
  brokerURL: "ws://localhost:8080/gs-guide-websocket",
});

stompClient.onConnect = (frame) => {
  console.log("Connected: " + frame);
  stompClient.subscribe("/topic/updates", (update) => {
    console.log("Update received: ", update.body);
    handleUpdate(JSON.parse(update));
  });
  sendSimulation();
};

stompClient.onWebSocketError = (error) => {
  console.log("WebSocket error: ", error);
};

stompClient.onStompError = (frame) => {
  console.log("STOMP error: " + frame.headers["message"]);
};

function connect() {
  console.log("Connecting...");
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  console.log("Disconnected");
}

function sendSimulation() {
  stompClient.publish({
    destination: "/app/simulation",
    body: JSON.stringify({
      no_machines: machines.length,
      no_queues: queues.length,
      m2q: Object.values(m2q),
      q2m: Object.values(q2m),
    }),
  });
}

simulationButton.addEventListener("click", () => {
  createSimulation();
  connect();
  isSimulation = true;
  //formProducts();
});
