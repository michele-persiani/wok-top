<%-- 
    Document   : patient-management
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

        <title>Gestione pazienti</title>

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

        <!-- Latest compiled bootbox -->
        <script type="text/javascript" src="resources/js/bootbox.min.js"></script>

    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="adminhome">                                
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

                <p class="lead"><strong>Gestione pazienti</strong></p>

                <c:if test="${!empty message}">
                    <div class="well">
                        <p class="lead"><strong>Attenzione!</strong></p>
                        <p class="lead alert alert-danger"><strong>${message}</strong></p>
                    </div>
                </c:if>

                <div>
                    <p><a class="btn btn-lg btn-success" onclick="post('patientform')" role="button">Nuovo  <!--paziente-->partecipante</a></p>
                </div>
                <!--div class="col-lg-4">
                    <p><form>
                        <input type="text" class="form-control" placeholder="Cerca partecipante...">
                    </form>
                </div-->

                <spring:url value="/files/photos/" var="photosPath" />

                <c:if test="${empty message}">

                    <div class="table-responsive">
                        <table class="table table-striped table-condensed">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th class="text-center">Id</th> 
                                    <th class="text-center">Username</th> 
                                    <th class="text-center">Password</th>
                                    <th class="text-center">Foto</th>
                                    <th class="text-center">Cognome</th>
                                    <th class="text-center">Nome</th>
                                    <th class="text-center">Email</th>
                                    <th class="text-center">Tel</th>
                                    <th class="text-left">|</th>
                                    <th class="text-center">Profilo</th>
                                    <th class="text-right">|</th>
                                    <th class="text-center">Gruppo</th>
                                </tr>
                            </thead>
                            <c:forEach var="patient" items="${patients}" varStatus="status">
                                <tr>
                                    <td>
                                        <button class="btn btn-primary"
                                                onclick="post('patientform',
                                                                {patientid: '${patient.id}'})">
                                            <span class="glyphicon glyphicon-edit"></span>
                                        </button>
                                    </td>
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
                                                            message: '<h4>Vuoi veramente cancellare il <!--paziente-->partecipante?</h4>',
                                                            callback: function (result) {
                                                                if (result === true) {
                                                                    this.disabled = true;
                                                                    post('deletepatient',
                                                                            {patientid: '${patient.id}'})
                                                                }
                                                            }
                                                        });">

                                            <span class="glyphicon glyphicon-remove"></span></button>

                                    </td>

                                    <td>${patient.id}</td> 
                                    <td>${patient.username}</td> 
                                    <td>${patient.password}</td>                                

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
                                    <td>
                                        |<a class="btn-lg btn-link" href="personaldataform?patientid=${patient.id}" role="button">Anag</a>
                                    </td>
                                    <td>
                                        <a class="btn-lg btn-link" href="clinicalprofileform?patientid=${patient.id}" role="button">Clin</a>
                                    </td>
                                    <td class="text-right">
                                        <a class="btn-lg btn-link" href="npsprofileform?patientid=${patient.id}" role="button">NPS</a>|
                                    </td>
                                    <td class="text-left">
                                        <c:if test="${!empty groups[status.index].name}">
                                            ${groups[status.index].name}
                                            <a class="btn-lg btn-link" href="patientgroup?groupid=${patient.gid}&back=showpatients&home=adminhome">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </a>
                                        </c:if>
                                        <c:if test="${empty groups[status.index].name}">
                                            ${groups[status.index].name}
                                            <a class="btn-lg btn-link" href="patientgroupform?patientid=${patient.id}&back=showpatients&home=adminhome">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </a
                                        </c:if>                                        
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                    <button type="button" class="btn-default btn-lg"
                            onclick="post('exportpatients',{email: '${email}'});">
                        Esporta pazienti
                    </button>
                    <button type="button" class="btn-default btn-lg"
                            onclick="post('exportpersonalprofiles',{email: '${email}'});">
                        Esporta profili anagrafici 
                    </button>
                    <button type="button" class="btn-default btn-lg"
                            onclick="post('exportclinicalprofiles',{email: '${email}'});">
                        Esporta profili clinici
                    </button>
                    <button type="button" class="btn-default btn-lg"
                            onclick="post('exportnpsprofiles',{email: '${email}'});">
                        Esporta profili NPS
                    </button>
                    <button type="button" class="btn-default btn-lg"
                            onclick="post('exportpatienthistory',{email: '${email}'});">
                        Esporta storia <!--pazienti-->partecipanti
                    </button>
                        
                    <h4> Riceverai un email con i dati esportati</h4>
                </c:if>
            </div>


            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div>

        <jsp:include page="modal-photo.jsp"/>
        <jsp:include page="modal-pat-man.jsp"/>                
        <jsp:include page="modal-no-anag.jsp"/>                

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>

        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>

        <script>
            // using latest bootstrap so, show.bs.modal
            $('#photoModal').on('show.bs.modal', function (e) {
                var photo = $(e.relatedTarget).data('photo');
                var patient = $(e.relatedTarget).data('patient');
                document.getElementById("photo").src = photo;
                document.getElementById("patient").innerHTML = patient;
            });
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
            
            $(document).ready(function(){
                
            <c:forEach var="group" items="${allgroups}">
                    
                var x = document.createElement("OPTION");
                var id = ${npstest.id}            
                x.setAttribute("value", id);
                var txt = new Date(${npstest.timestamp}).toLocaleString();
                var t = document.createTextNode(txt);
                x.appendChild(t);
                document.getElementById("npstest-select").appendChild(x);
                    
                    
                    
                var x = document.createElement("OPTION");
                var id = ${group.id}                        
                x.setAttribute("value", id);
                var txt = ${group.name};
                var t = document.createTextNode(txt);
                x.appendChild(t);
                document.getElementById("group-select").appendChild(x);                
            </c:forEach>

            });      
            
        </script>

    </body>
</html>
