
function selectAll(aForm, checkState) {
	for (var i=0; i < aForm.elements.length; i++) {
		if (aForm.elements[i].type == "checkbox") {
			aForm.elements[i].checked = checkState;
		}
	}
}

function checkSomethingIsSelected(aForm) {
	for (var i=0; i < aForm.elements.length; i++) {
		if (aForm.elements[i].type == "checkbox" && aForm.elements[i].checked) {
			return true;
		}
	}
	return false;
}

function getWindowSize() {
	var myWidth = 0, myHeight = 0;
	if( typeof( window.innerWidth ) == 'number' ) {
		//Non-IE
	    myWidth = window.innerWidth;
	    myHeight = window.innerHeight;
	} else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
	    //IE 6+ in 'standards compliant mode'
	    myWidth = document.documentElement.clientWidth;
	    myHeight = document.documentElement.clientHeight;
	} else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
	    //IE 4 compatible
	    myWidth = document.body.clientWidth;
	    myHeight = document.body.clientHeight;
	}
	return myWidth, myHeight;
	//window.alert( 'Width = ' + myWidth );
	//window.alert( 'Height = ' + myHeight );
}

var ddmenuitem = 0;

function jsddm_open()
{  
   jsddm_close();
   ddmenuitem = $(this).find('ul').css('visibility', 'visible');
}

function jsddm_close()
{  if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');
}

$(document).ready(function()
{  $('#jsddm > li').bind('mouseover', jsddm_open)
   $('#jsddm > li').bind('mouseout',  jsddm_close)
   });

document.onclick = jsddm_close;
