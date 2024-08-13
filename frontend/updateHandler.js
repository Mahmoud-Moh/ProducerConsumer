function handleUpdate(update) {
  const type = update.type;
  const operation = update.operation;
  const product = update.product;
  const id = praseInt(update.id, 10);
  if (type === "inputter") {
    addAnimation("inputter", 0, "queue", 0);
  } else if (type === "machine") {
    if (operation == "start") {
      let last_place = product.lastPlace;
      let last_place_type = last_place.slice(0, 1);
      let last_place_id = parseInt(last_place.slice(1), 10);
      if (last_place_type == "i") {
        addAnimation("inputter", 0, "machine", id);
      } else if (last_place_type === "m") {
        addAnimation("machine", last_place_id, "machine", id);
      } else if (last_place_type === "q") {
        addAnimation("queue", last_place_id, "machine", id);
      }
    } else if (operation === "finish") {
      changeMachineColor(id, machineColor);
    }
  } else if (type === "queue") {
    if (operation == "enqueue") {
      let last_place = product.lastPlace;
      let last_place_type = last_place.slice(0, 1);
      let last_place_id = parseInt(last_place.slice(1), 10);
      if (last_place_type == "i") {
        addAnimation("inputter", 0, "queue", id);
      } else if (last_place_type === "m") {
        addAnimation("machine", last_place_id, "queue", id);
      } else if (last_place_type === "q") {
        addAnimation("queue", last_place_id, "queue", id);
      }
    }
  }
}
