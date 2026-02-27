<%-- 
    Document   : pddl-pianificazione2a
    Created on : 18-gen-2018, 14.32.56
    Author     : bartolomeo lombardi
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
        <meta name="author" content="Bartolomeo Lombardi">
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
                        <h2>Visita allo Zoosafari</h2>
                        <article>
                            <p>Immagina di andare a visitare uno zoosafari.<br/>
                                Il tuo compito è di pianificare un percorso per visitare alcuni luoghi (il bar, la toilette) e animali presenti nella mappa.<br/>
                                Puoi spostare il visitatore attraverso le frecce della tastiera o i pulsanti freccia.
                                <br>
                                Se utilizzi i pulsanti con le frecce (<span class="glyphicon glyphicon-arrow-left"></span>, <span class="glyphicon glyphicon-arrow-right"></span>, <span class="glyphicon glyphicon-arrow-up"></span>, <span class="glyphicon glyphicon-arrow-down"></span>) 
                                ricorda di premere il pulsante pausa (<span class="glyphicon glyphicon-pause"></span>) <b>prima</b> di cambiare direzione.
                            </p>
                            <p> Quando pianifichi il tuo percorso devi rispettare delle regole:
                            <ul>                                
                                <li>Puoi passare per <b>i sentieri ombreggiati tutte le volte che vuoi</b></li>
                                <li>Puoi attraversare <b>una sola volta i sentieri marroni</b></li>
                                <li>Puoi navigare o guadare <b>il fiume una sola volta</b></li>
                                <li>Devi finire in un <b>luogo stabilito</b></li>
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
                                <div class="col-md-6">
                                    <ul class="legendlist">
                                        <li><img src ="resources/images/zoo/marrone.jpg" class="bigimg" />sentiero marrone</li>
                                        <li><img src ="resources/images/zoo/ombreggiato.jpg" class="bigimg" />sentiero ombreggiato</li>
                                        <li><img src ="resources/images/zoo/fiume.png" class="bigimg" />fiume</li>
                                        <li><img src ="resources/images/zoo/bar.jpg" class="bigimg" />bar</li>
                                        <li><img src ="resources/images/zoo/toilette.jpg" class="bigimg" />toilette</li>
                                    </ul> 
                                </div>
                                
                                        
                                
                                <div class="col-md-6">
                                    <ul class="legendlist">
                                        <li><img src ="resources/images/zoo/elefante.jpg" class="bigimg" />elefante</li>
                                        <li><img src ="resources/images/zoo/ippopotamo.jpg" class="bigimg" />ippopotamo</li>
                                        <li><img src ="resources/images/zoo/lama.jpg" class="bigimg" />lama</li>
                                        <li><img src ="resources/images/zoo/leone.jpg" class="bigimg" />leone</li>
                                        <li><img src ="resources/images/zoo/orso.jpg" class="bigimg" />orso</li>
                                       
                                    </ul> 
                                </div>
                                <div class="col-md-6">
                                    <ul class="legendlist">
                                         <li><img src ="resources/images/zoo/scimmia.jpg" class="bigimg" />scimmia</li>
                                        <li><img src ="resources/images/zoo/scoiattolo.jpg" class="bigimg" />scoiattolo</li>
                                        <li><img src ="resources/images/zoo/serpente.jpg" class="bigimg" />serpente</li>
                                        <li><img src ="resources/images/zoo/tigre.jpg" class="bigimg" />tigre</li>
                                        <li><img src ="resources/images/zoo/uccello.jpg" class="bigimg" />uccello</li>
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
                           href="pianificazione2phase2?idproblem=${idproblem}&color=${color}&difficulty=${difficulty}&level=${level}&patientid=${patientid}&exerciseid=${exerciseid}&sessid=${sessid}">
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
