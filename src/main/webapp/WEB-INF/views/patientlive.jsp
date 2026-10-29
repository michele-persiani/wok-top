<%-- 
    Document   : patientlive
    Created on : 28-nov-2016, 10.48.00
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
        <meta name="author" content="Michele Persiani">
        <link rel="icon" href="resources/favicon.ico">

        <title>Sessione Live</title>

        <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">

        <!-- font-awesome -->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">

        <!-- jQuery library -->
        <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

        <link href="https://unpkg.com/tabulator-tables@6.5.0/dist/css/tabulator.min.css" rel="stylesheet">
        <script type="text/javascript" src="https://unpkg.com/tabulator-tables@6.5.0/dist/js/tabulator.min.js"></script>
        

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link rel="stylesheet" href="resources/assets/css/ie10-viewport-bug-workaround.css">

        <!-- Custom styles for this template -->
        <link rel="stylesheet" href="resources/jumbotron-narrow.css">
        
        <style>
            .container{
                max-width:100%;
            }
            .short{
                width:50px;
            }
            .red{
                color:#C36900;
            }
            .padding-arrow{
                padding-left:20px;
            }
            .infodiff{
                padding-left:30px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="showlivelist">                                
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

            <div class="well">
                <div><h2>Esercizi assegnati a ${patient.name} ${patient.surname}</h2></div>
                <div id="tableDiv"></div>
            </div>

            <script type="text/javascript">
                function performanceFormatter(cell) {
                    let value = cell.getValue();
                    return Math.round(value * 100) + "%";
                }
                function completatoFormatter(cell) {
                    let value = cell.getValue();
                    return value ? "Si" : "No";
                }

                var table = new Tabulator("#tableDiv", {
                    height:false,
                    layout:"fitColumns", //fit columns to width of table (optional)
                    ajaxURL:"patientlive/data?patientid=${patient.id}",
                    columns:[
                        {title:"Nome", field:"exerciseName"},
                        {title:"Performance ultimo tentativo", field:"performance", formatter:performanceFormatter},
                        {title:"Livello raggiunto", field:"level"},
                        {title:"Completato", field:"completed", formatter:completatoFormatter},
                        {title:"Ultimo accesso", field:"lastAccess"},
                    ],
                });
            </script>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div>

        
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
