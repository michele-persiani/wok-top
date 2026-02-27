<%-- 
    Document   : memory1a
    Created on : May 9, 2016, 17:42:00 AM
    Author     : floriano
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html lang="it">
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Floriano Zini">
        <link rel="icon" href="resources/favicon.ico">

        <title>Memoria visiva</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron.css">

        <!--link rel="stylesheet" href="resources/signin.css"-->

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <script type="text/javascript" src="resources/js/hello.js"></script>        
            

        <style>            
            .navbar + .well {
                padding-top: 80px;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <c:if test="${difficulty=='training' && patientid=='-1'}">
                                <a href="patienttraining">                                
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </c:if>                            
                            <c:if test="${difficulty=='training' && patientid!='-1'}">
                                <a href="patientrehabilitation">                            
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </c:if>                            
                            <c:if test="${difficulty=='demo'}">
                                <a href="patientdemo">                                
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </c:if>                            
                            <c:if test="${difficulty!='training' && difficulty!='demo'}">
                                <a href="patientrehabilitation">                                
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </c:if>                            
                        </li>
                        <li>
                            <a href="patienthome">
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
                                <span class="glyphicon glyphicon-info-sign fa-2x"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            
            <!--div class="navbar navbar-fixed-top" style="background-color:white"-->
            
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-18">
                            <c:choose>
                                <c:when test="${exname=='MEM_VIS_1_FAC'}">
                                    <h3 class="pull-left">Memorizzi i volti sullo schermo per il tempo indicato.</h3>
                                    <h3 class="pull-left">Poi clicchi i volti memorizzati fra quelli che appariranno, entro il tempo massimo.</h3>
                                     <h3 class="pull-left">Occorre selezionare lo stimolo. Non &egrave; prevista la possibilit&agrave; di poter correggere la selezione.</h3>
                                    <h3 class="pull-left">L'ordine in cui ricorda i volti non &egrave; importante.</h3>
                                </c:when>
                                <c:when test="${exname=='MEM_VIS_1_CMP'}">
                                    <h3 class="pull-left">Memorizzi l’orientamento indicato dalle parole sullo schermo.</h3>
                                    <h3 class="pull-left">Poi clicchi su tutte frecce corrispondenti all’orientamento memorizzato.</h3>
                                     <h3 class="pull-left">Occorre selezionare lo stimolo. Non &egrave; prevista la possibilit&agrave; di poter correggere la selezione.</h3>
                                    <h3 class="pull-left">L'ordine in cui ricorda l'orientamento non &egrave; importante.</h3>
  
                                </c:when>                                     
                                <c:otherwise>
                                    <h3 class="pull-left">Memorizzi le figure sullo schermo per il tempo indicato.</h3>
                                    <h3 class="pull-left">Poi clicchi le figure memorizzate fra quelle che appariranno.</h3>
                                    <h3 class="pull-left">Occorre selezionare lo stimolo. Non &egrave; prevista la possibilit&agrave; di poter correggere la selezione.</h3>
                                    <h3 class="pull-left">L'ordine in cui ricorda le figure non &egrave; importante.</h3>
                               
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <c:if test="${difficulty=='training' || difficulty=='demo'}">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-4"><h3 style='color:red'>Esercizio di prova</h3></div>
                        </c:if>
                    </div>
                    <div class="row">
                    <div class="col-sm-18">                        
                                    <h3 class="pull-left">Quando ha finito clicchi il bottone "Ho finito".</h3>                            
                    </div>    
                        <div class="col-sm-6">
                            <button class="btn btn-lg btn-success pull-right"
                                    onclick=
                                        "post('memory1phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nelements: '${nelements}',
                                            ntargets: '${ntargets}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}'
                                        }, 'get');">
                                Inizia esercizio
                            </button>
                        </div>
                    </div>
                </div>

        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-mem1.jsp" />

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
