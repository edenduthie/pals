Template.footer.events = {
    'click a.user': function () {
        $(contentSelector).html(Meteor.render(function() {return Template.userList();}));
    }
};


