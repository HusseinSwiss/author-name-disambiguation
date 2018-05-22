function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}
function validateEditForm(){
	var x=document.forms["editForm"]["name"].value;
	if(x.length==0){
		alert("Error: Empty Name");
		return false;
	}
	x=document.forms["editForm"]["url"].value;
	if(x.length==0){
		alert("Error: Empty URL");
		return false;
	}
	x=document.forms["editForm"]["type"].value;
	if(x.length==0){
		alert("Error: Empty Type");
		return false;
	}
	x=document.forms["editForm"]["street"].value;
	if(x.length==0){
		alert("Error: Empty Type");
		return false;
	}
	x=document.forms["editForm"]["city"].value;
	if(x.length==0){
		alert("Error: Empty City");
		return false;
	}
	x=document.forms["editForm"]["zip"].value;
	if(x.length==0){
		alert("Error: Empty ZIP");
		return false;
	}
	if(!isNumber(x)){
		alert("Error: ZIP must be a number");
		return false;
	}
	x=document.forms["editForm"]["country"].value;
	if(x==0){
		alert("Error: Empty Country");
		return false;
	}
}