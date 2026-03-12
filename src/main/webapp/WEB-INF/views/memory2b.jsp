<%-- 
    Document   : memory2b
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

        <title>Memoria 2</title>

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
            input[type=checkbox] {
                display: none;
            }

           
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
                color: blueviolet;
                line-height: 1;
                font-size: 8vw;
                text-align: center;
            }
            
            .navbar + .well {
                padding-top: 50px;
            }
            
            label img{
                pointer-events: none;
            }
            
        </style>
    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div id="mem-cnt" class="well well-sm">
                    <div class="row">
                        <div class="col-sm-11">
                            <h3 class="pull-right">Memorizza la posizione delle seguenti parole per</h3>
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

            <c:if test="${distractor=='no'||distractor=='complex'||distractor=='simple'}">
                <div id="exElements" class="well well-sm">
            </c:if>
            <form id="form" role="form">
                    <table>
                         <tr>
                        <c:set var="n" value="0"></c:set>
                        <c:forEach var="element" items="${exElementList}">
                            <td>
                                <div class="checkbox">
                                    <input type="checkbox" disabled="true" id="i${n}" value="${element.id}" onclick="checkAnswer()"/>
                                    <label for="i${n}">
                                        <h3 id="e-i${n}" style="color:red">${element.eldescr}</h3>
                                    </label>                                    
                                </div>
                            </td>
                            <c:set var="n" value="${n+1}"></c:set>                                    
                        </c:forEach>
                        </tr>
                    </table>
                </form>
            </div>
                    
            <div id="target-img" class="hidden">
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-3">
                            <c:set var="l" value="0"></c:set>
                             <c:forEach var="t" items="${target}">
                            <img class="img-responsive hidden" id="${l}"  src="${t.url}" alt="${t.url}" value="${t.id}">
                             <c:set var="l" value="${l+1}"></c:set>                                    
                        </c:forEach>
                        </div>
                        <div class="col-sm-2">
                            <h3 class="pull-right">Dov'&egrave;?</h3>
                        </div>
                        <div class="col-sm-8">
                            <h3 class="pull-right">Clicchi sulla posizione esatta in</h3>
                        </div>
                        <div class="col-sm-1">
                            <h2 class="pull-right" style="color:red"><p id="timer1"></p></h2>
                        </div>
                        <div class="col-sm-3">
                            <h3> secondi </h3>
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
        
        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script> 
            var bbb;
            var startTime = new Date().getTime();
            var nTarget=${ntargets};
            var totalTime =  Math.round(${time} * ${nelements}*nTarget);//tempo totale dopo stimolo
             var timeTarget =  Math.round(${time} * ${nelements});//tempo per ogni target
            var startTimer =  timeTarget;//inizio del contatore per memorizzare
            var startTimerTotal=totalTime;//ininizio del contatore per esercizi dopo target
            
             
            var j=0;
            
            var aaa = setInterval(function () {
                myTimer();
            }, 1000);
            
           
            
            var elements = document.getElementById("form").elements;
            var sTime=0;
            var dTime = 0;
            var nCorrect = 0;
            var nWrong = 0;
            var nMissed = 0;
            
            function myTimer() {
                if (startTimer>0) {
                    startTimer--;
                }
                document.getElementById("timer").innerHTML = startTimer;
                 
                if (startTimer===0) {
                    clearInterval(aaa);//elimino il timer per la memorizzazione
                    $("#target-img").removeClass('hidden');
                    $("#"+j).removeClass('hidden');
                    $("#mem-cnt").addClass('hidden');
                   
                    for (var i = 0, element; element = elements[i]; i++) {
                        document.getElementById("e-i"+i).innerHTML="<h1>?</h1>";
                        //$("#"+element.id).disabled= false; 
                        document.getElementById("i"+i).disabled = false;                
                    }
                    if(sTime===0){
                        console.log("avvio stimolo")
                        sTime = new Date().getTime();//stimul
                    }
                    
                    startTimer = timeTarget;//avvio tempo per target
  
                    console.log("il mio j"+j);
                    
                    bbb = setInterval(function () {//tempo per target da resettare più volte 
                        myTimer1();
                    }, 1000);

                }
            }
            
            function myTimer1() {
            document.getElementById("timer1").innerHTML = startTimer; 
                if (startTimer>0) { //contatore tempo totale
                    startTimer--;
                }
                   if (startTimer===0) {//if di fine esercizio totale
                    
                     
                    nMissed = nMissed+1;
                    nCorrect = nCorrect+0;
                    nWrong = nWrong+0;
                    var rTime = new Date().getTime();
                    dTime = dTime+(rTime-sTime)/1000;
                    sTime = new Date().getTime();//stimul
                    console.log("if startTimer=0  "+dTime);
                    if(j+1===nTarget){
                    calculateResult();
                    clearInterval(bbb);}
                    else { 
                        j++;
                        startTimer=timeTarget;
                         $("#"+j).removeClass('hidden');
                         for(var k=j-1; k>=0;k--){
                         $("#"+k).addClass('hidden');}
                        console.log("dentro else j <> ntarget"+dTime+"rTime " + rTime +"j vale dentro "+j);
                        
                        console.log("il mio j"+j);
            
                     }
                }
                
               

            }
            
           
            
            
            function checkAnswer() {
              
                var passed;
                
                var rTime = new Date().getTime();
                dTime = dTime+(rTime-sTime)/1000;
                console.log("dentro checkAnswer "+dTime);
                for (var i = 0, element; element = elements[i++]; ) {
                    if (element.type === "checkbox") {
                        if (element.checked) {
                           
                             
                            if(element.value == ${target}[j]) {
                                nCorrect = nCorrect+1;
                                
                                 document.getElementById('i'+(i-1)).checked = false;
                            }
                            else {
                               
                                nWrong = nWrong+1;
                                 document.getElementById('i'+(i-1)).checked = false;
                            }
                        }
                    }
                }
               
                if(startTime===0||j===nTarget-1){
                calculateResult();
                clearInterval(bbb);
                }else{
                startTimer =timeTarget;
                sTime = new Date().getTime();//stimul
             
                    j++;
                    console.log("j vale dentro checkRispsota "+j);
                     $("#"+j).removeClass('hidden');
                         for(var k=j-1; k>=0;k--){
                         $("#"+k).addClass('hidden');}
                        console.log("il mio j"+j);
                    }
            }
            
            function calculateResult() {
                let maxTime = Math.round(${time}*${nelements}*nTarget);
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
                    "level": ${level},
                    "assignmentid": '${assignmentid}'
                },
                function(data, status){
                    var js=JSON.parse(data);
                    var perf = js.perf;
                    var passed = js.passed;
                    var thr = js.thr;
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
                        message:'<h4><b>Tempo</b>: ' + Math.floor(dTime) + ' secondi' +
                                        '<br>' +
                                        '<br><b>Risposte esatte</b>: ' + nCorrect +
                                        '<br><b>Risposte sbagliate</b>: ' + nWrong +
                                        '<br><b>Omissioni</b>: ' + nMissed +
                                        '<br>' +
                                        '<br><b>Performance</b>: ' + Math.floor(perf * 100) + '%' +
                                        '<br><b>Soglia Superamento</b>: ' + Math.floor(thr * 100) + '%' +
                                        '<br>' +
                                        '<br>' + passedMessage + '</h4>',
                        callback:function() {
                            post('memory2phase3', {
                                difficulty: '${difficulty}',
                                level: '${level}',
                                patientid: '${patientid}',
                                exerciseid: '${exerciseid}',
                                category: '${category}',
                                lastexercisepassed: '${lastexercisepassed}',
                                passed: passed,
                                nelements: '${nelements}',
                                ntargets: '${ntargets}',
                                color: '${color}',
                                distractor: '${distractor}',
                                time: '${time}',
                                pTime: dTime,
                                pCorrect: nCorrect,
                                pMissed: nMissed,
                                pWrong: nWrong,
                                sessid: '${sessid}',
                                exname: '${exname}',
                                rlagent: '${rlagent}',
                                assignmentid: ${assignmentid}
                            }, 'get');
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

    </body>
</html>