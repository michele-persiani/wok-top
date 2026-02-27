<%-- 
    Document   : clinicalprofile-form
    Created on : Feb 18, 2016, 09:52:34 AM
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

    <!--title>Profilo clinico paziente</title-->
    <title>Profilo clinico </title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="resources/css/bootstrap.min.css">
    
    <!-- <link rel="stylesheet" href="resources/css/daterangepicker.css">-->
    <link rel="stylesheet" href="resources/css/bootstrap-datepicker3.min.css">

    <!-- font-awesome -->
    <link rel="stylesheet" href="resources/css/font-awesome.min.css">

    <!-- jQuery library -->
    <script type="text/javascript" src="resources/assets/js/vendor/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
    
    <script src="resources/js/moment.min.js"></script>
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
    
    <script type="text/javascript" src="resources/js/bootstrap-filestyle.min.js"> </script>

    <!-- Latest compiled bootbox -->
    <script type="text/javascript" src="resources/js/bootbox.min.js"></script>
    
    <script src="resources/js/bootstrap-datepicker.it.js"></script>
    <style>
        input[readonly]{
                background-color: #fff !important;
            }
    </style>
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
                <h2>Profilo clinico di ${patientname}</h2>

                <spring:url value="/saveorupdateclinicalprofile" var="userActionUrl" />
                <spring:url value="/exportclinicalprofiles" var="userActionUrl1" />
                <spring:url value="/files/mrreports/" var="reportsPath" />
                

                <form:form class="form-horizontal"  name="clinicalProfile" method="post" modelAttribute="clinicalprofileForm" action="${userActionUrl}" enctype="multipart/form-data">

                    <form:hidden path="id" />

                    <form:hidden path="userid" />

                    <form:hidden path="timestamp" />

                    <spring:bind path="diagnosysyear">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-6 control-label">Anno diagnosi SM</label>
                            <div class="col-sm-18">
                                <form:input path="diagnosysyear" type="text" class="form-control " id="diagnosysyear" placeholder="* Anno diagnosi" />
                                <form:errors path="diagnosysyear" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>

                    <spring:bind path="mstype">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-6 control-label">Tipo SM</label>
                            <div class="col-sm-18">
                                <form:select path="mstype" class="form-control">
                                    <form:option value="RRMS"> RRMS</form:option>
                                    <form:option value="SPMS"> SPMS</form:option>
                                    <form:option value="PPMS"> PPMS</form:option>
                                    <form:option value="PRMS"> PRMS</form:option>
                                </form:select>
                            </div>
                        </div>                            
                    </spring:bind>
                    
                    <h4 align="left">Terapia farmacologica attuale</h4>
                    <div class="row">
                        <spring:bind path="dmt">
                            <div class="form-group col-sm-15 ${status.error ? 'has-error' : ''}">
                                <label class="col-sm-8 control-label">Terapia disease modifying</label>
                                <div class="col-sm-16">
                                    <form:input path="dmt" type="text" class="form-control" id="dmt" placeholder="* Terapia disease modifying" />
                                    <form:errors path="dmt" class="control-label" />
                                </div>
                            </div>
                        </spring:bind>

                        <spring:bind path="dmtdate">
                            <div class="form-group col-sm-9 ${status.error ? 'has-error' : ''}">
                                <fmt:formatDate value="${clinicalprofileForm.dmtdate}" pattern="dd/MM/yyyy" var="fdmtdate" />
                                <label class="col-sm-5 control-label">Data inizio</label>
                                <div class="col-sm-19">
                                    <form:input path="dmtdate" type="text" class="form-control dmtdate" id="stdate" readonly="true" 
                                                value="${fdmtdate}"
                                    />
                                    <form:errors path="dmtdate" class="control-label" />
                                </div>
                            </div>
                        </spring:bind>
                                                
                    </div>

                    <div class="row">
                        <spring:bind path="st">
                            <div class="form-group col-sm-15 ${status.error ? 'has-error' : ''}">
                                <label class="col-sm-8 control-label">Terapia sintomatica per SM</label>
                                <div class="col-sm-16">
                                    <form:input path="st" type="text" class="form-control" id="st" placeholder="* Terapia sintomatica per SM" />
                                    <form:errors path="st" class="control-label" />
                                </div>
                            </div>
                        </spring:bind>

                        <spring:bind path="stdate">
                            <div class="form-group col-sm-9 ${status.error ? 'has-error' : ''}">
                                <fmt:formatDate value="${clinicalprofileForm.stdate}" pattern="dd/MM/yyyy" var="fstdate" />
                                <label class="col-sm-5 control-label">Data inizio</label>
                                <div class="col-sm-19">
                                    <form:input path="stdate" type="text" class="form-control stdate" id="stdate" readonly="true" 
                                                value="${fstdate}"
                                    />
                                    <form:errors path="stdate" class="control-label" />
                                </div>
                            </div>
                        </spring:bind>
                    </div>

                    <h4 align="left">Profilo funzionale neurologico</h4>
                    <div class="form-group">
                        <div class="col-sm-4"><form:checkbox path="motsys" value="true"/>Sistema motorio</div>
                        <div class="col-sm-4"><form:checkbox path="sensys" class="checkbox-inline" value="true"/>Sistema sensoriale</div>
                        <div class="col-sm-4"><form:checkbox path="vissys" class="checkbox-inline" value="true"/>Sistema visivo</div>
                        <div class="col-sm-4"><form:checkbox path="sphsys" class="checkbox-inline" value="true"/>Sistema sfinterico</div>
                        <div class="col-sm-4"><form:checkbox path="cersys" class="checkbox-inline" value="true"/>Sistema cerebellare</div>
                        <div class="col-sm-4"><form:checkbox path="brasys" class="checkbox-inline" value="true"/>Sistema tronco-encefalico</div>
                    </div>
                    
                    <h4 align="left">EDSS</h4>
                    <div class="row">
                        <spring:bind path="edssval">
                            <label class="col-sm-4 control-label">EDSS</label>
                            <div class="col-sm-6">
                                <form:select path="edssval" class="form-control">
                                    <form:option value="0"> 0</form:option>
                                    <form:option value="0.5"> 0.5</form:option>
                                    <form:option value="1"> 1</form:option>
                                    <form:option value="1.5"> 1.5</form:option>
                                    <form:option value="2"> 2</form:option>
                                    <form:option value="2.5"> 2.5</form:option>
                                    <form:option value="3"> 3</form:option>
                                    <form:option value="3.5"> 3.5</form:option>
                                    <form:option value="4"> 4</form:option>
                                    <form:option value="4.5"> 4.5</form:option>
                                    <form:option value="5"> 5</form:option>
                                    <form:option value="5.5"> 5.5</form:option>
                                    <form:option value="6"> 6</form:option>
                                    <form:option value="6.5"> 6.5</form:option>
                                    <form:option value="7"> 7</form:option>
                                    <form:option value="7.5"> 7.5</form:option>
                                    <form:option value="8"> 8</form:option>
                                    <form:option value="8.5"> 8.5</form:option>
                                    <form:option value="9"> 9</form:option>
                                    <form:option value="9.5"> 9.5</form:option>
                                    <form:option value="10"> 10</form:option>
                                </form:select>
                            </div>
                        </spring:bind>

                            <button type="button" id="edssbutton" class="col-sm-9 btn btn-link left-align" onclick="calcEDSS()">Calcola</button>
                            
                    </div>                            

                    <h4 align="left">Risonanze</h4>
                    <div class="row">
                        <label class="col-sm-5 control-label">Referti precedenti</label>
                        <div class="col-sm-12">
                            <select id="mr-select" class="form-control">
                                <option value=""></option>
                                <c:forEach var="mr" items="${mrlist}">
                                    <option value="${mr.report}">${mr.report}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="button" id="repbutton" disabled class="col-sm-7 btn btn-link left-align" onclick="showReport(document.getElementById('mr-select').value)">
                            Visualizza
                        </button>
                                
                    </div>
                    <label class="right-align">Nuovo referto</label>
                    <div class="row">
                        <div class="form-group col-sm-12">
                            <label class="col-sm-8 control-label">Data</label>
                            <div class="col-sm-8">
                                <input type="text" name="mrlastdate" class="mrlastdate"
                                       placeholder="Scegli una data" value=""
                                       id="mrlastdate"/>
                            </div>
                            <div class="col-sm-8"></div>
                        </div>
                        
                        <div class="col-sm-12">
                            <input type="file" id="repfile" name="mrlastreportfile" class="filestyle"
                                   data-btnClass="btn-warning"
                                   data-disabled="false"
                                   data-input="true"
                                   data-buttonText="Carica"
                                   data-size="nr"
                                   data-placeholder="Nessun referto"
                                   data-buttonBefore="true"
                                   data-iconName="glyphicon glyphicon-upload"
                                   />
                        </div>
                    </div>                    
                    
                    <h4 align="left">Note</h4>
                    <spring:bind path="notes">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <div class="col-sm-24">
                                <form:textarea path="notes" class="form-control" rows="10" id="notes" placeholder="Note" />
                                <form:errors path="notes" class="control-label" />
                            </div>
                        </div>
                    </spring:bind>
                    <div class="form-group">
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
                                                                    document.clinicalProfile.submit();
                                                                }
                                                            }
                                                        });">Salva</button>
                        </div>
                    </div>                    
                </form:form>
            </div>
                    
            <!--button type="button" class="btn-lg"
                    onclick="
                        post('exportclinicalprofiles');">
                Esporta tutti i profili
            </button-->
                    
            <p></p>
            
            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <jsp:include page="modal-cprof-form.jsp"/>
        
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="resources/assets/js/ie10-viewport-bug-workaround.js"></script>
        
        <script>
            history.pushState(null, null, document.URL);
            window.addEventListener('popstate', function () {
                history.pushState(null, null, document.URL);
            });
        </script>
        
        <script>
            $("#mr-select").change(function() {
                if (this.value === "") {
                    $("#repbutton").prop("disabled", true);
                }
                else {
                    $("#repbutton").prop("disabled", false);
                }
            });            
            
        </script>
        
        <script>
            $(document).ready(function() {
                var tday = moment();
                
                $('.dmtdate').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                if($(".dmtdate").val()==""){
                    $('.dmtdate').datepicker("update",tday.format('DD/MM/YYYY'));
                }else{
                    //var g=moment($(".dmtdate").val());
                    //$('.dmtdate').datepicker("update",g.format('DD/MM/YYYY'));
                }
                
                $('.stdate').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                if($(".stdate").val()==""){
                    $('.stdate').datepicker("update",tday.format('DD/MM/YYYY'));
                }
                
                $('.mrlastdate').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                if($(".mrlastdate").val() =="") {
                    $('.mrlastdate').datepicker("update",tday.format('DD/MM/YYYY'));
                }
                $(".mrlastdate").datepicker().on('changeDate', function(e) {
                    $("#repfile").prop("disabled", false)
                    //$(":file").prop('disabled', false);
                });    
                

            });
        </script>
        
        <script>
            function showReport(rep) {
                var data = "${reportsPath}"+rep;
                var message = '<object id="repfile" type="application/pdf" width="100%" height="400" data="'+data+'"></object>';
                bootbox.alert(message);
            }

            function calcEDSS() {
                //var message = '<iframe width="100%" height="400" src="http://aedss.cs.unibo.it:8080/compute_eng.html"></iframe>';
                //bootbox.alert(message);
                window.open("http://aedss.cs.unibo.it:8080/compute_eng.html",'_blank');
            }
            
        </script>
        
        <script>
            $(document).ready(function(){
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
                
    </body>
</html>