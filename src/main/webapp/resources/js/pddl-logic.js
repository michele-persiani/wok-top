var BELIEF;
var idproblem;
var ACTIONS=[];
function sendAction(action,callback){
    if (callback === undefined) {
        callback = function(){
            $(".loader").hide();
            $(".modalloader").hide();
        };
    }
    $(".loader").show();
    $(".modalloader").show();
    ACTIONS.push(action);
    $.post("doaction",{"belief":BELIEF,"id":idproblem,"action":action},function(data){
        var js=JSON.parse(data);
        if(js.hasSolution==false){
            BELIEF=js.belief;
            endWindow(false);
            //alert("Il problema da qui in poi non ha più una soluzione!");
        }else{
            if(js.isSolved==true){
                //alert("Complimenti hai risolto il problema");
                endWindow(true);
                BELIEF=js.belief;
                callback();
            }else{
                console.log("ben fatto!");
                BELIEF=js.belief;
                callback();
            }   
            
        }
    })
}

function moveTo(src,dst,t){
    var t1 = t+1;
    sendAction('(move-to '+src+' '+dst+' p'+t+' p'+t1+')');
}

function driveTo(src,dst,t){
    sendAction('(drive-to '+src+' '+dst+' macchina p'+t+')');
}

function takeItemAt(js,pos,item,t){
    var to = getOraApertura(js,pos);
    var tp = to-1;   
    var tc = getOraChiusura(js,pos);
    
    if(t==to){
        sendAction('(take-item-at-now '+pos+' '+item+' p'+tp+' p'+t+' p'+tc+')');
    }else
        sendAction('(take-item-at '+pos+' '+item+' p'+tp+' p'+to+' p'+tc+' p'+t+')');
}

function changeItem(js,pos,item,change,t){
    var to = getOraApertura(js,pos);
    var tp = to-1;
    var tc=getOraChiusura(js,pos);
    sendAction('(change-item '+pos+' '+item+' '+change+' p'+tp+' p'+to+' p'+tc+')');
}

function dropItemAt(js,pos,item,t){
    var to = getOraApertura(js,pos);
    var tp = to-1;   
    var tc=getOraChiusura(js,pos);
    if(t==to){
        sendAction('(drop-item-at-now '+pos+' '+item+' p'+tp+' p'+t+' p'+tc+')');
    }else
        sendAction('(drop-item-at '+pos+' '+item+' p'+tp+' p'+to+' p'+tc+' p'+t+')');
}

function still1(pos,t,callback){
    var tn = t+1;
    sendAction('(still-1h '+pos+' p'+t+' p'+tn+')',callback);
}

function still2(pos,t,callback){
    var t1 = t+1;
    var t2 = t1+1;
    sendAction('(still-2h '+pos+' p'+t+' p'+t1+' p'+t2+')',callback);
}

function still3(pos,t,callback){
    var t1 = t+1;
    var t2 = t1+1;
    var t3 = t2+1;
    sendAction('(still-3h '+pos+' p'+t+' p'+t1+' p'+t2+' p'+t3+')',callback);
}

function still4(pos,t,callback){
    var t1 = t+1;
    var t2 = t1+1;
    var t3 = t2+1;
    var t4 = t3+1;
    sendAction('(still-4h '+pos+' p'+t+' p'+t1+' p'+t2+' p'+t3+' p'+t4+')',callback);
}

function stillNOld(tempo,pos,t,callback){
    switch(tempo){
        case 1:
                still1(pos,t,callback);
            break;
        case 2:
                still2(pos,t,callback);
            break;
        case 3:
                still3(pos,t,callback);
            break;
        case 4:
                still4(pos,t,callback);
            break;
    }
}

function stillList(listT,pos,t){
    console.log("entro in ricorsione: tempo: "+t);
    if(listT.length>0){
        var tempo=listT.pop();
        console.log("Richiedo attesa: "+tempo);
        stillNOld(tempo,pos,t,function(){
            stillList(listT,pos,t+tempo);
        });
    }else{
        $(".loader").hide();
        $(".modalloader").hide();
    }
}


function stillN(tempo,pos,t){
    console.log(tempo);
    var listT=[];
    var n=Math.floor(tempo/4);
    console.log("Uso: 4*"+n);
    for(var i=0;i<n;i++)
        listT.push(4);
    var tt=tempo%4;
    console.log("Rimane: "+tt);
    n=Math.floor(tt/3);
    console.log("Uso: 3*"+n);
    for(var i=0;i<n;i++)
        listT.push(3);
    tt=tt%3;
    console.log("Rimane: "+tt);
    n=Math.floor(tt/2);
    console.log("Uso: 2*"+n);
    for(var i=0;i<n;i++)
        listT.push(2);
    tt=tt%2;
    console.log("Rimane: "+tt);
    for(var i=0;i<tt;i++)
        listT.push(1);
    console.log("Lista: "+listT);
    
    stillList(listT,pos,t);
    
    /*switch(tempo){
        case 1:
                still1(pos,orario);
            break;
        case 2:
                still2(pos,orario);
            break;
        case 3:
                still3(pos,orario);
            break;
        case 4:
                still4(pos,orario);
            break;
    }*/
}

function appointment(pos,t){
    sendAction('(appointment '+pos+' p'+t+')');
}

function getOraChiusura(js,pos){
    for(var i=0;i<js.places.length;i++){
        if(js.places[i].name==pos){
            return js.places[i]['close-time'];
        }
    }
    return 24;
}

function getOraApertura(js,pos){
    for(var i=0;i<js.places.length;i++){
        if(js.places[i].name==pos){
            return js.places[i]['open-time'];
        }
    }
    return 1;
}
