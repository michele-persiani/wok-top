<%-- 
    Document   : attention1b
    Created on : Feb 3, 2017, 06:11:20 PM
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

        <title>Attenzione selettiva</title>

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

        <!--c:if test="${exname!='ATT_SEL_STD_CMP'}"-->
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
        <!--c:if test="${exname=='ATT_SEL_STD_CMP'}"-->
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
            
        

          
           
            .navbar + .well {
                padding-top: 130px;
            }
                        
            /*label img{
                pointer-events: none;
            }*/            
        </style>
    </head>

    <body>
        <div class="container-fluid">
            <div class="navbar navbar-fixed-top" style="background-color:white">                
                <div class="well well-sm">
                    <div id="targets-row" class="row">
                        <div class="col-sm-2">
                            <h2 class="pull-right" style="color:red" id="timer"></h2>
                        </div>
                        <div class="col-sm-4">
                            <h3 class="pull-right">Clicchi tutti</h3>
                        </div>
                        <div id="targets">
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
                        <div class="col-sm-5">
                            <input type="button" class="btn btn-lg btn-success pull-left" value="Ho finito" onclick="calculateResult()">
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
                    </div>
                    <div id="continue-row" class="row hidden">
                        <div class="col-sm-20">
                            <h3>In <span style="background-color:green">verde</span> le risposte corrette, in <span style="background-color:red">rosso</span> le risposte sbagliate, in <span style="background-color:yellow">giallo</span> le risposte omesse</h3>
                        </div>                        
                        <div class="col-sm-4">
                            <input type="button" class="btn btn-lg btn-success center-block" value="Continua"
                                   onclick="
                                       post('attention1phase3', {
                                           difficulty: '${difficulty}',
                                           level: '${level}',
                                           patientid: '${patientid}',
                                           exerciseid: '${exerciseid}',
                                           category: '${category}',
                                           lastexercisepassed: '${lastexercisepassed}',
                                           passed: passed,
                                           ntargets: '${ntargets}',
                                           nelements: '${nelements}',
                                           alignment: '${alignment}',
                                           color: '${color}',
                                           distractor: '${distractor}',
                                           time: '${time}',
                                           ncols: '${ncols}',
                                           pTime: dTime,
                                           pCorrect: nCorrect,
                                           pMissed: nMissed,
                                           pWrong: nWrong,
                                           sessid: '${sessid}',
                                           type: '${type}',
                                           exname: '${exname}',
                                           rlagent: '${rlagent}',
                                           assignmentid: '${assignmentid}'
                                        }, 'get');
                                    ">
                        </div> 
                    </div>
                </div>
            </div>

            <div id="exElements" class="well well-sm disableselect">

                <form id="form" role="form">
                    <table>
                        <c:if test="${exname!='ATT_SEL_STD_FAC'}">
                            <c:set var="nrighe" value="15"></c:set>
                        </c:if>
                        <c:if test="${exname=='ATT_SEL_STD_FAC'}">
                            <c:set var="nrighe" value="10"></c:set>
                        </c:if>
                        <c:set var="n" value="0"></c:set>
                        <c:forEach var="exelement" items="${exElementList}">
                            <c:if test="${n % nrighe ==0}"  > <tr> </c:if>


                            <td >

                                <input type="checkbox" id="i${n}" value="${exelement.id}">
                                <label for="i${n}">

                                    <img class="img-responsive img-thumbnail" id="img-i${n}" src="${exelement.url}" alt="Image" onmousedown="checkanddisablecheck(i${n},${n})">

                                </label>

                            </td>

                            <c:if test="${n%nrighe==nrighe-1}" > </tr> </c:if>
                            <c:set var="n" value="${n+1}"></c:set>
                        </c:forEach>

                        <c:if test="${n%nrighe!=nrighe-1}" > </tr> </c:if>
                    </table>
                </form>
            </div>
        </div>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
                <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
            <c:if test="${exname=='ATT_SEL_STD_CMP'}">
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
            var totalTime = Math.round(${time} * ${nelements});
            var startTimer = totalTime;

            var passed;
            var dTime;
            var nCorrect, nMissed, nWrong;

            function myTimer() {
                startTimer--;
                document.getElementById("timer").innerHTML = startTimer;
                if (startTimer === 0) {
                    calculateResult();
                }
            }


            function calculateResult()
            {
                clearInterval(id);
                //Exercise time
                dTime = (new Date().getTime() - startTime) / 1000;

                <c:if test="${exname!='ATT_SEL_STD_CMP'}">
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


                for (var i = 0, exElement; exElement = exElements[i++]; )
                {
                    if (exElement.type !== "checkbox")
                        continue;

                    <c:choose>
                    <c:when test="${exname=='ATT_SEL_STD_CMP'}">
                        var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                        if (arrTarElDescr.indexOf(elDescr) !== -1)
                        {
                    </c:when>
                    <c:otherwise>
                        if (targetIds.indexOf(exElement.value) != -1)
                        {
                    </c:otherwise>
                    </c:choose>

                        if (exElement.checked)
                        {
                            nCorrect++;
                            document.getElementById('i'+(i-1)).checked = false;
                            addBackground('img-i'+(i-1), "green");
                        }
                        else
                        {
                            nMissed++;
                            addBackground('img-i'+(i-1), "yellow");
                        }
                    }
                    else if (exElement.checked)
                    {
                        nWrong++;
                        document.getElementById('i'+(i-1)).checked = false;
                        addBackground('img-i'+(i-1), "red");
                    }
                }




                let maxtime = ${time}*${nelements};
                $.get("getperformance",
                    {
                        "assignmentid" : ${assignmentid},
                        "exerciseid": ${exerciseid},
                        "patientid": ${patientid},
                        "ptime": dTime,
                        "pcorrect": nCorrect,
                        "pwrong": nWrong,
                        "pmissed": nMissed,
                        "maxtime": maxtime,
                        "sessid": ${sessid},
                        "difficulty": '${difficulty}',
                        "level": ${level}
                    },
                    function(data, status) {
                        var js=JSON.parse(data);
                        let perf = Math.round(js.perf * 100);
                        passed = js.passed;
                        let thr = Math.round(js.thr * 100);
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
                                $("#continue-row").removeClass('hidden');
                                $("#legenda-row").removeClass('hidden');
                                $("#targets-row").addClass('hidden');
                            }
                        });
                    });
            }


            function checkanddisablecheck(id,i)
            {
                if(id.checked)
                    return
                id.checked = true;
                id.disabled = true;
                //document.getElementById('img-i'+(i)).style.filter="blur(5px)";
                document.getElementById('img-i' + (i)).style.backgroundColor = "orange";
                //addChildImage(document.getElementById('img-i'+(i)), "orange");

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
