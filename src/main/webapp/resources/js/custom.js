$(document).ready(function(){
	// In create session page, make buttons become green if an exercise is selected
	   $('.selectpicker:gt(1)').change(function() { // only the select after the first 2
	      var currentValue = $(this).val();
	      if (currentValue != "") {
	          $(this).parent().children( "button" ).removeClass('btn-warning').addClass('btn-success');
	      } else {
	    	  $(this).parent().children( "button" ).removeClass('btn-success').addClass('btn-warning');
	      }
	   }); 
           //var nome="paziente";
           //var ospedale="";
           var nome="partecipante";
           var ospedale="";
           var nomi="partecipanti";
           $("#blocco").append("<p>&copy; 2016-2022 Universit&agrave; di Bologna</p>");
           

});


