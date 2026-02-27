<%-- 
    Document   : pddl-pianificazione2b
    Created on : 18-gen-2018, 14.33.17
    Author     : bartolomeo lombardi
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>

<% 	
    int[][] gameMapFromServlet = (int [][]) request.getAttribute("gameMap");
    String inFinalPlace = (String) request.getAttribute("inFinalPlace");
    List<String> listGoals = (List<String>) request.getAttribute("listGoals");
    List<String> parameters = (List<String>) request.getAttribute("parameters");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta name="description" content="">
        <meta name="author" content="Bartolomeo Lombardi">
        <link rel="icon" href="resources/favicon.ico">

        <title>Pianificazione</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>
        
        <!--script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script-->
        <!-- Latest compiled and minified JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
        <!--script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script-->
     

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        <link rel="stylesheet" href="resources/css/pddl-default.css" />
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

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
            #snackbar {
                visibility: hidden;
                width: 250px;
                    margin: auto;
                background-color: #333;
                color: #fff;
                text-align: left;
                border-radius: 2px;
                padding: 16px;
                position: fixed;
                z-index: 1;
            }

            #snackbar.show {
                visibility: visible;
                -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
                animation: fadein 0.5s, fadeout 0.5s 2.5s;
            }
        </style>
        <!-- script per recuperare le variabili passate dalla servlet -->
        <script type="text/javascript">
            var gameMap = [
                <% 	for(int y = 0; y < 19; ++y) {
                        for(int x = 0; x < 25; ++x)
                        {
                                out.print(gameMapFromServlet[y][x] + ",");
                        }
                        out.print("\n");
                }%>]

            var listGoals =  [<% for (int i = 0; i < listGoals.size(); i++) { %>"<%= listGoals.get(i) %>"<%= i + 1 < listGoals.size() ? ",":"" %><% } %>].sort();
            var parameters = [<% for (int i = 0; i < parameters.size(); i++) { %>"<%= parameters.get(i) %>"<%= i + 1 < parameters.size() ? ",":"" %><% } %>];
            var inFinalPlace = "<%= inFinalPlace %>";

            idproblem=${idproblem};
            colorenable=${color};
            difficulty="${difficulty}";
            level = ${level};
            patientid=${patientid};
            exerciseid=${exerciseid};
            sessid=${sessid};
        </script>
        <!-- file js per disegnare e gestire canvas -->
        <script type="text/javascript" src="resources/js/pddl-zoomap.js"> </script>
        <!-- script per timer solo se easy, training e demo -->
        <script type="text/javascript">
            if(difficulty == "easy" || difficulty == "demo" || difficulty == "training")
            {
                var aaa = setInterval(function () {
                    myTimer();
                }, 1000);

                var startTime = new Date().getTime();
                var totalTime =  300 * listGoals.length;
                var startTimer = totalTime;

                function myTimer() {
                    startTimer--;
                    document.getElementById("timer").innerHTML = startTimer;
                    if (startTimer === 0) {
                        clearInterval(aaa);
                        gameOverStatus = true;
                    }
                }
            }
        </script>
       
        <script>
                //pulsante inizio
                function start() {
                
                var e = new Event("keydown");
                document.getElementById('xstart').disabled=true;
                e.keyCode=32;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }  
                //pulsante pause
                function pause() {
                resetArrows();
                var e = new Event("keydown");
                e.keyCode=36;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }       
            function left() {
                resetArrows();
                var e = new Event("keydown");
                e.keyCode=37;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }               
            function up() {
                resetArrows();
                var e = new Event("keydown");
                e.keyCode=38;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }               
            function right() {
                resetArrows();
                var e = new Event("keydown");
                e.keyCode=39;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }               
            function down() {
                resetArrows();
                var e = new Event("keydown");
                e.keyCode=40;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
            }
            function resetArrows() {
                //pulsante pause
                var e = new Event("keyup");
                e.keyCode=36;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
                
                
                var e = new Event("keyup");
                e.keyCode=37;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
                
                e = new Event("keyup");
                e.keyCode=38;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
                
                var e = new Event("keyup");
                e.keyCode=39;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);
                
                e = new Event("keyup");
                e.keyCode=40;    // just enter the code you want to send 
                //e.keyCode=e.key.charCodeAt(0);
                e.which=e.keyCode;
                e.altKey=false;
                e.ctrlKey=true;
                e.shiftKey=false;
                e.metaKey=false;
                e.bubbles=true;
                window.dispatchEvent(e);                
            }               
            
        </script>
        
    </head>
    <body>
        <div class="container-fluid shorten">
            <div class="navbar clearfix navbar-fixed-top " style="background-color:white">                
                <div class="well well-sm">
                    <div class="row">
                        <div class="col-sm-2">
                            <h4 class="pull-right" style="color:red" id="timer"></h4>
                        </div>
                        <div class="col-sm-22">
                            <!--input type="button" class="btn btn-lg btn-success pull-right" value="Esercizio concluso"
                            <button id="elenco" class="btn btn-lg btn-primary" type="button" data-toggle="modal" data-target="#myModal" data-whatever="@showInfo"><span class="glyphicon glyphicon-info-sign"></span> Cose da fare</button>-->
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

            <div class="starter-template">
                <div class="col-md-16">
                    <div id="snackbar"></div>
                    <!--canvas id="game" height="680" width="800"></canvas-->
                    <canvas id="game"  height="600" width= "800" style="height: 570px;"> </canvas>
                </div>               
                
                <div class="col-md-8 box" style="height: 570px;">
                    <div>
                        <h2>Regole dello zoosafari</h2> <!-- <img src="resources/images/locali/map.png" width="50" />-->
                    </div>
                
                    <p style="text-align:left">Immagina di andare a visitare uno zoosafari. 
                        Il tuo compito è di pianificare un percorso per visitare i seguenti posti (non necessariamente in quest'ordine)</p>
                    <div id="ids" style="text-align:left"></div>
                    <p style="text-align:left"> 
                        Devi terminare la tua visita in <b> <%= inFinalPlace %> </b><br /><br />
                    Quando pianifichi il tuo percorso devi rispettare queste regole: </p>
                    <ul style="text-align:left">
                        <li>puoi passare per <b>i sentieri ombreggiati tutte le volte che vuoi</b>
                        <li>puoi attraversare <b>una sola volta i sentieri marroni</b></li>
                        <li>puoi navigare o guadare <b>il fiume una sola volta</b></li>                        
                    </ul>
                   <p style="text-align:left">Puoi far patire il tempo con il tasto qui a fianco
                       
                          <button type="button" class="btn btn-success" onclick="start()" id=xstart>
                                        <span class="glyphicon glyphicon-play"></span>
                          </button></p>
                    <p style="text-align:left">Puoi muoverti cliccando sulle frecce oppure usando i tasti cursore della tastiera (se presenti).
                    Se utilizzi le frecce qui sotto ricorda di cliccare sulla pausa (<span class="glyphicon glyphicon-pause"></span>) prima di cambiare direzione.          
                    <div class="row">
                        <div class="col-sm-8"></div>
                        <div class="col-sm-8">
                            <div class="row">
                                <div class="col-sm-8"></div>
                                <div class="col-sm-8" align="center">
                                    <button type="button" class="btn btn-success" onclick="up()">
                                        <span class="glyphicon glyphicon-arrow-up"></span>
                                    </button>
                                </div>
                                <div class="col-sm-8"></div>
                            </div>
                            
                            
                            <div class="row">
                                <div class="col-sm-8" align="center">
                                    <button type="button" class="btn btn-success" onclick="left()">
                                        <span class="glyphicon glyphicon-arrow-left"></span>
                                    </button>
                                </div>
                                <!--pause-->
                                <div class="col-sm-8" align="center">
                                    <button type="button" class="btn btn-success" onclick="pause()">
                                        <span class="glyphicon glyphicon-pause"></span>
                                    </button>
                                </div>
                                <div class="col-sm-8" align="center">
                                    <button type="button" class="btn btn-success" onclick="right()">
                                        <span class="glyphicon glyphicon-arrow-right"></span>
                                    </button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-8"></div>
                                <div class="col-sm-8" align="center">
                                    <button type="button" class="btn btn-success" onclick="down()">
                                        <span class="glyphicon glyphicon-arrow-down"></span>
                                    </button>
                                </div>
                                <div class="col-sm-8"></div>
                            </div>
                        </div>
                    </div>  

            </div>
            </div>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
              </div>
              <div class="modal-body">
                ...
              </div>
              <div class="modal-footer">
              </div>
            </div>
          </div>
        </div>
    </body>
</html>
