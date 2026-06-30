<%-- 
    Document   : attention2b
    Created on : Apr 13, 2016, 4:45:20 PM
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

        <title>Memoria</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">
        
        <!-- disableselect -->
        <link rel="stylesheet" href="resources/css/disableselect.css">

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

        <!--c:if test="${exname!='ATT_SEL_FLW_CMP'}"-->
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
        <!--c:if test="${exname=='ATT_SEL_FLW_CMP'}"-->
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
            
            /*label img{
                pointer-events: none;
            }*/
        </style>

        
           <script type="text/javascript">
     window.onload=function checkDevice(){

            if(isMobile())
            {
             if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Tocchi i volti che ha memorizzato prima.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Tocchi le frecce che prima ha memorizzato.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Tocchi le frecce che indicano le direzioni che ha memorizzato prima." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Tocchi le figure che ha memorizzato prima.";
          }
            else
            {
               if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Prema spazio quando compariranno i volti che ha memorizzato prima.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Prema spazio quando compariranno le frecce che ha memorizzato prima";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Prema spazio solo quando compariranno le frecce che indicano le direzioni che ha memorizzato prima." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Prema spazio quando compariranno le figure che ha memorizzato prima.";
           
     }
    };
    </script>
        
        
        
    </head>

    <!--body onmouseover="loadAudio()"-->        
     <body onkeypress="fun(event)">   
        <div class="container">
            <div class="navbar" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <!--div class="col-sm-3">
                            <h2 class="pull-right" style="color:red" id="timer"></h2>
                        </div-->
                       
                        <div class="col-sm-8">
                            <c:if test="${type=='ATT_SEL_FLW_FAC'}">
                                <h3 class="pull-left"><div id="im2"></div></h3>
                            </c:if>
                            <c:if test="${type=='ATT_SEL_FLW_ORI'}">
                                <c:if test="${exname=='ATT_SEL_FLW_ARR'}">
                                    <h3 class="pull-left"><div id="im2"></div></h3>
                                </c:if>
                                <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                                    <h3 class="pull-left"><div id="im2"></div></h3>
                                </c:if>
                            </c:if>
                            <c:if test="${type=='ATT_SEL_FLW'}">
                                <h3 class="pull-left"><div id="im2"></div></h3>
                            </c:if>
                        
                             </div>
                      
                        <div id="targets" class="col-sm-3">
                            <c:forEach var="target" items="${targetElementList}">
                                <div id="${target.id}" class="col-sm-3 hidden">
                                    <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                </div>
                            </c:forEach>
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

            <div id="myCarousel" class="carousel slide disableselect"
                 data-ride="carousel"
                 data-interval="${time*1000}"
                 data-wrap="false"
                 data-pause="false"
               <!--tolti i distrattori  <c:if test="${distractor=='complex'}">
                     <c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%></c:set>
                     style="background: url('resources/images/distractors/complex/${rand1}.gif'); background-size: 100% 100%;"
                 </c:if>
                 <c:if test="${distractor=='simple'}">
                     <c:set var="rand1"><%= (int) (java.lang.Math.random() * 4)%></c:set>
                     style="background: url('resources/images/distractors/simple/${rand1}.gif'); background-size: 100% 100%;"
                 </c:if>-->
                 <c:if test="${distractor=='no'}">
                 </c:if>
				
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
                                        <input type="checkbox" id="i${n}" value="${exelement.id}"/>
                                        <label for="i${n}">
                                            <img style="width: 180px; height: 180px" class="img-responsive img-thumbnail center-block" id="img-i${n}" src="${exelement.url}" alt="Image" onmousedown="checkanddisablecheck('i${n}')">
                                        </label>
                                    </div>
                                </div>
                                <c:set var="n" value="${n+1}"></c:set>
                            </c:forEach>
                        </div>
            </div>
                </form>
            </div>

        <div id="buttondiv" class="well" style="text-align: center">
            <button id="spacebtn" type="button" class="buttonsound btn-success center-block" value="Spazio" onclick="spacePressed()" style="width:100px; height:60px; font-size:20px;">
                Spazio
            </button>
        </div>
        </div>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script type="application/javascript">

            if(isMobile()) {
                let buttondiv = document.getElementById('buttondiv');
                buttondiv.style.display = 'none';
            }


            <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                var arrExElId = [];
                var arrExElDescr = [];
                <c:forEach items="${exElementList}" var="listItem">
                    arrExElId.push("<c:out value="${listItem.id}" />");
                    arrExElDescr.push("<c:out value="${listItem.eldescr}" />");
                </c:forEach>
                        
                var arrTarElDescr = [];
                <c:forEach items="${targetElementList}" var="listItem">
                    arrTarElDescr.push("<c:out value="${listItem.eldescr}" />");
                </c:forEach>                    
            </c:if>

            <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                var targets = document.getElementById("targets").children;
                var targetIds = [];
                for (var i = 0, target; target = targets[i++]; ) {
                    targetIds.push(target.id);
                }
            </c:if>

            var audio01 = new Audio('resources/audio/audio-01.mp3');
            var audio02 = new Audio('resources/audio/audio-02.mp3');
            var audio03 = new Audio('resources/audio/audio-03.mp3');
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
            //    }
            //};

//            function loadAudio() {
//                if(audio02.readyState!==4) {
//                    audio02.load();
//                }
//            };

//            function pauseAudio(aud) {
//                aud.pause();
//            };

//            function setCurTime(t) {
//                myAudio.currentTime = t;
//            };
                
            var aaa = setInterval(function () {
                myTimer();
            }, 1000);

            var totalTime = Math.round(${time} * ${nelements});
            var startTimer = totalTime;

            var nCorrect = 0;
            var nWrong = 0;
            var nMissed = 0;
                
            var dTime = 0;
            var sTime = new Date().getTime();

            function myTimer() {
                startTimer--;
                //document.getElementById("timer").innerHTML = startTimer;
                if (startTimer === 0) {
                    calculateResult();
                }
            }

            function calculateResult() {
                clearInterval(aaa);
                var currentIndex = $('div.active').index();
                var id = 'i' + currentIndex;
                var exElement = document.getElementById(id);
                <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                    if (targetIds.indexOf(exElement.value) != -1) {
                </c:if>
                <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                    var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                    if (arrTarElDescr.indexOf(elDescr) !== -1) {     
                </c:if>
                if (!exElement.checked) {
                    nMissed++;
                    //setCurTime(spriteData.s2.start);
                    //myAudio.currentTime = spriteData.s2.start;
                    //myAudio.play();
                    audio02.load();
                    audio02.play();
                    //setTimeout(function(audio02) {
                    //    pauseAudio();
                    //}, spriteData.s2.length*1000);

                    addBackground('img-' + id, 'yellow');
                }}
                    
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
                    "maxtime": ${time}*${nelements},
                    "sessid": ${sessid},
                    "difficulty": '${difficulty}',
                    "level": ${level},
                    "assignmentid" : ${assignmentid}
                },
                function(data, status) {
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
                                '<br><b>Performance</b>: ' + Math.round(perf * 100) + '%' +
                                '<br><b>Soglia Superamento</b>: ' + Math.round(thr * 100) + '%' +
                                '<br>' +
                                '<br>' + passedMessage + '</h4>',
                        callback:function() {
                            post('attention2phase3',
                                {
                                difficulty: '${difficulty}',
                                level: '${level}',
                                patientid: '${patientid}',
                                exerciseid: '${exerciseid}',
                                category: '${category}',
                                lastexercisepassed: '${lastexercisepassed}',
                                passed: passed,
                                ntargets: '${ntargets}',
                                nelements: '${nelements}',
                                color: '${color}',
                                distractor: '${distractor}',
                                time: '${time}',
                                pTime: dTime,
                                pCorrect: nCorrect,
                                pMissed: nMissed,
                                pWrong: nWrong,
                                sessid: '${sessid}',
                                type: '${type}',
                                exname: '${exname}',
                                assignmentid : '${assignmentid}',
                                rlagent: '${rlagent}',
                            },'get');
                                }
                    });
                });
            }


            function checkanddisablecheck(id) {
                var exElement = document.getElementById(id);

                if(exElement.checked)
                    return;
                exElement.checked = true;

                var rTime = new Date().getTime();
                dTime = dTime+(rTime-sTime);
                sTime = new Date().getTime();
                exElement.disabled = true;
                <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                    if (targetIds.indexOf(exElement.value) != -1) {
                </c:if>
                <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                    var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                    if (arrTarElDescr.indexOf(elDescr) !== -1) {     
                </c:if>
                
                nCorrect++;
                //setCurTime(spriteData.s3.start);
                //myAudio.currentTime = spriteData.s3.start;
                //myAudio.play();
                audio03.load();
                audio03.play();
                
                //setTimeout(function(audio03) {
                //    pauseAudio();
                //}, spriteData.s3.length*1000);


                addBackground('img-' + id, 'green');
                }
                else {
                    nWrong++;
                    //setCurTime(spriteData.s1.start);
                    //myAudio.currentTime = spriteData.s1.start;
                    //myAudio.play();
                    audio01.load();
                    audio01.play();

                    //setTimeout(function(audio01) {
                    //    pauseAudio();
                    //}, spriteData.s1.length*1000);

                    addBackground('img-' + id, 'red');
                }
            }
                                            
            $("#myCarousel").on('slide.bs.carousel',
                function () {
                    currentIndex = $('div.active').index();
                    var id = 'i' + currentIndex;
                    var exElement = document.getElementById(id);
                    <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                        if (targetIds.indexOf(exElement.value) != -1) {
                    </c:if>
                    <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                        var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                        if (arrTarElDescr.indexOf(elDescr) !== -1) {     
                    </c:if>
                    if (!exElement.checked) {
                        nMissed++;
                        audio02.load();
                        //myAudio.load();
                        //audio02.oncanplay = function() {
                            //myAudio.currentTime=spriteData.s2.start;
                            audio02.play();

                            //myAudio.currentTime = spriteData.s2.start;
                            //myAudio.play();
                            //setTimeout(function(audio02) {
                            //    pauseAudio();
                            //}, spriteData.s2.length*1000);

                            document.getElementById('img-' + id).style.backgroundColor = "yellow";
                            document.getElementById('img-' + id).style.filter= "grayscale(0%)";
                        //};
                    }
                    }
                    sTime = new Date().getTime();
                    console.log("salvo nuovo timestamp per slide");
                });

                    function fun(event) {
                        if(event.keyCode === 32)
                            spacePressed();

                    }

                    function spacePressed()
                    {
                        console.log('Premo barra spaziatrice');

                        var currentIndex = $('div.active').index();
                        var id = 'i' + currentIndex;
                        checkanddisablecheck(id);
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
