<%-- 
    Document   : patient
    Created on : Oct 22, 2016, 9:49:34 PM
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

        <title><!--paziente-->partecipante</title>

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
        <!--link rel="stylesheet" href="resources/signin.css"-->

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

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
                            <a href="logout">
                                <span class="glyphicon glyphicon-log-out fa-2x" data-toggle="tooltip" data-placement="bottom" title="Logout"></span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>

            <div class="jumbotron">
                <div class="row">
                    <div class="col-md-4 col-lg-4">
                        <img class="img-circle" src="resources/images/logo.jpg" width="120" height="120" alt="logo">
                    </div>
                    <div class="col-md-4 col-lg-4">
                        <!--h1>Riabilitazione cognitiva</h1-->
                        
                        <h1>Training cognitivo</h1>
                    </div>
                </div>
                <p></p>
                <p class="lead"><h3>Salve <b>${name}</b></h3>
            </div>
            <div class="row">
                <div class="well col-sm-24">
                    <h3>Introduzione sulle funzioni cognitive</h3>
                    <a data-toggle="modal" href="#infoModal1">
                        <span class="glyphicon glyphicon-info-sign fa-2x" data-toggle="tooltip" data-placement="bottom" title="Info"></span>
                    </a>
                </div>
            </div>
            <div class="row">
                <c:if test="${id<99999999}">
                    <div class="well col-sm-12">
                        <div><!--h3>Esercizi di riabilitazione</h3-->
                        <h3>Esercizi di training</h3>
                        </div>
                        <a class="btn btn-lg btn-primary" href="patientrehabilitation" role="button">Vai</a>
                    </div>
                    <div class="well col-sm-12">
                        <div><h3>Esercizi di prova</h3></div>
                        <a class="btn btn-lg btn-primary" href="patienttraining" role="button">Vai</a>
                    </div>
                </c:if>
                <c:if test="${id>=9999999999}">
                    <div class="well col-sm-24">
                        <div><h3>Esercizi demo</h3></div>
                        <a class="btn btn-lg btn-primary" href="patientdemo" role="button">Vai</a>
                    </div>
                </c:if>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->


        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <jsp:include page="modal-pat.jsp" />
        
        <jsp:include page="modal-intro.jsp" />


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