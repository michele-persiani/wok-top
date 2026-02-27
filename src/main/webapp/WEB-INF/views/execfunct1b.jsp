<%-- 
    Document   : execfunct1b
    Created on : Mar 23, 2017, 1:01:20 PM
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
            
        </style>

    </head>

    <!--body onmouseover="loadAudio()"-->
    <body>
        <div class="container">
            <div class="navbar" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-21">
                            <h3 class="pull-left">Vedr&agrave; delle figure che scorrono sullo schermo, da destra a sinistra.</h3>
                            <h3 class="pull-left">
                            <c:if test="${cat1=='I_FRUIT'}">
                                Quando passer&agrave; la <b style="color:red">frutta</b> dovr&agrave; cliccare il pulsante verde,
                            </c:if>
                            <c:if test="${cat1=='I_VEGETABLE'}">
                                Quando passer&agrave; la <b style="color:red">verdura</b> dovr&agrave; cliccare il pulsante verde,
                            </c:if>
                            <c:if test="${cat1=='I_ANIMAL'}">
                                Quando passer&agrave; un <b style="color:red">animale</b> dovr&agrave; cliccare il pulsante verde,
                            </c:if>
                            <c:if test="${cat2=='I_FRUIT'}">
                                quando passer&agrave; la <b style="color:red">frutta</b> dovr&agrave; cliccare il pulsante rosso.
                            </c:if>
                            <c:if test="${cat2=='I_VEGETABLE'}">
                                quando passer&agrave; la <b style="color:red">verdura</b> dovr&agrave; cliccare il pulsante rosso.
                            </c:if>
                            <c:if test="${cat2=='I_ANIMAL'}">
                                quando passer&agrave; un <b style="color:red">animale</b> dovr&agrave; cliccare il pulsante rosso.
                            </c:if>
                            </h3>
                            <h3 class="pull-left">Attenzione per&ograve;! Se passer&agrave; un <b style="color:red">${inhElement}</b> non dovr&agrave; cliccare alcun bottone.</h3>
                            <c:if test="${answer=='alternate'}">
                                <h3 class="pull-left">Ad ogni risposta l'uso dei bottoni va invertito.</h3>
                            </c:if>
                            <c:if test="${answer=='random'}">
                                <h3 class="pull-left">Quando sente un suono l'uso dei bottoni va invertito.</h3>
                            </c:if>
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
                 data-pause="false"

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
                        <button type="button" id="greenButton" class="btn btn-lg btn-success btn-block pull-right" style="height:10%" onclick="greenPushed()">                            
                        </button>
                    </div>
                    <div class="col-sm-3">
                        <button type="button" id="redButton" class="btn btn-lg btn-danger btn-block pull-left" style="height:10%" onclick="redPushed()">
                        </button>
                    </div>
                    <div class="col-sm-9"></div>                    
                </div>                            
            </div>

                        
            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

            <script>
                
                var exElementListForJavascript = [];
                <c:forEach items="${exElementList}" var="listItem">
                    var arr = [];
                    arr.push("<c:out value="${listItem.id}" />");
                    arr.push("<c:out value="${listItem.category}" />");
                    arr.push("<c:out value="${listItem.eldescr}" />");
                    exElementListForJavascript.push(arr);
                </c:forEach>

                var audio01 = new Audio('resources/audio/audio-01.mp3');
                var audio02 = new Audio('resources/audio/audio-02.mp3');
                var audio03 = new Audio('resources/audio/audio-03.mp3');
                var audio05 = new Audio('resources/audio/audio-05.mp3');
                //var audio = new Audio('resources/audio/multiaudio-02.mp3');
                //var spriteData = {
                //    s1: {
                //        start: 0,
                //        length: 0.5
                //    },
                //    s2: {
                //        start: 1,
                //        length: 0.5
                //    },
                //    s3: {
                //        start: 2,
                //        length: 0.5
                //    },
                //    s4: {
                //        start: 3,
                //        length: 1.5
                //    }
                //};
                                
                //function loadAudio() {
                //    audio.load();
                //};
                                
                //function pauseAudio() {
                //    audio.pause();
                //};

                var id = setInterval(function () {
                    myTimer();
                }, 1000);

                var startTime = new Date().getTime();
                var totalTime = Math.round(${time} * ${nelements});
                var startTimer = totalTime;

                var elements = ${exElementList};

                var nCorrect = 0;
                var nWrong = 0;
                var nMissed = 0;
                
                var dTime = 0;
                var sTime = new Date().getTime();
                
                var greenButtonPushed = false, redButtonPushed = false;
                var currentIndex = -1;

                var alternate = false;

                function myTimer() {
                    startTimer--;
                    if (startTimer === 0) {
                        calculateResult();
                    }
                }

                function greenPushed() {
                    greenButtonPushed = true;

                    var rTime = new Date().getTime();
                    dTime = dTime+(rTime-sTime);
                    sTime = new Date().getTime();
        
                    document.getElementById('redButton').disabled = true;
                    document.getElementById('greenButton').disabled = true;
                    
                    var suffix = currentIndex+1
                    var imgId = 'img-i' + suffix;
                    
                    if (exElementListForJavascript[currentIndex+1][2] !== '${inhElement}') {
                        if(!alternate) {
                            if ('${cat1}' === exElementListForJavascript[currentIndex+1][1])  {
                                audio03.load();
                                audio03.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s3.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s3.length*1000);
                                
                                nCorrect++;
                                document.getElementById(imgId).style.backgroundColor = "green";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                            else {
                                audio01.load();
                                audio01.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s1.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s1.length*1000);
                                
                                nWrong++;
                                document.getElementById(imgId).style.backgroundColor = "red";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                        }
                        else {
                            if ('${cat1}' === exElementListForJavascript[currentIndex+1][1])  {
                                audio01.load();
                                audio01.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s1.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s1.length*1000);
                                
                                nWrong++;
                                document.getElementById(imgId).style.backgroundColor = "red";
                            }
                            else {
                                audio03.load();
                                audio03.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s3.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s3.length*1000);
                                
                                nCorrect++;
                                document.getElementById(imgId).style.backgroundColor = "green";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                        }
                    }
                    else {
                        audio01.load();
                        audio01.play();
                        //audio.load();
                        //audio.currentTime = spriteData.s1.start;
                        //audio.play();
                        //setTimeout(function() {
                        //    pauseAudio();
                        //},
                        //spriteData.s1.length*1000);
                        
                        nWrong++;
                        document.getElementById(imgId).style.backgroundColor = "red";
                        document.getElementById(imgId).style.filter= "grayscale(0%)";
                    }
                }

                function redPushed() {
                    redButtonPushed = true;
                    
                    var rTime = new Date().getTime();
                    dTime = dTime+(rTime-sTime);
                    sTime = new Date().getTime();
                    
                    document.getElementById('redButton').disabled = true;
                    document.getElementById('greenButton').disabled = true;
                    
                    var suffix = currentIndex+1;
                    var imgId = 'img-i' + suffix;
                    
                    if (exElementListForJavascript[currentIndex+1][2] !== '${inhElement}') {
                        if(!alternate) {
                            if ('${cat2}' === exElementListForJavascript[currentIndex+1][1]) {
                                audio03.load();
                                audio03.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s3.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s3.length*1000);
                                
                                nCorrect++;
                                document.getElementById(imgId).style.backgroundColor = "green";
                            }
                            else {
                                audio01.load();
                                audio01.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s1.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s1.length*1000);
                                
                                nWrong++;
                                document.getElementById(imgId).style.backgroundColor = "red";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                        }
                        else {
                            if ('${cat2}' === exElementListForJavascript[currentIndex+1][1]) {
                                audio01.load();
                                audio01.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s1.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s1.length*1000);
                                
                                nWrong++;
                                document.getElementById(imgId).style.backgroundColor = "red";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                            else {
                                audio03.load();
                                audio03.play();
                                //audio.load();
                                //audio.currentTime = spriteData.s3.start;
                                //audio.play();
                                //setTimeout(function() {
                                //    pauseAudio();
                                //},
                                //spriteData.s3.length*1000);
                                
                                nCorrect++;
                                document.getElementById(imgId).style.backgroundColor = "green";
                                document.getElementById(imgId).style.filter= "grayscale(0%)";
                            }
                        }
                    }
                    else {
                        audio01.load();
                        audio01.play();
                        //audio.load();
                        //audio.currentTime = spriteData.s1.start;
                        //audio.play();
                        //setTimeout(function() {
                        //    pauseAudio();
                        //},
                        //spriteData.s1.length*1000);
                        
                        nWrong++;
                        document.getElementById(imgId).style.backgroundColor = "red";
                        document.getElementById(imgId).style.filter= "grayscale(0%)";
                    }
                }

                function calculateResult() {
                    clearInterval(id);
                    
                    currentIndex = ${nelements}-1;
                    var imgId = 'img-i' + currentIndex;
                    
                    if (!(greenButtonPushed || redButtonPushed)) {
                        if (exElementListForJavascript[currentIndex][2] === '${inhElement}') {
                            audio03.load();
                            audio03.play();
                            //audio.load();
                            //audio.currentTime = spriteData.s3.start;
                            //audio.play();
                            //setTimeout(function() {
                            //    pauseAudio();
                            //},
                            //spriteData.s3.length*1000);
                            
                            nCorrect++;
                            document.getElementById(imgId).style.backgroundColor = "green";
                            document.getElementById(imgId).style.filter= "grayscale(0%)";
                        }
                        else {
                            audio02.load();
                            audio02.play();
                            //audio.load();
                            //audio.currentTime = spriteData.s2.start;
                            //audio.play();
                            //setTimeout(function() {
                            //    pauseAudio();
                            //},
                            //spriteData.s2.length*1000);
                            
                            nMissed++;
                            document.getElementById(imgId).style.backgroundColor = "yellow";
                            document.getElementById(imgId).style.filter= "grayscale(0%)";
                        }
                    }
                    
                    // il dTime medio lo moltiplichiamo per ${nelements}
                    // e lo trasformiamo in secondi
                    dTime = dTime/(nCorrect+nWrong)*${nelements}/1000;
                    
                    var perf;
                    var passed;

                    $.get("getperformance",
                    {
                        "exerciseid": ${exerciseid},
                        "patientid": ${patientid},
                        "ptime": dTime,
                        "pcorrect": nCorrect,
                        "pwrong": nWrong,
                        "pmissed": nMissed,
                        "maxtime": Math.round(${time}*${nelements}),
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
                                post('execfunct1phase3', {
                                    difficulty: '${difficulty}',
                                    level: '${level}',
                                    patientid: '${patientid}',
                                    exerciseid: '${exerciseid}',
                                    category: '${category}',
                                    lastexercisepassed: '${lastexercisepassed}',
                                    cat1: '${cat1}',
                                    cat2: '${cat2}',
                                    inhElement: '${inhElement}',
                                    answer: '${answer}',
                                    time: '${time}',
                                    color: '${color}',
                                    nelements: '${nelements}',
                                    sessid: '${sessid}',                                
                                    passed: passed,
                                    pTime: dTime,
                                    pCorrect: nCorrect,
                                    pMissed: nMissed,
                                    pWrong: nWrong
                                }, 'get');
                            }
                        });
                    });
                }
                                            
                $("#myCarousel").on('slide.bs.carousel', function () {
                    document.getElementById('greenButton').disabled = false;
                    document.getElementById('redButton').disabled = false;
                    currentIndex = $(this).find('.active').index();
                    var imgId = 'img-i' + currentIndex;
                    if (!(greenButtonPushed || redButtonPushed)) {
                        if (exElementListForJavascript[currentIndex][2] === '${inhElement}') {
                            audio03.load();
                            audio03.play();
                            //audio.load();
                            //audio.currentTime = spriteData.s3.start;
                            //audio.play();
                            //setTimeout(function() {
                            //    pauseAudio();
                            //},
                            //spriteData.s3.length*1000);
                            
                            nCorrect++;
                            document.getElementById(imgId).style.backgroundColor = "green";
                            document.getElementById(imgId).style.filter= "grayscale(0%)";
                        }
                        else {
                            audio02.load();
                            audio02.play();
                            //audio.load();
                            //audio.currentTime = spriteData.s2.start;
                            //audio.play();
                            //setTimeout(function() {
                            //    pauseAudio();
                            //},
                            //spriteData.s2.length*1000);
                            
                            nMissed++;
                            document.getElementById(imgId).style.backgroundColor = "yellow";
                            document.getElementById(imgId).style.filter= "grayscale(0%)";
                        }
                    }
                    
                    greenButtonPushed = false;
                    redButtonPushed = false;

                    <c:if test="${answer=='alternate'}">
                        alternate = !alternate;
                    </c:if>
                    <c:if test="${answer=='random'}">
                        if(Math.random() < 0.3) {
                            alternate = !alternate;
                            audio05.load();
                            audio05.play();
                            //audio.load();
                            //audio.currentTime = spriteData.s4.start;
                            //audio.play();
                            //setTimeout(function() {
                            //    pauseAudio();
                            //},
                            //spriteData.s4.length*1000);
                            
                        }
                    </c:if>
                    
                    sTime = new Date().getTime();
                    console.log("salvo nuovo timestamp per slide");
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