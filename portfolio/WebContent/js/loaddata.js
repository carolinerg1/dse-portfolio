$(document).ready(function() {
	
	 $("#frmPortfolio").submit(function(e){
         e.preventDefault();
  });
 
	$('.error').hide();  
	
	$('#btnLoadData').click(function() {
		$('.error').hide();  
	
		//alert (dataString);//return false;  
		
		$.ajax({  
			  type: "POST",  
			  url: "LoadDataServlet",  
			  data: dataString,  
			  dataType:  'json',
			  success: function(json) {  
				  $("#results").append("<br/>Results: " + json.data + " <br/><br/>");
			  }
		});  
		
		return false;  
		// do not submit the form
	
	});
	
	$('#btnAnalyze').click(function() {
		$('.error').hide();  
		
		//alert (dataString);//return false;  
		
		$.ajax({  
			  type: "POST",  
			  url: "AnalyzeServlet",  
			  data: dataString,  
			  dataType:  'json',
			  success: function(json) {  
				  $("#results").append("<br/>Results: " + json.data + " <br/><br/>");
			  }
		});  
		
		return false;  
		// do not submit the form
	
	});
	
});