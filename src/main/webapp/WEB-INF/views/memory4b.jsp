<%-- 
    Document   : memory4b
    Created on : Apr 12, 2017, 7:55:00 PM
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

        <title>Chi sar&agrave;?</title>

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
                padding-top: 120px;
        </style>

    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div class="well">
                    <div class="row">
                        <div class="col-sm-10">
                            <!-- 1 hour = unlimited -->
                            <c:if test="${time==60}">
                                <h3 class="pull-right">Memorizzi i nomi delle seguenti persone per il tempo che vuole, poi clicchi continua. </h3>
                            </c:if>
                            <c:if test="${time!=60}">
                                <h3 class="pull-right">Memorizzi i nomi delle seguenti persone per al massimo </h3>
                            </c:if>
                        </div>
                        <c:if test="${time!=60}">
                            <div class="col-sm-2">
                                <h2 class="pull-right" style="color:red" id="timer"></h2>
                            </div>
                            <div class="col-sm-3">
                                <h3 class="pull-right">secondi</h3>
                            </div>
                        </c:if>

                        <div class="col-sm-3">
                            <button class="btn btn-lg btn-success pull-right"
                                    onclick="
                                        post('memory4phase3', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nfaces: '${nfaces}',
                                            time: '${time}',
                                            name: '${name}',
                                            namediff: '${namediff}',
                                            sessid: '${sessid}',
                                            namelist: '${namelist}',
                                            facelist: '${facelist}'
                                        }, 'get');">
                                Continua
                            </button>
                        </div>
                        <div class="col-sm-5">
                            <c:if test="${difficulty=='training' && patientid=='-1'}">
                                <a href="patienttraining" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty=='training' && patientid!='-1'}">
                                <a href="patientrehabilitation" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty=='demo'}">
                                <a href="patientdemo" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                            <c:if test="${difficulty!='training' && difficulty!='demo'}">
                                <a href="patientrehabilitation" class="btn btn-lg btn-warning pull-right" role="button">Interrompi esercizio</a>
                            </c:if>                            
                        </div>
                    </div>                                        
                </div>
            </div>
            <div id="exElements" class="well well-sm">
                <div class="row">
                        <c:forEach var="target" items="${facelist}" varStatus="loop">
                            <div id="${target.id}" class="col-sm-3" form-group>
                                <img class="img-responsive" src="${target.url}" alt="${target.url}">
                                <h4 style="color:red">${namelist[loop.index]}</h4>
                            </div>
                        </c:forEach>
                </div>
                <!--div class="row">
                    <div>
                        <c:forEach var="target" items="${namelist}">
                            <div class="col-sm-4">
                                <h3 style="color:red">${target}</h3>
                            </div>
                        </c:forEach>
                    </div>
                </div-->
            </div>
        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
            <c:if test="${time!=60}">   // 1 hour = unlimited
                var aaa = setInterval(function () {
                    myTimer();
                }, 1000);

                var startTime = new Date().getTime();
                var totalTime =  Math.round(${time} * ${nfaces} * 60);                
                var startTimer = totalTime;
                
                function myTimer() {
                    startTimer--;
                    document.getElementById("timer").innerHTML = startTimer;
                    if (startTimer===0) {
                        clearInterval(aaa);
                        post('memory4phase3', {
                            difficulty: '${difficulty}',
                            level: '${level}',
                            patientid: '${patientid}',
                            exerciseid: '${exerciseid}',
                            category: '${category}',
                            lastexercisepassed: '${lastexercisepassed}',
                            nfaces: '${nfaces}',
                            time: '${time}',
                            name: '${name}',
                            namediff: '${namediff}',
                            sessid: '${sessid}',
                            facelist: '${facelist}',
                            namelist: '${namelist}'                            
                        }, 'get');
                    }
                }
            </c:if>
        </script>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>

    </body>
</html>
