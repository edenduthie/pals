$(document).ready(function(){
    $('#vegetationTypeSelect select').change(function(){
    	$('#vegetationTypeNew input').attr('value','');
    });

    function makeExclusive(number) {
        $('.exclusive-'+number+'-a').focus(function(){
        	$('.exclusive-'+number+'-b').val('');
        });
        $('.exclusive-'+number+'-b').focus(function(){
        	$('.exclusive-'+number+'-a').val('');
        });
    }
    makeExclusive(1);
    makeExclusive(2);
});