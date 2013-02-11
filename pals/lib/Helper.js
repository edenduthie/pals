getFormData = function(form) {
    var data = new Array();
    form.find('input').each(function(key,value){
        data[value.attr("name")] = value.val();
    });
    return data;
}


