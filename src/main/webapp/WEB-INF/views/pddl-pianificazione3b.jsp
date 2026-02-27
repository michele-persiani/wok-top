<%-- 
    Document   : pddl-pianificazione3b
    Created on : 14-mag-2018, 14.33.17
    Author     : Margherita Donnici
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>



<% 	
	//Get attributes to pass to JS
//	int sleepHour = (int) request.getAttribute("sleepHour");
	int level = (int) request.getAttribute("level");
       %>
  <script type="text/javascript">
        idproblem=${idproblem};
        difficulty="${difficulty}";
            patientid=${patientid};
            exerciseid=${exerciseid};
            sessid=${sessid}; 
  //  List<String> trains = (List<String>) request.getAttribute("trains");
   // List<String> goals = (List<String>) request.getAttribute("goals");
</script>

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
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<title>Weekend a Roma</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<!-- font-awesome -->
<link rel="stylesheet" href="resources/css/font-awesome.min.css">
<!-- Custom css -->
<link rel="stylesheet" href="resources/css/weekend-in-rome.css">

<script type="text/javascript"
	src="resources/assets/js/vendor/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link rel="stylesheet"
	href="resources/assets/css/ie10-viewport-bug-workaround.css">
<!-- Latest compiled bootbox -->
<script type="text/javascript" src="resources/js/bootbox.min.js"></script>

</head>
<body>
	<nav class="navbar navbar-default navbar-static-top btn-toolbar">
		<div class="container-fluid">
			<div class="btn-toolbar" id="topToolbar">
				<!-- INFO BUTTONS -->
				<button type="button" class="btn btn-primary navbar-btn btn-lg"
					data-toggle="modal" data-target="#legendModal">
					<span class="glyphicon glyphicon-question-sign align-middle"
						aria-hidden="true"></span>
				</button>
				<button type="button" class="btn btn-primary navbar-btn btn-lg"
					data-toggle="modal" data-target="#goalModal" onclick="goalsAction()">
					<span class="glyphicon glyphicon-check align-middle"
						aria-hidden="true"></span>
					Obiettivi
                                        
				</button>
				<button type="button" class="btn btn-primary navbar-btn btn-lg"
					data-toggle="modal" data-target="#bookingsModal"
                                        onclick="bookAction()"
					id="bookingsButton">
					<i class="fa fa-train"></i>/ <i class="fa fa-bed"></i>
					Prenotazioni
				</button>
				<!--- DATE -->
				<button class="btn btn-default navbar-btn disabled btn-lg"
					style="cursor: default; display: none;" id="date"></button>
				<!-- ACTION BUTTONS -->
				<button type="button" id="chooseTrains"
					class="btn btn-danger navbar-btn btn-lg" data-toggle="modal"
					data-target="#trainModal">Scegli Treni</button>
				<button type="button" class="btn btn-danger navbar-btn btn-lg"
					id="chooseHotel" style="display: none;">Scegli Albergo</button>
				<button type="button"
					class="btn btn-danger navbar-btn btn-lg"
					id="beginButton" style="display: none;">Parti!</button>
				<!-- RIGHT SIDE BUTTONS -->
                                 <a href="patientrehabilitation">
				<button type="button"
					class="btn btn-warning navbar-btn pull-right btn-lg" id="intButton">Interrompi</button>
                                 </a>       
				<!--button type="button"
					class="btn btn-success navbar-btn pull-right btn-lg"
					id="hintButton" style="display: none;">Suggerimento</button>
				<button type="button"
					class="btn btn-success navbar-btn pull-right btn-lg"
					id="checkSolution" style="display: none;">Verifica</button-->
				<button type="button"
					class="btn btn-success navbar-btn pull-right btn-lg"
					id="compass" style="display: none;"><i class="fa fa-compass"></i></button>
				<button type="button"
					class="btn btn-danger navbar-btn btn-lg pull-right"
					id="waitButton" style="display: none;">Aspetta</button>				
			</div>
		</div>
	</nav>
	<div id="level" style="display: none;">${level}</div> <!-- dirty trick to pass current level to js -->
	<div class="container-fluid text-center">
		<div id="map" style="overflow-x: visible"></div>
	</div>

	<!-- Place Info Modal -->
	<div class="modal fade" id="infoModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title" id="infoTitle">
						<span class="glyphicon glyphicon-info-sign align-middle"
							aria-hidden="true"></span>Informazioni
					</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row" id="openingHoursRow">
							<div>
								<h3>Orari di apertura</h3>
								<table class="table table-bordered">
									<tr>
										<th>Sabato</th>
										<th>Domenica</th>
									</tr>
									<tr>
										<td id="saturdayOpenHours"></td>
										<td id="sundayOpenHours"></td>
									</tr>
								</table>
							</div>
						</div>
						<div class="row" id="breakfastHoursRow">
							<div>
								<h3>Orari per la colazione</h3>
								<h4>La colazione verrà servita dalle 7 alle 11.</h4>
							</div>
						</div>
						<div class="row" id="bus">
							<div>
								<h3>Orari degli autobus</h3>
								<h4 id="noBus"><small>Non c'è nessuna fermata dell'autobus qui.</small></h4>
								<table class="table table-bordered" id="busSchedulelist">
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Map Legend Modal -->
	<div class="modal fade" id="legendModal" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title" id="infoTitle">Weekend a Roma</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
                <div class="row">
                    <div class="col-md-24">
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
                                        <li>La ginnastica si può iniziare da due ore prima della colazione a due ore dopo.</li>
                                        </c:if>
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
                                <div class="col-md-24">
                                    <ul>
                                    	<li><img src ="resources/images/weekend-rome/currentPos.png" height="70" width="70"style="margin-bottom: 10px;"/>La tua posizione corrente.</li>
                                        <li><img src ="resources/images/weekend-rome/bus.png" height="70" width="70" style="margin-bottom: 10px;"/> Luogo raggiungibile in autobus</li>
                                        <li><img src ="resources/images/weekend-rome/walk.png" height="70" width="70" style="margin-bottom: 10px;"/> Luogo raggiungibile a piedi</li>
                                        <c:if test="${level > 3}">
                                        <li><img src ="resources/images/weekend-rome/info.png" style="margin-bottom: 10px;"/> Clicca qui per vedere gli orari di apertuta e degli autobus del luogo</li>
                                        </c:if>
                                        <li><button type="button" class="btn btn-danger" style="margin-bottom: 10px;">Aspetta</button> Clicca qui per aspettare 1 ora nel luogo dove sei.</li>
                                        <li><button type="button" class="btn btn-danger" style="margin-bottom: 10px;">Visita</button> I bottoni rossi sono le azioni che puoi effettuare, come ad esempio visitare un monumento.</li>
                                        <!--li><button type="button" class="btn btn-success" style="margin-bottom: 10px;">Verifica</button> Clicca qui per verificare se sei sulla buona strada.</li>
                                        <li><button type="button" class="btn btn-success" style="margin-bottom: 10px;">Suggerimento</button> Clicca qui per ottenere un suggerimento sulla prossima azione da eseguire.</li-->                                      
                                        <li><button type="button" class="btn btn-success" style="margin-bottom: 10px;"><i class="fa fa-compass"></i></button> Clicca qui per ottenere un suggerimento sui luoghi raggiungibili.</li>                                      
                                    
                                          
                                    </ul> 
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Bookings Info Modal -->
	<div class="modal fade" id="bookingsModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title">Prenotazioni</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div>
								<h3>Treni</h3>
								<h4 id="noTrains">
									<small>Non hai ancora prenotato i treni.</small>
								</h4>
								<div id="bookedTrains" style="display: none;">
									<h4 class="text-center">
										Treno Andata <small id="trenoAndata"></small>
									</h4>
									<h4 class="text-center">
										Treno Ritorno <small id="trenoRitorno"></small>
									</h4>
								</div>
							</div>
						</div>
						<div class="row">
							<div>
								<h3>Albergo</h3>
								<h4 id="noHotel">
									<small>Non hai ancora prenotato l'albergo.</small>
								</h4>
								<h4 class="text-center" id="bookedHotel" style="display: none;">
									Albergo <small id="hotelName">Hotel Belvedere</small>
								</h4>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Goal Modal -->
	<div class="modal fade" id="goalModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title">I tuoi obiettivi</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<ul id="goalList" style="list-style-type:none" class="lead">
							<c:forEach items="${goals}" var="goal">
								<c:if test="${not empty goal.userFriendlyString}">
								<li id="${goal.id}"><i class="fa fa-square-o"></i> ${goal.userFriendlyString}</li>
								</c:if>
							</c:forEach>
							<c:choose>
							    <c:when test="${sleepHour < 25}">
							     <li id="sleepHour"><i class="fa fa-square-o"></i> Torna in hotel alle ${sleepHour} sabato, per poterti riposare.</li>
							    </c:when>    
							    <c:otherwise>
                                                             <li id="sleepHour2"><i class="fa fa-square-o"></i> Torna in hotel alle ${sleepHour%24} domenica, per poterti riposare.</li>     
							    
                                                             </c:otherwise>
							</c:choose>						
						</ul>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Warning Modal -->
	<div class="modal fade" id="warningModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title" id="warningTitle"></h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<span id="warningMsg"></span>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							id="bookHotel">Prenota</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- Book Train Modal -->
	<div class="modal fade" id="trainModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title">Prenota Treni</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<table class="table table-bordered">
							<tr>
								<th></th>
								<th>Andata</th>
								<th>Ritorno</th>
							</tr>
							<c:forEach items="${trains}" var="train">
								<tr>
									<td>
										<div class="radio">
											<label> <input type="radio" name="trains"
												id="optionsRadios1" value="option1">
											</label>
										</div>
									</td>
									<td>
										<strong>Partenza</strong>: Sabato, ${train.outwardDeparture}:00 (${train.station})<br> 
										<strong>Arrivo</strong>: Sabato, ${train.outwardDeparture + 2}:00 (${train.station})
									</td>
									<td>
										<strong>Partenza</strong>: Domenica, ${train.returnDeparture%24}:00 (${train.station})<br>
										<strong>Arrivo</strong>: Domenica, ${train.returnDeparture%24 + 2}:00 (${train.station})
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							id="bookTrains">Prenota</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- End Exercise Modal -->
	<div class="modal fade" id="endExerciseModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h2 class="modal-title" >Esercizio completato</h2>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<h3 id="summarySentence"></h3>
                                               <!-- <p>Con una performance di </p><p id="loadperformance"> </p>-->
						<h4>Obiettivi risolti:</h4>
						<h4 id="noSolved"><small>Nessuno.</small></h4>
						<ul id="solvedGoalsList" style="list-style-type: none" class="lead"></ul>
						<h4>Obiettivi non risolti:</h4>
						<h4 id="noUnsolved"><small>Nessuno.</small></h4>
						<ul id="unsolvedGoalsList" style="list-style-type: none" class="lead"></ul>
                                                <p id ="fine"></p>
                                        </div>
				</div>
				<!--div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">Chiudi</button>
						
					</div-->
			</div>
		</div>
	</div>
                    </div>
            </div>

	<div id="snackbar"> </div>
	<!-- Graphics library -->
	<script type="text/javascript" src="resources/js/pixi.min.js"></script>
	<!-- JS files for map handling & location data -->
	<script type="text/javascript" src="resources/js/pddl-rome-locations.js"></script>
	<script type="text/javascript" src="resources/js/pddl-rome-goals.js"></script>
	<script type="text/javascript" src="resources/js/pddl-rome-map.js"></script>
	     <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
</body>
</html>
