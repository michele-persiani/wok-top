<%-- 
    Document   : patientgraphoverview
    Created on : 17-nov-2016, 10.41.56
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
        <meta name="author" content="DangerBlack">
        <link rel="icon" href="resources/favicon.ico">
        <title>Grafici partecipante</title>
        
         <!-- Bootstrap core CSS -->
        <link rel="stylesheet" href="resources/css/bootstrap.min.css">
        
        <link rel="stylesheet" href="resources/css/daterangepicker.css">
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
        
        <!-- Load c3.css -->
        <link href="resources/css/c3.min.css" rel="stylesheet" type="text/css">

        <!-- Load d3.js and c3.js -->
        
        <script src="resources/js/d3.v4.min.js" charset="utf-8"></script>
        <script src="resources/js/c3.min.js"></script>
        <script src="resources/js/bootstrap-datepicker.it.js"></script>

        <script type="application/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.5.0/chart.umd.js"></script>
        <style>
            .morepadding button{
                padding-left:15px;
            }
            .morepadding h3{
                padding-left:15px;
            }
            #graphchart{
                width:90%;
            }
            .update{
                min-width: 127px;
            }
            .c3-ygrid-line.leasy line {
                stroke: grey;
                /*green;*/
            }
            .c3-ygrid-line.lmedium line {
                stroke: grey;
                /*blue;*/
            }
            .c3-ygrid-line.lhard line {
                stroke: grey;
                /*black;*/
            }
            svg{
                overflow:visible !important;
            }
            input[readonly]{
                background-color: #fff !important;
            }
            .right{
                text-align:right;
            }

        </style>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">

                        <c:if test="${not empty userId}">
                            <li role="presentation">
                                <a id="backbutton" href="patientchosegraph?patientid=${userId}">
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${not empty groupId}">
                            <li role="presentation">
                                <a id="backbutton" href="groupstatistics">
                                    <span class="glyphicon glyphicon-chevron-left fa-2x" data-toggle="tooltip" data-placement="bottom" title="Indietro"></span>
                                </a>
                            </li>
                        </c:if>



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
                
                <div id="graph" class="tab-pane fade in">
                    <div class="row">
                        <div class="col-md-24 morepadding"> 
                            <h3 id="graph_name"></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 morepadding">
                            <!-- <button class="btn btn-default" ><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span> Dati .csv</button> -->
                        </div>
                        <div class="col-md-12 morepadding">
                           <p>Scegli il periodo:</p>
                            <div class="row">
                                <div class="col-md-12">
                                    <label>Data iniziale</label>
                                    <div class="input-group">
                                      <label for="datainiziale" id="config-datarange" class="input-group-addon btn"> 
                                            <span class="glyphicon glyphicon-calendar"></span>
                                      </label>
                                      <input id="datainiziale" type="text" class="form-control datainiziale" readonly />                              
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <label>Data finale</label>
                                    <div class="input-group">
                                      <label for="datafinale" id="config-datarange" class="input-group-addon btn"> 
                                            <span class="glyphicon glyphicon-calendar"></span>
                                      </label>
                                      <input id="datafinale" type="text" class="form-control datafinale" readonly/>                              
                                    </div>
                                </div>
                            </div>                                   
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <div>
                                <canvas id="graphchart1"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <div>
                                <canvas id="graphchart2"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <div>
                                <canvas id="graphchart3"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <div>
                                <canvas id="graphchart4"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <div>
                                <canvas id="graphchart5"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
                    
                    
                    
                
                </div>
            </div>                
            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->

        <script type="application/javascript">
            let time_metrics = [
                "total_time", "effective_time"
            ];
            let attempts_metrics = [
                "total_attempts", "completed_attempts", "interrupted_attempts", "passed_attempts", "failed_attempts",
                "max_level_reached"
            ];
            let rel_attempts_metrics = [
                "rel_completed_attempts", "rel_interrupted_attempts", "rel_passed_attempts", "rel_failed_attempts"
            ];
            let performance_metrics = [
                "total_responses", "correct_responses", "wrong_responses", "missing_responses",
            ];
            let rel_performance_metrics = [
                "mean_performance", "rel_correct_responses", "rel_wrong_responses", "rel_missing_responses",
            ];

            function createGraph(canvasId, title)
            {
                let graphCanvas = document.getElementById(canvasId);
                return new Chart(
                    graphCanvas,
                    {
                        type: 'line',
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {
                                    position: 'top',
                                    align: 'start',
                                    font: {
                                        size: 30
                                    }
                                },
                                title: {
                                    fullSize : true,
                                    align : 'start',
                                    display: true,
                                    text: title
                                }
                            }
                        },
                    }
                )
            }

            function getMoment(dateText)
            {

            }

            function sortData(response)
            {
                response.data = response.data.sort(
                    (d0, d1) => {
                        let m0 = moment(d0.date, 'DD-MM-YYYY').set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0})
                        let m1 = moment(d1.date, 'DD-MM-YYYY').set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0})
                        if(m0 > m1) return 1;
                        if(m0 < m1) return -1;
                        return 0;
                    }
                )
            }

            function isDisplayed(row, fromMoment, toMoment)
            {
                let date = moment(row.date, 'DD-MM-YYYY')
                    .set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0});
                return date >= fromMoment && date <= toMoment;
            }

            function createGraphDatasets(response, fromMoment, toMoment, fields)
            {

                return Array.from(response.fields)
                    .filter(field => field !== "date")
                    .filter(field => fields.includes(field))
                    .map(field => {
                        return {
                            label: response.displayNames[field],
                            data: response.data.filter(el => isDisplayed(el, fromMoment, toMoment)).map(el => el[field]),
                            borderWidth: 1
                        }
                    })
            }

            // Inizializzazione datepickers

            var startDate = moment().subtract(7, 'days');
            var endDate = moment();
            $('.datainiziale').datepicker({
                format: "dd-mm-yyyy",
                language: "it",
                keyboardNavigation: false
            });
            $('.datafinale').datepicker({
                format: "dd-mm-yyyy",
                language: "it",
                keyboardNavigation: false
            });
            $('#datainiziale').datepicker('setDate', startDate.set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0}).format('DD-MM-YYYY'));
            $('#datafinale').datepicker('setDate', endDate.set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0}).format('DD-MM-YYYY'));
            $('.datainiziale').datepicker().on('changeDate', function(e) {
                startDate = moment(e.date)
                    .set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0});
                reloadData();
            });
            $('.datafinale').datepicker().on('changeDate', function(e) {
                endDate = moment(e.date)
                    .set({'hour': 0, 'minute': 0, 'second' : 0, 'millisecond' : 0});
                reloadData();
            });


            let timeChart = createGraph('graphchart1', 'Tempo di esecuzione esercizi');
            let attemptsChart = createGraph('graphchart2', 'Tentativi esercizi');
            let relAttemptsChart = createGraph('graphchart3', 'Tentativi esercizi relativi');
            let performanceChart = createGraph('graphchart4', 'Risposte esercizi');
            let relPerformanceChart = createGraph('graphchart5', 'Risposte esercizi relative');

            let exerciseCategory = "${exerciseCategory}";
            let exerciseName = "${exerciseName}";
            let groupId = "${groupId}";
            let userId = "${userId}";

            let queryParams = {};

            if(userId !== "")
                queryParams.userId = userId;
            if(exerciseCategory !== "")
                queryParams.exerciseCategory = exerciseCategory;
            if(groupId !== "")
                queryParams.userGroup = groupId;
            if(exerciseName !== "")
                queryParams.exerciseName = exerciseName;

            function reloadData()
            {
                $.get("metrics/history",
                    queryParams,
                    function(response, status) {
                        sortData(response);

                        let labels = Array.from(response.data)
                            .filter(el => isDisplayed(el, startDate, endDate))
                            .map(el => el["date"]);

                        timeChart.data = {
                            labels : labels,
                            datasets : createGraphDatasets(response, startDate, endDate, time_metrics)
                        }
                        timeChart.update();

                        attemptsChart.data = {
                            labels : labels,
                            datasets : createGraphDatasets(response, startDate, endDate, attempts_metrics)
                        }
                        attemptsChart.update();

                        relAttemptsChart.data = {
                            labels : labels,
                            datasets : createGraphDatasets(response, startDate, endDate, rel_attempts_metrics)
                        }
                        relAttemptsChart.update();

                        performanceChart.data = {
                            labels : labels,
                            datasets : createGraphDatasets(response, startDate, endDate, performance_metrics)
                        }
                        performanceChart.update();

                        relPerformanceChart.data = {
                            labels : labels,
                            datasets : createGraphDatasets(response, startDate, endDate, rel_performance_metrics)
                        }
                        relPerformanceChart.update();
                    },
                    "json");
            }
            $(document).ready(function(){
                reloadData();
            });
        </script>
        <jsp:include page="modal-pat-graph-ovw.jsp"/>        
        
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
