<%-- 
    Document   : pddl-pianificazione1a
    Created on : 12-mag-2017, 10.25.30
    Author     : danger
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Daniele Baschieri">
        <link rel="icon" href="resources/favicon.ico">

        <title>Pianificazione</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

        <script type="text/javascript" src="resources/js/hello.js"></script>        

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron.css">

        <style>
            .legendlist{
                list-style-type: none; 
            }
            .legendlist li {
                margin-bottom: 3px;
            }
            .legendlist img{
                width: 30px;
                margin-right: 15px;
            }
            .legendlist img.bigimg{
                width:50px;
            }
        </style>
        <script type="text/javascript" >
            $(document).ready(function () {
                $.get("getproblemdescription", {"id":${idproblem}}, function (data) {
                    $("#description").html(data);
                });
            })
        </script>
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
                    <div class="col-md-24">
                        <h2>Pianifica la tua giornata</h2>
                        <article>
                            <p>Pianifica la tua giornata tenendo conto della lista degli impegni e degli orari di apertura e chiusura dei negozi.</p>
                             <p>Assicurati di fare il minor numero di mosse e di muoverti solo lungo le strade; 
                                puoi passare davanti ai negozi chiusi ma puoi prendere o depositare oggetti solo negli orari di apertura! 
                            </p>
                            <p>Non occorre memorizzare gli orari di apertura/chiusura, sono sempre visibili sulla mappa</p>
                            <p>Durante l'esercizio puoi visionare gli obiettivi cliccando la voce “Cose da fare", questa azione non viene considerata
                            come un’ulteriore mossa. </p>
                           
                        </article>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-24">
                        <div id="description"></div>
                        <div id="legend">
                            <hr />
                            <p><b>Legenda:</b></p>
                            <div class="row">
                               
                                   
                                    <ul class="legendlist">
                                        
                                    <li><b>Ricorda che</b> la tessera si usa in piscina, mentre l'abbonamento si usa in stazione</li>    
                                        <li><img src ="resources/images/locali/path.png" /> strada transitabile solo a piedi.</li>
                                        <li><img src ="resources/images/locali/road.png" /> strada transitabile solo in automobile.</li>
                                        <li><img src ="resources/images/locali/both_path_road.png" /> strada transitabile sia in auto che a piedi.</li>
                                        <li><img src ="resources/images/locali/clock.png" /> luogo in cui è possibile attendere.</li>
                                    </ul> 
                                </div>
                                <div class="col-md-12">
                                    <ul class="legendlist">
                                        <li><img src ="resources/images/locali/map.png" class="bigimg" /> Spostamento a piedi, occorrono 30 minuti per transitare su qualsiasi percorso.</li>
                                        <li><img src ="resources/images/locali/map_macchina.png" class="bigimg" /> Spostamento in macchina, gli spostamenti non richiedono tempo.</li>
                                        <li><button class="btn btn-sm btn-primary">Aspetta</button> Permette di far scorrere il tempo, il tempo passa solo quando si decide di aspettare.</li>
                                    </ul> 
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-18"></div>
                    <c:if test="${difficulty=='training' || difficulty=='demo'}">
                        <div class="col-sm-2"></h3></div>
                        <div class="col-sm-6"><h3 style='color:red'>Esercizio di prova</h3></div>
                    </c:if>

                    <div class="col-md-4">
                        <a class="btn btn-lg btn-success pull-right"
                           href="pianificazione1phase2?idproblem=${idproblem}&color=${color}&difficulty=${difficulty}&level=${level}&patientid=${patientid}&exerciseid=${exerciseid}&sessid=${sessid}">
                            Inizia esercizio
                        </a>
                    </div>
                </div>
            </div>                
            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div>

        <script>
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
    </body>
</html>
