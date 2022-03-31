$(function(){

    let getMessageElement = function(message) {
        let item = $('<div class="message-item"></div>');
        let header = $('<div class="message-header"></div>');

        header.append($('<div class="datetime">' + message.datetime + '</div>'));
        header.append($('<div class="username">' + message.username + '</div>'));

        let text = $('<div class="message-text">' + message.text + '</div>')
        item.append(header, text);
        return item;
    };

    let getUser = function(user) {
        let item = $('<div class="user-name">' + user.name + '</div>');
        return item;
    };

    let updateMessages = function() {
        $.get('/messages', {}, function(response) {
            if(response.length == 0) {
                return;
            }
            $('.messages-list').html('');
            for(i in response) {
                let element = getMessageElement(response[i]);
                $('.messages-list').append(element);
            }
        });
    };

    let updateUsers = function() {
        $.get('/users', {}, function(response){
            if(response.length == 0) {
                return;
            }
            $('.users-list').html('');
            for(i in response) {
                let user = getUser(response[i]);
                $('.users-list').append(user);
            }
        });
    }

    let initApplication = function() {
        $('.messages-and-users').css({display : 'flex'});
        $('.controls').css({display : 'flex'});

        $('.send-message').on('click', function() {
            let message = $('.new-message').val();
            $.post('/messages', {message : message}, function(response) {
                if(response.result) {
                    $('.new-message').val('');
                } else {
                    alert('Всё сломалось, но можно потыкать ещё');
                }
            });
        });

        setInterval(updateMessages, 1000);
        setInterval(updateUsers, 1000);
    };

    let registerUser = function(name) {
        $.post('/auth', {name: name}, function(response) {
            if(response.result) {
                initApplication();
            }else{
                let name = prompt('Имя не должно быть пустым');
                registerUser(name)
            }
        });
    };

    $.get('/init', {}, function(response) {
         if(!response.result){
             let name = prompt('Введите ваше');
             registerUser(name);
             return;
         }
         initApplication();

    });
});