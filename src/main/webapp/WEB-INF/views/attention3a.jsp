<%-- 
    Document   : attention3a
    Created on : Apr 18, 2016, 12:50:20 PM
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

        <title>Attenzione Alternata</title>

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
            
            <!--div class="navbar" style="background-color:white"-->
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-5">
                            <h3 class="pull-left" id="im1"> </h3>
                           <!-- <c:if test="${type=='ATT_ALT_FAC'}">
                                <h3 class="pull-left">Clicchi o prema il tasto spazio su questo volto.</h3>
                            </c:if>
                            <c:if test="${type=='ATT_ALT_ORI'}">
                                <c:if test="${exname=='ATT_ALT_ARR'}">
                                    <h3 class="pull-left">Clicchi o prema il tasto spazio su questa freccia.</h3>
                                </c:if>
                                <c:if test="${exname=='ATT_ALT_CMP'}">
                                    <h3 class="pull-left">Clicchi o prema il tasto spazio sulla freccia che indica questa direzione.</h3>
                                </c:if>
                            </c:if>
                            <c:if test="${type=='ATT_ALT'}">
                                <h3 class="pull-left">Clicchi o prema il pulsante spazio su questa figura.</h3>
                            </c:if>-->
                        </div>
                        <div id="${targetelement1.id}" class="col-sm-3">
                            <c:if test="${exname=='ATT_ALT_CMP'}">
                                <h3 style="color: red">${targetelement1.eldescr}</h3>
                            </c:if>
                            <c:if test="${exname!='ATT_ALT_CMP'}">
                                <img class="img-responsive pull-left" src="${targetelement1.url}" alt="${targetelement1.url}">
                            </c:if>                                
                        </div>
                        <div class="col-sm-5"><h3 class="pull-left" id="im2"></h3>
                          <!-- <c:if test="${type=='ATT_ALT_FAC'}">
                                <h3 class="pull-left">Quando sentir&agrave; il suono inizi a cliccare o a premere il pulsante spazio su quest'altro volto.</h3>
                            </c:if>
                            <c:if test="${type=='ATT_ALT_ORI'}">
                                <c:if test="${exname=='ATT_ALT_ARR'}">
                                    <h3 class="pull-left">Quando sentir&agrave; il suono inizi a cliccare o a premere il pulsante spazio su quest'altra freccia.</h3>
                                </c:if>
                                <c:if test="${exname=='ATT_ALT_CMP'}">
                                    <h3 class="pull-left">Quando sentir&agrave; il suono inizi a cliccare o a premere il pulsante spazio sulla freccia che indica questa nuova direzione.</h3>
                                </c:if>
                            </c:if>
                            <c:if test="${type=='ATT_ALT'}">
                                <h3 class="pull-left">Quando sentir&agrave; il suono inizi a cliccare o a premere il pulsante spazio su quest'altra figura.</h3>
                            </c:if>
                          -->
                        </div>
                        <div id="${targetelement2.id}" class="col-sm-3">
                            <c:if test="${exname=='ATT_ALT_CMP'}">
                                <h3 style="color: red">${targetelement2.eldescr}</h3>
                            </c:if>
                            <c:if test="${exname!='ATT_ALT_CMP'}">
                                <img class="img-responsive pull-left" src="${targetelement2.url}" alt="${targetelement2.url}">
                            </c:if>                                
                        </div>
                        <div class="col-sm-8">
                            <h3 class="pull-left"><div id="im3"></div></h3>
                            <!--<c:if test="${type=='ATT_ALT_FAC'}">
                                <h3 class="pull-left">Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sul volto precedente o a premere il pulsante spazio.<br>Alterni i due volti. Prima di iniziare provi il suono.</h3>
                            </c:if>
                            <c:if test="${type=='ATT_ALT_ORI'}">
                                <c:if test="${exname=='ATT_ALT_ARR'}">
                                    <h3 class="pull-left">Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla freccia precedente o a premere il pulsante spazio.<br>Alterni le due frecce. Prima di iniziare provi il suono.</h3>
                                </c:if>
                                <c:if test="${exname=='ATT_ALT_CMP'}">
                                    <h3 class="pull-left">Se sentir&agrave; di nuovo il suono, torni a cliccare o a premere il pulsante spazio sulla freccia che indica la direzione precedente.<br> Prima di iniziare provi il suono.</h3>
                                </c:if>
                            </c:if>
                            <c:if test="${type=='ATT_ALT'}">
                                <h3 class="pull-left">Quando sentir&agrave; il suono dovr&agrave; tornare a cliccare sulla figura precedente o a premere il pulsante spazio.<br>Alterni le due figure. Prima di iniziare provi il suono.</h3>
                            </c:if>-->
                        </div>
                    </div>
                    <p></p>
                    <div class="row">
                        <div class="col-sm-2"></div>                        
                        <div class="col-sm-4">
                            <button id="trysound" class="btn btn-lg btn-warning pull-right"
                                    onclick="trySound()"
                                    >
                                Prova suono
                            </button>
                        </div>
                        <c:if test="${difficulty=='training' || difficulty=='demo'}">
                            <div class="col-sm-2"></h3></div>
                            <div class="col-sm-10"><h3 style='color:red'>Esercizio di prova</h3></div>
                        </c:if>
                        <div class="col-sm-4">
                            <button class="btn btn-lg btn-success pull-right"
                                    onclick="
                                        post('attention3phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            leftiterations: '${leftiterations}',
                                            iterations: '${iterations}',
                                            targetelement1: '${targetelement1}',
                                            targetelement2: '${targetelement2}',
                                            firsttarget: '${firsttarget}',
                                            nelementspertarget: '${nelementspertarget}',
                                            color: '${color}',
                                            frequenza: '${frequenza}',
                                            time: '${time}',
                                            passed: '${passed}',
                                            pTime: 0,
                                            pCorrect: 0,
                                            pMissed: 0,
                                            pWrong: 0,
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}'
                                        }, 'get');">
                                Inizia esercizio
                            </button>
                        </div>
                        <div class="col-sm-2"></div>
                    </div>
                </div>
            <!--/div-->

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>


            <jsp:include page="modal-att3.jsp" />
            
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
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sul volto precedente.<br>Alterni i due volti. Prima di iniziare provi il suono.";
            }
              if(${type=='ATT_ALT_ORI'})
                if(${exname=='ATT_ALT_ARR'}){
              document.getElementById("im1").innerHTML="Clicchi su questa freccia.";
              document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare su quest'altra freccia.";
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla freccia precedente.<br>Alterni le due frecce. Prima di iniziare provi il suono.";
          }
                else if(${exname=='ATT_ALT_CMP'}){
               document.getElementById("im1").innerHTML="Clicchi sulla freccia che indica questa direzione.";
              document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare sulla freccia che indica questa nuova direzione.";
              document.getElementById("im3").innerHTML= "Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla freccia che indica la direzione precedente.<br>Alterni le direzioni. Prima di iniziare provi il suono.";
              }
             if(${type=='ATT_ALT'}){
                document.getElementById("im1").innerHTML="Clicchi su questa figura.";
                document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a cliccare su quest'altra figura.";
                document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a cliccare sulla figura precedente.<br>Alterni le due figure. Prima di iniziare provi il suono.";
                  }
              }
            else
            {  
                
                 if(${type=='ATT_ALT_FAC'}){
              document.getElementById("im1").innerHTML ="Prema il tasto spazio quando vedr&agrave; questo volto" ;
              document.getElementById("im2").innerHTML = "Quando sentir&agrave; il suono prema il tasto spazio quando vedr&agrave; il volto a destra ."
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; il volto precedente.<br>Alterni i due volti. Prima di iniziare provi il suono.";
            
          } if(${type=='ATT_ALT_ORI'})
                if(${exname=='ATT_ALT_ARR'})
                {  document.getElementById("im1").innerHTML="Prema il tasto spazio quando vedr&agrave; questa freccia";
                       document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono inizi a premere il pulsante spazio quando vedr&agrave; la freccia qui a destra.";
                    document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la freccia precedente.<br>Alterni i due volti. Prima di iniziare provi il suono.";
          
                }else if(${exname=='ATT_ALT_CMP'})
                {document.getElementById("im1").innerHTML="Prema il tasto spazio quando vedr&agrave; la freccia che indica questa direzione.";
                  document.getElementById("im2").innerHTML="Quando sentir&agrave; il suono prema il pulsante spazio quando vedr&agrave; la freccia che indica la direzione a destra.";
              document.getElementById("im3").innerHTML= "Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la freccia che indica la direzione precedente.<br>Alterni le direzioni. Prima di iniziare provi il suono.";
             
            }
             if(${type=='ATT_ALT'}){
                 document.getElementById("im1").innerHTML="Prema il pulsante spazio quando vedr&agrave; questa figura.";
                 document.getElementById("im2").innerHTML = "Quando sentir&agrave; il suono prema il tasto spazio quando vedr&agrave; la figura a destra."
              document.getElementById("im3").innerHTML="Se sentir&agrave; di nuovo il suono, dovr&agrave; tornare a premere il pulsante spazio quando vedr&agrave; la prima figura.<br>Alterni le due figure. Prima di iniziare provi il suono.";
            
             }
                
                
            }
    };
    </script>
    </body>
</html>
