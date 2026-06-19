<%-- 
    Document   : attention2a
    Created on : Feb 4, 2016, 5:50:00 PM
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
                    <div class="col-sm-6">
                        <c:if test="${type=='ATT_SEL_FLW_FAC'}">
                            <h3 class="pull-left">Memorizzi attentamente questi volti</h3>
                        </c:if>
                        <c:if test="${type=='ATT_SEL_FLW_ORI'}">
                            <c:if test="${exname=='ATT_SEL_FLW_ARR' || exname == 'ATT_SEL_FLW_ARR_RL'}">
                                <h3 class="pull-left">Memorizzi attentamente queste frecce</h3>
                            </c:if>
                            <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                                <h3 class="pull-left">Memorizzi attentamente queste direzioni</h3>
                            </c:if>
                        </c:if>
                        <c:if test="${type=='ATT_SEL_FLW'}">
                            <h3 class="pull-left"> Memorizzi attentamente queste figure</h3>
                        </c:if>
                    </div>
                    <div id="targets">
                        <c:forEach var="target" items="${targetElementList}">
                            <div id="${target.id}" class="col-sm-3">
                                <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                                    <h3 style="color: red">${target.eldescr}</h3>
                                </c:if>
                                <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                                    <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                    </div>
                    <!--div class="col-sm-5">
                        <h3 class="pull-left"> entro il tempo massimo </h3>
                    </div-->
                <div class="row">
                        <h3 id="im2"></h3>
                </div>


                
                <div class="row">
                    <c:if test="${difficulty=='training' || difficulty=='demo'}">
                        <div class="col-sm-6"><h3 style='color:red'>Esercizio di prova</h3></div>
                        <div class="col-sm-18"></div>
                    </c:if>
                </div>
                <div class="row">
                    <div class="col-sm-20"></div>
                    <div class="col-sm-4">
                        <button class="btn btn-lg btn-success pull-right"
                                onclick="                                        
                                        post('attention2phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nelements: '${nelements}',
                                            ntargets: '${ntargets}',
                                            targetElementList: '${targetElementList}',
                                            exElementList: '${exElementList}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}',
                                            assignmentid: '${assignmentid}',
                                            rlagent: '${rlagent}',
                                        },'get');">
                            Inizia esercizio
                        </button>
                    </div>
                </div>
            </div>
        </div>

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

            <jsp:include page="modal-att2.jsp" />
            
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
        
        <script type="text/javascript">
    window.onload=function checkDevice(){
        

            if(isMobile())
            {
              
              if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Nella prossima pagina vedrà scorrere delle immagini. Tocchi lo schermo in corrispondenza delle figure quando compariranno quelle memorizzate.";
             if(${type=='ATT_SEL_FLW_ORI'} )
                document.getElementById("im2").innerHTML = "Nella prossima pagina vedrà scorrere delle immagini. Tocchi lo schermo in corrispondenza delle figure quando compariranno quelle memorizzate.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Nella prossima pagina vedrà scorrere delle immagini. Tocchi lo schermo in corrispondenza delle figure quando compariranno quelle memorizzate." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Nella prossima pagina vedrà scorrere delle immagini. Tocchi lo schermo in corrispondenza delle figure quando compariranno quelle memorizzate.";
              
              }
            else
            {
                 if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Nella prossima pagina vedrà scorrere delle immagini. Prema il tasto spazio sulla tastiera quando compariranno le figure memorizzate.";
             if(${type=='ATT_SEL_FLW_ORI'} )
                document.getElementById("im2").innerHTML = "Nella prossima pagina vedrà scorrere delle immagini. Prema il tasto spazio sulla tastiera quando compariranno le frecce memorizzate.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Nella prossima pagina vedrà scorrere delle immagini. Prema il tasto spazio sulla tastiera quando compariranno le direzioni memorizzate." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Nella prossima pagina vedrà scorrere delle immagini. Prema il tasto spazio sulla tastiera quando compariranno le figure memorizzate.";

                
            }
    }
    </script>
        

    </body>
</html>
