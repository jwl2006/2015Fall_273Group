<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js" type="text/javascript"></script> 
<title>Device Panel</title>

<style>

#notify_result_table {

width: 400px;
}

.notify_txt {
width: 400px;
}

div.rst_div {
    width: 700px;
    height: 100px;
    border: 3px solid #73AD21;
    overflow: scroll;
}

button{
	width:120px;
}

select{
	width:120px;
}

table {
border: 1px solid black;
}
</style>

<script>
$(document).ready(function(){

	
	$("#Read").click(function(){
		
		var rscpth = $("#read_obj_id :selected").val();
		
		if($("#read_obj_ist_id :selected").text()!="Null")
			rscpth = rscpth +"*" + $("#read_obj_ist_id").val();
		if($("#read_obj_ist_id :selected").text()=="Null"&&$("#read_rsc_id :selected").text()!="Null")
			rscpth = rscpth +"*" + "0";
		if($("#read_rsc_id :selected").text()!="Null")	
			rscpth = rscpth +"*" + $("#read_rsc_id :selected").val().substring($("#read_rsc_id :selected").val().indexOf("/")+1,
					$("#read_rsc_id :selected").val().indexOf("!"));
	//		alert(rscpth + " " + $("#read_obj_ist_id :selected").text()+ " " + $("#read_rsc_id :selected").text());
	    $.ajax({url: "http://localhost:8080/Restful_WebService/readclient/${devid}/"+rscpth, 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	    		$("#read_result").html("<table></table>");
	            var newhd = $('<tr class="dy_created">').append(
	                    $('<th>').append("object id"),
	                    $('<th>').append("obj instance"),
	    	            $('<th>').append("rsc id"),
	                    $('<th>').append("rsc value")
	                );
	            $("#read_result table").append(newhd);
		    	$.each($.parseJSON(result), function(idx, obj) {
		    		//alert(obj.rsciddes);
		            var newtr = $('<tr class="dy_created">').append(
		                    $('<td>').append(
		                    		$('<input type="text" name='+obj.objid+' readonly>').val(obj.objiddes)	),
		                    $('<td>').append(
		    	               		$('<input type="text" name='+obj.objist+' readonly>').val(obj.objist)	),
		    	            $('<td>').append(
		    	                    $('<input type="text" name='+obj.rscid+' readonly>').val(obj.rsciddes)	),
		                    $('<td>').append(
		                    		$('<input type="text" class="dy_txt" name='+obj.objid+'.'+obj.objist+'.'+obj.rscid+' readonly>').val(obj.rscval)	)
		                );
		            $("#read_result table").append(newtr);
		    	});	
	      //  $("#read_result").html(result);
	    }});
	});
	
	
	$("#Discover").click(function(){
		
		var rscpth = $("#disc_obj_id :selected").val();
		
		if($("#disc_obj_ist_id :selected").text()!="Null")
			rscpth = rscpth +"*" + $("#disc_obj_ist_id").val();
		if($("#disc_obj_ist_id :selected").text()=="Null"&&$("#disc_rsc_id :selected").text()!="Null")
			rscpth = rscpth +"*" + "0";
		if($("#disc_rsc_id :selected").text()!="Null")	
			rscpth = rscpth +"*" + $("#disc_rsc_id :selected").val().substring($("#disc_rsc_id :selected").val().indexOf("/")+1,
					$("#disc_rsc_id :selected").val().indexOf("!"));
	//		alert(rscpth + " " + $("#read_obj_ist_id :selected").text()+ " " + $("#read_rsc_id :selected").text());
	    $.ajax({url: "http://localhost:8080/Restful_WebService/discoverclient/${devid}/"+rscpth, 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
			result = result.replace(/\</g, '&lt;');
			result = result.replace(/\>/g, '&gt;');
	        $("#disc_result").html(result);
	    }});
	});
	
	
	$("#Write").click(function(){
		
		var rscpth = $("#write_obj_id :selected").val();
		
		if($("#write_obj_ist_id :selected").text()!="Null")
			rscpth = rscpth +"*" + $("#write_obj_ist_id").val();
		if($("#write_obj_ist_id :selected").text()=="Null"&&$("#write_rsc_id :selected").text()!="Null")
			rscpth = rscpth +"*" + "0";
		if($("#write_rsc_id :selected").text()!="Null")	
			rscpth = rscpth +"*" + $("#write_rsc_id :selected").val().substring($("#write_rsc_id :selected").val().indexOf("/")+1,
					$("#write_rsc_id :selected").val().indexOf("!"));
		//	alert("http://localhost:8080/Restful_WebService/writeclient/${devid}/"+rscpth+"/"+$("#write_value").val());
		    $.ajax({url: "http://localhost:8080/Restful_WebService/writeclient/${devid}/"+rscpth+"/"+$("#write_value").val(), 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	        $("#write_result").html(result);
	    }}); 
	});
	
	
	$("#Write_attribute").click(function(){
		
		var attpth = $("#write_att_obj_id :selected").val();

		if($("#write_att_rsc_id :selected").text()!="Null")	
			attpth = attpth +"*" + $("#write_att_rsc_id :selected").val().substring($("#write_att_rsc_id :selected").val().indexOf("/")+1,
					$("#write_att_rsc_id :selected").val().indexOf("!"));            
			//alert("http://localhost:8080/Restful_WebService/writeattclient/${devid}/"+attpth+"/"+$("#write_att_attribute_id :selected").text()+"/"+$("#write_att_value").val());
		    $.ajax({url: "http://localhost:8080/Restful_WebService/writeattclient/${devid}/"+attpth+"/"+$("#write_att_attribute_id :selected").val()+"/"+$("#write_att_value").val(), 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	        $("#write_att_result").html(result);
	    }});  
	});
	
	
	$("#Execute").click(function(){
		
		var rscpth = $("#execute_obj_id :selected").val();

		if($("#execute_obj_ist_id :selected").text()!="Null")
			rscpth = rscpth +"*" + $("#execute_obj_ist_id").val();
		if($("#execute_obj_ist_id :selected").text()=="Null"&&$("#execute_rsc_id :selected").text()!="Null")
			rscpth = rscpth +"*" + "0";
		if($("#execute_rsc_id :selected").text()!="Null")	
			rscpth = rscpth +"*" + $("#execute_rsc_id :selected").val().substring($("#execute_rsc_id :selected").val().indexOf("/")+1,
					$("#execute_rsc_id :selected").val().indexOf("!"));            
	
	//	alert("http://localhost:8080/Restful_WebService/executeclient/${devid}/"+rscpth+"/"+$("#execute_command_id :selected").val());
 		    $.ajax({url: "http://localhost:8080/Restful_WebService/executeclient/${devid}/"+rscpth+"/"+$("#execute_command_id :selected").val(), 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	        $("#execute_result").html(result);
	    }});  
	});
	
	
	$("#Create").click(function(){
		
		var rscpth = $("#create_obj_id :selected").val();

		if($("#create_rsc_id :selected").text()!="Null")	
			rscpth = rscpth +"*" + $("#create_rsc_id :selected").val().substring($("#create_rsc_id :selected").val().indexOf("/")+1,
					$("#create_rsc_id :selected").val().indexOf("!"));
			//alert("http://localhost:8080/Restful_WebService/createinstanceclient/${devid}/"+rscpth+"/"+$("#createinstance_resource_value").val());
		    $.ajax({url: "http://localhost:8080/Restful_WebService/createinstanceclient/${devid}/"+rscpth+"/"+$("#createinstance_resource_value").val(), 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	        $("#create_result").html(result);
	    }});  
	});
	
	
	
	$("#Delete").click(function(){
		
		var rscpth = $("#delete_obj_id :selected").val();
		rscpth = rscpth +"*" + $("#delete_obj_ist_id").val();

		//	alert("http://localhost:8080/Restful_WebService/deleteinstanceclient/${devid}/"+rscpth);
 		    $.ajax({url: "http://localhost:8080/Restful_WebService/deleteinstanceclient/${devid}/"+rscpth, 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	        $("#delete_result").html(result);
	    }});   
	});	

	
	
	//alert("${devid}");
	callAjax();
	getnotify();
	

	function callAjax(){

	    $.ajax({url: "http://localhost:8080/Restful_WebService/getobjrsc", 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){	 	       	
		    	$.each($.parseJSON(result), function(idx, obj) {
		     		if(obj.substring(1,2)=="!")
		     			$(".obj_id_sel").append($("<option></option>").attr("value",obj.substring(0,1)).text(obj));
		     		else
		     			$(".rsc_id_sel").append($("<option></option>").attr("value",obj).text(obj));
		    	});
		    	$(".obj_ist_id_sel").append($("<option></option>").attr("value",0).text(0));
		    	$(".obj_ist_id_sel").append($("<option></option>").attr("value",1).text(1));
		    	$(".obj_ist_id_sel").append($("<option></option>").attr("value",2).text(2));
		    	$(".obj_ist_id_sel").append($("<option></option>").attr("value",3).text(3));
		    	$(".obj_ist_id_sel").append($("<option></option>").attr("value",4).text(4));
	 	       

		    	 
	    }
	        });
	};
	
	

	function getnotify(){
		
		
	    $.ajax({url: "http://localhost:8080/Restful_WebService/getnotify", 
	    	type: "GET",
	    	DataType: "text",
	    	error: function(xhr){
	            alert("An error occured: " + xhr.status + " " + xhr.statusText);
	        }, 
	    	success: function(result){
	 	        /* window.location.reload(); */
		    	$.each($.parseJSON(result), function(idx, obj) {

			    	$.each($.parseJSON(result), function(idx, obj) {
			            var newtr = $('<tr class="dy_created">').append(
			                    $('<td>').append($('<input type="text" class = "notify_txt" readonly>').val(obj)	)
			                );
			            $("#notify_result_table").append(newtr);
			    	});	
		    	});
		    	 
	    },
	    complete: function() {
	        setTimeout(getnotify, 1500);
	      }
	        });
	};
	
});



</script>

</head>
<body>

<h3>${devid}</h3>

<div id = "div_layout">
<table id="manage_layout">
<tr><td><table><tr>
<td><button type="button" id="Read" onclick="">Read</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "read_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "read_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "read_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div id = "read_result" class="rst_div"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Discover" onclick="">Discover</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "disc_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "disc_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "disc_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div  class="rst_div" id = "disc_result"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Write" onclick="">Write</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "write_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "write_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "write_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr>
<tr>
<td></td>
<td><a>Value</a>
<input type="text" id = "write_value">
</td>
</tr>
</table></td></tr>
<tr><td><div class="rst_div" id = "write_result"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Write_attribute" onclick="">Write Attribute</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "write_att_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "write_att_obj_ist_id" disabled>
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "write_att_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr>
<tr>
<td></td>
<td><a>Attribute</a>
<select class = "attribute_sel" id = "write_att_attribute_id">
  <option value="">Null</option>
  <option value="pmin">Minimum Period</option>
  <option value="pmax">Maximum Period</option>
  <option value="gt">Greater Than</option>
  <option value="lt">Less Than</option>
  <option value="st">Step</option>
  <option value="cancel">Cancel</option>
</select></td>
<td><a>Value</a>
<input type="text" id = "write_att_value">
</td>
</tr>
</table></td></tr>
<tr><td><div class="rst_div" id = "write_att_result"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Execute" onclick="">Execute</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "execute_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "execute_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "execute_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr>
<tr>
<td></td>
<td><a>command</a>
<select class = "command_sel" id = "execute_command_id">
  <option value="Null">Null</option>
  <option value="light_on">Indicator Light On</option>
  <option value="light_off">Indicator Light Off</option>
</select></td>
</tr>
</table></td></tr>
<tr><td><div class="rst_div" id = "execute_result"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Create" onclick="">Create</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "create_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "create_obj_ist_id" disabled>
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "create_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr>
<tr>
<td></td>
<td><a>Value</a>
<input type="text" id = "createinstance_resource_value">
</td>
</tr>
</table></td></tr>
<tr><td><div class="rst_div" id = "create_result"></div></td></tr>

<tr><td><table><tr>
<td><button type="button" id="Delete" onclick="">Delete</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "delete_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "delete_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "delete_rsc_id" disabled>
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div class="rst_div" id = "delete_result"></div></td></tr>

<!-- 
<tr><td><table><tr>
<td><button type="button" id="Observe" onclick="">Observe</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "observe_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "observe_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "observe_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div class="rst_div"></div></td></tr>
 -->
<tr><td><table><tr>
<td><button type="button" id="Notify" onclick="" disabled>Notify</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "notify_obj_id" disabled>
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "notify_obj_ist_id" disabled>
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "notify_rsc_id" disabled>
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div class="rst_div" id = "notify_result"><table id = "notify_result_table"></table></div></td></tr>
<!--  
<tr><td><table><tr>
<td><button type="button" id="Cancel_observation" onclick="">Cancel Observation</button></td>
<td><a>Object id</a>
<select class = "obj_id_sel" id = "cancel_obv_obj_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Object Instance id</a>
<select  class = "obj_ist_id_sel" id = "cancel_obv_obj_ist_id">
  <option value="">Null</option>
</select>
</td>
<td><a>Resource id</a>
<select class = "rsc_id_sel" id = "cancel_obv_rsc_id">
  <option value="">Null</option>
</select>
</td>
</tr></table></td></tr>
<tr><td><div class="rst_div"></div></td></tr>
-->
</table>
</div>
</body>
</html>