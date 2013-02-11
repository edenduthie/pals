function User() {
    this.createdAt = new Date().getTime();
}

User.factory = function(formData) {
    var user = new User();
    user.formData = formData;
    return user;
}

User.prototype.create = function() {
    var me = this;
    me.user = Accounts.createUser({
        username : me.formData.username,
        email : me.formData.email,
        password : me.formData.password,
        profile : {
            name : me.formData.name,
            institution : me.formData.institution
        }
    });
}

User.prototype.validate = function() {
    var me = this;
    var error = new Array();
    if( !me.username || me.username.length <= 0 ) error['username'] = 'Username is required';
    if( Object.size(error) > 0 ) return error;
}


