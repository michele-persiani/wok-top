<%-- 
    Document   : patientchosegraph2
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
        
        <style>
            .bianca{
                min-width:300px;
                color:white !important;
            }
            .bianca:hover{
                background-color:#286090 !important;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="patientchosegraph?patientid=${patient.id}">                                
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
                </nav>
            </div>
            
            <h1>Grafici ${tipo} di ${patient.surname} ${patient.name}</h1>
            <div class="well">
                <h2>Seleziona uno dei grafici disponibili</h2>                
                <ul class="list-group">
                    <li class="list-group-item">
                       <a class="btn btn-lg btn-primary" href="patientbuildgraph?patientid=${patient.id}&kind=${kind}" style="min-width:300px">${tipo} generale</a>
                    </li>
                    <hr />
                <c:forEach begin="0" end="${fn:length(category) - 1}" var="index">
                    <c:choose>
                        <c:when test="${(index>0)&&(category_testo[index]!=category_testo[index-1])}">
                            <li class="list-group-item">
                            <div class="btn-group">
                            <button type="button" class="btn btn-lg btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="min-width:300px;">
                              <c:out value="${category_testo[index]} "/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                        </c:when>
                        <c:when test="${(index==0)}">
                            <li class="list-group-item">
                            <div class="btn-group">
                            <button type="button" class="btn btn-lg btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="min-width:300px;">
                              <c:out value="${category_testo[index]} "/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                        </c:when> 
                    </c:choose>
                    <li class="list-group-item">
                       <a class="btn btn-lg btn-primary bianca" href="patientbuildgraph?patientid=${patient.id}&kind=<c:out value="${category[index]}"/>"><c:out value="${globalcategory[index]} "/></a>
                    </li>
                    <c:choose>

                        <c:when test="${(index<fn:length(category))&&(category_testo[index]!=category_testo[index+1])}">
                            </ul>
                          </div>
                          </li>
                        </c:when> 
                    </c:choose>
                 </c:forEach>
                </ul>
            </div>
        </div>
                    
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
