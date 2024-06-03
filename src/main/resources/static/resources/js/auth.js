"use script"
const url = 'http://localhost:8081/api/v1';
(function () {
    getToken();
    $('#errorAlert').hide();
    showGagal();
}())

function collectionLoginForm() {
    let data = {
        username: $('#username').val(),
        password: $('#password').val()
    };
    return data;
}

function getToken() {
    $('#login-form').on("submit", function (event) {
        event.preventDefault();
        let data = collectionLoginForm();
        $.ajax({
            method: "Post",
            url: `${url}/auth`,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function (response) {
                event.target.submit();
                localStorage.setItem("token", response.token);
                localStorage.setItem("message",response.message);
            },
              error: function({status, responseJSON}){
               $('#errorAlert').show().text(`${responseJSON.message}`);
             }
        });
    })

}

function showGagal(){
    let message = localStorage.getItem('message');
    if(message){
        $('#errorAlert').show().text(message);
    }
    localStorage.removeItem('message');

}