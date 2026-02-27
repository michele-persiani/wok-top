var mimg=[];
var ROAD_FAULT_TOLLERANCE = 55;
var ROAD_FAULT_TOLLERANCE_STRICKT = 30;
var MAP_SIZE_X = 800;
var MAP_SIZE_Y = 600;
var IMAGE_PATH='resources/images/locali/';
var MAP_POINTER = IMAGE_PATH+'map.png';

var PLACE_SIZE = 100;


function composeTime(x){
    var h=parseInt(x/2);
    var m=twoDigit(x%2*30);
    return ""+h+":"+m;
}
// Load image lazy wait untill image is full loaded before drawing it on map!
function lazyLoad(ctx,ii,js){
    clockImg = new Image();
    clockImg.src = IMAGE_PATH+'clock.png';
    var colorcode="";
    if(colorenable==true){
        colorcode="_c"
    }
    if(ii<js.places.length){
        var p = js.places[ii];
        var img = new Image();
        img.src = IMAGE_PATH+p.img+colorcode+'.png';
        img.onload = function(){
            ctx.save();
              ctx.drawImage(img,p.x,p.y,PLACE_SIZE,PLACE_SIZE);
              //if(!p.name.includes("rotonda")){
              if(p.name.indexOf("rotonda")===-1){
                drawTextOnPlace(p.name,p.x,p.y,ctx,20);
                drawTextOnPlace(composeTime(p["open-time"])+" - "+composeTime(p["close-time"]),p.x,p.y,ctx,4);
              }
              if(p.wait){
                ctx.drawImage(clockImg,p.x,p.y+PLACE_SIZE-16,16,16);
              }
            ctx.restore();
            ii++;
            lazyLoad(ctx,ii,js);
        }
        mimg.push(img);
    }
    if(!drawing){
      drawPointerOnPlace(ctx,nearPlace);
    }
}

/*
 * FUNCTION FOR DRAWING THINGS ON CANVAS!
 */
function clearScreen(ctx){
    ctx.save();
      ctx.fillStyle="#ffffff";
      ctx.fillRect(0,0,MAP_SIZE_X,MAP_SIZE_Y);
    ctx.restore();
}

function drawRoadRaw(ctx,r){
    ctx.beginPath();
        ctx.moveTo(r.ix+PLACE_SIZE/2,r.iy+PLACE_SIZE/2);
        ctx.lineTo(r.ex+PLACE_SIZE/2,r.ey+PLACE_SIZE/2);
        ctx.stroke();
    ctx.closePath();
}
function drawRoad(ctx,r){
    ctx.save();
        switch(r.type){
            case "both":
                ctx.strokeStyle="#994d00";
                ctx.lineWidth=40;
                drawRoadRaw(ctx,r);
                ctx.strokeStyle="#585858";
                ctx.lineWidth=30;
                drawRoadRaw(ctx,r);
                drawRoadWhite(ctx,r);
            break;
            case "road":
                ctx.strokeStyle="#585858";
                ctx.lineWidth=30;
                drawRoadRaw(ctx,r);
                drawRoadWhite(ctx,r);
            break;
            case "path":
                ctx.strokeStyle="#994d00";
                ctx.lineWidth=30;
                drawRoadRaw(ctx,r);
            break;
            default:
                ctx.strokeStyle="#994d00";
                ctx.lineWidth=40;
                drawRoadRaw(ctx,r);
                ctx.strokeStyle="#585858";
                ctx.lineWidth=30;
                drawRoadRaw(ctx,r);
                drawRoadWhite(ctx,r);
            break;
        };        
    ctx.restore();
}
function drawRoadWhite(ctx,r){
    ctx.save();
        ctx.beginPath();
        ctx.strokeStyle="#ffffff";
        ctx.lineWidth=5;
        ctx.moveTo(r.ix+PLACE_SIZE/2,r.iy+PLACE_SIZE/2);
        ctx.setLineDash([10,15])
        ctx.lineTo(r.ex+PLACE_SIZE/2,r.ey+PLACE_SIZE/2);
        ctx.stroke();
        ctx.closePath();
    ctx.restore();
}
function drawPlaces(ctx,js){
    for(var i=0;i<js.places.length;i++){
        var p = js.places[i];
        ctx.drawImage(mimg[i],p.x,p.y,PLACE_SIZE,PLACE_SIZE);
        //if(!p.name.includes("rotonda")){
        if(p.name.indexOf("rotonda")===-1){  
            drawTextOnPlace(p.name,p.x,p.y,ctx,20);
            drawTextOnPlace(composeTime(p["open-time"])+" - "+composeTime(p["close-time"]),p.x,p.y,ctx,4);
        }
        if(p.wait){
            ctx.drawImage(clockImg,p.x,p.y+PLACE_SIZE-16,16,16);
        }
    }
}

function drawTextOnPlace(name,x,y,ctx,top){
    ctx.save();
        var font_size=12;
        ctx.fillStyle="#FF0000";
        ctx.font = font_size+'pt Verdana';
        var padding = PLACE_SIZE/2-ctx.measureText(name).width/2;
        // ctx.fillStyle="#FFFFFF";
        ctx.shadowColor ="#FFFFFF";
        ctx.shadowBlur = 2;
        ctx.shadowOffsetX = 1;
        ctx.shadowOffsetY = 1;
        // ctx.fillText(name,x+padding-1,y-6-1);
        ctx.fillStyle="#FF0000";
        ctx.fillText(name,x+padding,y-top);
    ctx.restore();
    
    ctx.save();
        var font_size=12;
        ctx.fillStyle="#0000FF";
        ctx.font = font_size+'pt Verdana';
        var padding = PLACE_SIZE/2-ctx.measureText(name).width/2;
        // ctx.fillStyle="#FFFFFF";
        ctx.shadowColor ="#FFFFFF";
        ctx.shadowBlur = 2;
        ctx.shadowOffsetX = 1;
        ctx.shadowOffsetY = 1;
        // ctx.fillText(name,x+padding-1,y-6-1);
        ctx.fillStyle="#FF0000";
        ctx.fillText(name,x+padding,y-top);
    ctx.restore();
}

function twoDigit(x){
    if(x<=9){
        return "0"+x;
    }
    return x;
}
function getFormattedOrario(orario){
    var tempo=orario/2;
    tempo=parseInt(orario/2)+":"+twoDigit((orario%2)*30);
    return tempo;
}
function updateTempo(){
    var tempo=orario/2;
    tempo=parseInt(orario/2)+":"+twoDigit((orario%2)*30);
    $("#tempo").html('<span class="glyphicon glyphicon-time small" aria-hidden="true"></span> '+tempo);
}
function drawMap(ctx,js){

    clearScreen(ctx);
    for(var i=0;i<js.roads.length;i++){
        var r = js.roads[i];
        drawRoad(ctx,r);
        // drawRoadWhite(ctx,r);
    }

    if(mimg.length>0){
        drawPlaces(ctx,js);
    }else{
        var ii =0
        lazyLoad(ctx,ii,js);
    }

    updateTempo();
    if(!drawing){
      drawPointerOnPlace(ctx,nearPlace);
    }
}

function drawPath(ctx,p){
    ctx.save();
    ctx.beginPath();
        ctx.strokeStyle="red";
        ctx.lineWidth=2;
        ctx.arc(p.x,p.y,3,0,2*Math.PI);
        ctx.stroke();
    ctx.closePath();
    ctx.restore();
}
function drawPathOnRoad(ctx,p,road,nearPlace,js){
    drawMap(ctx,js);
    ctx.save();
    ctx.beginPath();
        ctx.strokeStyle="red";
        ctx.lineWidth=5;
        ctx.moveTo(nearPlace.x+PLACE_SIZE/2,nearPlace.y+PLACE_SIZE/2);
        var y1 = road.iy+PLACE_SIZE/2;
        var y2 = road.ey+PLACE_SIZE/2;
        var x1 = road.ix+PLACE_SIZE/2;
        var x2 = road.ex+PLACE_SIZE/2;
        var m = (y2-y1)/(x2-x1);
        var q = -m*x1+y1;
        var y = m*(p.x)+q;
        ctx.lineTo(p.x,y);
        ctx.stroke();
    ctx.closePath();

    var img = new Image();
    img.src = MAP_POINTER;// 'locali/map.png';
    ctx.drawImage(img,p.x-PLACE_SIZE/2,y-PLACE_SIZE/2,PLACE_SIZE,PLACE_SIZE);
    ctx.restore();
}
function updateMapPointer(img){
    MAP_POINTER = img;
}

function drawPointerOnPlace(ctx,place){
  var img = new Image();
  img.src = MAP_POINTER;// 'locali/map.png';
  ctx.drawImage(img,place.x,place.y,PLACE_SIZE,PLACE_SIZE);
  ctx.restore();
}

/*
 * EVENTS ON MAP
 */
function mouseOrTapDown(ctx,js,lastPos,mousePos){
    rightRoad=0;
    nearRoad = distanze(lastPos,js);
    var tnearPlace = distanzaMinPlace(lastPos,js);
    if(tnearPlace!=false){
      if(tnearPlace==nearPlace){
        drawing = true;
        nearPlace = tnearPlace;
      }
    }else{
      stopDrawing(ctx,js);
    }
}
function mouseOrTapMove(ctx,js,mousePos){
    var place = distanza(mousePos,js);
    if(place!=false){
        drawPathOnRoad(ctx,mousePos,place,nearPlace,js);
    }else{
        stopDrawing(ctx,js);
    }
    np = distanzaMinPlace(mousePos,js);
    if(np!=false){
        drawMap(ctx,js);
        if(np!=nearPlace){
            moveToMap(ctx,js,np);
        }
        nearPlace = np;
    }
}

// Event that occurs when move to a new place
function moveToMap(ctx,js,np){
    if(MAP_POINTER==IMAGE_PATH+'map.png'){
        moveTo(nearPlace.name,np.name,orario);
        orario++;
        updateTempo();
    }else{
        driveTo(nearPlace.name,np.name,orario);
    }
    
    loadPositionItemMap(np,js);
    nearPlace = np;
    stopDrawing(ctx,js);
    updateWaiting();
}

function updateWaiting(){
    console.log("Aspetto? "+nearPlace.wait);
    $("#aspetta").prop("disabled",!nearPlace.wait);
}

// Return mouse Position on Canvas
function getMousePos(canvasDom, mouseEvent) {
  var rect = canvasDom.getBoundingClientRect();
  return {
    x: mouseEvent.clientX - rect.left,
    y: mouseEvent.clientY - rect.top
  };
}
// Return touch position on canvas
function getTouchPos(canvasDom, touchEvent) {
  touchEvent = touchEvent.originalEvent;
  var rect = canvasDom.getBoundingClientRect();
  return {
    x: touchEvent.touches[0].clientX - rect.left,
    y: touchEvent.touches[0].clientY - rect.top
  };
}

// Calculate the distance betwen the position of mouse (pos) and a rect defined
// by the most nearest road!
function distanza(pos,js){
    if(nearRoad.length==1){
        var y1 = nearRoad[0].iy+PLACE_SIZE/2;
        var y2 = nearRoad[0].ey+PLACE_SIZE/2;
        var x1 = nearRoad[0].ix+PLACE_SIZE/2;
        var x2 = nearRoad[0].ex+PLACE_SIZE/2;
        var d = dotLineLength(pos.x,pos.y,x1,y1,x2,y2,true);
        if(d<ROAD_FAULT_TOLLERANCE){
            return nearRoad[0];
        }
    }else{
        for(var i=0;i<js.roads.length;i++){
            var y1 = js.roads[i].iy+PLACE_SIZE/2;
            var y2 = js.roads[i].ey+PLACE_SIZE/2;
            var x1 = js.roads[i].ix+PLACE_SIZE/2;
            var x2 = js.roads[i].ex+PLACE_SIZE/2;
            var d = dotLineLength(pos.x,pos.y,x1,y1,x2,y2,true);
            if(d<ROAD_FAULT_TOLLERANCE){
                if(nearRoad.length==1){
                    var idx = findInNearRoad(js.roads[i]);
                    if(idx!=null){
                        return js.roads[i];
                    }
                }else{
                    if(d<ROAD_FAULT_TOLLERANCE_STRICKT){
                        var idx = findInNearRoad(js.roads[i]);
                        if(idx!=null){
                            rightRoad++;
                            if(rightRoad>maxRightRoad){
                                console.log("abbastanza tick");
                                nearRoad=[js.roads[i]];
                            }
                            return js.roads[i];
                        }
                    }
                }
            }
        }
    }
    return false;
}

function distanze(pos,js){
    var road=[];
    for(var i=0;i<js.roads.length;i++){
        var y1 = js.roads[i].iy+PLACE_SIZE/2;
        var y2 = js.roads[i].ey+PLACE_SIZE/2;
        var x1 = js.roads[i].ix+PLACE_SIZE/2;
        var x2 = js.roads[i].ex+PLACE_SIZE/2;
        var d = dotLineLength(pos.x,pos.y,x1,y1,x2,y2,true);
        if(d<ROAD_FAULT_TOLLERANCE_STRICKT){
            road.push(js.roads[i]);
        }
    }
    return road;
}

function findInNearRoad(road){
    if((nearRoad!=null)&&(nearRoad.length>0)){
        for(var i=0;i<nearRoad.length;i++){
            if((road.ix==nearRoad[i].ix)&&(road.ex==nearRoad[i].ex)&&(road.iy==nearRoad[i].iy)&&(road.ey==nearRoad[i].ey)){
                // nearRoad=[nearRoad[i]];
                return nearRoad[i];
            }
        }
    }
    return null;
}

// Return the matematical distance between the mouse (pos) and a free point x,y
function distanzaPoint(pos,x,y){
    return Math.sqrt((pos.x-x)*(pos.x-x)+(pos.y-y)*(pos.y-y));
}
// Return the place where the distance between mouse and the place are minimal
// and the distance is lower than 50px
// return false if no place are right!
function distanzaMinPlace(pos,js){
    var dmin=1000;
    var didx=-1;
    for(var i=0;i<js.places.length;i++){
        d = distanzaPoint(pos,js.places[i].x+50,js.places[i].y+50);
        if((d<dmin)&&(d<50)){
            dmin = d;
            didx = i;
        }
    }
    if(didx!=-1){
        return js.places[didx];
    }
    else {
        return false;
    }
}

function stopDrawing(ctx,js){
    nearRoad = [];
    rightRoad=0;
    drawing = false;
    drawMap(ctx,js);
}

// + Jonas Raoni Soares Silva
// @ http://jsfromhell.com/math/dot-line-length [rev. #1]

dotLineLength = function(x, y, x0, y0, x1, y1, o){
    function lineLength(x, y, x0, y0){
        return Math.sqrt((x -= x0) * x + (y -= y0) * y);
    }
    if(o && !(o = function(x, y, x0, y0, x1, y1){
        if(!(x1 - x0)) return {x: x0, y: y};
        else if(!(y1 - y0)) return {x: x, y: y0};
        var left, tg = -1 / ((y1 - y0) / (x1 - x0));
        return {x: left = (x1 * (x * tg - y + y0) + x0 * (x * - tg + y - y1)) / (tg * (x1 - x0) + y0 - y1), y: tg * left - tg * x + y};
    }(x, y, x0, y0, x1, y1), o.x >= Math.min(x0, x1) && o.x <= Math.max(x0, x1) && o.y >= Math.min(y0, y1) && o.y <= Math.max(y0, y1))){
        var l1 = lineLength(x, y, x0, y0), l2 = lineLength(x, y, x1, y1);
        return l1 > l2 ? l2 : l1;
    }
    else {
        var a = y0 - y1, b = x1 - x0, c = x0 * y1 - y0 * x1;
        return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
    }
};
