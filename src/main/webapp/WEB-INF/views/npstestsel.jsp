<%-- 
    Document   : npstestsel
    Created on : Sep 27, 2017, 12:37:34 PM
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

    <title>Profilo neuropsicologico <!--paziente-->partecipante</title>

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
                            <a href="npsprofileform?patientid=${patientid}">                                
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
                <h2>NPS test di ${patientname}</h2>
                
                <div class="row">
                    <h4><label class="col-sm-4 control-label">Valutazioni precedenti</label></h4>
                    <div class="col-sm-10">
                        <select id="npstest-select" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <button type="button" id="oldnpstestbutton" disabled class="col-sm-10 btn-md btn-primary"
                            onclick="showNpstestform()">
                        Visualizza
                    </button>
                </div>
                <div class="row">
                    <div class="col-sm-7"></div>
                    <div class="col-sm-7"></div>
                    <button type="button" id="newnpstestbutton" class="col-sm-10 btn-md btn-success" 
                            onclick="post('npstestform',{patientid: '${patientid}',formid: '' },'get')">
                        Nuova valutazione
                    </button> 
                </div>
                <hr>
                <div class="row">
                    <h4><label class="col-sm-4 control-label">Confronta valutazioni</label></h4>
                    <div class="col-sm-8">
                        <select id="npstest-comp-1" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <div class="col-sm-8">
                        <select id="npstest-comp-2" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <button type="button" id="npstestcompbutton" disabled class="col-sm-4 btn-md btn-success" 
                            onclick="showNpstestcompform()">
                        Ok
                    </button>
                </div>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-npstest.jsp"/>
        

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
                
            <c:forEach var="npstest" items="${npstestlist}">
                var x = document.createElement("OPTION");
                var id = ${npstest.id};            
                x.setAttribute("value", id);
                var txt = new Date(${npstest.timestamp}).toLocaleString();
                var t = document.createTextNode(txt);
                x.appendChild(t);
                document.getElementById("npstest-select").appendChild(x);
                
                var x1 = document.createElement("OPTION");
                var id1 = ${npstest.id};            
                x1.setAttribute("value", id1);
                var txt1 = new Date(${npstest.timestamp}).toLocaleString();
                var t1 = document.createTextNode(txt1);
                x1.appendChild(t1);
                document.getElementById("npstest-comp-1").appendChild(x1);
                
                var x2 = document.createElement("OPTION");
                var id2 = ${npstest.id};            
                x2.setAttribute("value", id2);
                var txt2 = new Date(${npstest.timestamp}).toLocaleString();
                var t2 = document.createTextNode(txt2);
                x2.appendChild(t2);
                document.getElementById("npstest-comp-2").appendChild(x2);
            </c:forEach>

            });      
        </script>

        <script>
            $("#npstest-select").change(function() {
                if (this.value === "") {
                    $("#oldnpstestbutton").prop("disabled", true);
                }
                else {
                    $("#oldnpstestbutton").prop("disabled", false);
                }
            });            
            
            $("#npstest-comp-1").change(function() {
                if (this.value === "") {
                    $("#npstestcompbutton").prop("disabled", true);
                }
                else if (document.getElementById("npstest-comp-2").value !== '') {
                    $("#npstestcompbutton").prop("disabled", false);
                }
            });
                
            $("#npstest-comp-2").change(function() {
                if (this.value === "") {
                    $("#npstestcompbutton").prop("disabled", true);
                }
                else if (document.getElementById("npstest-comp-1").value !== '') {
                    $("#npstestcompbutton").prop("disabled", false);
                }
                
            });            
        </script>
        
        <script>
            function showNpstestform() {
                var data = document.getElementById('npstest-select').value;
                post('npstestform',{patientid: '${patientid}',formid: data},'get');
            }
            
            function showNpstestcompform() {
                var data1 = document.getElementById('npstest-comp-1').value;
                var data2 = document.getElementById('npstest-comp-2').value;
                post('npstestcompform',{patientid: '${patientid}',formid1: data1, formid2: data2},'get');
            }
        </script>
        
    </body>
</html>