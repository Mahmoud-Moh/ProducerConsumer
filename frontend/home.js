const button = document.querySelector(".button-35");
const title = document.querySelector(".title");
const titleSpan = document.querySelector(".title span");

button.addEventListener("mouseover", () => {
  title.style.color = "yellow";
  titleSpan.style.color = "aqua";
});

button.addEventListener("mouseout", () => {
  title.style.color = "aqua"; // Reset to original color
  titleSpan.style.color = "yellow"; // Reset to original color
});

button.addEventListener("click", () => {
  window.location.href = "index.html";
});
