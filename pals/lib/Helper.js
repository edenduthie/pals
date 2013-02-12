getFormData = function(form) {
    var data = new Array();
    form.find('input').each(function(key,value){
        var input = $(this);
        data[input.attr('name')] = input.val();
    });
    return data;
}

setError = function(message) {
    $('#error .error-content').html(message);
    $('#error').show();
}

clearError = function() {
    $('#error').hide();
}

Object.size = function(object) {
    var size = 0;
    for( var key in object ) ++size;
    return size;
}

