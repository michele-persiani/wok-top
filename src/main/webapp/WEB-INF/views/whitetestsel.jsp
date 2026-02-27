<%-- 
    Document   : whitetestsel
    Created on : Sep 28, 2017, 6:51:34 PM
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

    <!--title>Profilo neuropsicologico paziente </title-->
  <title>Profilo neuropsicologico partecipante </title>
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
                <h2>White test di ${patientname}</h2>
                
                <div class="row">
                    <h4><label class="col-sm-5 control-label">Valutazioni precedenti</label></h4>
                    <div class="col-sm-11">
                        <select id="whitetest-select" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <button type="button" id="oldwhitetestbutton" disabled class="col-sm-8 btn-md btn-primary"
                            onclick="showWhitetestform()">
                        Visualizza
                    </button>
                </div>
                <div class="row">
                    <div class="col-sm-8"></div>
                    <div class="col-sm-8"></div>
                    <button type="button" id="newwhitetestbutton" class="col-sm-8 btn-md btn-success" 
                            onclick="post('whitetestform',{patientid: '${patientid}',formid: '' },'get')">
                        Nuova valutazione
                    </button> 
                </div>
                <hr>
                <div class="row">
                    <h4><label class="col-sm-4 control-label">Confronta valutazioni</label></h4>
                    <div class="col-sm-8">
                        <select id="whitetest-comp-1" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <div class="col-sm-8">
                        <select id="whitetest-comp-2" class="form-control">
                            <option value=""></option>
                        </select>
                    </div>
                    <button type="button" id="whitetestcompbutton" disabled class="col-sm-4 btn-md btn-success" 
                            onclick="showWhitetestcompform()">
                        Ok
                    </button> 
                </div>                        
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-whitetest.jsp"/>
        

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
                
            <c:forEach var="white" items="${whitetestlist}">
                var x = document.createElement("OPTION");
                var id = ${white.id}                        
                x.setAttribute("value", id);
                var txt = new Date(${white.timestamp}).toLocaleString();
                var t = document.createTextNode(txt);
                x.appendChild(t);
                document.getElementById("whitetest-select").appendChild(x);
                
                var x1 = document.createElement("OPTION");
                var id1 = ${white.id}; 
                x1.setAttribute("value", id1);
                var txt1 = new Date(${white.timestamp}).toLocaleString();
                var t1 = document.createTextNode(txt1);
                x1.appendChild(t1);
                document.getElementById("whitetest-comp-1").appendChild(x1);
                
                var x2 = document.createElement("OPTION");
                var id2 = ${white.id}     
                x2.setAttribute("value", id2);
                var txt2 = new Date(${white.timestamp}).toLocaleString();
                var t2 = document.createTextNode(txt2);
                x2.appendChild(t2);
                document.getElementById("whitetest-comp-2").appendChild(x2);                
            </c:forEach>

            });      
        </script>

        <script>
            $("#whitetest-select").change(function() {
                if (this.value === "") {
                    $("#oldwhitetestbutton").prop("disabled", true);
                }
                else {
                    $("#oldwhitetestbutton").prop("disabled", false);
                }
            });            

            $("#whitetest-comp-1").change(function() {
                if (this.value === "") {
                    $("#whitetestcompbutton").prop("disabled", true);
                }
                else if (document.getElementById("whitetest-comp-2").value !== '') {
                    $("#whitetestcompbutton").prop("disabled", false);
                }
            });
                
            $("#whitetest-comp-2").change(function() {
                if (this.value === "") {
                    $("#whitetestcompbutton").prop("disabled", true);
                }
                else if (document.getElementById("whitetest-comp-1").value !== '') {
                    $("#whitetestcompbutton").prop("disabled", false);
                }
            });            
    
        </script>
        
        <script>
            function showWhitetestform() {
                var data = document.getElementById('whitetest-select').value;
                post('whitetestform',{patientid: '${patientid}',formid: data},'get');
            }

            function showWhitetestcompform() {
                var data1 = document.getElementById('whitetest-comp-1').value;
                var data2 = document.getElementById('whitetest-comp-2').value;
                post('whitetestcompform',{patientid: '${patientid}',formid1: data1, formid2: data2},'get');
            }
        </script>
        
    </body>
</html>