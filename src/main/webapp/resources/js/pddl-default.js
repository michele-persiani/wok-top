var nearPlace;
var drawing = false;
var orario =0;
var nearRoad;
var maxRightRoad=30;
var rightRoad=0;

var legendaHTML='<div id="legend">'+
                            '<hr />'+
                            '<p><b>Legenda:</b></p><div class="row">'+
                           
                                '<div class="col-md-12">'+
                                    '<ul class="legendlist">'+
                                     '<li><b>Ricorda che</b> la tessera si usa in piscina, mentre in stazione si prendono gli abbonamenti</li>'+
                                        '<li><img src ="resources/images/locali/path.png" /> strada transitabile solo a piedi.</li>'+
                                        '<li><img src ="resources/images/locali/road.png" /> strada transitabile solo in automobile.</li>'+
                                        '<li><img src ="resources/images/locali/both_path_road.png" /> strada transitabile sia in auto che a piedi.</li>'+
                                        '<li><img src ="resources/images/locali/clock.png" /> luogo in cui è possibile attendere.</li>'+
                                    '</ul>'+
                                '</div>'+
                                '<div class="col-md-12">'+
                                    '<ul class="legendlist">'+
                                        '<li><img src ="resources/images/locali/map.png" class="bigimg" /> Spostamento a piedi, occorrono 30 minuti per transitare su qualsiasi percorso.</li>'+
                                        '<li><img src ="resources/images/locali/map_macchina.png" class="bigimg" /> Spostamento in macchina, gli spostamenti non richiedono tempo.</li>'+
                                        '<li><button class="btn btn-sm btn-primary">Aspetta</button> Permette di far scorrere il tempo, il tempo passa solo quando si decide di aspettare.</li>'+
                                    '</ul>'+ 
                                '</div>'+
                            '</div>'+
                '</div>';

function initMappa(canvas,js){
    var c = document.getElementById(canvas);
    var ctx = c.getContext("2d");
    nearPlace = js.places[js.initplace];
    orario = js.time;
    drawMap(ctx,js);
    loadPositionItemMap(js.places[js.initplace],js);
    mouseListening(canvas,c,ctx,js);
    initEventi(canvas,c,ctx,js);
}

function isOpen(){
    if(orario>=nearPlace["open-time"]&&orario<=nearPlace["close-time"]){
        return true;
    }else{
        return false;
    }
}

function isDropAllowed(item){
	console.log("isDropAllowed: " + item);
	allowedDropsArray = nearPlace["can-drop"];
        return true;
       /* console.log("isDropAllowed: nearPlace " + nearPlace["items-leave"] );
	if(allowedDropsArray.toString().indexOf(item) != -1){
		return true;
	} else {
		return false;
	}*/
}

function isExchangeAllowed(item1, item2){
	console.log("isDropAllowed: " + item1 + " " + item2);
	allowedExchangesArray = nearPlace["can-exchange"];
	for(var i = 0; i < allowedExchangesArray.length; i++) {
	    var obj = allowedExchangesArray[i];
	    if (obj.item1 == item1 && obj.item2 == item2){
	    	return true;
	    }
	}
	return false;
}

function initEventi(canvas,c,ctx,js){
    $("#aspetta").click(function(){
        if(orario<48){
            waitTime();
        }else{
            $("#aspetta").prop("disabled",true);
        }
        updateTempo();
    });
    $("#tempo").click(function(){
        if($("#aspetta").prop("disabled")==false){
            if(orario<48){
                waitTime();
            }else{
                $("#tempo").prop("disabled",true);
            }
            updateTempo();
        }
    });
    
    updateWaiting();
    $("#inventario").on("click",".leave",function(e){
    	var name = $(this).val();
    	console.log("Leaving " + name)
        if(!isOpen()){
            bootbox.alert({
                size: 'small',
                message: '<h4>Il negozio è chiuso a quest\'ora!</h4>'
            });
        }else if (!isDropAllowed(name)){
            bootbox.alert({
                size: 'small',
                message: 'Non puoi lasciare questo oggetto qui!'
            });
        } else {
            var img = $(this).attr("img");
            $(this).parent().parent().remove();
            leaveItemMap(name,img,js,ctx);
        }
        //e.stopPropagation();
    });
    $("#posizioneattuale").on("click",".prendi",function(e){
        if(!isOpen()){
            bootbox.alert({
                size: 'small',
                message: '<h4>Il negozio è chiuso a quest\'ora!</h4>'
            });
        }else{
            var name = $(this).val();
            $(this).parent().parent().remove();
            takeItemMap(name,nearPlace,js,ctx);
            loadPositionItemMap(nearPlace,js);
        }
    });

    $("#posizioneattuale").on("click",".scambia",function(e){
        if(!isOpen()){
            bootbox.alert({
                size: 'small',
                message: '<h4>Il negozio è chiuso a quest\'ora!</h4>'
            });
        }else{
            var name = $(this).val();
            offerATrade(js,name);
            //$(this).parent().parent().remove();
            //takeItemMap(name,nearPlace,js);
            //loadPositionItemMap(nearPlace,js);
        }
    });
    
    $('#myModal').on('show.bs.modal', function (event) {
          var button = $(event.relatedTarget) // Button that triggered the modal
          var recipient = button.data('whatever') // Extract info from data-* attributes
          // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
          // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
          var modal = $(this);
          //alert(recipient);
          if(recipient=="@showInfo"){
            modal.find('.modal-title').html("Piani per la giornata");
            $.get("getproblemdescription?id="+idproblem,function(data){
                modal.find('.modal-body').html(data+legendaHTML);
            })
            var fbody=modal.find('.modal-footer');

            fbody.html('<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>');
            //modal.find('.modal-body').load("getproblemdescription?id="+idproblem);
          }
      });
}

function leaveItemMap(name,img,js,ctx){
    nearPlace.items.push({"name":name,"img":img});
    loadPositionItemMap(nearPlace,js);
    if(name=="macchina"){
        updateMapPointer(IMAGE_PATH+'map.png');
        drawMap(ctx,js);
        setTimeout(function(){drawMap(ctx,js);},1000);
    }
    dropItemAt(js,nearPlace.name,name,orario);
}

function takeItemMap(name,pos,js,ctx){
    var r = parseInt(Math.random()*100);
    var j = findItemByName(pos,name);
    var item=pos.items[j];
    //$("#inventario").append('<div class="itembox"> <img class="item" src="locali/'+pos.items[j].img+'.png" /><button id="leave'+pos.items[j].name+r+'" class="btn btn-primary leave" value="'+pos.items[j].name+'" img="'+pos.items[j].img+'">lascia</button></div>')
    $("#inventario").append('<li class="list-group-item">'+
                            '<div class="media">'+
                                '<div class="media-left">'+
                                    '<img class="media-object item" src="'+IMAGE_PATH+item.img+'.png" />'+
                                '</div>'+
                                '<div class="media-body">'+
                                    '<label>'+item.name+'</label><br />'+
                                '</div>'+
                            '</div>'+
                            '<div class="bottoni-p-s">'+
                                '<hr />'+
                                '<button id="leave'+item.name+r+'" class="btn btn-sm btn-primary leave" value="'+item.name+'" img="'+item.img+'">lascia</button>'+
                            '</div>'+
                        '</li>');
    pos.items.splice(j,1);
    if(name=="macchina"){
        updateMapPointer(IMAGE_PATH+'map_macchina.png');
        drawMap(ctx,js);
        setTimeout(function(){drawMap(ctx,js);},1000);
    }
    takeItemAt(js,pos.name,item.name,orario);
}

function findItemByName(pos,name){
    for(var j=0;j<pos.items.length;j++){
        if(pos.items[j].name==name){
            return j;
        }
    }
    return -1;
}

function generateButton(item){
    var s='';
    if($("#inventario .media").length>0)
        s+='<button class="scambia btn btn-sm btn-success" value="'+item.name+'" img="'+item.img+'">scambia</button>';
    s+='<button class="prendi btn btn-sm btn-primary" value="'+item.name+'" img="'+item.img+'">prendi</button>';
    return s;
}
function loadPositionItemMap(pos,js){
    //if(!pos.name.includes("rotonda")){
    if(pos.name.indexOf("rotonda")===-1){
        $("#positionname").text(pos.name);
    }else{
        $("#positionname").text("rotonda");
    }
    $("#posizioneattuale").html("");
    try{
        for(var i=0;i<pos.items.length;i++){
            var item=pos.items[i];
            //$("#posizioneattuale").append('<div class="itembox"> <img class="item" src="locali/'+item.img+'.png" /><button class="prendi btn btn-primary" value="'+item.name+'" img="'+item.img+'">prendi</button><button class="scambia btn btn-success" value="'+item.name+'" img="'+item.img+'">Scambia</button></div>')
            $("#posizioneattuale").append(
                                        '<li class="list-group-item">'+
                                            '<div class="media">'+
                                                '<div class="media-left">'+
                                                    '<img class="media-object item" src="'+IMAGE_PATH+item.img+'.png" />'+
                                                '</div>'+
                                                '<div class="media-body">'+
                                                    '<label>'+item.name+'</label><br />'+
                                                '</div>'+
                                            '</div>'+
                                            '<div class="bottoni-p-s">'+
                                                '<hr />'+
                                                generateButton(item)+
                                            '</div>'+
                                        '</li>');
        }
    }catch(err){
        console.log("FATAL ERROR: "+err);
    }
}
function mouseListening(canvas,c,ctx,js){
    var mousePos = { x:0, y:0 };
    var lastPos = mousePos;

    $("#"+canvas).on("mousedown", function (e) {
        lastPos = getMousePos(c, e);
        mouseOrTapDown(ctx,js,lastPos,mousePos);
    });
    $("#"+canvas).on("touchstart", function (e) {
        lastPos = getTouchPos(c, e);
        mouseOrTapDown(ctx,js,lastPos,mousePos);
    });
    $("#"+canvas).on("mouseup", function (e) {
        stopDrawing(ctx,js);
    });
    $("#"+canvas).on("touchend", function (e) {
        stopDrawing(ctx,js);
    });
    $("#"+canvas).on("mousemove", function (e) {
        if(drawing){
            var mousePos = getMousePos(c, e);
            mouseOrTapMove(ctx,js,mousePos)
        }
    });
    $("#"+canvas).on("touchmove", function (e) {
        if(drawing){
            var mousePos = getTouchPos(c, e);
            mouseOrTapMove(ctx,js,mousePos);
        }
    });
}

function offerATrade(js,name){
    $('#myModal').modal('show');
    var modal = $('#myModal');
    modal.find('.modal-title').html("Scegli cosa vuoi scambiare");
    var mbody= modal.find('.modal-body');
    mbody.html('');

    var fbody=modal.find('.modal-footer');

    fbody.html('<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>'+
    '<button id="change" type="button" class="btn btn-primary">Scambia</button>');

    var j = findItemByName(nearPlace,name);
    var item = nearPlace.items[j];
    console.log(j);
    var img = item.img;

    $("#inventario .media").each(function(){
        var i_img = $(this).find('img').attr('src');
        var i_name = $(this).find('label').text();
        mbody.append(
            '<li class="list-group-item">'+
                    '<div class="row">'+
                        '<div class="col-md-3">'+
                            '<img class="media-object item" src="'+i_img+'" />'+
                        '</div>'+
                        '<div class="col-md-2">'+
                            '<label>'+i_name+'</label><br />'+
                        '</div>'+
                        '<div class="col-md-2">'+
                            '<span class="glyphicon glyphicon-refresh verybig" ></span>'+
                        '</div>'+
                        '<div class="col-md-2">'+
                            '<label>'+name+'</label><br />'+
                        '</div>'+
                        '<div class="col-md-3">'+
                            '<img class="media-object item" src="'+IMAGE_PATH+img+'.png" />'+
                        '</div>'+
                    '</div>'+
                    '<div class="bottoni-p-s">'+
                        '<hr />'+
                        '<input type="radio" name="selectexchange" nome="'+i_name+'" img="'+i_img+'" /> Seleziona questo scambio'+
                    '</div>'+
            '</li>');
    });

    $("#change").click(function(){
        var s_name=$('input:radio[name=selectexchange]:checked').attr('nome');
        var s_img=$('input:radio[name=selectexchange]:checked').attr('img');
        console.log("Change : " + item + " with: " + s_name);
        console.log(s_img);
        if (!isExchangeAllowed(item.name,s_name)){
            bootbox.alert({
                size: 'small',
                message: '<h4>Questo scambio non è ammesso!</h4>'
            });
        } else {
        nearPlace.items.push({"name":s_name,"img":s_img.split('/')[3].split('.')[0]});
        $('button[value='+s_name+']').parent().parent().remove();

        $("#inventario").append('<li class="list-group-item">'+
                                '<div class="media">'+
                                    '<div class="media-left">'+
                                        '<img class="media-object item" src="'+IMAGE_PATH+item.img+'.png" />'+
                                    '</div>'+
                                    '<div class="media-body">'+
                                        '<label>'+item.name+'</label><br />'+
                                    '</div>'+
                                '</div>'+
                                '<div class="bottoni-p-s">'+
                                    '<hr />'+
                                    '<button class="btn btn-sm btn-primary leave" value="'+item.name+'" img="'+item.img+'">lascia</button>'+
                                '</div>'+
                            '</li>');
        nearPlace.items.splice(j,1);
        loadPositionItemMap(nearPlace,js);
        $('#myModal').modal('hide');

        if(item.name=="macchina"){
            updateMapPointer(IMAGE_PATH+"map_macchina.png");
        }
        if(s_name=="macchina"){
            updateMapPointer(IMAGE_PATH+"map.png");
        }
        changeItem(js,nearPlace.name,item.name,s_name,orario);
        }
    });
}

function waitTime() {
    $('#myModal').modal('show');
    var modal = $('#myModal');
    modal.find('.modal-title').html("Fino a che ora vuoi aspettare?");
    var mbody = modal.find('.modal-body');
    mbody.html('');
    var ore = "";
    var minuti = "";
    for (var i = parseInt(orario / 2); i < 24; i++) {
        ore = ore + '<option id="hour" value="' + (i - orario / 2) * 2 + '">' + i + '</option>';
    }
    if (orario % 2 === 0) {
        minuti = '<option id="min00" selected=\"true\" value="0">00</option><option id="min30" value="1">30</option>';
    }
    else {
        minuti = '<option id="min00" disabled=\"true\" value="0">00</option><option id="min30" selected=\"true\" value="1">30</option>';
    }
    mbody.html(
            '<div class="centered">' +
            '<h3>Ti trovi in <b>' + nearPlace.name + '</b> e sono le <b>' + getFormattedOrario(orario) + '</b></h3>' +
            '<h3>Aspetta le:</h3>' +
            '<select id="selore">' + ore + '</select><label> ore</label>' +
            '<select id="selminuti">' + minuti + '</select><label> minuti</label>' +
            '</div>'
            );

    var fbody = modal.find('.modal-footer');

    fbody.html('<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>' +
            '<button id="aspore" type="button" class="btn btn-primary">Aspetta</button>');

    $("#aspore").click(function () {
        var tempo = parseInt($('#selore').val()) + parseInt($('#selminuti').val());
        stillN(tempo, nearPlace.name, orario);
        orario = orario + tempo;
        updateTempo();
        $('#myModal').modal('hide');
    });
    
    $("#selore").change(function () {
        if (orario % 2 !== 0) {
            if ($("#selore").val() != (parseInt(orario / 2) - orario / 2) * 2) {
                $("#min00").prop("disabled", false);
            }
            else {
                $("#min00").prop("disabled", true);
                $("#min30").prop("selected", true);
            }
        }
    });
}

/*
function waitTimeOld(){
    $('#myModal').modal('show');
    var modal = $('#myModal');
    modal.find('.modal-title').html("Fino a che ora vuoi aspettare?");
    var mbody= modal.find('.modal-body');
    mbody.html('');
    mbody.html(
            '<h3>Ti trovi in '+nearPlace.name+' e sono le '+orario+':00</h3>'+
            '<ul class="list-group">'+
                    '<li class="list-group-item">'+
                        '<input type="radio" name="selectexchange" tempo="1" /> Aspetta 30 minuti ('+getFormattedOrario(orario)+' -> '+getFormattedOrario(orario+1)+')'+
                    '</li>'+
                    '<li class="list-group-item">'+
                        '<input type="radio" name="selectexchange" tempo="2" /> Aspetta 1 ora ('+getFormattedOrario(orario)+' -> '+getFormattedOrario(orario+2)+')'+
                    '</li>'+
                    '<li class="list-group-item">'+
                        '<input type="radio" name="selectexchange" tempo="3" /> Aspetta 1 ora e 30 minuti ('+getFormattedOrario(orario)+' -> '+getFormattedOrario(orario+3)+')'+
                    '</li>'+
                    '<li class="list-group-item">'+
                        '<input type="radio" name="selectexchange" tempo="4" /> Aspetta 2 ore ('+getFormattedOrario(orario)+' -> '+getFormattedOrario(orario+4)+')'+
                    '</li>'+
            '</ul>');

    var fbody=modal.find('.modal-footer');

    fbody.html('<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>'+
    '<button id="aspore" type="button" class="btn btn-primary">Aspetta</button>');
    
    $("#aspore").click(function(){
        var tempo=$('input:radio[name=selectexchange]:checked').attr('tempo');
        tempo=parseInt(tempo);
        stillN(tempo,nearPlace.name,orario);
        orario=orario+tempo;
        updateTempo();
        $('#myModal').modal('hide');
    });
}*/


function appuntamento(){
    $('#myModal').modal('show');
    var modal = $('#myModal');
    modal.find('.modal-title').html("Sei ufficialmente passato per "+nearPlace.name);
    var mbody= modal.find('.modal-body');
    mbody.html('');
    mbody.html(
            '<h3>Ti trovi in '+nearPlace.name+' alle '+orario+':00</h3>');
    var fbody=modal.find('.modal-footer');

    fbody.html('<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>');
}

function endWindow(solved){
    $('#myModal').modal('show',{"backdrop":"static","keyboard":false});
    var modal = $('#myModal');
    var mbody= modal.find('.modal-body');
    mbody.html('');
    if(solved){
        modal.find('.modal-title').html("Hai completato il piano con successo!");
        mbody.html('<p>Hai trovato una soluzione in '+ACTIONS.length+' mosse!</p><p id="loadperformance">Caricamento risultati performance...</p>');
    }
    else{
        modal.find('.modal-title').html("Non sei riuscito a completare il piano!");
        mbody.html('<p>Purtroppo non sei riuscito a risolvere il problema</p><p id="loadperformance">Caricamento risultati performance...</p>');
    }
 
       $("#loadperformance").load('getproblemperformance?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(ACTIONS)+'&sessid='+sessid+'&color='+colorenable+"&belief="+encodeURI(BELIEF));

    var fbody=modal.find('.modal-footer');
    fbody.html('<a type="button" class="btn btn-default" href="pianificazione1phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(ACTIONS)+'&sessid='+sessid+'&color='+colorenable+'" >Ok</a>');
   // $('#myModal').on('hidden.bs.modal', function () {
     //       location.href='pianificazione1phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(ACTIONS)+'&sessid='+sessid+'&color='+colorenable;
    //});
}