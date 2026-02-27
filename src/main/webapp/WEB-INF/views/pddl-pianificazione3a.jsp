<%-- 
    Document   : pddl-pianificazione3a
    Created on : 11-mag-2018, 18.09.56
    Author     : Margherita Donnici
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
        <meta name="author" content="Margherita Donnici">
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
                        <h2>Weekend a Roma</h2>
                        <article>
                            <p>Immagina di dover organizzare un fine settimana a Roma.<br/>
                                Il tuo compito è di prenotare i treni e l'albergo. Dopodichè pianifica la visita rispettando gli impegni e gli orari, in modo da
                                poter prendere il treno per tornare a casa.<br/>
                            </p>
                            <p> Ricordati che:
                            	<ul>
                            		<li>Uno spostamento tra due luoghi ha la durata di 1 ora (sia a piedi che in autobus).</li>
                            		<li>Le strade bianche sono transitabili a piedi.</li>
                            		<li>Puoi dormire solo nell'albergo che hai prenotato.</li>
                                        <li>Per incontrare i tuoi amici devi cliccare il pulsante "incontra amici".</li>
                                        <li>Ogni attivit&agrave;, visita e l'azione di mangiare ha una durata di 2 ore. Quindi devi arrivare entro due ore dalla chiusura per poter svolgere l'attivit&agrave;</li>
                                        <c:if test="${level >= 4}">
                                        <li>Le strade grigie sono transitabili in autobus.</li>
                                   	<li>La colazione e la ginnastica durano un'ora.</li>
                                        <li>                                        <li>La ginnastica si può iniziare da due ore prima della colazione a due ore dopo.</li></li>
                                        </c:if>
                            		<li>Puoi accedere a queste informazioni ogniqualvolta lo desideri premendo sul bottone <button type="button" class="btn btn-primary navbar-btn btn-sm"><span class="glyphicon glyphicon-question-sign align-middle"
						aria-hidden="true"></span>
				</button></li>
                            	</ul>
                            </p>
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
                                <div class="col-md-12">
                                    <ul>
                                    	<li><img src ="resources/images/weekend-rome/currentPos.png" height="70" width="70"style="margin-bottom: 10px;"/>La tua posizione corrente.</li>
                                        <li><img src ="resources/images/weekend-rome/bus.png" height="70" width="70" style="margin-bottom: 10px;"/> Luogo raggiungibile in autobus</li>
                                        <li><img src ="resources/images/weekend-rome/walk.png" height="70" width="70" style="margin-bottom: 10px;"/> Luogo raggiungibile a piedi</li>                                  
                                    </ul> 
                                </div>
                                <div class="col-md-12">
                                    <ul>
                                    	<c:if test="${level > 3}">
                                    	<li><img src ="resources/images/weekend-rome/info.png" style="margin-bottom: 10px;"/> Clicca qui per vedere gli orari di apertuta e degli autobus del luogo</li>
                                    	</c:if>
                                        <li><button type="button" class="btn btn-danger" style="margin-bottom: 10px;">Aspetta</button> Clicca qui per aspettare 1 ora nel luogo dove sei.</li>
                                        <li><button type="button" class="btn btn-danger" style="margin-bottom: 10px;">Visita</button> I bottoni rossi sono le azioni che puoi effettuare, come per esempio visitare un monumento.</li>
                                      <!--  <li><button type="button" class="btn btn-success" style="margin-bottom: 10px;">Verifica</button> Clicca qui per verificare se sei sulla buona strada.</li>
                                        <li><button type="button" class="btn btn-success" style="margin-bottom: 10px;">Suggerimento</button> Clicca qui per ottenere un suggerimento sulla prossima azione da eseguire.</li-->                                      
                                        <li><button type="button" class="btn btn-success" style="margin-bottom: 10px;"><i class="fa fa-compass"></i></button> Clicca qui per ottenere un suggerimento sui luoghi raggiungibili.</li>                                      
                                    
                                    </ul> 
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12"></div>
                    <c:if test="${difficulty!='training' && difficulty!='demo'}">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-4">
                            <button id="tryexercise" class="btn btn-lg btn-warning pull-right"
                                    onclick="
                                        post('pianificazione3phase2', { 
                                            idproblem: '${idproblem}',
                                            difficulty: 'training',
                                            level: 1,
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            sessid: '${sessid}'
                                        }, 'get');">                                
                                Prova esercizio
                            </button>                        
                        </div>
                        <div class="col-sm-2"></div>
                    </c:if>
                    <c:if test="${difficulty=='training'}">
                        <div class="col-sm-8"></div>
                    </c:if>

                    <div class="col-md-4">
                        <!--a class="btn btn-lg btn-success pull-right"
                             href="pianificazione3phase2?idproblem=${idproblem}&difficulty=${difficulty}&level=${level}&patientid=${patientid}&exerciseid=${exerciseid}&sessid=${sessid}">
                        Inizia esercizio
                        </a-->
                           <button class="btn btn-lg btn-success pull-right"
                                    onclick=
                                        "post('pianificazione3phase2', {
                                            idproblem: '${idproblem}',
                                            difficulty: '${difficulty}',
                                            level: '${level}',
                                            patientid: '${patientid}',
                                            exerciseid: '${exerciseid}',
                                            sessid: '${sessid}'
                                        }, 'get'); this.disabled=true;">
                                Inizia esercizio
                            </button>

                    </div>
                </div>
            </div>                
            <footer class="footer">
                <p>&copy; 2016-2022 Universit&agrave; di Bologna</p>
            </footer>

        </div>

        <script>
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
    </body>
</html>