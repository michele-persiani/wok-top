<%-- 
    Document   : exercise1
    Created on : Mar 11, 2016, 11:29:20 AM
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

        <c:if test="${color=='bw'}">
            <style>
                img {
                    -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
                    filter: grayscale(100%);
                }
            </style>        
        </c:if>
        <c:if test="${color=='bw'}">
            <style>
                img {
                    -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
                    filter: grayscale(100%);
                }
            </style>        
        </c:if>
        <c:if test="${color=='omo'}">
            <style>
                img {
                    -webkit-filter: grayscale(50%); /* Chrome, Safari, Opera */
                    filter: grayscale(50%);
                }
            </style>        
        </c:if>            
            

        <style>            
            .navbar + .well {
                padding-top: 50px;
            }
        </style>
    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-8">
                            <h4 class="pull-right">Memorizza le seguenti figure per</h4>
                        </div>
                        <div class="col-sm-2">
                            <h4 class="pull-right" id="timer"></h4>
                        </div>
                        <div class="col-sm-3">
                            <h4 class="pull-right">secondi</h4>
                        </div>
                        <div class="col-sm-5">
                            <c:if test="${difficulty=='training'}">
                                <a href="patienttraining">                                
                                    <span class="glyphicon glyphicon-chevron-left fa-4x pull-right" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </c:if>                            
                            <c:if test="${difficulty!='training'}">
                                <a href="patienthome">                                
                                    <span class="glyphicon glyphicon-home fa-4x pull-right" data-toggle="tooltip" data-placement="bottom" title="Home"></span>
                                </a>
                            </c:if>                            
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${distractor=='complex'}">
                <c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%></c:set>
                <div id="exElements" class="well well-sm" style="background: url('resources/images/distractors/complex/${rand1}.gif'); background-size: 100% 100%;">
            </c:if>
            <c:if test="${distractor=='simple'}">
                <c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%></c:set>
                <div id="exElements" class="well well-sm" style="background: url('resources/images/distractors/simple/${rand1}.gif'); background-size: 100% 100%;">
            </c:if>
            <c:if test="${distractor=='no'}">
                <div id="exElements" class="well well-sm">
            </c:if>                     
                    <div class="row">
                        <div id="targets">
                        <c:forEach var="target" items="${exElementList}">
                            <div id="${target.id}" class="col-sm-4">
                                <img class="img-responsive" src="${target.url}" alt="${target.url}">
                            </div>
                        </c:forEach>
                    </div>
                    </div>
                </form>
            </div>

            <!--footer class="footer">
                <p>&copy; 2016-2018 Universit&agrave; di Bologna</p>
            </footer-->

        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>

                            setInterval(function () {
                                myTimer()
                            }, 1000);

                            var startTime = new Date().getTime();
                            var totalTime =  Math.round(${time} * ${nelements});
                            var startTimer = totalTime;

                            function myTimer() {
                                startTimer--;
                                document.getElementById("timer").innerHTML = startTimer;
                                if (startTimer===0) {
                                    post('memory1phase2',
                                        {
                                            difficulty: '${difficulty}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nelements: '${nelements}',
                                            exElementIds: '${exElementList}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            shuffle: '${shuffle}'
                                        }, 'get');
                                }
                            }

                        </script>

        <jsp:include page="modal.jsp" />
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
    </body>
</html>
