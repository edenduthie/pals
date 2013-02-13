function User() {
    this.createdAt = new Date().getTime();
}

User.factory = function(formData) {
    var user = new User();
    user.formData = formData;
    return user;
}

User.prototype.create = function(callback) {
    var me = this;
    var error = me.validate();
    if( error ) callback(error);
    else
    {
        try
        {
            me.user = Accounts.createUser({
                username : me.formData.username,
                email : me.formData.email,
                password : me.formData.password,
                profile : {
                    name : me.formData.name,
                    institution : me.formData.institution
                }
            },callback);
        }
        catch(err)
        {
            callback(err.toString());
        }
    }
}

User.prototype.validate = function() {
    var me = this;
    if( me.formData.password != me.formData.confirmPassword ) return 'passwords do not match';
}


