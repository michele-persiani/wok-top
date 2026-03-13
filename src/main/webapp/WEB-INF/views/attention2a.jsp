<%-- 
    Document   : attention2a
    Created on : Feb 4, 2016, 5:50:00 PM
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

        <title>Attenzione Selettiva</title>

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
            
            <div class="well well-sm">
                <div class="row">
                    <div class="col-sm-6">
                        <c:if test="${type=='ATT_SEL_FLW_FAC'}">
                            <h3 class="pull-left">Memorizzi questi volti</h3>
                        </c:if>
                        <c:if test="${type=='ATT_SEL_FLW_ORI'}">
                            <c:if test="${exname=='ATT_SEL_FLW_ARR'}">
                                <h3 class="pull-left">Memorizzi queste frecce </h3>
                            </c:if>
                            <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                                <h3 class="pull-left">Memorizzi queste direzioni </h3>
                            </c:if>
                        </c:if>
                        <c:if test="${type=='ATT_SEL_FLW'}">
                            <h3 class="pull-left"> Memorizzi queste figure</h3>
                        </c:if>
                    </div>
                    <div id="targets">
                        <c:forEach var="target" items="${targetElementList}">
                            <div id="${target.id}" class="col-sm-3">
                                <c:if test="${exname=='ATT_SEL_FLW_CMP'}">
                                    <h3 style="color: red">${target.eldescr}</h3>
                                </c:if>
                                <c:if test="${exname!='ATT_SEL_FLW_CMP'}">
                                    <img class="img-responsive pull-left" src="${target.url}" alt="${target.url}">
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="col-sm-6">
               
                    </div>
                    <!--div class="col-sm-5">
                        <h3 class="pull-left"> entro il tempo massimo </h3>
                    </div-->
                </div>
                
                
                  <div class="row">
                    <div class="col-sm-18">
                        <H3>Nella prossima pagina vedr&agrave; scrorrere delle immagini. <br></h3>
                    <h3 class="pull-left"><div id="im2"></div></h3>
                            
                             </div>
                      
                    <!--div class="col-sm-5">
                        <h3 class="pull-left"> entro il tempo massimo </h3>
                    </div-->
                </div>
                
                
                <div class="row">
                    <c:if test="${difficulty=='training' || difficulty=='demo'}">
                        <div class="col-sm-6"><h3 style='color:red'>Esercizio di prova</h3></div>
                        <div class="col-sm-18"></div>
                    </c:if>
                </div>
                <div class="row">
                    <div class="col-sm-20"></div>
                    <div class="col-sm-4">
                        <button class="btn btn-lg btn-success pull-right"
                                onclick="                                        
                                        post('attention2phase2', {
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            category: '${category}',
                                            lastexercisepassed: '${lastexercisepassed}',
                                            nelements: '${nelements}',
                                            ntargets: '${ntargets}',
                                            targetElementList: '${targetElementList}',
                                            exElementList: '${exElementList}',
                                            color: '${color}',
                                            distractor: '${distractor}',
                                            time: '${time}',
                                            sessid: '${sessid}',
                                            type: '${type}',
                                            exname: '${exname}',
                                            assignmentid: '${assignmentid}',
                                            rlagent: '${rlagent}',
                                        },'get');">
                            Inizia esercizio
                        </button>
                    </div>
                </div>
            </div>
        </div>

            <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
            <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

            <jsp:include page="modal-att2.jsp" />
            
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
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
              
              if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Clicchi su queste figure quando le vedr&agrave; scorrere.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Clicchi su queste frecce quando le vedr&agrave; scorrere.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Clicchi sulle frecce che indicano queste direzioni quando le vedr&agrave; scorrere." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Clicchi su queste figure quando le vedr&agrave; scorrere.";
              
              }
            else
            {
                 if(${type=='ATT_SEL_FLW_FAC'})
             document.getElementById("im2").innerHTML="Prema la barra spaziatrice quando vedr&agrave; scorrere queste figure.";
             if(${type=='ATT_SEL_FLW_ORI'} )
             if (${exname=='ATT_SEL_FLW_ARR'})
                document.getElementById("im2").innerHTML = "Prema la barra spaziatrice quando vedr&agrave; scorrere queste frecce.";
             if(${exname=='ATT_SEL_FLW_CMP'})
                 document.getElementById("im2").innerHTML ="Prema la barra spaziatrice quando vedr&agrave; scorrere le frecce che indicano queste direzioni." ;
            if(${type=='ATT_SEL_FLW'})
             document.getElementById("im2").innerHTML="Prema la barra spaziatrice quando vedr&agrave; scorrere queste figure.";
              
                
                
                
            }
    }
    </script>
        

    </body>
</html>
