Template.header.events = {
    'click a.button': function () {
        $(contentSelector).html(Meteor.render(function() {return Template.palsSignUp();}));
    },
    'click a.home-link': function () {
        $(contentSelector).html(Template.welcome());
    },
    'click .logo': function () {
        $(contentSelector).html(Template.welcome());
    },
    'click a.error-close': function () {
        $('#error').hide();
    }
};


