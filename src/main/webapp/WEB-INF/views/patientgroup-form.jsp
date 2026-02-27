<%-- 
    Document   : patientgroup-form
    Created on : Nov 8, 2017, 5:07:34 PM
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

        <title>Selezione gruppo per <!--paziente-->partecipante</title>
        
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
        
        <script type="text/javascript" src="resources/js/bootstrap-filestyle.min.js"> </script>
        
        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

    </head>

    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="showpatients">                                
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
                <h2>Aggiungi <!--paziente-->partecipante a gruppo</h2>
                <form class="form-inline" method="post" action="addpatienttogroup">
                        <div class="form-group">
                            <label>Gruppo</label>
                            <select id="grp-list" name="groupid">
                                <option value="Nessuno" selected="">Nessuno</option>
                                <c:forEach var="gr" items="${groups}">
                                    <option value="${gr.id}">${gr.name}</option>                                    
                                </c:forEach>
                            </select>
                        </div>
                    
                    <input hidden type="text" name="patientid" value="${patientid}">
                    <input hidden type="text" name="back" value="${back}">
                    <input hidden type="text" name="home" value="${home}">
                    
                    <div class="form-group">
                        <button id="submit-data"  disabled="" type="submit" class="btn btn-warning">Aggiungi</button>
                    </div>
                </form>

            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
        <jsp:include page="modal-patgrp-form.jsp"/>
        
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
        
        <script>
            $("#grp-list").change(function() {
                if (this.value === "") {
                    $("#submit-data").prop("disabled", true);
                } else {
                    $("#submit-data").prop("disabled", false);
                }
            });
        </script>
        
        
    </body>
</html>