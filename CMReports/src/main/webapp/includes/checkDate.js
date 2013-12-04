// JavaScript Document
function checkDateFormat(dateTextBox)
{
	var dateValue;
	var dt_current_day = new Date();
	dateValue = dateTextBox.value;
	var short_date = /^(\d+)\/(\d+)/;
	var re_date = /^(\d+)\/(\d+)\/(\d+)/;  
	if(!(dateValue.length>5))
	{
	if(short_date.exec(dateValue))
	{
		dateValue = dateValue + "/" +  dt_current_day.getYear();
		dateTextBox.value = dateValue;
	}
	else
		return alert("Invalid Datetime format: "+ dateValue);
	}
	else
	{
	    
	if (!re_date.exec(dateValue))
		return alert("Invalid Datetime format: "+ dateValue);
	}
}

function checkEmail(obj)
{
	var emailReg ="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,6}$.";
	myregexp = new RegExp(emailReg);
}