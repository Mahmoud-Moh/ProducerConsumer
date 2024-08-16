function handleUpdate(update) {
  const type = update.type;
  const operation = update.operation;
  const product = update.product;
  const id = parseInt(update.id, 10);
  const color = product.color;

  if (type === "inputter") {
    addAnimation("inputter", 0, "queue", id, color, product.id);
  } else if (type === "machine") {
    if (operation == "start") {
      //machines[id].color = product.color;
      let last_place = product.lastPlace;
      let last_place_type = last_place.slice(0, 1);
      let last_place_id = parseInt(last_place.slice(1));
      if (last_place_type == "i") {
        addAnimation("inputter", 0, "machine", id, color, product.id);
      } else if (last_place_type === "m") {
        addAnimation(
          "machine",
          last_place_id,
          "machine",
          id,
          color,
          product.id
        );
      } else if (last_place_type === "q") {
        addAnimation("queue", last_place_id, "machine", id, color, product.id);
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
        addAnimation("inputter", 0, "queue", id, color, product.id);
      } else if (last_place_type === "m") {
        addAnimation("machine", last_place_id, "queue", id, color, product.id);
      } else if (last_place_type === "q") {
        queues[last_place_id].content -= 1;
        addAnimation("queue", last_place_id, "queue", id, color, product.id);
      }
    } else if (operation === "dequeue") {
      queues[id].content = update.sizeNow;
    } else if (operation === "throw") {
      addAnimation("queue", id, "outputter", 0, color, product.id);
    }
  }
}

function updateQueueContentSize(id, sizeNow) {
  queues[id].laterContent = sizeNow;
}
