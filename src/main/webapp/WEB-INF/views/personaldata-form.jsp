<%-- 
    Document   : personaldata-form
    Created on : Mar 6, 2017, 04:25:34 PM
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

    <title>Profilo anagrafico partecipante</title>
    <!--title>Profilo anagrafico paziente<title-->
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
                <h2>Profilo anagrafico di ${patientname}</h2>

                <spring:url value="/saveorupdatepersonaldata" var="userActionUrl" />

                <form:form class="form-horizontal" name="personalData" method="post" modelAttribute="personaldataForm" action="${userActionUrl}">

                    <form:hidden path="id" />

                    <form:hidden path="userid" />

                    <form:hidden path="timestamp" />

                    <spring:bind path="gender">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Genere</label>
                            <div class="col-sm-20">
                                <form:select path="gender" class="form-control">
                                    <form:option value="F">Femmina</form:option>
                                    <form:option value="M">Maschio</form:option>
                                </form:select>
                            </div>
                        </div>
                    </spring:bind>                      

                    <spring:bind path="birthyear">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Anno di nascita</label>
                            <div class="col-sm-20">
                                <form:input path="birthyear" type="text" class="form-control " id="birthyear" placeholder="* Anno di nascita" />
                                <form:errors path="birthyear" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="educationyears">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Scolarit&agrave;</label>
                            <div class="col-sm-20">
                                <form:input path="educationyears" type="text" class="form-control " id="educationyears" placeholder="* Scolarità" />
                                <form:errors path="educationyears" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="job">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Lavoro</label>
                            <div class="col-sm-20">
                                <form:select path="job" class="form-control">
                                    <form:option value="disoccupato">Disoccupata/o</form:option>
                                    <form:option value="salario-minimo">Salario minimo</form:option>
                                    <form:option value="tempo-parziale">Tempo parziale</form:option>
                                    <form:option value="tempo-pieno">Tempo pieno</form:option>
                                    <form:option value="pensionato">Pensionata/o</form:option>
                                    <form:option value="disabile">Pensione invalidit&agrave;</form:option>
                                </form:select>
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="maritalstatus">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Stato civile</label>
                            <div class="col-sm-20">
                                <form:select path="maritalstatus" class="form-control">
                                    <form:option value="libero">Libera/o</form:option>
                                    <form:option value="coniugato">Coniugata/o</form:option>
                                    <form:option value="convivente">Convivente</form:option>
                                    <form:option value="separato-divorziato">Saparata/o o divorziata/o</form:option>
                                    <form:option value="vedovo">Vedova/o</form:option>
                                </form:select>
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="sonnumber">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Figli</label>
                            <div class="col-sm-20">
                                <form:select path="sonnumber" class="form-control">
                                    <form:option value="0">0</form:option>
                                    <form:option value="1">1</form:option>
                                    <form:option value="2">2</form:option>
                                    <form:option value="3">3</form:option>
                                    <form:option value="99">Pi&ugrave; di 3</form:option>
                                </form:select>
                            </div>
                        </div>
                    </spring:bind>
                    <div class="form-group">
                        <div class="col-sm-6">
                        </div>
                        <div class="col-sm-6">
                        </div>
                        <div class="col-sm-6">
                            <button type="reset" class="btn-lg btn-warning pull-right">Annulla</button>
                        </div>
                        <div class="col-sm-6">
                            <button type="button" class="btn-lg btn-success pull-right"
                                                                                    onclick="
                                                        bootbox.confirm({
                                                            size:'small',
                                                            buttons: {
                                                                confirm: {
                                                                    label: 'Si',
                                                                    className: 'btn-success'
                                                                },
                                                                cancel: {
                                                                    label: 'No',
                                                                    className: 'btn-danger'
                                                                }
                                                            },
                                                            message: '<h4>Vuoi salvare i dati inseriti?</h4>',
                                                            callback: function (result) {
                                                                if (result === true) {
                                                                    document.personalData.submit();
                                                                }
                                                            }
                                                        });">Salva</button>
                        </div>
                    </div>                    
                </form:form>
            </div>
                
            <!--button type="button" class="btn-lg"
                    onclick="
                        post('exportpersonalprofiles');">
                Esporta tutti i profili
            </button-->
                    
            <p></p>
                

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-pdata-form.jsp"/>

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
        </script>
        
    </body>
</html>