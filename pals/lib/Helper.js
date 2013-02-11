getFormData = function(form) {
    var data = new Array();
    form.find('input').each(function(key,value){
        var input = $(this);
        data[input.attr('name')] = input.val();
    });
    return data;
}

setFormError = function(form,error) {
//    for( var key in error ) {
//        var input = form.find('input[name="'+key+'"]');
//        input.css('border-color','red');
//        input.attr('title',error[key]);
//        console.log(key + ': ' + error[key]);
//    }
    form.validator().data("validator").invalidate(error);
}

Object.size = function(object) {
    var size = 0;
    for( var key in object ) ++size;
    return size;
}

