$(document).ready(() => {
  $("#like-button").click(async function (event) {
    event.preventDefault();
    console.log("liking post id: " + $(this).data("id"))
    const response = await fetch(`/like?r=${$(this).data("id")}`);
    const result = await response.text();
    console.log("like value: " + $(this).data("id"))
    // const currentLikes = $('#like-count').text();
    // const newLikes = Number.parseInt(result) + Number.parseInt(currentLikes);
    $('#like-count').text(result);
  });
});