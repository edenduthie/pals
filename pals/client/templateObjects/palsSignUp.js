Template.palsSignUp.events ({
    'click .sign-up-button': function () {
        var accounts = new PalsAccounts();
        console.log(getFormData($('#sign-up-form')));
        accounts.signUp();
    }
});
