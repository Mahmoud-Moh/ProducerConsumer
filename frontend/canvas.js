//------------- HTML ELEMENTS AND INIT -----------------
const canvas = document.querySelector("canvas");
const machineButton = document.getElementById("machine-button");
const queueButton = document.getElementById("queue-button");
const lineButton = document.getElementById("line-button");
const simulationButton = document.getElementById("simulation-button");
const colorPicker = document.getElementById("color-picker");

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
const ctx = canvas.getContext("2d");
const canvas_height = 700;
const product_radius = 10;
const product_col_eps = 5;
let machineColor = "#40E0D0";
let queueColor = "#FFFF00";
//-------------------------------------------------------

//----------------- CANVAS VARIABLES --------------------
const machines = [];
const queues = [];
let selections = [];
const lines = [];
const texts = [];
const products = [];
const animations = [];
let isDrawingMachine = false;
let isDrawingQueue = false;
let isSimulation = false;
let selectedElement = null;
let currentColor = "black"; // Default color
let squareId = 0;
let queueId = 0;
let productId = 0;
let m2q = {},
  q2m = {};

const inputter = {
  x: innerWidth - 200,
  y: innerHeight / 2,
};
const outputter = {
  x: 200,
  y: innerHeight / 2,
};

//-------------------------------------------------------
function animate() {
  requestAnimationFrame(animate);
  ctx.clearRect(0, 0, innerWidth, innerHeight);
  drawInput();
  drawOutput();
  drawLines();
  //if (isSimulation) {
  //drawProducts();
  //}
  drawAnimations();
  drawMachines();
  drawQueues();
  drawTexts();
  drawSelectionBoxs();
}
animate();

//======================= Animation drawing ======================
function drawMachines() {
  for (const square of machines) {
    ctx.fillStyle = square.color;
    ctx.fillRect(square.x, square.y, square.size, square.size);
    ctx.strokeStyle = "black";
    ctx.strokeRect(square.x, square.y, square.size, square.size);
  }
}

function drawQueues() {
  for (const square of queues) {
    ctx.fillStyle = square.color;
    ctx.fillRect(square.x, square.y, square.size * 2, square.size);
    ctx.strokeStyle = "black";
    ctx.strokeRect(square.x, square.y, square.size * 2, square.size);
  }
}

function drawSelectionBoxs() {
  ctx.strokeStyle = "gray";
  ctx.lineWidth = 1;
  ctx.setLineDash([10, 10]);
  for (const selection of selections) {
    ctx.strokeRect(selection.x, selection.y, selection.w, selection.h);
  }
}

function drawLines() {
  ctx.lineWidth = 2;
  ctx.strokeStyle = "black";
  ctx.setLineDash([]);
  for (const line of lines) {
    ctx.beginPath();
    ctx.moveTo(line.xs, line.ys);
    ctx.lineTo(line.xe, line.ye);
    ctx.stroke();
  }
}

function drawTexts() {
  ctx.fillStyle = "#000000";
  ctx.font = "18px Arial";
  ctx.textAlign = "center";
  for (const text of texts) {
    ctx.fillText(text.val, text.x, text.y);
  }
}

function drawProducts() {
  for (let product of products) {
    if (!product.stop) {
      ctx.fillStyle = "red";
      ctx.beginPath();
      ctx.arc(
        Math.ceil(product.x),
        Math.ceil(product.y),
        product_radius,
        0,
        2 * Math.PI
      );
      ctx.fill();
      product.x = product.x + product.xinc;
      product.y = product.y + product.yinc;
      product.step++;
      if (Math.abs(product.step - product.steps) < product_col_eps)
        product.stop = true;
    }
  }
}

function drawAnimations() {
  for (let animation of animations) {
    ctx.fillStyle = "black";
    ctx.fillText(animation.val, animation.x, animation.y);
  }
}
//================================================================

//======================= Shape Forming ==========================
function drawMachine(x, y) {
  const size = 80;
  machines.push({
    id: squareId,
    x: x - size / 2,
    y: y - size / 2,
    size,
    color: machineColor,
  });
  texts.push({
    val: "M" + squareId.toString(),
    x: x,
    y: y,
  });
  squareId++;
  isDrawingMachine = false;
}

function drawQueue(x, y) {
  const size = 60;
  queues.push({
    id: queueId,
    x: x - size,
    y: y - size / 2,
    size,
    color: queueColor,
  });
  texts.push({
    val: "Q" + queueId.toString(),
    x: x,
    y: y,
  });
  queueId++;
  isDrawingQueue = false;
}
function drawMachineSelectionBox(square) {
  let padding = 30;

  selections.push({
    x: square.x - padding,
    y: square.y - padding,
    w: square.size + padding * 2,
    h: square.size + padding * 2,
  });
}

function drawQueueSelectionBox(square) {
  let padding = 30;
  ctx.setLineDash([5, 15]);
  selections.push({
    x: square.x - padding,
    y: square.y - padding,
    w: square.size * 2 + padding * 2,
    h: square.size + padding * 2,
  });
}

function drawLine() {
  if (selections.length >= 2) {
    const points = [];
    for (const selection of selections) {
      points.push({
        x: selection.x + selection.w / 2,
        y: selection.y + selection.h / 2,
      });
    }
    for (let i = 0; i < points.length; i += 2) {
      let xs = points[i].x;
      let ys = points[i].y;
      let xe = points[i + 1].x;
      let ye = points[i + 1].y;

      lines.push({
        xs: points[i].x,
        ys: points[i].y,
        xe: points[i + 1].x,
        ye: points[i + 1].y,
      });
    }
    selections = [];
  }
}

function findElement(x, y) {
  selectedElement = machines.find(
    (square) =>
      x >= square.x &&
      x <= square.x + square.size &&
      y >= square.y &&
      y <= square.y + square.size
  );
  let isQueue = false;

  if (!selectedElement) {
    isQueue = true;
    selectedElement = queues.find(
      (square) =>
        x >= square.x &&
        x <= square.x + square.size * 2 &&
        y >= square.y &&
        y <= square.y + square.size
    );
  }
  return {
    selectedElement: selectedElement,
    isQueue: isQueue,
  };
}

function drawProduct(line) {
  let xs = line.xs;
  let ys = line.ys;
  let xe = line.xe;
  let ye = line.ye;
  let dx = xe - xs;
  let dy = ye - ys;
  let steps = dx > dy ? Math.abs(dx) : Math.abs(dy);
  let stepsDx = dx > dy;
  xinc = dx / steps;
  yinc = dy / steps;

  products.push({
    id: productId,
    x: Math.floor(xs),
    y: Math.floor(ys),
    steps: steps,
    xinc: xinc,
    yinc: yinc,
    stepsDx: stepsDx,
    stop: false,
    step: 0,
  });

  productId++;
}

function formProducts() {
  console.log("form points");
  for (let line of lines) {
    drawProduct(line);
  }
}

function createSimulation() {
  for (let line of lines) {
    let xs = line.xs;
    let ys = line.ys;
    let xe = line.xe;
    let ye = line.ye;
    //There's an assumption that q->m or m->q (no q->q or m->m)
    let foundElement1 = findElement(xs, ys);
    let selectedElement1 = foundElement1.selectedElement;
    let isQueue1 = foundElement1.isQueue;
    let foundElement2 = findElement(xe, ye);
    let selectedElement2 = foundElement2.selectedElement;
    let isQueue2 = foundElement2.isQueue;
    let id = selectedElement1.id;
    if (isQueue1) {
      q2m[id] = selectedElement2.id;
    } else {
      m2q[id] = selectedElement2.id;
    }
  }
}

function addAnimation(type1, id1, type2, id2) {
  let xs = 0,
    ys = 0,
    xe = 0,
    ye = 0;
  if (type1 === "inputter") {
    xs = inputter.x;
    ys = inputter.y;
  } else if (type1 === "queue") {
    xs = queues[id1].x;
    ys = queues[id1].y;
  } else if (type1 === "machine") {
    xs = machines[id1].x;
    ys = machines[id1].y;
  }

  if (type2 === "outputter") {
    xe = outputter.x;
    ye = outputter.y;
  }
  if (type2 === "queue") {
    xe = queues[id2].x;
    ye = queues[id2].y;
  } else if (type2 === "machine") {
    xe = machines[id2].x;
    ye = machines[id2].y;
  }
  animations.push({
    val: "start",
    x: xs,
    y: ys,
  });
  animations.push({
    val: "end",
    x: xe,
    y: ye,
  });
}
//===================================================================
// Add event listener to draw squares
canvas.addEventListener("click", (event) => {
  var x = event.clientX;
  var y = event.clientY;
  console.log(x + " " + y);
  y = (y * innerHeight) / canvas_height;

  if (isDrawingMachine) {
    drawMachine(x, y);
  } else if (isDrawingQueue) {
    drawQueue(x, y);
  } else {
    // Check if click is inside any square
    let foundElement = findElement(x, y);
    let selectedElement = foundElement.selectedElement;
    let isQueue = foundElement.isQueue;
    if (selectedElement) {
      if (isQueue) drawQueueSelectionBox(selectedElement);
      else drawMachineSelectionBox(selectedElement);
      if (isQueue) selectedElement.color = queueColor;
      else selectedElement.color = machineColor; // Change color of selected square
    } else {
      selections = [];
    }
  }
});

//=================== Evenet Listeners==================

// Toggle drawing mode
machineButton.addEventListener("click", () => {
  isDrawingMachine = true;
});
queueButton.addEventListener("click", () => {
  //console.log("queue butto");
  isDrawingQueue = true;
});
lineButton.addEventListener("click", () => {
  drawLine();
});

// Update current color from color picker
colorPicker.addEventListener("input", (event) => {
  currentColor = event.target.value;
  machineColor = currentColor;
  queueColor = currentColor;
  console.log(currentColor);
});

//=======================================================
//=============Draw Input and Output=====================

//Input

function drawInput() {
  ctx.fillStyle = "#A569BD";
  ctx.beginPath();
  ctx.arc(inputter.x, inputter.y, 50, 0, 2 * Math.PI);
  ctx.fill();
  ctx.setLineDash([]);
  ctx.strokeStyle = "black";
  ctx.beginPath();
  ctx.arc(inputter.x, inputter.y, 50, 0, 2 * Math.PI);
  ctx.stroke();
  ctx.fillStyle = "black";
  ctx.fillText("Input", inputter.x, inputter.y);
}

function drawOutput() {
  ctx.fillStyle = " #D35400";
  ctx.beginPath();
  ctx.arc(outputter.x, outputter.y, 50, 0, 2 * Math.PI);
  ctx.fill();
  ctx.setLineDash([]);
  ctx.strokeStyle = "black";
  ctx.beginPath();
  ctx.arc(outputter.x, outputter.y, 50, 0, 2 * Math.PI);
  ctx.stroke();
  ctx.fillStyle = "black";
  ctx.fillText("Output", outputter.x, outputter.y);
}
