<%-- 
    Document   : patientchosegraph
    Created on : 14-feb-2017, 11.26.08
    Author     : danger
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="DangerBlack">
        <link rel="icon" href="resources/favicon.ico">
        <title>Scegli il Grafico per il <!--paziente-->partecipante</title>
        
         <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">
        
        <link rel="stylesheet" href="resources/css/daterangepicker.css">
        <!-- <link rel="stylesheet" href="resources/css/bootstrap-datepicker3.min.css"> -->

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
        
        
        <!-- <script src="resources/js/bootstrap-datepicker.min.js"></script> -->
        
        <script src="resources/js/moment.min.js"></script>
        <script src="resources/js/daterangepicker.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        
        <!-- Load c3.css -->
        <link href="resources/css/c3.min.css" rel="stylesheet" type="text/css">

        <!-- Load d3.js and c3.js -->
        
        <script src="resources/js/d3.v4.min.js" charset="utf-8"></script>
        <script src="resources/js/c3.min.js"></script>
        
        <script>
            function updatePrevPage(back_link){
                if (typeof(Storage) !== "undefined") {
                    localStorage.setItem("back_link", back_link);
                    
                }
            }
            function getPrevPage(){
                if (typeof(Storage) !== "undefined") {
                    return localStorage.getItem("back_link"); 
                } else {
                    return "patientsstatistics";
                }
            }
            $(document).ready(function(){
                 var back_link=document.referrer;
                 console.log(back_link);
                 var ind=back_link.indexOf("patientchosegraph2");
                 console.log(ind);
                if((ind!=-1)||(back_link.indexOf("patientbuildgraphoverview")!=-1)){
                    console.log("2");
                    back_link=getPrevPage();
                }else{
                    console.log("1");
                    updatePrevPage(back_link);                    
                }

                $("#backbutton").attr("href",back_link);
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a id="backbutton" href="patientsstatistics">                                
                                <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="adminhome">                                
                                <span class="glyphicon glyphicon-home fa-2x" data-toggle="tooltip" data-placement="bottom" title="Home"></span>
                            </a>
                        </li>
                        <li>
                            <a href="logout">
                                <span class="glyphicon glyphicon-log-out fa-2x" data-toggle="tooltip" data-placement="bottom" title="Logout"></span>
                            </a>
                        </li>
                        
                    </ul>
                    <ul class="nav nav-pills pull-left">
                        <li role="presentation">
                            <a data-toggle="modal" href="#infoModal">
                                <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            
            <h1>Grafici di ${patient.surname} ${patient.name}</h1>
            <div class="well">
                <h2>Seleziona uno dei grafici disponibili</h2>
                
                <ul class="list-group">
                    <li class="list-group-item">
                       <a class="btn btn-lg btn-primary" href="patientbuildgraphoverview?patientid=${patient.id}&kind=panoramica" style="min-width:300px">Panoramica</a>
                    </li>
                    <hr />                    
                   <!--  <c:forEach begin="0" end="${fn:length(types) - 1}" var="index">
                        <li class="list-group-item">
                           <a class="btn btn-lg btn-primary" href="patientchosegraph2?patientid=${patient.id}&kind=<c:out value="${types[index]}"/>" style="min-width:300px"><c:out value="${fn:toUpperCase(fn:substring(types[index],0,1))}${fn:toLowerCase(fn:substring(types[index],1,fn:length(types[index])))}"/></a>
                        </li>
                     </c:forEach> -->
                     	 <li class="list-group-item">
                      		 <a class="btn btn-lg btn-primary" href="patientchosegraph2?patientid=${patient.id}&kind=attenzione" style="min-width:300px">Attenzione</a>
                    	 </li>
                    	 <li class="list-group-item">
                      		 <a class="btn btn-lg btn-primary" href="patientchosegraph2?patientid=${patient.id}&kind=memoria" style="min-width:300px">Memoria</a>
                    	 </li>
                    	 <li class="list-group-item">
                      		 <a class="btn btn-lg btn-primary" href="patientchosegraph2?patientid=${patient.id}&kind=funzioni esecutive" style="min-width:300px">Funzioni Esecutive</a>
                    	 </li>
                </ul>
            </div>
        </div>
                    
        <jsp:include page="modal-chosegraph.jsp"/>        
                    
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>

    </body>
</html>
