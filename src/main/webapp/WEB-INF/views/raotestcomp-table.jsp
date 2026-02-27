<%-- 
    Document   : raotestcomp-table
    Created on : Sep 28, 2017, 04:35:00 AM
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

        <!--title>Confronta Rao test paziente</title-->
         <title>Confronta Rao test partecipante</title>

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
                            <a href="raotestsel?patientid=${patientid}">                                
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
                <h2>Confronto Rao test di ${patientname}</h2>
                
                <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                        <thead>
                            <tr>
                                <th></th>
                                <th id="date1"></th>
                                <th id="date2"></th>
                            </tr>
                        </thead>
                        <tr>
                            <td><b>Esaminatore</b></td>
                            <td>${test1.examinator}</td>
                            <td>${test2.examinator}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-LTS (raw)</b></td>
                            <td>${test1.srtltsraw}</td>
                            <td>${test2.srtltsraw}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-LTS (adj)</b></td>
                            <td>${test1.srtltsadj}</td>
                            <td>${test2.srtltsadj}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-LTS (eq)</b></td>
                            <td>${test1.srtltseq}</td>
                            <td>${test2.srtltseq}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-CLTR (raw)</b></td>
                            <td>${test1.srtcltrraw}</td>
                            <td>${test2.srtcltrraw}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-CLTR (adj)</b></td>
                            <td>${test1.srtcltradj}</td>
                            <td>${test2.srtcltradj}</td>
                        </tr>
                        <tr>
                            <td><b>SRT-CLTR (eq)</b></td>
                            <td>${test1.srtcltreq}</td>
                            <td>${test2.srtcltreq}</td>
                        </tr>
                        <tr>
                            <td><b>SPART-1036 (raw)</b></td>
                            <td>${test1.spart1036raw}</td>
                            <td>${test2.spart1036raw}</td>
                        </tr>
                        <tr>
                            <td><b>SPART-1036 (adj)</b></td>
                            <td>${test1.spart1036adj}</td>
                            <td>${test2.spart1036adj}</td>
                        </tr>
                        <tr>
                            <td><b>SPART-1036 (eq)</b></td>
                            <td>${test1.spart1036eq}</td>
                            <td>${test2.spart1036eq}</td>
                        </tr>
                        <tr>
                            <td><b>SDMT (raw)</b></td>
                            <td>${test1.sdmtraw}</td>
                            <td>${test2.sdmtraw}</td>
                        </tr>
                        <tr>
                            <td><b>SDMT (adj)</b></td>
                            <td>${test1.sdmtadj}</td>
                            <td>${test2.sdmtadj}</td>
                        </tr>
                        <tr>
                            <td><b>SDMT (eq)</b></td>
                            <td>${test1.sdmteq}</td>
                            <td>${test2.sdmteq}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 3 (raw)</b></td>
                            <td>${test1.pasat3raw}</td>
                            <td>${test2.pasat3raw}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 3 (adj)</b></td>
                            <td>${test1.pasat3adj}</td>
                            <td>${test2.pasat3adj}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 3 (eq)</b></td>
                            <td>${test1.pasat3eq}</td>
                            <td>${test2.pasat3eq}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 2 (raw)</b></td>
                            <td>${test1.pasat2raw}</td>
                            <td>${test2.pasat2raw}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 2 (adj)</b></td>
                            <td>${test1.pasat2adj}</td>
                            <td>${test2.pasat2adj}</td>
                        </tr>
                        <tr>
                            <td><b>PASAT 2 (eq)</b></td>
                            <td>${test1.pasat2eq}</td>
                            <td>${test2.pasat2eq}</td>
                        </tr>
                        <tr>
                            <td><b>SRTD (raw)</b></td>
                            <td>${test1.srtdraw}</td>
                            <td>${test2.srtdraw}</td>
                        </tr>
                        <tr>
                            <td><b>SRTD (adj)</b></td>
                            <td>${test1.srtdadj}</td>
                            <td>${test2.srtdadj}</td>
                        </tr>
                        <tr>
                            <td><b>SRTD (eq)</b></td>
                            <td>${test1.srtdeq}</td>
                            <td>${test2.srtdeq}</td>
                        </tr>
                        <tr>
                            <td><b>SPARTD (raw)</b></td>
                            <td>${test1.spartdraw}</td>
                            <td>${test2.spartdraw}</td>
                        </tr>
                        <tr>
                            <td><b>SPARTD (adj)</b></td>
                            <td>${test1.spartdadj}</td>
                            <td>${test2.spartdadj}</td>
                        </tr>
                        <tr>
                            <td><b>SPARTD (eq)</b></td>
                            <td>${test1.spartdeq}</td>
                            <td>${test2.spartdeq}</td>
                        </tr>
                        <tr>
                            <td><b>WLG (raw)</b></td>
                            <td>${test1.wlgraw}</td>
                            <td>${test2.wlgraw}</td>
                        </tr>
                        <tr>
                            <td><b>WLG (adj)</b></td>
                            <td>${test1.wlgadj}</td>
                            <td>${test2.wlgadj}</td>
                        </tr>
                        <tr>
                            <td><b>WLG (eq)</b></td>
                            <td>${test1.wlgeq}</td>
                            <td>${test2.wlgeq}</td>
                        </tr>
                        
                    </table>
                </div>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <jsp:include page="modal-raotestcomp-form.jsp"/>    
        
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