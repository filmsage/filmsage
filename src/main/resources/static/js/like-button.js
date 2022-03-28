$(document).ready(() => {
  $("#like-button").click(async function (event) {
    event.preventDefault();
    const response = await fetch(`/like?r=${$(this).data("id")}`);
    const result = await response.text();
    $('#like-count').text(result);
  });

});