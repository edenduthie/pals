Template.palsSignUp.rendered = function() {
    var me = this;
//    $("input[title]").tooltip({
//        position: "center right",
//        offset: [-2, 10],
//        effect: "fade",
//        opacity: 0.7
//    });
    var signUpButton = me.find('.sign-up-button');
    $(signUpButton).click(function(){
        var validator = $('#sign-up-form :input').validator(); // html markup-based validation
        if( validator.data("validator").checkValidity() ) {
            formData = getFormData($('#sign-up-form'));
            var user = User.factory(formData);
            user.create(me.createFinished);
        }
    });
}

Template.palsSignUp.created = function() {
    this.createFinished = function(error) {
        if( error ) setError(error);
        else 
        {
            $(contentSelector).html(Template.main());
        }
    };
}
