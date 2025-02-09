document.body.addEventListener('htmx:responseError', function (event) {
    const errorMessage =
        event.detail.xhr.responseText || "An error occurred";
    const error = JSON.parse(errorMessage);
    alert(error.message);
});
