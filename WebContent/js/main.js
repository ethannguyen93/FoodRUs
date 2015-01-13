
function increaseQuantity(node){
	
	var inputbox = document.getElementById(node.value + "_item_quantity");
	inputbox.value++;
}
function decreaseQuantity(node){
	var inputbox = document.getElementById(node.value + "_item_quantity");
	if (inputbox.value > 1){
		inputbox.value--;
	}
}
function getquantity(node){
	var num = document.getElementById(node.value + "_item_quantity").value;
	document.forms[node.value + "_submit"].elements[node.value + "_itemquantity"].value = num;
}
function CartincreaseQuantity(node, p){
	var inputbox = document.getElementById(node.value + "_item_quantity");
	inputbox.value++;
	document.forms["submit"].elements["quantity_" + node.value].value = inputbox.value;
	var price = p * inputbox.value;
	document.getElementById(node.value + "_total").innerHTML = "$" + parseFloat(Math.round(price * 100) / 100).toFixed(2);
}
function CartdecreaseQuantity(node, p){
	var inputbox = document.getElementById(node.value + "_item_quantity");
	if (inputbox.value > 1){
		inputbox.value--;
		var price = p * inputbox.value;
		document.forms["submit"].elements["quantity_" + node.value].value = inputbox.value;
		document.getElementById(node.value + "_total").innerHTML = "$" + parseFloat(Math.round(price * 100) / 100).toFixed(2);
	}
}
function changequantity(node){
	alert(node.value);
	alert(document.getElementById("quantity_" + node.value).value);
	document.getElementById("quantity_" + node.value).value = document.getElementById(node.value + "_item_quantity");
}