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
            var isMobile = false; //initiate as false
                // device detection
                if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) 
                    || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4)))
                { 
                    isMobile = true;
                }
         
            if(isMobile)
            {
             if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Clicchi solo i volti che ha memorizzato prima.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Clicchi sulle frecce che prima ha memorizzato.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Clicchi solo le frecce che indicano le direzioni che ha memorizzato prima." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Clicchi solo le figure che ha memorizzato prima.";
          }
            else
            {
               if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Prema la barra spaziatrice quando compariranno i volti che ha memorizzato prima.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Prema la barra spaziatrice quando compariranno le frecce che ha memorizzato prima";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Prema la barra spaziatrice solo quando compariranno le frecce che indicano le direzioni che ha memorizzato prima." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Prema la barra spaziatrice quando compariranno le figure che ha memorizzato prima.";
           
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
                                            <img class="img-responsive img-thumbnail center-block" id="img-i${n}" src="${exelement.url}" alt="Image" onmousedown="checkanddisablecheck('i${n}')">                                                                                               
                                        </label>
                                    </div>
                                </div>
                                <c:set var="n" value="${n+1}"></c:set>
                            </c:forEach>
                        </div>
            </div>
                </form>
            </div>

        <div class="well" style="text-align: center">
            <button id="spacebtn" type="button" class="buttonsound btn-success center-block" value="Spazio" onclick="spacePressed()">Spazio</button>
        </div>
        </div>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
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
                    "level": ${level}
                },
                function(data, status) {
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
                                '<br>' + passedMessage +'</h4>',
                        callback:function() {
                            post('attention2phase3', {
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
                            },'get');
                                }
                    });
                });
            }


            function checkanddisablecheck(id) {
                var exElement = document.getElementById(id);
                if(!exElement.checked) {
                    exElement.checked = true;
                }
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
