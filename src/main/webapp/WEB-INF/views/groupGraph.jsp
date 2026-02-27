<%-- 
    Document   : groupGraph
    Created on : 26-gen-2017, 10.41.56
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
        <title>Grafici Gruppo</title>
        
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
            update{
                min-width: 127px;
            }
            #overviewchart{
                width:90%;
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
            .user a{
                color:black;
                text-shadow: 2px 2px 3px white;
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
            function loadSection(){
                var kind = ["attenzione","memoria"];
                var difficulty=["placeholder","easy","medium","difficult"];
                var difficolta=["placeholder","facile","media","difficile"];
                for(var i=0;i<kind.length;i++){
                    for(var j=1;j<=3;j++){
                $(".tab-content").append(''+
                '<div id="'+kind[i]+j+'" class="tab-pane fade in">'+
'                                <div class="row">'+
'                                    <div class="col-md-24 morepadding">'+
'                                        <h3>'+kind[i]+' '+difficolta[j]+'</h3>'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-12 morepadding">'+
'                                        <!-- <button class="btn btn-default" ><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span> Dati .csv</button> -->'+
'                                    </div>'+
'                                    <div class="col-md-12 morepadding">'+
'                                        <div class="input-group date daterange" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="day">'+
'                                            <input type="text" class="form-control" >'+
'                                            <div id="config-datarange" class="input-group-addon"> '+
'                                                <span class="glyphicon glyphicon-th"></span>'+
'                                            </div>'+
'                                        </div>                                    '+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-24 morepadding">'+
'                                        <div id="'+kind[i]+j+'chart" ></div>'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-primary center-block update" type="graph" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="month">Mensile</button>'+
'                                    </div>'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-primary center-block update" type="graph" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="week">Settimanale</button>'+
'                                    </div>'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-success center-block update" type="graph" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="day">Giornaliero</button>'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-24 morepadding">'+
'                                        <hr />'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-24 morepadding">'+
'                                        <h3>'+kind[i]+' '+difficolta[j]+' Successi</h3>'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-24 morepadding">'+
'                                        <div id="'+kind[i]+j+'bar" ></div>'+
'                                    </div>'+
'                                </div>'+
'                                <div class="row">'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-primary center-block update" type="bar" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="month">Mensile</button>'+
'                                    </div>'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-primary center-block update" type="bar" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="week">Settimanale</button>'+
'                                    </div>'+
'                                    <div class="col-md-8">'+
'                                        <button class="btn btn-lg btn-success center-block update" type="bar" kind="'+kind[i]+'" difficulty="'+difficulty[j]+'" group="day">Giornaliero</button>'+
'                                    </div>'+
'                                </div>'+
'                            </div>'+
                '');
                }
                }
            }
            $(document).ready(function(){
                if(${json}.json.length<=0){
                    $("#infomessage").html("Nessun dato nel periodo scelto!");
                    $("#infomessage").show();
                }else{
                    $("#infomessage").hide();
                }
                loadSection();
                var dif2num = [];
                dif2num["easy"]=1;
                dif2num["medium"]=2;
                dif2num["difficult"]=3;
                
                var start = moment().subtract(7, 'days');
                var end = moment();
                $('.datainiziale').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                $('.datainiziale').datepicker("update",start.format('DD/MM/YYYY'));
                $('.datafinale').datepicker({
                    format: "dd/mm/yyyy",
                    language: "it",
                    keyboardNavigation: false
                });
                $('.datafinale').datepicker("update",end.format('DD/MM/YYYY'));
                end =  moment().add(1,'days').format('DD/MM/YYYY');
                start = moment().subtract(7, 'days').format('DD/MM/YYYY');
                
                
                function drawGraph(element,json){
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
                                label:"performance",
                                max:1,
                                min:0.1
                            }
                        },
                        grid:{
                            y:{
                                //show:true,
                                //lines: [
                                //    {value: 33, text: 'Facile', class: 'leasy'},
                                //    {value: 66, text: 'Media',class: 'lmedium'},
                                //    {value: 100, text: 'Difficile',class: 'lhard'}
                                //]
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
                              ratio: 0.2
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
                function loadGraph(element,kind,difficulty,group,start,end){
                    $("#infomessage").hide();
                    $.get("groupgraphjson",
                        {
                            "kind":kind,
                            "groupid":${group.id},
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
                            drawGraph(element,data);
                        },
                        "json");
                    
                }
                
                function loadBar(element,kind,difficulty,group,start,end){
                    $("#infomessage").hide();
                    $.get("grouphistogramjson",
                        {
                            "kind":kind,
                            "groupid":${group.id},
                            "difficulty":difficulty,
                            "start":start,
                            "end":end,
                            "group":group
                        },
                        function(data){
                            drawBar(element,data);
                        },
                        "json");
                    
                }
                
                function loadOverview(group,element,start,end){
                    $("#infomessage").hide();
                    $.get("groupoverviewjson",
                        {
                            "groupid":${group.id},
                            "start":start,
                            "end":end,
                            "group":group
                        },
                        function(data){
                            if(data.json.length<=0){
                                $("#infomessage").html("Nessun dato nel periodo scelto!");
                                $("#infomessage").show();
                            }
                            drawGraph(element,data);
                        },
                        "json");
                    
                }
                
                function loadAll(kind,difficulty,group,element,restore){
                    var elementchart= element+"chart";
                    var elementbar= element+"bar";                  
                    
                    loadGraph(elementchart,kind,difficulty,group,start,end);
                    loadBar(elementbar,kind,difficulty,group,start,end);
                    
                    if(restore != true){
                        $("li").removeClass();
                        $(this).addClass("active");
                    }
                      
                    $(".update").removeClass("btn-success");
                    $(".update").addClass("btn-primary");
                    $(".update[group=day]").addClass("btn-success");
                }
                
                drawGraph("#overviewchart",${json});
                
                //Configure bounds in datepicker
                $(".datainiziale").datepicker("setStartDate",moment("2015-01-01").format('DD/MM/YYYY'));
                $(".datainiziale").datepicker("setEndDate",moment().add(1,"days").format('DD/MM/YYYY'));
                
                $(".datafinale").datepicker("setStartDate",moment("2015-01-01").format('DD/MM/YYYY'));
                $(".datafinale").datepicker("setEndDate",moment().add(1,"days").format('DD/MM/YYYY'));
                
                
                $('.datainiziale').datepicker().on('changeDate', function(e) {
                    start=$('.datainiziale').val();
                    end = $('.datafinale').val();
                    disableButton(start,end);
                    $(".datafinale").datepicker("setStartDate",start);
                    var kind = $("li.active a").attr("kind");
                    console.log(start);
                    console.log(kind);
                    var group = $("li.active a").attr("group");
                    if(kind!="overview"){
                        var difficulty = $("li.active a").attr("difficulty");
                        var element= "#"+kind+dif2num[difficulty];
                        loadAll(kind,difficulty,group,element,true);
                    }else{
                        var element="#"+kind+"chart";
                        console.log("carico overview");
                        loadOverview(group,element,start,end);
                        $(".update").removeClass("btn-success");
                        $(".update").addClass("btn-primary");
                        $(".update[group=day]").addClass("btn-success");
                    }                   
                });
                
                $('.datafinale').datepicker().on('changeDate', function(e) {
                    start=$('.datainiziale').val();
                    end = $('.datafinale').val();
                    disableButton(start,end);
                    $(".datainiziale").datepicker("setEndDate",end);
                    var kind = $("li.active a").attr("kind");
                    console.log(start);
                    console.log(kind);
                    var group = $("li.active a").attr("group");
                    if(kind!="overview"){
                        var difficulty = $("li.active a").attr("difficulty");
                        var element= "#"+kind+dif2num[difficulty];
                        loadAll(kind,difficulty,group,element,true);
                    }else{
                        var element="#"+kind+"chart";
                        console.log("carico overview");
                        loadOverview(group,element,start,end);
                        $(".update").removeClass("btn-success");
                        $(".update").addClass("btn-primary");
                        $(".update[group=day]").addClass("btn-success");
                    }                   
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
        
                    if(kind!="overview"){
                        var element= "#"+kind+dif2num[difficulty];
                        //var element= $(this).attr("href");                    
                        var elementchart= element+"chart";
                        var elementbar= element+"bar";


                        if(type == "graph"){
                            loadGraph(elementchart,kind,difficulty,group,start,end);
                            //loadBar(elementbar,kind,difficulty,group);
                            $(".update[type=graph]").removeClass("btn-primary");
                            $(".update[type=graph]").addClass("btn-default");
                            $(this).addClass("btn-primary");
                        }else{
                            loadBar(elementbar,kind,difficulty,group,start,end);
                            $(".update[type=bar]").removeClass("btn-primary");
                            $(".update[type=bar]").addClass("btn-default");
                            $(this).addClass("btn-primary");
                        }                    
                    }else{
                        var element="#"+kind+"chart";
                        console.log("carico overview");
                        loadOverview(group,element,start,end);
                        $(".update[type=graph]").removeClass("btn-primary");
                        $(".update[type=graph]").addClass("btn-default");
                        $(this).addClass("btn-primary");
                    }
                });
        });
        </script>
        
        <script>
            function udf(x){
                return x;
            }
            $(document).ready(function(){
                $.get("getusersfromgroup?groupid="+${group.id},function(data){
                    var js=data;
                    if(js.length==0){
                        //$("#infomessage").html("Nessun paziente nel gruppo selezionato!");
                        $("#infomessage").html("Nessun partecipante nel gruppo selezionato!");
                    
                    }
                    for(var i=0;i<js.length;i++){                        
                        $("#utenti").append('<li class="user"><a href="patientchosegraph?patientid='+js[i].id+'"><img class="img-responsive img-thumbnail" src="'+udf(js[i].photo)+'"/><p>'+js[i].surname+' '+js[i].name+'</p></a></li>');
                    }
                });
            });
        </script>
        <style>
            #utenti{
                list-style: none;
                margin-left:0px;
                margin-rigth:0px;
                padding-left:0px;
                padding-righ:0px;
            }
            #utenti li{
                float:left;
                margin-right:5px;
                margin-bottom:5px;
                width:100px;
                height:100px;
                /*border:solid;
                border-width:1px;*/
                text-align: center;
                background-repeat: no-repeat;
                background-size: cover;
                -webkit-border-radius: 9px;
                -moz-border-radius: 9px;
                border-radius: 9px;
            }
            li p{
                vertical-align:bottom;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header clearfix">
                <nav>
                    <ul class="nav nav-pills pull-right">
                        <li role="presentation">
                            <a href="groupstatistics">                                
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
                <div><h2>Grafici di ${group.name}</h2></div>
                
                <div class="row">
                    <div class="col-md-24">
                            <ul id="utenti">

                            </ul>
                    </div>
                </div>

                <div class="row">
                    <div class="row">
                        <div class="col-md-24 morepadding">
                            <hr />
                            <div id="infomessage" class="alert alert-danger" role="alert"></div>
                        </div>
                    </div>
                    
                    <ul class="nav nav-tabs">
                        <li role="presentation" class="active"><a data-toggle="tab" href="#overview" kind="overview" group="day" element="overview">Panoramica</a></li>
                    </ul>
                    
                    <div class="tab-content" id="grafici">
                        <div id="overview" class="tab-pane fade in active">
                            <div class="row">
                                <div class="col-md-24 morepadding">
                                    <h3>Panoramica</h3>
                                </div>
                            </div>
                            <div class="row">
                                    <div class="col-md-12 morepadding">
                                        <p class="small"><i>Premi sui punti del grafico per visualizzare il punteggio relativo.</i></p>
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
                                    <div id="overviewchart" ></div>
                                </div>
                            </div>
                            <div class="row">
                                    <div class="col-md-8">
                                        <button class="btn btn-lg btn-default center-block update" type="graph" kind="overview" difficulty="overview" group="month">Mensile</button>
                                    </div>
                                    <div class="col-md-8">
                                        <button class="btn btn-lg btn-default center-block update" type="graph" kind="overview" difficulty="overview" group="week">Settimanale</button>
                                    </div>
                                    <div class="col-md-8">
                                        <button class="btn btn-lg btn-primary center-block update" type="graph" kind="overview" difficulty="overview" group="day">Giornaliero</button>
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
        
        <jsp:include page="modal-groupgraph.jsp"/>        
        
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
