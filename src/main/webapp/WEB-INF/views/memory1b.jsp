<%-- 
    Document   : memory1b
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

        <!--c:if test="${exname!='MEM_VIS_1_CMP'}"-->
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
        <!--c:if test="${exname=='MEM_VIS_1_CMP'}"-->
            <!--c:if test="${color=='color'}"-->
                <!--style>
                    img {
                        -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
                        filter: grayscale(100%);
                    }
                </style-->        
            <!--/c:if-->
        <!--/c:if-->
                
        <style>            
            .navbar + .well {
                padding-top: 80px;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${exname=='MEM_VIS_1_FAC'}">
                                    <h3 class="pull-right">Memorizza i seguenti volti per</h3>
                                </c:when>
                                <c:when test="${exname=='MEM_VIS_1_CMP'}">
                                    <h3 class="pull-right">Memorizza le seguenti parole per</h3>
                                </c:when>                                     
                                <c:otherwise>
                                    <h3 class="pull-right">Memorizza le seguenti figure per</h3>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-sm-2">
                            <h2 class="pull-right" style="color:red" id="timer"></h2>
                        </div>
                        <div class="col-sm-3">
                            <h3 class="pull-right">secondi</h3>
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
            
            
            
            <!--c:if test="${distractor=='complex'}" -->
                <!-- c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%>< /c:set -->
                <!-- div id="exElements" class="well well-sm" style="background: url('resources/images/distractors/complex/${rand1}.gif'); background-size: 100% 100%;" -->
            <!-- /c:if-->
            <!-- c:if test="${distractor=='simple'}"-->
                <!-- c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%>< /c:set -->
                <!-- div id="exElements" class="well well-sm" style="background: url('resources/images/distractors/simple/${rand1}.gif'); background-size: 100% 100%;"-->
            <!-- /c:if -->
           <c:if test="${distractor=='no'||distractor=='complex'||distractor=='simple'}">
          
                <div id="exElements" class="well well-sm">
            </c:if>                     
                    <div class="row">
                        <div id="targets">
                        <c:forEach var="target" items="${targetElementList}">
                            <div id="pippo" class="col-sm-3">
                                <c:if test="${exname=='MEM_VIS_1_CMP'}">
                                    <h3 style="color:red">${target.eldescr}</h3>
                                </c:if>
                                <c:if test="${exname!='MEM_VIS_1_CMP'}">
                                    <img class="img-responsive" src="${target.url}" alt="${target.url}">
                                </c:if>                                  
                            </div>
                        </c:forEach>
                        </div>
                    </div>
            </div>

            <!--footer class="footer">
                <p>&copy; 2016-2018 Universit&agrave; di Bologna</p>
            </footer-->

        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
            var aaa = setInterval(function () {
                myTimer();
            }, 1000);

            var startTime = new Date().getTime();
            var totalTime =  Math.round(${time} * ${ntargets});
            var startTimer = totalTime;

            function myTimer() {
                startTimer--;
                document.getElementById("timer").innerHTML = startTimer;
                if (startTimer===0) {
                    clearInterval(aaa);
                    post('memory1phase3',
                        {
                            difficulty: '${difficulty}',
                            level: '${level}',
                            patientid: '${patientid}',
                            exerciseid: '${exerciseid}',
                            category: '${category}',
                            lastexercisepassed: '${lastexercisepassed}',
                            nelements: '${nelements}',
                            ntargets: '${ntargets}',
                            exElementIds: '${exElementList}',
                            targetElementIds: '${targetElementList}',
                            color: '${color}',
                            distractor: '${distractor}',
                            time: '${time}',
                            sessid: '${sessid}',
                            type: '${type}',
                            exname: '${exname}'
                        }, 'get');
                }
            }
        </script>

        <jsp:include page="modal-mem1.jsp"/>

        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
    </body>
</html>
