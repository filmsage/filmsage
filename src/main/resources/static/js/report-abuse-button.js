$(document).ready(() =>{

$("#report-abuse").click(async function(event){
    event.preventDefault();
    const response = await fetch(`/report?id=${$(this).data("id")}&type=${$(this).data("type")}`);
    const result = await response.text();
    alert(result);
})

});