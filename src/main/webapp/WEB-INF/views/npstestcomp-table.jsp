<%-- 
    Document   : npstestcomp-table
    Created on : Sep 29, 2017, 01:42:34 PM
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

    <title>Confronta NPS test <!--paziente-->partecipante</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="resources/css/bootstrap.min.css">
    
    <!-- font-awesome -->
    <link rel="stylesheet" href="resources/css/font-awesome.min.css">

    <!-- jQuery library -->
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
    
    <script src="resources/js/bootstrap-datepicker.min.js"></script>

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
                            <a href="npstestsel?patientid=${patientid}">                                
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
                <h2>Confronto NPS test di ${patientname}</h2>

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
                            <td><b>TMTA (raw)</b></td>
                            <td>${test1.tmtaraw}</td>
                            <td>${test2.tmtaraw}</td>
                        </tr>
                        <tr>
                            <td><b>TMTA (adj)</b></td>
                            <td>${test1.tmtaadj}</td>
                            <td>${test2.tmtaadj}</td>
                        </tr>
                        <tr>
                            <td><b>TMTA (eq)</b></td>
                            <td>${test1.tmtaeq}</td>
                            <td>${test2.tmtaeq}</td>
                        </tr>
                        <tr>
                            <td><b>TMTB (raw)</b></td>
                            <td>${test1.tmtbraw}</td>
                            <td>${test2.tmtbraw}</td>
                        </tr>
                        <tr>
                            <td><b>TMTB (adj)</b></td>
                            <td>${test1.tmtbadj}</td>
                            <td>${test2.tmtbadj}</td>
                        </tr>
                        <tr>
                            <td><b>TMTB (eq)</b></td>
                            <td>${test1.tmtbeq}</td>
                            <td>${test2.tmtbeq}</td>
                        </tr>
                        <tr>
                            <td><b>TMTB-TMTA (raw)</b></td>
                            <td>${test1.tmtbaraw}</td>
                            <td>${test2.tmtbaraw}</td>
                        </tr>
                        <tr>
                            <td><b>TMTB-TMTA (adj)</b></td>
                            <td>${test1.tmtbaadj}</td>
                            <td>${test2.tmtbaadj}</td>
                        </tr>
                        <tr>
                            <td><b>TMTA-TMTA (eq)</b></td>
                            <td>${test1.tmtbaeq}</td>
                            <td>${test2.tmtbaeq}</td>
                        </tr>
                        <tr>
                            <td><b>MFISP</b></td>
                            <td>${test1.mfisp}</td>
                            <td>${test2.mfisp}</td>
                        </tr>
                        <tr>
                            <td><b>MFISC</b></td>
                            <td>${test1.mfisc}</td>
                            <td>${test2.mfisc}</td>
                        </tr>
                        <tr>
                            <td><b>MFISPS</b></td>
                            <td>${test1.mfisps}</td>
                            <td>${test2.mfisps}</td>
                        </tr>
                        <tr>
                            <td><b>MFISTOT</b></td>
                            <td>${test1.mfistot}</td>
                            <td>${test2.mfistot}</td>
                        </tr>
                        <tr>
                            <td><b>STAI-1</b></td>
                            <td>${test1.stai1}</td>
                            <td>${test2.stai1}</td>
                        </tr>
                        <tr>
                            <td><b>STAI-2</b></td>
                            <td>${test1.stai2}</td>
                            <td>${test2.stai2}</td>
                        </tr>
                        <tr>
                            <td><b>BECK</b></td>
                            <td>${test1.beck}</td>
                            <td>${test2.beck}</td>
                        </tr>
                        <tr>
                            <td><b>MSQOL-54</b></td>
                            <td>${test1.msqol54}</td>
                            <td>${test2.msqol54}</td>
                        </tr>
                        <tr>
                            <td><b>DKEFS DESCR</b></td>
                            <td>${test1.dkefsdescr}</td>
                            <td>${test2.dkefsdescr}</td>
                        </tr>
                        <tr>
                            <td><b>DKEFS CAT</b></td>
                            <td>${test1.dkefscat}</td>
                            <td>${test2.dkefscat}</td>
                        </tr>
                        <tr>
                            <td><b>Notes</b></td>
                            <td>${test1.notes}</td>
                            <td>${test2.notes}</td>
                        </tr>
                    </table>
                </div>

            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-npstestcomp-form.jsp"/>        

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