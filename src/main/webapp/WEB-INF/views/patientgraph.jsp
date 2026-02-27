<%-- 
    Document   : patientGraph
    Created on : 14-nov-2016, 10.41.56
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
        <title>Grafici <!--paziente-->partecipante</title>
        
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
        <style>
            .morepadding button{
                padding-left:15px;
            }
            .morepadding h3{
                padding-left:15px;
            }
            .update{
                min-width: 127px;
            }
            #graphchart{
                width:90%;
            }
            .c3-ygrid-line.leasy line {
                stroke: green;
            }
            .c3-ygrid-line.lmedium line {
                stroke: blue;
            }
            .c3-ygrid-line.lhard line {
                stroke: black;
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
            /*.i{
                float:left;
                margin-left:-30px;
                margin-top:10px;
                cursor:pointer;
            }
            input[name=daterange]{
                float:left;
                color:red;
            }*/
        </style>
        <script>
            function parseDate(str) {
                var mdy = str.split('/');
                return new Date(mdy[2], mdy[1]-1, mdy[0]);
            }
            function disableButton(start,end){
                var days = Math.round((parseDate(end)-parseDate(start))/(1000*60*60*24)); 
                console.log(days);
                if(days<30){
                    $("button[group=month]").prop("disabled",true);
                }else{
                     $("button[group=month]").prop("disabled",false);
                }
                if(days<7){
                    $("button[group=week]").prop("disabled",true);
                }else{
                    $("button[group=week]").prop("disabled",false);
                }
            }
            function drawGraphExp(element,json,more){
                var overviewchart = c3.generate({
                    bindto: element,
                    data:json,
                    axis: {
                        x: {
                            type: 'timeseries',
                            tick: {
                                format: '%d-%m-%Y',
                                rotate: 60,
                                multiline:false
                            }
                        },
                        y:{
                            label: "media %",
                            max:100,
                            min:1
                        },
                        
                    }
                });
            }
            
            function drawGraph(element,json,more){
                if(more){
                    max=15;
                }else{
                    max=100;
                }
                var overviewchart = c3.generate({
                    bindto: element,
                    data:json,
                    axis: {
                        x: {
                            type: 'timeseries',
                            tick: {
                                format: '%d-%m-%Y',
                                rotate: 60,
                                multiline:false
                            }
                        },
                        y:{
                            label:"Performance %",
                            max:100,
                            min:1
                        }
                    },
                    grid:{
                        y:{
                            //show:true,
                        }
                    }
                });
            }
            function drawBar(element,json){
                    var overviewbar = c3.generate({
                        bindto: element,
                        data:json,
                        bar:{
                          width:{
                              ratio: 0.1
                          }  
                        },
                        axis: {
                            x: {
                                type: 'timeseries',
                                tick: {
                                    format: '%d-%m-%Y',
                                    rotate: 60,
                                    multiline:false
                                }
                            }
                        }
                    });
            }

            function loadGraph(element,kind,difficulty,group,start,end,more){
                $("#infomessage").hide();
                $.get("patientgraphjson",
                    {
                        "kind":kind,
                        "patientid":${patient.id},
                        "difficulty":difficulty,
                        "start":start,
                        "end":end,
                        "group":group,
                        "exploso":more
                    },
                    function(data){
                        if(data.json.length<=0){
                            $("#infomessage").html("Nessun dato nel periodo scelto!");
                            $("#infomessage").show();
                        }
                        if(more){
                            drawGraphExp(element,data,more);
                        }else{
                            drawGraph(element,data,more);
                        }
                    },
                    "json");

            }

            function loadBar(element,kind,difficulty,group,start,end){
                $("#infomessage").hide();
                $.get("patienthistogramjson",
                    {
                        "kind":kind,
                        "patientid":${patient.id},
                        "difficulty":difficulty,
                        "start":start,
                        "end":end,
                        "group":group
                    },
                    function(data){
                        if(data.json.length<=0){
                            $("#infomessage").html("Nessun dato nel periodo scelto!");
                            $("#infomessage").show();
                        }
                        drawBar(element,data);
                    },
                    "json");

            }

            function loadOverview(group,element,start,end){
                $("#infomessage").hide();
                $.get("patientoverviewjson",
                    {
                        "patientid":${patient.id},
                        "start":start,
                        "end":end,
                        "group":group
                    },
                    function(data){
                        if(data.json.length<=0){
                            $("#infomessage").html("Nessun dato nel periodo scelto!");
                            $("#infomessage").show();
                        }
                        drawBar(element,data);
                    },
                    "json");

            }
            function loadExploso(element,kind,difficulty,group,start,end){
                var tipi=["overview","attenzione","memoria","funzioni esecutive"];
                if(tipi.indexOf(kind)!=-1){
                    $(".exploso").hide();
                }else{
                    loadGraph(element,kind,difficulty,group,start,end,true);
                    
                }
            }
            function loadSection(){
                loadExploso("#graphexploso","${kind}","${difficulty}","${group}","${start}","${end}");
                //$('a[difficulty="${difficulty}"').removeClass("btn-primary");
                //$('a[difficulty="${difficulty}"').addClass("btn-success");
                $('a[difficulty="${difficulty}"').parent().siblings().removeClass("active");
                $('a[difficulty="${difficulty}"').parent().addClass("active");
            }
            function updatePrevPage(back_link){
                if (typeof(Storage) !== "undefined") {
                    localStorage.setItem("back_link", back_link);
                    
                }
            }
            function getPrevPage(){
                if (typeof(Storage) !== "undefined") {
                    return localStorage.getItem("back_link"); 
                } else {
                    return "";
                }
            }
            $(document).ready(function(){
                
                /*var back_link=document.referrer;
                back_link=back_link.replace("&difficulty=medium","").replace("&difficulty=easy","").replace("&difficulty=difficult","");
                var old_link=getPrevPage();
                var now_link = window.location.href ;
                now_link = now_link.replace("&difficulty=medium","").replace("&difficulty=easy","").replace("&difficulty=difficult","");
                console.log(back_link);
                console.log(old_link);
                if(now_link==back_link){
                    console.log("sono uguali");
                    back_link=old_link;
                }
                console.log("salvo: "+back_link);
                updatePrevPage(back_link);
                back_link=back_link.replace("&difficulty=medium","");               
                $("#backbutton").attr("href",back_link);*/
        
                if(${json_graph}.json.length<=0){
                    $("#infomessage").html("Nessun dato nel periodo scelto!");
                    $("#infomessage").show();
                }else{
                    $("#infomessage").hide();
                }
                var dif2num = [];
                dif2num["easy"]=1;
                dif2num["medium"]=2;
                dif2num["difficult"]=3;
                
                var start = moment('${start}', 'DD/MM/YYYY');
                if(!start.isValid()) {
                    start = moment().subtract(7, 'days');
                }
                
                var end = moment('${end}', 'DD/MM/YYYY');
                if(!end.isValid()) {
                    end = moment();
                }

                $('.datainiziale').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                $('.datafinale').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                $('.datainiziale').datepicker("update",start.format('DD/MM/YYYY'));
                $('.datafinale').datepicker("update",end.format('DD/MM/YYYY'));
                end =  moment().add(1,'days').format('DD/MM/YYYY');
                start = moment().subtract(7, 'days').format('DD/MM/YYYY');
                var s = $('.datainiziale').val();
                var e = $('.datafinale').val();
                var url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=general&start="+s+"&end="+e;
                document.getElementById("ge").setAttribute("href",url);
                url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=easy&start="+s+"&end="+e;
                document.getElementById("ea").setAttribute("href",url);
                url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=medium&start="+s+"&end="+e;
                document.getElementById("me").setAttribute("href",url);
                url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=difficult&start="+s+"&end="+e;
                document.getElementById("di").setAttribute("href",url);
                
                
                
                function loadAll(kind,difficulty,group){
                    var elementchart= "#graphchart";
                    var elementbar= "#graphbar";                  
                    
                    loadGraph(elementchart,kind,difficulty,group,start,end,false);
                    loadBar(elementbar,kind,difficulty,group,start,end);
                    loadExploso("#graphexploso",kind,difficulty,group,start,end);
                      
                    $(".update").removeClass("btn-primary");
                    $(".update").addClass("btn-default");
                    $(".update[group=day]").addClass("btn-primary");
                }
                
                drawGraph("#graphchart",${json_graph});
                drawBar("#graphbar",${json_bar});
                
                
                //Configure bounds in datepicker
                $(".datainiziale").datepicker("setStartDate",moment("2015-01-01").format('DD/MM/YYYY'));
                $(".datainiziale").datepicker("setEndDate",moment().add(1,"days").format('DD/MM/YYYY'));
                
                $(".datafinale").datepicker("setStartDate",moment("2015-01-01").format('DD/MM/YYYY'));
                $(".datafinale").datepicker("setEndDate",moment().add(1,"days").format('DD/MM/YYYY'));
    
    
                $('.datainiziale').datepicker().on('changeDate', function(e) {
                    start=$('.datainiziale').val();
                    end = $('.datafinale').val();
                    var url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=general&start="+start+"&end="+end;
                    document.getElementById("ge").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=easy&start="+start+"&end="+end;
                    document.getElementById("ea").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=medium&start="+start+"&end="+end;
                    document.getElementById("me").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=difficult&start="+start+"&end="+end;
                    document.getElementById("di").setAttribute("href",url);
                    disableButton(start,end);
                    $(".datafinale").datepicker("setStartDate",start);
                    var kind = "${kind}";
                    var group = "${group}";
                    var difficulty= "${difficulty}";
                    loadAll(kind,difficulty,group);                    
                });
                
                $('.datafinale').datepicker().on('changeDate', function(e) {
                    start=$('.datainiziale').val();
                    end = $('.datafinale').val();
                    var url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=general&start="+start+"&end="+end;
                    document.getElementById("ge").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=easy&start="+start+"&end="+end;
                    document.getElementById("ea").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=medium&start="+start+"&end="+end;
                    document.getElementById("me").setAttribute("href",url);
                    url = "patientbuildgraph?patientid=${patient.id}&kind=${kind}&difficulty=difficult&start="+start+"&end="+end;
                    document.getElementById("di").setAttribute("href",url);
                    disableButton(start,end);
                    $(".datainiziale").datepicker("setEndDate",end);
                    var kind = "${kind}";
                    var group = "${group}";
                    var difficulty= "${difficulty}";
                    loadAll(kind,difficulty,group);                    
                });
                
                
                $(".menutab").on("click",function(){
                    
                    var kind = $(this).attr("kind");
                    var difficulty = $(this).attr("difficulty");
                    var group = $(this).attr("group");
                    
                    var element= "#"+kind+dif2num[difficulty];
                    //var element= $(this).attr("href");                    
                    
                    loadAll(kind,difficulty,group,element);
                });
                
                $(".update").on("click",function(){
                    
                    var kind = $(this).attr("kind");
                    var difficulty = $(this).attr("difficulty");
                    var group = $(this).attr("group");
                    var type = $(this).attr("type");                                                     
                
                    var elementchart= "graphchart";
                    var elementbar= "graphbar";

                    if(type == "graph"){
                        loadGraph(elementchart,kind,difficulty,group,start,end,false);
                        //loadBar(elementbar,kind,difficulty,group);
                        $(".update[type=graph]").removeClass("btn-primary");
                        $(".update[type=graph]").addClass("btn-default");
                        $(this).addClass("btn-primary");
                    }
                    if(type=="bar"){
                        loadBar(elementbar,kind,difficulty,group,start,end);
                        $(".update[type=bar]").removeClass("btn-primary");
                        $(".update[type=bar]").addClass("btn-default");
                        $(this).addClass("btn-primary");
                    }
                    if(type=="exploso"){
                        loadGraph("#graphexploso",kind,difficulty,group,start,end,true);
                        //loadBar(elementbar,kind,difficulty,group);
                        $(".update[type=exploso]").removeClass("btn-primary");
                        $(".update[type=exploso]").addClass("btn-default");
                        $(this).addClass("btn-primary");
                    }
                });
                loadSection();
        });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a id="backbutton" href="patientchosegraph2?patientid=${patient.id}&kind=${kind}">                                
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
 
            <div class="well">
                <div class="row">
                    <div class="col-md-24">
                        <h2>Grafici di ${tipo}, ${patient.surname} ${patient.name}</h2>
                        <br />
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-24 morepadding">
                        <hr />
                    </div>
                </div>
                
                
                <div id="graph" class="tab-pane fade in">
                    <div class="row">
                        <div class="col-md-12 morepadding">
                            <p>Scegli la difficolt&agrave:</p>
                            <ul class="pagination" role="group" aria-label="...">
                                <li><a difficulty="general" id="ge" href="...">Tutte</a></li>
                                <li><a difficulty="easy" id="ea" href="...">Facile</a></li>
                                <li><a difficulty="medium" id="me" href="...">Media</a></li>
                                <li><a difficulty="difficult" id="di" href="...">Difficile</a></li>
                            </ul>                                
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
                            <hr />
                            <div id="infomessage" class="alert alert-danger" role="alert"></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <h3>${tipo} performance</h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <p class="right small"><i>Premi sui punti per visualizzare il punteggio relativo.</i></p>
                            <div id="graphchart" ></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="graph" kind="${kind}" difficulty="${difficulty}" group="month">Mensile</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="graph" kind="${kind}" difficulty="${difficulty}" group="week">Settimanale</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-primary center-block update" type="graph" kind="${kind}" difficulty="${difficulty}" group="day">Giornaliero</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <hr />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <h3>${tipo} (Successi e Fallimenti)</h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <p class="right small"><i>Premi sulle barre per visualizzare il punteggio relativo.</i></p>
                            <div id="graphbar" ></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="bar" kind="${kind}" difficulty="${difficulty}" group="month">Mensile</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="bar" kind="${kind}" difficulty="${difficulty}" group="week">Settimanale</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-primary center-block update" type="bar" kind="${kind}" difficulty="${difficulty}" group="day">Giornaliero</button>
                        </div>
                    </div>
                        
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <hr />
                        </div>
                    </div>
                    <div class="row exploso">
                        <div class="col-md-24 morepadding">
                            <h3>Dettagli ${tipo}</h3>
                        </div>
                    </div>
                    <div class="row exploso">
                        <div class="col-md-24 morepadding">
                            <p class="right small"><i>Premi sui punti per visualizzare il punteggio relativo.</i></p>
                            <div id="graphexploso" ></div>
                        </div>
                    </div>
                    <div class="row exploso">
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="exploso" kind="${kind}" difficulty="${difficulty}" group="month">Mensile</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-default center-block update" type="exploso" kind="${kind}" difficulty="${difficulty}" group="week">Settimanale</button>
                        </div>
                        <div class="col-md-8">
                            <button class="btn btn-lg btn-primary center-block update" type="exploso" kind="${kind}" difficulty="${difficulty}" group="day">Giornaliero</button>
                        </div>
                    </div>    
                </div>
                    
                    
                    
                
                </div>
            </div>                
            <footer class="footer">
                <p>&copy; 2016-2026 Universit&agrave; di Bologna</p>
            </footer>

        </div> <!-- /container -->
        
        <jsp:include page="modal-pat-graph.jsp"/>        

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
