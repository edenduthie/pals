Template.palsSignUp.events ({
    'click .sign-up-button': function () {
        var formData = getFormData($('#sign-up-form'));
        var user = User.factory(formData);
        var error = user.validate();
        if( error ) setFormError($('#sign-up-form'),error);
        else user.create();
    }
});

Template.palsSignUp.rendered = function() {
    $("input[title]").tooltip({
        position: "center right",
        offset: [-2, 10],
        effect: "fade",
        opacity: 0.7
    });
}
