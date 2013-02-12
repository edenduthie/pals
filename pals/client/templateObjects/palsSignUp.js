//Template.palsSignUp.events ({
//    'click .sign-up-button': function () {
//        var formData = getFormData($('#sign-up-form'));
//        console.log('sign up');
//        var user = User.factory(formData);
//        //user.create();
//    }
//});

Template.palsSignUp.rendered = function() {
    var me = this;
    $("input[title]").tooltip({
        position: "center right",
        offset: [-2, 10],
        effect: "fade",
        opacity: 0.7
    });
    var signUpButton = me.find('.sign-up-button');
    $(signUpButton).click(function(){
        formData = getFormData($('#sign-up-form'));
        var user = User.factory(formData);
        user.create(me.createFinished);
    });
}

Template.palsSignUp.created = function() {
    this.createFinished = function(error) {
        console.log(error);
        if( error ) setError(error);
        else console.log('created!!!');
    };
}
