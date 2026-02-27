<%-- 
    Document   : execfunct1a
    Created on : Mar 23, 2017, 12:11:00 PM
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

        <title>Caccia agli oggetti</title>

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

            <div class="well well-sm">
                <div class="row">
                    <div class="col-sm-18">
                        <h3 class="pull-left">Osservi le figure che scorrono sullo schermo, da destra a sinistra.</h3>
                        <h3 class="pull-left">
                            <c:if test="${cat1=='I_FRUIT'}">
                                Quando passa la <b style="color:red">frutta</b> clicchi il pulsante verde,
                            </c:if>
                            <c:if test="${cat1=='I_VEGETABLE'}">
                                Quando passa la <b style="color:red">verdura</b> clicchi il pulsante verde,
                            </c:if>
                            <c:if test="${cat1=='I_ANIMAL'}">
                                Quando passa un <b style="color:red">animale</b> clicchi il pulsante verde,
                            </c:if>
                            <c:if test="${cat2=='I_FRUIT'}">
                                quando passa la <b style="color:red">frutta</b> clicchi il pulsante rosso.
                            </c:if>
                            <c:if test="${cat2=='I_VEGETABLE'}">
                                quando passa la <b style="color:red">verdura</b> clicchi il pulsante rosso.
                            </c:if>
                            <c:if test="${cat2=='I_ANIMAL'}">
                                quando passa un <b style="color:red">animale</b> clicchi il pulsante rosso.
                            </c:if>
                        </h3>
                    </div>
                    <c:if test="${difficulty=='training' || difficulty=='demo'}">
                        <div class="col-sm-2"></h3></div>
                        <div class="col-sm-4"><h3 style='color:red'>Esercizio di prova</h3></div>
                    </c:if>
                    <div class="col-sm-18">                    
                        <h3 class="pull-left">Attenzione per&ograve;! Se passa un <b style="color:red">${inhElement}</b> non clicchi alcun bottone.</h3>
                        <c:if test="${answer=='alternate'}">
                            <h3 class="pull-left">Ad ogni risposta l'uso dei bottoni va invertito.</h3>
                        </c:if>
                        <c:if test="${answer=='random'}">
                            <h3 class="pull-left">Quando sente un suono l'uso dei bottoni va invertito.</h3>
                        </c:if>
                    </div>
                    <div class="col-sm-6">
                        <button class="btn btn-lg btn-success pull-right"
                                onclick="
                                        post('execfunct1phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            answer: '${answer}',
                                            nelements: '${nelements}',
                                            time: '${time}',
                                            exElementList: '${exElementList}',
                                            color: '${color}',
                                            sessid: '${sessid}',
                                            cat1: '${cat1}',
                                            cat2: '${cat2}',
                                            inhElement: '${inhElement}'
                                        }, 'get');">
                            Inizia esercizio
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-exf1.jsp" />

        <script>
                                    history.pushState(null, null, document.URL);
                                    window.addEventListener('popstate', function () {
                                        history.pushState(null, null, document.URL);
                                    });
        </script>

        <script>
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>

    </body>
</html>
