<%-- 
    Document   : whitetestcomp-table
    Created on : Sep 29, 20167, 04:38:34 PM
    Author     : floriano
--%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html lang="it">
    <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="resources/favicon.ico">

    <title>Confronta White test <!--paziente-->partecipante</title>

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
    <link rel="stylesheet" href="resources/jumbotron-narrow.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="resources/js/hello.js"></script>
    <!-- Latest compiled bootbox -->
    <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="whitetestsel?patientid=${patientid}">                                
                                <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="adminhome">                                
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
            
            <div class="jumbotron">
                <h2>Confronto White test di ${patientname}</h2>

                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <thead>
                            <tr>
                                <th class="col-sm-10"></th>
                                <th class="col-sm-7" id="date1"></th>
                                <th class="col-sm-7" id="date2"></th>
                            </tr>
                        </thead>
                        <tr>
                            <td class="col-sm-10"><b>Esaminatore</b></td>
                            <td class="col-sm-7">${test1.examinator}</td>
                            <td class="col-sm-7">${test2.examinator}</td>
                        </tr>
                    </table>
                </div>
                        
                <h3>Linguaggio</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <tr>
                            <td class="col-sm-10"><b>Parlando ho difficolt&agrave; a comunicare le cose</b></td>
                            <td class="col-sm-7">${test1.l01}</td>
                            <td class="col-sm-7">${test2.l01}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Posso eseguire una conversazione al telefono</b></td>
                            <td class="col-sm-7">${test1.l02}</td>
                            <td class="col-sm-7">${test2.l02}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Mi devo fermare per trovare le parole per esprimere i miei pensieri</b></td>
                            <td class="col-sm-7">${test1.l03}</td>
                            <td class="col-sm-7">${test2.l03}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Il mio linguaggio &egrave; lento ed esitante</b></td>
                            <td class="col-sm-7">${test1.l04}</td>
                            <td class="col-sm-7">${test2.l04}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Mi capita di dare un nome sbagliato ad un oggetto familiare</b></td>
                            <td class="col-sm-7">${test1.l05}</td>
                            <td class="col-sm-7">${test2.l05}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Trovo facile capire quello che la gente mi dice</b></td>
                            <td class="col-sm-7">${test1.l06}</td>
                            <td class="col-sm-7">${test2.l06}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Mi sembra che la gente parli troppo rapidamente</b></td>
                            <td class="col-sm-7">${test1.l07}</td>
                            <td class="col-sm-7">${test2.l07}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Mi &egrave; facile leggere e seguire l'articolo di un giornale</b></td>
                            <td class="col-sm-7">${test1.l08}</td>
                            <td class="col-sm-7">${test2.l08}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Devo udire o leggere qualcosa varie volte prima di potermene ricordare senza difficolt&agrave;</b></td>
                            <td class="col-sm-7">${test1.l09}</td>
                            <td class="col-sm-7">${test2.l09}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Posso richiamare i nomi di persone che erano famose quando ero piccolo</b></td>
                            <td class="col-sm-7">${test1.l10}</td>
                            <td class="col-sm-7">${test2.l10}</td>
                        </tr>
                    </table>
                </div>
                        
                <h3>Memoria visuo-spaziale</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <tr>
                            <td class="col-sm-10"><b>Dopo che ho messo qualcosa da parte sono in grado di ricordare il posto</b></td>
                            <td class="col-sm-7">${test1.mvs01}</td>
                            <td class="col-sm-7">${test2.mvs01}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Se vado per la prima volta in un ristorante, riesco facilmente a tornare al mio posto dopo essermi alzato</b></td>
                            <td class="col-sm-7">${test1.mvs02}</td>
                            <td class="col-sm-7">${test2.mvs02}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Ho difficolt&agrave; a trovare un negozio in un centro commerciale dove sono gi&agrave; stato altre volte</b></td>
                            <td class="col-sm-7">${test1.mvs03}</td>
                            <td class="col-sm-7">${test2.mvs03}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Posso localizzare facilmente un oggetto che so trovarsi nel mio armadio</b></td>
                            <td class="col-sm-7">${test1.mvs04}</td>
                            <td class="col-sm-7">${test2.mvs04}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Trovo difficile ricordarmi le facce di persone che ho incontrato recentemente</b></td>
                            <td class="col-sm-7">${test1.mvs05}</td>
                            <td class="col-sm-7">${test2.mvs05}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Dopo la visita ad un posto sono in grado di ritrovarlo con scarsa difficolt&agrave; (ristorante, negozio)</b></td>
                            <td class="col-sm-7">${test1.mvs06}</td>
                            <td class="col-sm-7">${test2.mvs06}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Ricordo le fotografie che accompagnano i settimanali o i quotidiani che ho letto recentemente</b></td>
                            <td class="col-sm-7">${test1.mvs07}</td>
                            <td class="col-sm-7">${test2.mvs07}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Posso facilmente trovare il mio cappotto in mezzo ad altri in un guardaroba</b></td>
                            <td class="col-sm-7">${test1.mvs08}</td>
                            <td class="col-sm-7">${test2.mvs08}</td>
                        </tr>
                    </table>                        
                </div>
                        
                <h3>Attenzione-concentrazione</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <tr>
                            <td class="col-sm-10"><b>Sono in grado di fare calcoli semplici</b></td>
                            <td class="col-sm-7">${test1.ac01}</td>
                            <td class="col-sm-7">${test2.ac01}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Chiedo spesso alle persone di ripetere quanto hanno detto perch&egrave; durante la conversazione la mia testa vaga</b></td>
                            <td class="col-sm-7">${test1.ac02}</td>
                            <td class="col-sm-7">${test2.ac02}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Sto attento a quanto mi accade intorno</b></td>
                            <td class="col-sm-7">${test1.ac03}</td>
                            <td class="col-sm-7">${test2.ac03}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Ho difficolt&agrave; a stare fermo seduto per guardare i miei programmi televisivi preferiti</b></td>
                            <td class="col-sm-7">${test1.ac04}</td>
                            <td class="col-sm-7">${test2.ac04}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Vengo facilmente distratto nel mio lavoro dalle cose che stanno intorno a me</b></td>
                            <td class="col-sm-7">${test1.ac05}</td>
                            <td class="col-sm-7">${test2.ac05}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Riesco a tenere a mente pi&ugrave; cose contemporaneamente</b></td>
                            <td class="col-sm-7">${test1.ac06}</td>
                            <td class="col-sm-7">${test2.ac06}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Riesco a focalizzare la mia attenzione su una prova per solo pochi minuti alla volta</b></td>
                            <td class="col-sm-7">${test1.ac07}</td>
                            <td class="col-sm-7">${test2.ac07}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Ho difficolt&agrave; a riprendere il filo dei miei pensieri anche dopo una breve interruzione</b></td>
                            <td class="col-sm-7">${test1.ac08}</td>
                            <td class="col-sm-7">${test2.ac08}</td>
                        </tr>
                    </table>
                </div>
                        
                <h3>Abilit&agrave; visuo-spaziali</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <tr>
                            <td class="col-sm-10"><b>Sono in grado di mettere insieme i pezzi di un puzzle</b></td>
                            <td class="col-sm-7">${test1.avs01}</td>
                            <td class="col-sm-7">${test2.avs01}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Sono capace di usare le istruzioni per costruire oggetti da solo (bricolage)</b></td>
                            <td class="col-sm-7">${test1.avs02}</td>
                            <td class="col-sm-7">${test2.avs02}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Ho difficolt&agrave; a focalizzare un amico in mezzo ad una folla</b></td>
                            <td class="col-sm-7">${test1.avs03}</td>
                            <td class="col-sm-7">${test2.avs03}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Trovo difficolt&agrave; a stimare le distanze (ad esempio, tra casa mia e quella di un parente)</b></td>
                            <td class="col-sm-7">${test1.avs04}</td>
                            <td class="col-sm-7">${test2.avs04}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Mi perdo quando viaggio</b></td>
                            <td class="col-sm-7">${test1.avs05}</td>
                            <td class="col-sm-7">${test2.avs05}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>&Egrave; facile per me leggere una carta geografica e trovare una nuova localit&agrave;</b></td>
                            <td class="col-sm-7">${test1.avs06}</td>
                            <td class="col-sm-7">${test2.avs06}</td>
                        </tr>
                    </table>
                </div>

                <h3>Memoria verbale</h3>
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <tr>
                            <td class="col-sm-10"><b>Dimentico di citare argomenti importanti durante una conversazione</b></td>
                            <td class="col-sm-7">${test1.mv01}</td>
                            <td class="col-sm-7">${test2.mv01}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Dimentico cose importanti che avevo detto solo pochi giorni prima</b></td>
                            <td class="col-sm-7">${test1.mv02}</td>
                            <td class="col-sm-7">${test2.mv02}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Sono in grado di ricordare dopo parecchie ore ci&ograve; che &egrave; stato detto al notiziario</b></td>
                            <td class="col-sm-7">${test1.mv03}</td>
                            <td class="col-sm-7">${test2.mv03}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Dimentico avvenimenti importanti avvenuti oltre il mese scorso</b></td>
                            <td class="col-sm-7">${test1.mv04}</td>
                            <td class="col-sm-7">${test2.mv04}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Dimentico parti importanti di chiacchiere (pettegolezzi) interessanti che ho sentito</b></td>
                            <td class="col-sm-7">${test1.mv05}</td>
                            <td class="col-sm-7">${test2.mv05}</td>
                        </tr>
                        <tr>
                            <td class="col-sm-10"><b>Dimentico di dare messaggi telefonici</b></td>
                            <td class="col-sm-7">${test1.mv06}</td>
                            <td class="col-sm-7">${test2.mv06}</td>
                        </tr>
                    </table>
                </div>                        
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <jsp:include page="modal-whitetestcomp-form.jsp"/>
        
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
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
            
            $(document).ready(function(){
                var date1 = new Date(${test1.timestamp}).toLocaleString();
                var date2 = new Date(${test2.timestamp}).toLocaleString();
                document.getElementById("date1").innerHTML = date1;
                document.getElementById("date2").innerHTML = date2;
            });            
        </script>

    </body>
</html>