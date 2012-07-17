function Validation()
{
};

Validation.prototype.textLength = function(name,numCharacters,description)
{
	$("input[name='" + name + "']" ).change(function(){
		var length = $(this).val().length;
		if( length > numCharacters )
		{
			alert(description + " cannot be more than " + numCharacters + " characters. It is currently " + length);
		}
	});
};
