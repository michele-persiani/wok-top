<%-- 
    Document   : memory1c
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
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>        

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
                content: '\2714';
                /*content: '';
                content is required, though it can be empty - content: '';*/
                height: 1em;
                position: absolute;
                top: 0;
                left: 0;
                bottom: 0;
                right: 0;
                margin: auto;
                color: blueviolet;
                line-height: 1;
                font-size: 11vw;
                text-align: center;
            }

            .navbar + .well {
                padding-top: 150px;
            }
            
            label img{
                pointer-events: none;
            }
            
        </style>
    </head>

    <body>
        <div class="container">
            <div class="navbar navbar-fixed-top" style="background-color:white">
                <div class="well well-sm">
                    <div id="targets-row" class="row">
                        <div class="col-sm-2">
                            <h2 class="pull-right" style="color:red" id="timer"></h2>
                        </div>
                        <div class="col-sm-10">
                            <c:choose>
                                <c:when test="${exname=='MEM_VIS_1_FAC'}">
                                    <h3 class="pull-right">Clicchi tutti i volti che ha visto prima.</h3>
                                </c:when>
                                <c:when test="${exname=='MEM_VIS_1_CMP'}">
                                    <h3 class="pull-right">Clicchi tutte le figure corrispondenti alle parole che ha visto prima.</h3>
                                </c:when>                                     
                                <c:otherwise>
                                    <h3 class="pull-right">Clicchi tutte le figure che ha visto prima.</h3>
                                </c:otherwise>
                            </c:choose>                            
                        </div>
                        <div id="targets">
                            <c:forEach var="target" items="${targetElementList}">
                                <div id="${target.id}" class="col-sm-2 hidden">
                                    <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                </div>
                            </c:forEach>
                        </div>
                        <div class="col-sm-8">
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
                            <div class="col-sm-8">
                                <input type="button" class="btn btn-lg btn-success pull-right" value="Ho finito" onclick="calculateResult()">
                            </div>
                        </div>
                    </div>
                    <div id="legenda-row" class="row hidden">
                        <div class="col-sm-2"></div>
                        <c:forEach var="target" items="${targetElementList}">
                            <div id="${target.id}" class="col-sm-2">
                                <c:if test="${exname=='ATT_SEL_STD_CMP'}">
                                    <h3 style="color: red">${target.eldescr}</h3>
                                </c:if>
                                <c:if test="${exname!='ATT_SEL_STD_CMP'}">
                                    <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                </c:if>
                            </div>
                        </c:forEach>
                        <div class="col-sm-2"></div>
                        <div class="col-sm-18">
                            <h3>In <span style="background-color:green">verde</span> le risposte corrette, in <span style="background-color:red">rosso</span> le risposte sbagliate, in <span style="background-color:yellow">giallo</span> le risposte omesse</h3>
                        </div>
                    </div>
                    <div id="continue-row" class="row hidden">
                        <div class="col-sm-24">
                            <input type="button" class="btn btn-lg btn-success center-block" value="Continua"
                                   onclick="
                                       post('memory1phase4', {
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
                                            exname: '${exname}'
                                        }, 'get');
                            ">
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
                    <form id="form" role="form">
                        <div class="row">
                            <c:set var="n" value="0"></c:set>
                                <c:forEach var="exelement" items="${exElementList}">
                                    <div class="col-sm-4">
                                    <div class="checkbox">
                                        <input type="checkbox" id="i${n}" value="${exelement.id}" onclick="disableCheck('i${n}')"/>
                                        <label for="i${n}">
                                            <img class="img-responsive img-thumbnail" id="img-i${n}" src="${exelement.url}" alt="Image">                                                                                               
                                        </label>
                                    </div>
                                    </div>
                                    <c:set var="n" value="${n+1}"></c:set>
                                </c:forEach>
                            </div>
                        </div>
                    </form>
                </div>

            </div>

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

            <script>
                <c:if test="${exname=='MEM_VIS_1_CMP'}">
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
                
                var id = setInterval(function () {
                    myTimer();
                }, 1000);

                var startTime = new Date().getTime();
                var totalTime =  Math.round(${time} * ${nelements});
                var startTimer = totalTime;

                var passed;
                var dTime;
                var nCorrect, nMissed, nWrong;

                function myTimer() {
                    startTimer--;
                    document.getElementById("timer").innerHTML = startTimer;
                    if(startTimer===0) {
                        calculateResult();
                    }
                }
                
                function calculateResult() {
                    clearInterval(id);
                    //Exercise time
                    
                    dTime = (new Date().getTime() - startTime) / 1000 ; //padova Marzo
                    
                   // dTime = (new Date().getTime() - startTime) / 1000 - (0.35 * ${nelements});
                    //if(dTime<0) dTime=0;
                    <c:if test="${exname!='MEM_VIS_1_CMP'}">
                        var targets = document.getElementById("targets").children;
                        var targetIds = [];
                        for (var i = 0, target; target = targets[i++]; ) {
                            targetIds.push(target.id);
                        }
                    </c:if>

                    var exElements = document.getElementById("form").elements;
                    nCorrect = 0;
                    nWrong = 0;
                    nMissed = 0;


                    let icheckbox = document.getElementById('i'+(i-1));
                    for (var i = 0, exElement; exElement = exElements[i++]; )
                    {
                        if (exElement.type === "checkbox")
                        {
                            <c:if test="${exname=='MEM_VIS_1_CMP'}">
                                var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                                if (arrTarElDescr.indexOf(elDescr) !== -1) {     
                            </c:if>
                            <c:if test="${exname!='MEM_VIS_1_CMP'}">
                                if (targetIds.indexOf(exElement.value) != -1) {
                            </c:if>
                                if (exElement.checked)
                                {
                                    nCorrect++;
                                    addBackground('img-i'+(i-1), "green");
                                    icheckbox.checked = false;
                                }
                                else
                                {
                                    nMissed++;
                                    addBackground('img-i'+(i-1), "yellow");
                                    icheckbox.checked = false;
                                }
                            }
                            else if (exElement.checked)
                                {
                                    nWrong++;
                                    addBackground('img-i'+(i-1), "red");
                                    icheckbox.checked = false;
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
                        "maxtime": Math.round(${time}*${nelements}),
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
                            message:'<h4><b>Tempo</b>: ' + Math.floor(dTime) + ' secondi' +
                                '<br>' +
                                '<b>Risposte esatte</b>: ' + nCorrect +
                                '<br><b>Risposte sbagliate</b>: ' + nWrong +
                                '<br><b>Omissioni</b>: ' + nMissed +
                                '<br>' +
                                '<br><b>Prestazione</b>: ' + Math.round(perf*100) + ' %' +
                                '<br><b>Soglia Superamento</b>: ' + Math.round(thr*100) + ' %' +
                                '<br>' +
                                '<br>' + passedMessage + '</h4>',
                            callback:function() {
                                $("#continue-row").removeClass('hidden');
                                $("#legenda-row").removeClass('hidden');
                                $("#targets-row").addClass('hidden');
                            }
                        });
                    });
                }
                                            
                function disableCheck(id) {
                    document.getElementById(id).disabled = true;
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
