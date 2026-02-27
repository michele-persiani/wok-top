<%-- 
    Document   : raotest-form
    Created on : Feb 26, 2016, 10:18:00 AM
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

        <title>Rao test</title>

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
                <h2>Rao test di ${patientname}</h2>
                                
                <spring:url value="/saveorupdateraotest" var="userActionUrl" />

                <form:form class="form-horizontal" name="raoTest" method="post" modelAttribute="raotestForm" action="${userActionUrl}">

                    <form:hidden path="id" />

                    <form:hidden path="userid" />

                    <form:hidden path="timestamp" />

                    <spring:bind path="examinator">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">Esaminatore</label>
                            <div class="col-sm-20">
                                <form:input path="examinator" type="text" class="form-control " id="examinator" placeholder="* Esaminatore" />
                                <form:errors path="examinator" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>                      

                    <spring:bind path="srtltsraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SRT-LTS</label>
                            <div class="col-sm-20">
                                <form:input path="srtltsraw" type="text" class="form-control " id="srtltsraw" placeholder="* SRT-LTS" />
                                <form:errors path="srtltsraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="srtcltrraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SRT-CLTR</label>
                            <div class="col-sm-20">
                                <form:input path="srtcltrraw" type="text" class="form-control " id="srtcltrraw" placeholder="* SRT-CLTR" />
                                <form:errors path="srtcltrraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="spart1036raw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SPART10/36</label>
                            <div class="col-sm-20">
                                <form:input path="spart1036raw" class="form-control" id="spart1036raw" placeholder="* SPART10/36" />
                                <form:errors path="spart1036raw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="sdmtraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SMDT</label>
                            <div class="col-sm-20">
                                <form:input path="sdmtraw" rows="5" class="form-control" id="sdmtraw" placeholder="* SMDT" />
                                <form:errors path="sdmtraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="pasat3raw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">PASAT 3</label>
                            <div class="col-sm-20">
                                <form:input path="pasat3raw" rows="5" class="form-control" id="pasat3raw" placeholder="* PASAT 3" />
                                <form:errors path="pasat3raw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="pasat2raw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">PASAT 2</label>
                            <div class="col-sm-20">
                                <form:input path="pasat2raw" rows="5" class="form-control" id="pasat2raw" placeholder="* PASAT 2" />
                                <form:errors path="pasat2raw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="srtdraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SRTD</label>
                            <div class="col-sm-20">
                                <form:input path="srtdraw" rows="5" class="form-control" id="srtdraw" placeholder="* SRTD" />
                                <form:errors path="srtdraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="spartdraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">SPART-D</label>
                            <div class="col-sm-20">
                                <form:input path="spartdraw" rows="5" class="form-control" id="spartdraw" placeholder="* SPART-D" />
                                <form:errors path="spartdraw" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <spring:bind path="wlgraw">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-4 control-label">WLG</label>
                            <div class="col-sm-20">
                                <form:input path="wlgraw" rows="5" class="form-control" id="wlgraw" placeholder="* WLG" />
                                <form:errors path="wlgraw" class="control-label" />
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
                                                                    document.raoTest.submit();
                                                                }
                                                            }
                                                        });">Salva</button>
                        </div>
                    </div>                    
                </form:form>
            </div>

            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <jsp:include page="modal-raotest-form.jsp"/>        
        
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