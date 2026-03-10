<%-- 
    Document   : memory4c
    Created on : Apr 12, 2017, 11:38:00 PM
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
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>        
        
        <style>            
            .navbar + .well {
                padding-top: 150px;
        </style>

    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div class="well">
                    <div id="targets-row" class="row">
                        <div class="col-sm-12">
                            <h3 class="pull-right">Scriva adesso il nome giusto sotto ciascuna persona.</h3>
                        </div>
                        <div class="col-sm-6">
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
                        <div class="col-sm-6">
                            <input type="button" class="btn btn-lg btn-success pull-left" value="Ho finito" onclick="calculateResult()">
                        </div>
                    </div>
                    <div id="legenda-row" class="row hidden">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-22 pull-right">
                            <h3>In <span style="background-color:green">verde</span> le risposte corrette, in <span style="background-color:red">rosso</span> le risposte sbagliate, in <span style="background-color:yellow">giallo</span> le risposte omesse</h3>
                        </div>
                    </div>
                    <div id="continue-row" class="row hidden">
                        <div class="col-sm-24">
                            <input type="button" class="btn btn-lg btn-success center-block" value="Continua"
                                   onclick="
                                        post('memory4phase4', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            passed: passed,
                                            nfaces: '${nfaces}',
                                            time: '${time}',
                                            namediff: '${namediff}',
                                            name: '${name}',
                                            pTime: dTime,
                                            pCorrect: nCorrect,
                                            pMissed: nMissed,
                                            pWrong: nWrong,
                                            sessid: '${sessid}'
                                        }, 'get');
                            ">
                        </div> 
                    </div>                    
                </div>
            </div>
            <div id="exElements" class="well">
                <form id="form" role="form">
                    <div class="row">
                        <c:set var="n" value="0"></c:set>
                        <c:forEach var="target" items="${facelist}">
                            <div id="${target.id}" class="col-sm-4">                                
                                <img class="img-responsive img-thumbnail" id="img-i${n}" src="${target.url}" alt="${target.url}">
                                    <div>
                                        <input type="text" class="form-control " placeholder="Nome" />                                    
                                    </div>
                            </div>
                            <c:set var="n" value="${n+1}"></c:set>
                        </c:forEach>
                    </div>
                </form>
                <div id="names-row" class="row hidden">
                    <c:forEach var="target" items="${namelist}">
                        <div class="col-sm-4">
                            <h4>${target}</h4>
                        </div>
                    </c:forEach>
                </div>                        
            </div>
        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
            var startTime = new Date().getTime();
            
            var passed;
            var dTime;
            var nCorrect = 0;
            var nWrong = 0;
            var nMissed = 0;
            
            var nameArr=[];
            <c:forEach var="name" items="${namelist}">
               nameArr.push("<c:out value="${name}" />");
            </c:forEach>            
            
            function calculateResult() {
                
                //Exercise time
                dTime = (new Date().getTime() - startTime) / 1000;
               

                var exElements = document.getElementById("form").elements;
                
                for (var i = 0, exElement; exElement = exElements[i++]; ) {
                    if (exElement.type === "text") {
                        if(exElement.value === "") {
                            nMissed++;
                            document.getElementById('img-i' + (i - 1)).style.backgroundColor = "yellow";
                            document.getElementById('img-i' + (i - 1)).style.filter= "grayscale(0%)";
                        }
                        else {
                            var l = new Levenshtein(exElement.value.toLowerCase(), nameArr[i - 1].toLowerCase());
                              if(l.distance <= (nameArr[i - 1].toLowerCase().toString().length /100*15)) {//modificata la distanza con una percentuale del 15% sulla
                                nCorrect++;      //lunghezza della stringa
                                document.getElementById('img-i' + (i - 1)).style.backgroundColor = "green";
                                document.getElementById('img-i' + (i - 1)).style.filter= "grayscale(0%)";
                             }
                            else {
                                nWrong++;
                                document.getElementById('img-i' + (i - 1)).style.backgroundColor = "red";
                                document.getElementById('img-i' + (i - 1)).style.filter= "grayscale(0%)";
                            }
                        }
                    }
                }

                var perf;
                
                $.get("getperformance",
                {
                    "exerciseid": ${exerciseid},
                    "patientid": ${patientid},
                    "ptime": dTime,
                    "pcorrect": nCorrect,
                    "pwrong": nWrong,
                    "pmissed": nMissed,
                    "maxtime": Math.round(${time}*${nfaces}*60),
                    "sessid": ${sessid},
                    "difficulty": '${difficulty}',
                    "level": ${level}
                },
                function(data, status){
                    var js=JSON.parse(data);
                    perf = js.perf;
                    var thr = js.thr;
                    passed = js.passed;
                            var passedMessage='';
                            <c:if test="${difficulty!='training'}">
                                if(passed) {
                                    passedMessage = '<b>Ottimo! Esercizio superato!</b>';
                                }
                                else {
                                    passedMessage = '<b>Mi spiace, esercizio non superato</b>';
                                }
                            </c:if>                            
                                        
                    bootbox.alert({
                        size:'small',
                        message:'<h4><b>Risposte esatte</b>: ' + nCorrect +
                                '<br><b>Risposte sbagliate</b>: ' + nWrong +
                                '<br><b>Omissioni</b>: ' + nMissed +
                                '<br>' +
                                '<br><b>Prestazione</b>: ' + Math.floor(perf*100) + ' %' +
                                '<br><b>Soglia superamento:</b>: ' + Math.floor(thr*100) + ' %' +
                                '<br>' +
                                '<br>' + passedMessage + '</h4>',
                        callback:function() {
                            $("#names-row").removeClass('hidden');
                            $("#continue-row").removeClass('hidden');
                            $("#legenda-row").removeClass('hidden');
                            $("#targets-row").addClass('hidden');
                        }
                    });
                });
            }
        </script>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script src="resources/js/levenshtein.js"></script>        

    </body>
</html>
