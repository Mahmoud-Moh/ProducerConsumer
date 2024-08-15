function handleUpdate(update) {
  //console.log("Handling Update .....");
  const type = update.type;
  const operation = update.operation;
  const product = update.product;
  const id = parseInt(update.id, 10);
  const color = product.color;
  console.log("color: ", color);
  if (type === "inputter") {
    addAnimation("inputter", 0, "queue", 0, color);
  } else if (type === "machine") {
    if (operation == "start") {
      machines[id].color = product.color;
      let last_place = product.lastPlace;
      let last_place_type = last_place.slice(0, 1);
      let last_place_id = parseInt(last_place.slice(1));
      if (last_place_type == "i") {
        console.log("@first add animation");
        addAnimation("inputter", 0, "machine", id, color);
      } else if (last_place_type === "m") {
        console.log("@@second add animation");
        addAnimation("machine", last_place_id, "machine", id, color);
      } else if (last_place_type === "q") {
        console.log("@@@@third add animation");
        addAnimation("queue", last_place_id, "machine", id, color);
      }
    } else if (operation === "finish") {
      machines[id].color = machineColor;
      //changeMachineColor(id, machineColor);
    }
  } else if (type === "queue") {
    updateQueueContentSize(id, update.sizeNow);
    if (operation === "enqueue") {
      let last_place = product.lastPlace;
      let last_place_type = last_place.slice(0, 1);
      let last_place_id = parseInt(last_place.slice(1));
      if (last_place_type == "i") {
        console.log("@@@@fourth add animation");
        addAnimation("inputter", 0, "queue", id, color);
      } else if (last_place_type === "m") {
        console.log("@@@@@fifth add animation");
        addAnimation("machine", last_place_id, "queue", id, color);
      } else if (last_place_type === "q") {
        console.log("@@@@@@sixth add animation");
        addAnimation("queue", last_place_id, "queue", id, color);
      }
    }
  }
}

function updateQueueContentSize(id, sizeNow) {
  queues[id].content = sizeNow;
}
