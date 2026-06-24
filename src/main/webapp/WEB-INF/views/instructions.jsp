<%--
    Document   : administrator
    Created on : Feb 16, 2016, 9:49:34 AM
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

    <title>Amministratore</title>


    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="resources/css/bootstrap.min.css">
    <!-- prova -->
    <link rel="text/javascript" href="resources/js/custom.js">


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

    <!--link rel="stylesheet" href="resources/signin.css"-->

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="resources/js/hello.js"></script>
</head>

<body>
<div class="container">
    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-left">
                <li role="presentation">
                    <a data-toggle="modal" href="#infoModal">
                        <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                    </a>
                </li>
            </ul>
            <ul class="nav nav-pills pull-right">
                <li>
                    <a href="patienthome">
                        <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                    </a>
                </li>
                <li>
                    <a href="logout">
                        <span class="glyphicon glyphicon-log-out fa-2x" data-toggle="tooltip" data-placement="bottom" title="Logout"></span>
                    </a>
                </li>
            </ul>
        </nav>
    </div>

    <div id="textComputer">
        <div>
            Per svolgere gli esercizi di questo programma, le verrà richiesto di fornire delle risposte selezionandole con il mouse (click sul tasto sinistro del mouse) oppure premendo dei tasti specifici sulla tastiera.
        </div>
        <div>
            Cerchi di identificarli sulla propria tastiera fin da ora:
        </div>
        <ul>
            <li>Barra spaziatrice o spazio, in basso al centro</li>
            <li>Tasto 'invio' o 'Enter', a destra</li>
        </ul>
    </div>
    <div id="imageComputer">
        <img src="resources/images/mouse.png" alt="Mouse Image" style="width: 40%"/>
        <img src="resources/images/keyboard.png" alt="Keyboard Image" style="width: 100%"/>
    </div>


    <div id="textMobile">
        Per svolgere gli esercizi di questo programma, le verrà richiesto di fornire delle risposte toccando specifici tasti che compariranno sullo schermo, ad esempio:
    </div>
    <div id="imageMobile">
        <img src="resources/images/buttons.jpg" alt="Buttons Image" style="width: 40%"/>
    </div>

    <script type="application/javascript">

        if(isMobile())
        {
            document.getElementById("textComputer").style.visibility = "hidden";
            document.getElementById("imageComputer").style.visibility = "hidden";
        }
        else
        {
            document.getElementById("textMobile").style.visibility = "hidden";
            document.getElementById("imageMobile").style.visibility = "hidden";
        }

    </script>

    <footer class="footer">
        <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
    </footer>

</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

<jsp:include page="modal-admin.jsp" />

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

</body>
</html>