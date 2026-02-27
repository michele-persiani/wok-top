<%-- 
    Document   : patientgroup
    Created on : Dec 20, 2016, 2:09:34 PM
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

        <title>Pazienti per gruppo</title>
        
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
        <link rel="stylesheet" href="resources/jumbotron.css">

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
                            <a href="${back}">                                
                                <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                            </a>
                        </li>
                        <li role="presentation">
                            <a href="${home}">                                
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
                        
            <spring:url value="/addpatienttogroup" var="userActionUrl" />
            
            <div class="jumbotron">
                <p class="lead"><strong>Gruppo ${group.name}</strong></p>                                                          
                                                
                <spring:url value="/files/photos/" var="photosPath" />                
                
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Foto</th>
                                <th>Cognome</th>
                                <th>Nome</th>
                                <th>Email</th>
                                <th>Telefono</th>
                            </tr>
                        </thead>
                        <c:forEach var="patient" items="${patientsingroup}">
                            <tr>
                                <td>                                    
                                    <button class="btn btn-danger"
                                            onclick="
                                                bootbox.confirm({
                                                    size:'small',
                                                    buttons: {
                                                        confirm: {
                                                            label: 'Si',
                                                            className: 'btn-danger'
                                                        },
                                                        cancel: {
                                                            label: 'No',
                                                            className: 'btn-success'
                                                        }
                                                    },
                                                  //  message:'<h4>Vuoi veramente cancellare il paziente dal gruppo?</h4>',
                                                  message:'<h4>Vuoi veramente cancellare il partecipante dal gruppo?</h4>',
                                                        
                                                callback:function(result) {
                                                        if(result===true) {
                                                            this.disabled = true;
                                                            post('deletepatientfromgroup', {
                                                                patientid: '${patient.id}',
                                                                groupid: '${group.id}',
                                                                cid: '${cid}',
                                                                back: '${back}',
                                                                home: '${home}'});
                                                        }
                                                    }
                                                });">

                                        <span class="glyphicon glyphicon-remove" style="font-size: 20px"></span></button>
                                        
                                </td>

                                <td>
                                    <c:if test="${patient.photo!='' && patient.photo!=null}">
                                        <a data-toggle="modal" href="#photoModal" data-patient = "${patient.name} ${patient.surname}" data-photo="${photosPath}${patient.photo}">
                                            <img src="${photosPath}${patient.photo}" class="img-circle img-responsive" width="40" height="40" alt="photo">
                                        </a>
                                    </c:if>
                                    <c:if test="${patient.photo=='' || patient.photo==null}">
                                        <img src="resources/images/photos/default-portrait.png" class="img-circle img-responsive" width="40" height="40" alt="photo">
                                    </c:if>                                     
                                    
                                </td>

                                <td>${patient.surname}</td>
                                <td>${patient.name}</td>
                                <td><a href="mailto:${patient.email}">${patient.email}</a></td>
                                <td>${patient.phone}</td>
                            </tr>
                        </c:forEach>
                    </table>
                    
                <form class="form-inline" method="post" action="${userActionUrl}">
                    
                        <div class="form-group">
                            <label>Paziente</label>
                            <select id="patient-list" name="patientid">
                                <option value="Nessuno" selected="">Nessuno</option>
                                <c:forEach var="patient" items="${patientsoutgroups}">
                                    <option value="${patient.id}">${patient.surname} ${patient.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    
                    <input hidden type="text" name="groupid" value="${group.id}">
                     <input hidden type="text" name="cid" value="${cid}">
                    <input hidden type="text" name="back" value="${back}">
                    <input hidden type="text" name="home" value="${home}">
                    
                    <div class="form-group">
                        <button id="submit-data"  disabled="" type="submit" class="btn btn-warning">Aggiungi</button>
                    </div>
                </form>
                    
                </div>
                
                

            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
        <jsp:include page="modal-photo.jsp"/>
        <jsp:include page="modal-pat-grp.jsp"/>        
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script>
            $("#patient-list").change(function() {
                if (this.value === "") {
                    $("#submit-data").prop("disabled", true);
                } else {
                    $("#submit-data").prop("disabled", false);
                }
            });
        </script>

        <script>
            // using latest bootstrap so, show.bs.modal
            $('#photoModal').on('show.bs.modal', function(e) {
                var photo = $(e.relatedTarget).data('photo');
                var patient = $(e.relatedTarget).data('patient');
                document.getElementById("photo").src=photo;
                document.getElementById("patient").innerHTML=patient;
            });
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>        
        
    </body>
</html>