<%-- 
    Document   : attention4a
    Created on : Feb 4, 2016, 06:51:20 PM
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

        <title>Attenzione Divisa</title>

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

        <!--c:if test="${exname!='ATT_DIV_CMP'}"-->
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
        <!--c:if test="${exname=='ATT_DIV_CMP'}"-->
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
            .buttonsound {
                position: relative;
                background-color: red;
                border: none;
                font-size: 28px;
                color: #FFFFFF;
                padding: 20px;
                width: 200px;
                text-align: center;
                -webkit-transition-duration: 0.4s; /* Safari */
                transition-duration: 0.4s;
                text-decoration: none;
                overflow: hidden;
                cursor: pointer;
                display: inline-block;
            }

            .buttonsound:after {
                content: "";
                background: #90EE90;
                display: block;
                position: absolute;
                padding-top: 300%;
                padding-left: 350%;
                margin-left: -20px!important;
                margin-top: -120%;
                opacity: 0;
                transition: all 0.8s
            }

            .buttonsound:active:after {
                padding: 0;
                margin: 0;
                opacity: 1;
                transition: 0s
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
                                <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
            
            <!--div class="navbar" style="background-color:white"-->
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-12">
                            <h3 id="im1">
                        </div>
                        <div id="targets">
                            <c:forEach var="target" items="${targetElementList}">
                                <div id="${target.id}" class="col-sm-3">
                                    <c:if test="${exname=='ATT_DIV_CMP'}">
                                        <h3 style="color: red">${target.eldescr}</h3>
                                    </c:if>
                                    <c:if test="${exname!='ATT_DIV_CMP'}">
                                        <img class="img-responsive pull-left" src="${target.url}" alt="${target.eldescr}">
                                    </c:if>                                  
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8">
                            <h3 class="pull-left" id ="suono"> </h3>
                        </div>
                        <div class="col-sm-16">
                            <div style="height: 20px;"></div>
                            <button id="trysound" class="btn btn-lg btn-danger pull-left" onclick="trySound()">
                                Prova suono
                            </button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-24 pull-left">
                            <h3 id="im2"></h3>
                        </div>
                    </div>
                    <div id="buttonsDiv" class="col-sm-24 pull-left">
                        <!--<h3>Può usare i bottoni 'Spazio' e 'Suono' o tasti spazio e invio su tastiera intercambiabilmente</h3>-->
                        <button id="spacebtn" type="button" class="buttonsound btn-success center-block" value="Figura">Figura</button>
                        <button id="sndbtn" type="button" class="buttonsound btn-danger center-block" value="Suono">Suono</button>
                    </div>
                   
                        
                    <div class="row">
                        <c:if test="${difficulty=='training' || difficulty=='demo'}">
                            <div class="col-sm-6"><h3 style='color:red'>Esercizio di prova</h3></div>
                            <div class="col-sm-18"></h3></div>
                        </c:if>
                    </div>
                    <div class="row">
                        <div class="col-sm-18"></div>                        
                        <div class="col-sm-6">
                            <button class="btn btn-lg btn-success pull-left"
                                    onclick="                                        
                                        post('attention4phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            ntargets: '${ntargets}',
                                            nelements: '${nelements}',
                                            targetElementIds: '${targetElementList}',
                                            exElementIds: '${exElementList}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            soundinterval: '${soundinterval}',
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}',
                                            rlagent: '${rlagent}',
                                            assignmentid: '${assignmentid}',
                                        }, 'get');">
                                Inizia esercizio
                            </button>
                        </div>                        
                    </div>
                </div>
            <!--/div-->

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>


            <jsp:include page="modal-att4.jsp" />
            
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>

        <script>
            var audio07 = new Audio('resources/audio/audio-07.mp3');
            
            function trySound() {
                document.getElementById("trysound").blur();
                audio07.load();
                audio07.play();
            }
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });



            if(isMobile())
            {
                document.getElementById("suono").innerHTML="Quando sentirà un suono, tocchi il tasto “Invio”.";
                if(${type=='ATT_DIV_FAC'}){
                    document.getElementById("im1").innerHTML ="Quando vedrà questi volti sullo schermo, tocchi il tasto “Spazio”.";
                    document.getElementById("im2").innerHTML = "Se volto e suono compariranno contemporaneamente, tocchi su “Spazio” e “Invio” il più velocemente possibile."
                }
                if(${type=='ATT_DIV_ORI'})
                {
                    document.getElementById("im1").innerHTML="Quando vedrà queste frecce sullo schermo, tocchi il tasto “Spazio”.";
                    document.getElementById("im2").innerHTML="Se freccia e suono compariranno contemporaneamente, tocchi su “Spazio” e “Invio” il più velocemente possibile."

                }
                if(${type=='ATT_DIV'}){
                    document.getElementById("im1").innerHTML="Quando vedrà queste figure sullo schermo, tocchi il tasto “Spazio”.";
                    document.getElementById("im2").innerHTML="Se figura e suono compariranno contemporaneamente, tocchi su “Spazio” e “Invio” il più velocemente possibile."
                }
            }
            else
            {
                document.getElementById("buttonsDiv").style.visibility = "hidden";


                document.getElementById("suono").innerHTML="Quando sentirà un suono, prema il tasto “Invio” sulla tastiera.";

                if(${type=='ATT_DIV_FAC'}){
                    document.getElementById("im1").innerHTML ="Quando vedrà questi volti sullo schermo, prema il tasto “Spazio” sulla tastiera." ;
                    document.getElementById("im2").innerHTML = "Se volto e suono compariranno contemporaneamente, prema “Spazio” e “Invio” il più velocemente possibile."

                }  if(${type=='ATT_DIV_ORI'})

            {  document.getElementById("im1").innerHTML="Quando vedrà queste frecce sullo schermo, prema il tasto “Spazio” sulla tastiera.";
                document.getElementById("im2").innerHTML="Se freccia e suono compariranno contemporaneamente, prema “Spazio” e “Invio” il più velocemente possibile."

            }
                if(${type=='ATT_DIV'}){
                    document.getElementById("im1").innerHTML="Quando vedrà queste figure sullo schermo, prema il tasto “Spazio” sulla tastiera.";
                    document.getElementById("im2").innerHTML = "Se figura e suono compariranno contemporaneamente, prema “Spazio” e “Invio” il più velocemente possibile."

                }


            }

        </script>

    </body>
</html>
