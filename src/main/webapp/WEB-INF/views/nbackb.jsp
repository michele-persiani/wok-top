<%-- 
    Document   : nbackb
    Created on : Mar 13, 2017, 6:45:20 PM
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

        <title>L'ho visto prima?</title>

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
            input[type=checkbox] {
                display: none;
            }

            /* 
            - Style each label that is directly after the input
            - position: relative; will ensure that any position:
              absolute children will position themselves in relation to it
            */
            /*
            input[type=checkbox] + label {
                position: relative;
                //background: url(http://i.stack.imgur.com/ocgp1.jpg) no-repeat;
                height: 50px;
                width: 50px;
                display: block;
                border-radius: 50%;
                transition: box-shadow 0.4s, border 0.4s;
                border: solid 5px #FFF;
                box-shadow: 0 0 1px #FFF; //Soften the jagged edge 
                cursor: pointer;
            }
            */

            /* Provide a border when hovered and when the checkbox before it
            is checked */
            /*
            input[type=checkbox] + label:hover,
            input[type=checkbox]:checked + label {
                border: solid 5px #F00;
                box-shadow: 0 0 1px #F00;
                //Soften the jagged edge 
            }
            */

            /* 
            - Create a pseudo element :after when checked and provide a tick
            - Center the content
            */
            input[type=checkbox]:checked + label:after {
                /*content: '\2714';*/
                content: '';
                /*content is required, though it can be empty - content: '';*/
                height: 1em;
                position: absolute;
                top: 0;
                left: 0;
                bottom: 0;
                right: 0;
                margin: auto;
                color: greenyellow;
                line-height: 1;
                font-size: 11vw;
                text-align: center;
            }

            .inner-item {
                text-align: center;
                img {
                    margin: 0 auto;
                }
            }
            
            label img{
                pointer-events: none;
            }


            <c:if test="${color=='bw'}">
            img {
                -webkit-filter: grayscale(100%); /* Chrome, Safari, Opera */
                filter: grayscale(100%);
            }
            </c:if>

            <c:if test="${color=='omo'}">
            img {
                -webkit-filter: grayscale(50%); /* Chrome, Safari, Opera */
                filter: grayscale(50%);
            }
            </c:if>
            
        </style>
    </head>

    <!--body onmouseover="loadAudio()"-->
    <body>
        <div class="container">
            <div class="navbar" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <!--div class="col-sm-3"></div-->
                        <div class="col-sm-18">
                            <h3 class="pull-left">Osserva le figure che scorrono sullo schermo.</h3>
                            <h3 class="pull-left">Se la figura corrente &egrave; uguale a quella vista <span style="background-color:red">${nback}</span> figure prima, allora clicchi il tasto verde.</h3>
                        </div>
                        <div class="col-sm-3">
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

            <div id="myCarousel" class="carousel slide"
                 data-ride="carousel"
                 data-interval="${time*1000}"
                 data-wrap="false"
                 data-pause="false">

                 <!-- Wrapper for slides -->
                 <form id="form" role="form">

                    <div class="carousel-inner" role="listbox">

                        <c:set var="n" value="0"></c:set>
                        <c:forEach var="exelement" items="${exElementList}">
                            <c:if test="${n==0}">
                                <div class="item active">
                                </c:if>
                                <c:if test="${n>0}">
                                    <div class="item">
                                    </c:if>
                                    <div class="inner-item">
                                        <!--input type="checkbox" id="i${n}" value="${exelement.id}" onclick="disableCheck('i${n}')"/-->
                                        <!--label for="i${n}"-->                                                                                       
                                            <img class="img-responsive img-thumbnail center-block" id="img-i${n}" src="${exelement.url}" alt="Image">                                                                                               
                                        <!--/label-->
                                    </div>
                                </div>
                                <c:set var="n" value="${n+1}"></c:set>
                            </c:forEach>

                        </div>
                </form>
            </div>
            <div></div>
            <div class="well">
                <div class="row">
                    <div class="col-sm-9"></div>
                    <div class="col-sm-3">
                        <button type="button" id="sameButton" class="btn btn-lg btn-success btn-block pull-right" style="height:10%" disabled="true" onclick="samePushed()">
                        </button>
                    </div>
                   
                </div>
                <div class="col-sm-9"></div>
            </div>

                        
            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

            <script>
                var audio01 = new Audio('resources/audio/audio-01.mp3');
                var audio02 = new Audio('resources/audio/audio-02.mp3');
                var audio03 = new Audio('resources/audio/audio-03.mp3');

               

                var id = setInterval(function () {
                    myTimer();
                }, 1000);

                var totalTime = Math.round(${time} * ${nelements});
                var startTimer = totalTime;

                var nCorrect = 0;
                var nWrong = 0;
                var nMissed = 0;
                var elementiBack=0;
                var dTime = 0;
                var sTime = new Date().getTime();
                
                var elements = ${exElementList};                
                
                var sameButtonPushed = false;
                var currentIndex = -1;

                function myTimer() {
                    startTimer--;
                    if (startTimer === 0) {
                        calculateResult();
                    }
                }

                function samePushed() {
                    var rTime = new Date().getTime();
                    dTime = dTime+(rTime-sTime);
                    sTime = new Date().getTime();
                    
                    sameButtonPushed = true;
                    document.getElementById('sameButton').disabled = true;
                    
                    var suffix = currentIndex+1
                    var imgId = 'img-i' + suffix;
                    if (elements[currentIndex+1] == elements[currentIndex+1-${nback}]) {
                        audio03.load();
                        audio03.play(); 
                        nCorrect++;
                        elementiBack++;
                        //document.getElementById(imgId).style.backgroundColor = "green";
                        //document.getElementById(imgId).style.filter= "grayscale(0%)";
                        addBackground(imgId, 'green');
                    }
                    else {
                        audio01.load();
                        audio01.play();        
                        nWrong++;
                        //document.getElementById(imgId).style.backgroundColor = "red";
                        //document.getElementById(imgId).style.filter= "grayscale(0%)";
                        addBackground(imgId, 'red');
                    }
                }

              
                function calculateResult() {
                    clearInterval(id);

                    currentIndex = ${nelements}-1;
                    var imgId = 'img-i' + currentIndex;
                     
                    dTime = dTime/(nCorrect+nWrong)*${nelements}/1000;

                    let perf;
                    let passed;
                    let maxTime = Math.round(${time}*${nelements});
                    $.get("getperformance",
                    {
                        "exerciseid": ${exerciseid},
                        "patientid": ${patientid},
                        "ptime": dTime,
                        "pcorrect": nCorrect,
                        "pwrong": nWrong,
                        "pmissed": nMissed,
                        "maxtime": maxTime,
                        "sessid": ${sessid},
                        "difficulty": '${difficulty}',
                        "level": ${level}
                    },
                    function(data, status){
                        var js=JSON.parse(data);
                        perf = js.perf;
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
                                        '<br><b>Prestazione</b>: ' + Math.round(perf*100) + ' %' +                                    
                                        '<br>' +
                                        '<br>' + passedMessage + '</h4>',
                            callback:function() {
                                post('nbackphase3', {
                                    difficulty: '${difficulty}',
                                    level: '${level}',
                                    patientid: '${patientid}',
                                    exerciseid: '${exerciseid}',
                                    category: '${category}',
                                    lastexercisepassed: '${lastexercisepassed}',
                                    nback: '${nback}',
                                    time: '${time}',
                                    color: '${color}',
                                    nelements: '${nelements}',
                                    sessid: '${sessid}',                                
                                    passed: passed,
                                    pTime: dTime,
                                    pCorrect: nCorrect,
                                    pMissed: nMissed,
                                    pWrong: nWrong,
                                    type: '${type}',
                                    exname : '${exname}',
                                    rlagent: '${rlagent}'
                                }, 'get');
                            }
                        });
                    });   
                }
                                                            
                 $("#myCarousel").on('slide.bs.carousel', function () {
                    document.getElementById('sameButton').disabled = false;
    
                
                    currentIndex = $(this).find('.active').index();
                    var imgId = 'img-i' + currentIndex;
                    if (currentIndex >= ${nback}) {
                        if (!(sameButtonPushed) ) {
                            if((elements[currentIndex] == elements[currentIndex-${nback}])){
                             audio01.load();
                            audio01.play();     
                            nWrong++;
                            elementiBack++;
                            document.getElementById(imgId).style.backgroundColor = "red";
                            document.getElementById(imgId).style.filter= "grayscale(0%)";}
                        else {
                        //audio03.load();
                        //audio03.play(); 
                        //nCorrect++;
                        //document.getElementById(imgId).style.backgroundColor = "green";
                        document.getElementById(imgId).style.filter= "grayscale(0%)";
 
                        }
                    }
                        sameButtonPushed = false;
                    }
                    sTime = new Date().getTime();
                    console.log("salvo nuovo timestamp per slide"+elements[currentIndex]+imgId);
                });
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