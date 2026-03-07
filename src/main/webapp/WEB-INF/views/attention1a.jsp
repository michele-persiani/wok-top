<%-- 
    Document   : attention1a
    Created on : Feb 4, 2016, 12:22:20 PM
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

        <title>Attenzione Selettiva</title>

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

                   <c:if test="${color=='bw'}">
                <style>
                    img {
                        -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
                        filter: grayscale(100%);
                    }
                </style>        
            </c:if>
        <!--/c:if-->
        <c:if test="${color=='omo'}">
            <style>
                img {
                    -webkit-filter: grayscale(50%); /* Chrome, Safari, Opera */
                    filter: grayscale(50%);
                }
            </style>        
        </c:if>
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
                                <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            
            <!--div class="navbar" style="background-color:white"-->
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-4">
                            <h3 class="pull-right">Clicchi tutte le immagini di </h3>
                        </div>
                        <div id="targets">
                            <c:forEach var="target" items="${targetElementList}">
                                <div id="${target.id}" class="col-sm-3">
                                    <c:if test="${exname=='ATT_SEL_STD_CMP'}">
                                        <h3 style="color: red">${target.eldescr}</h3>
                                    </c:if>
                                    
                                    <c:if test="${exname!='ATT_SEL_STD_CMP'}">
                                        <c:if test="${exname!='ATT_SEL_STD_FAC'}">
                                        <h3 style="color: red">${target.eldescr}</h3>
                                        </c:if>
                                        <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="col-sm-8">
                            <h3 class="pull-left">tra gli elementi che appariranno,<br> entro il tempo massimo. In alto a sinistra visualizza il tempo a disposizione.</h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-22">
                            <h3 class="pull-left">Quando ha finito clicchi il bottone "Ho finito".</h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12"></div>
                        <c:if test="${difficulty=='training' || difficulty=='demo'}">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-6"><h3 style='color:red'>Esercizio di prova</h3></div>
                        </c:if>
                        <div class="col-sm-4">
                            <button class="btn btn-lg btn-success pull-right"
                                    onclick="                                        
                                        post('attention1phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nelements: '${nelements}',
                                            ntargets: '${ntargets}',
                                            alignment: '${alignment}',
                                            targetElementList: '${targetElementList}',
                                            exElementList: '${exElementList}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            ncols: '${ncols}',
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}',
                                            assignmentid : '${assignmentid}',
                                            rlagent: '${rlagent}'
                                        }, 'get');">
                                Inizia
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>


            <jsp:include page="modal-att1.jsp" />
            
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });


            function begin(){

            }
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>

    </body>
</html>
