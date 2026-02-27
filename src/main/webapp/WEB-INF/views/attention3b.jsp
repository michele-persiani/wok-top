p<%-- 
    Document   : attention3b
    Created on : Apr 18, 2016, 12:50:20 PM
    Author     : s
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

        <title>Attenzione Alternata</title>

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

        <!--c:if test="${exname!='ATT_ALT_CMP'}"-->
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
            
       
        </style>

    </head>

    <!--body onmouseover="loadAudio()"-->
    <body onkeypress="fun(event)">
        <div class="container">
            <div class="navbar" style="background-color:white">
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-5">
                            <h3 class="pull-left" id="im1"></h3>
                         
                        </div>
                        <div id="${targetelement1}" class="col-sm-3">
                            <c:if test="${exname=='ATT_ALT_CMP'}">
                                <h3 style="color: red">${targetelement1img}</h3>
                            </c:if>
                            <c:if test="${exname!='ATT_ALT_CMP'}">
                                <img class="img-responsive pull-left" src="${targetelement1img}" alt="${targetelement1img}">
                            </c:if>                                
                        </div>
                        <div class="col-sm-5">
                            <h3 class="pull-left" id="im2"></h3>
                                          
                        </div>
                        <div id="${targetelement2}" class="col-sm-3">
                            <c:if test="${exname=='ATT_ALT_CMP'}">
                                <h3 style="color: red">${targetelement2img}</h3>
                            </c:if>
                            <c:if test="${exname!='ATT_ALT_CMP'}">
                                <img class="img-responsive pull-left" src="${targetelement2img}" alt="${targetelement2img}">
                            </c:if>                                
                        </div>
                        <div class="col-sm-8"> <h3 class="pull-left" id="im3"></h3>
                                           
                        </div>
                    </div>
            
                    <p></p>
                    <div class="row">
                        <div class="col-sm-20"></div>
                        <div class="col-sm-4">
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
                                        <input type="checkbox" id="i${n}" value="${exelement.id}"/>
                                        <label for="i${n}">                                                                                       
                                            <img class="img-responsive img-thumbnail center-block" id="img-i${n}" src="${exelement.url}" alt="Image" onmousedown="checkanddisablecheck('i${n}')">                                                                                               
                                        </label>
                                    </div>
                                </div>
                            <c:set var="n" value="${n+1}"></c:set>
                        </c:forEach>
                        </div>
                </form>
            </div>

            <div class="well" style="text-align: center">
                <button id="spacebtn" type="button" class="buttonsound btn-success center-block" value="Spazio" onclick="spacePressed()">Spazio</button>
            </div>

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
            
            <script>
                <c:if test="${exname=='ATT_ALT_CMP'}">
                    var arrExElId = [];
                    var arrExElDescr = [];
                    <c:forEach items="${exElementList}" var="listItem">
                        arrExElId.push("<c:out value="${listItem.id}" />");
                        arrExElDescr.push("<c:out value="${listItem.eldescr}" />");
                    </c:forEach>
                        
                    var arrTarElDescr = [];
                    arrTarElDescr.push("<c:out value="${targetelement1img}" />");
                    arrTarElDescr.push("<c:out value="${targetelement2img}" />");
                </c:if>

                var audio07 = new Audio('resources/audio/audio-07.mp3');
                audio07.load();
               
    
                var nCorrect = 0;
                var nWrong = 0;
                var nMissed = 0;
                var nIteration=${iterations};
                var dTime = 0;
                var sTime = new Date().getTime();
               // var targetattivo=${targetelement1img};
                var targetId=${target};
                           console.log("TARGET INIZIALE"+${target});
                
                var targetDesc='${target.eldescr}';
                console.log("TARGET INIZIALE"+targetDesc);
                var contaScambiaTarget=0;
                
                                
                function checkanddisablecheck(id) {
                    var exElement = document.getElementById(id);
                    if(!exElement.checked) {
                        exElement.checked = true;
                    }
                    var rTime = new Date().getTime();
                    dTime = dTime+(rTime-sTime);
                    sTime = new Date().getTime();
                    var exElement = document.getElementById(id);
                    exElement.disabled = true;
                    <c:if test="${exname!='ATT_ALT_CMP'}">
                        if (exElement.value == targetId) {
                    </c:if>
                    <c:if test="${exname=='ATT_ALT_CMP'}">
                        var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                        if (elDescr == targetDesc) {
                    </c:if>
                        nCorrect++;
                        
                        //document.getElementById('img-' + id).style.backgroundColor = "green";
                        //document.getElementById('img-' + id).style.filter= "grayscale(0%)";
                        addBackground('img-' + id, 'green')
                    }
                    else {
                        nWrong++;
                        
                        //document.getElementById('img-' + id).style.backgroundColor = "red";
                        //document.getElementById('img-' + id).style.filter= "grayscale(0%)";
                        addBackground('img-' + id, 'red')
                    }
                }
                        
                $("#myCarousel").on('slide.bs.carousel', function () {
                    currentIndex = $('div.active').index();
                    var id = 'i' + currentIndex;
                    var exElement = document.getElementById(id);
                    <c:if test="${exname!='ATT_ALT_CMP'}">
                        if (exElement.value ==targetId ) {
                    </c:if>
                    <c:if test="${exname=='ATT_ALT_CMP'}">
                        var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                        if (elDescr == targetDesc) {
                    </c:if>
                        if (!exElement.checked) {
                            nMissed++;                            
                            document.getElementById('img-' + id).style.backgroundColor = "yellow";
                            document.getElementById('img-' + id).style.filter= "grayscale(0%)";
                        }
                        
                        }
                     sTime = new Date().getTime();
                     var p=${nelementspertarget}*(contaScambiaTarget+1)-1;
                    
                     if(exElement.id=="i"+p&&nIteration!=0){
                    
                        
                        audio07.play();
                        if(contaScambiaTarget%2==0){
                         //   targetattivo=${targetelement2img};
                            targetId=${noTarget};
                            targetDesc='${noTarget.eldescr}';
                            console.log("dentro if nItaration contaScambiaTarget"+contaScambiaTarget);
                            
                             console.log("dentro if nItaration  NONONOtarget"+${noTarget});
                            contaScambiaTarget++;
                            
                        }
                        else
                        {
                          //  targetattivo=${targetelement1img};
                            targetId=${target};
                            targetDesc='${target.eldescr}';
                            console.log("dentro else nItaration contaScambiaTarget"+contaScambiaTarget);
                            
                            console.log("dentro if nItaration  target"+${target});
                            contaScambiaTarget++;
                        }
                        nIteration--;
                    }
                    else 
                       if(nIteration==0&&exElement.id=="i"+p-1) {
                    calculateResult();
                    console.log("dentro else"+ exElement.id);
                    }
                    });

                
                 $("#myCarousel").on('slid.bs.carousel', function () {
                    currentIndex = $('div.active').index();
                    currentIndex++;
                    if (currentIndex === $('.item').length) {
                        
                        nextTarget($('div.active'));
                    }
                });
                
               async function nextTarget(sld) {
                    await sleep (${time} * 1000);
                    calculateResult();
                }
                  function sleep(ms) {
                    return new Promise(resolve => setTimeout(resolve, ms));
                }
              
                
                function calculateResult() {
                 

                   console.log("sono in calcola risutlato");
                   
                   
                    var currentIndex = $('div.active').index();
                    var id = 'i' + currentIndex;
                    var exElement = document.getElementById(id);
                    <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                        if (exElement.value == targetId) {
                    </c:if>
                    <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                        var elDescr = arrExElDescr[arrExElId.indexOf(exElement.value)];
                        if (elDescr == targetDesc) {
                    </c:if>
                        if (!exElement.checked) {
                            nMissed++;
                            //document.getElementById('img-' + id).style.backgroundColor = "yellow";
                            //document.getElementById('img-' + id).style.filter= "grayscale(0%)";
                            addBackground('img-' + id, 'yellow');
                        }
                    }

                   
                        // il dTime medio lo moltiplichiamo per ${nelementspertarget}*${iterations}
                        // e lo trasformiamo in secondi
                        dTime = dTime/(nCorrect+nWrong)*${nelementspertarget}*${iterations}/1000;
                        
                        var perf;
                        var passed;
                        nWrognPer=nMissed+nWrong;
                        $.get("getperformance",
                        {
                            "exerciseid": ${exerciseid},
                            "patientid": ${patientid},
                            "ptime": dTime,
                            "pcorrect": nCorrect,
                            "pwrong": nWrong,
                            "pmissed": nMissed,
                            "maxtime": Math.round(${time}*${nelementspertarget}*${iterations}),
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
                                message:'<h4><br><b>Risposte esatte</b>: ' + nCorrect +
                                        '<br><b>Risposte sbagliate</b>: ' + nWrong +
                                        '<br><b>Omissioni</b>: ' + nMissed +
                                        '<br>' +
                                        '<br><b>Prestazione</b>: ' + Math.round(perf*100) + ' %' +                                    
                                        '<br>' +
                                        '<br>' + passedMessage + '</h4>',
                                callback:function() {
                                    post('attention3phase3', {
                                        difficulty: '${difficulty}',
                                        level: '${level}',
                                        patientid: '${patientid}',
                                        exerciseid: '${exerciseid}',
                                        category: '${category}',
                                        lastexercisepassed: '${lastexercisepassed}',
                                        nelementspertarget: '${nelementspertarget}',
                                        iterations: '${iterations}',
                                        color: '${color}',
                                        frequenza: '${frequenza}',
                                        time: '${time}',
                                        passed: passed,
                                        pTime: dTime,
                                        pCorrect: nCorrect,
                                        pMissed: nMissed,
                                        pWrong: nWrong,
                                        sessid: '${sessid}',
                                        type: '${type}',
                                        exname: '${exname}'
                                    }, 'get');
                                }
                            });
                        });
                    
                
                };
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
                if(${type=='ATT_ALT_FAC'}){
              document.getElementById("im1").innerHTML ="Clicchi su questo volto." ;
              document.getElementById("im2").innerHTML = "Quando sentir&agrave; il suono inizi a cliccare su quest'altro volto."
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sul volto precedente.<br>Alterni i due volti.";
            }
              if(${type=='ATT_ALT_ORI'})
                if(${exname=='ATT_ALT_ARR'}){
              document.getElementById("im1").innerHTML="Clicchi su questa freccia.";
              document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare su quest'altra freccia.";
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla freccia precedente.<br>Alterni le due frecce.";
          }
                else if(${exname=='ATT_ALT_CMP'}){
               document.getElementById("im1").innerHTML="Clicchi sulla freccia che indica questa direzione.";
              document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare sulla freccia che indica questa nuova direzione.";
              document.getElementById("im3").innerHTML= "Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla freccia che indica la direzione precedente.<br>Alterni le direzioni.";
              }
             if(${type=='ATT_ALT'}){
                document.getElementById("im1").innerHTML="Clicchi su questa figura.";
                document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare su quest'altra figura.";
                document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla figura precedente.<br>Alterni le due figure.";
                  }
              }
            else
            {

                 if(${type=='ATT_ALT_FAC'}){
              document.getElementById("im1").innerHTML ="Prema il tasto spazio quando vedr&agrave; questo volto" ;
              document.getElementById("im2").innerHTML = "Quando sentir&agrave; il suono prema il tasto spazio quando vedr&agrave; il volto a destra ."
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; il volto precedente.<br>Alterni i due volti.";

          } if(${type=='ATT_ALT_ORI'})
                if(${exname=='ATT_ALT_ARR'})
                {  document.getElementById("im1").innerHTML="Prema il tasto spazio quando vedr&agrave; questa freccia";
                       document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a premere il pulsante spazio quando vedr&agrave; la freccia qui a destra.";
                    document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la freccia precedente.<br>Alterni i due volti.";

                }else if(${exname=='ATT_ALT_CMP'})
                {document.getElementById("im1").innerHTML="Prema il tasto spazio quando vedr&agrave; la freccia che indica questa direzione.";
                  document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono prema il pulsante spazio quando vedr&agrave; la freccia che indica la direzione a destra.";
              document.getElementById("im3").innerHTML= "Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la freccia che indica la direzione precedente.<br>Alterni le direzioni.";

            }
             if(${type=='ATT_ALT'}){
                 document.getElementById("im1").innerHTML="Prema il pulsante spazio quando vedr&agrave; questa figura.";
                 document.getElementById("im2").innerHTML = "Quando sentir&agrave; il suono prema il tasto spazio quando vedr&agrave; la figura a destra."
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la prima figura.<br>Alterni le due figure.";

             }


            }
    };
    </script>
    </body>
</html>
