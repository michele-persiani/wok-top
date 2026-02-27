/* 
    Document   : pddl-pianificazione2b
    Created on : 18-gen-2018, 14.33.17
    Author     : Bartolomeo Lombardi
*/

var listNodesVisited = [];
var diffGoals = [];
var winStatus = false;
var gameOverStatus = false;

console.log(listGoals.toString());
console.log(parameters.toString());

var ctx = null;
var tileW = 32, tileH = 32;
var mapW = 25, mapH = 19;

var currentSecond = 0, frameCount = 0, framesLastSecond = 0, lastFrameTime = 0;
var minutesLabel, secondsLabel;
var pTime = 0;
var moveCount = 0;
var vstart=false;
/*function pad ( val ) { return val > 9 ? val : "0" + val; }

setInterval( function() {
    secondsLabel = (pad(++pTime%60));
    minutesLabel = (pad(parseInt(pTime/60,10)));
}, 1000);
*/
var tileset = null, tilesetURL = "resources/images/zoo/tiles.png", tilesetLoaded = false;

if(!colorenable)
{
    tilesetURL = "resources/images/zoo/tilesbw.png"
}

var floorTypes = {
    solid	: 0,
    shadow	: 1,
    road	: 2
};

var tileTypes = {
    0 :  { colour:"#685b48", floor:floorTypes.solid, index:0,type:"grass", visited:null, sprite:[{x:0,y:0,w:64,h:64}]	},
    1 :  { colour:"#5aa457", floor:floorTypes.shadow, index:1,type:"shadow", visited:null,sprite:[{x:64,y:0,w:64,h:64}]	},
    2 :  { colour:"#c58f5c", floor:floorTypes.road, index:2,type:"road", visited:null,sprite:[{x:128,y:0,w:64,h:64}]	},
    3 :  { colour:"#286625", floor:floorTypes.solid,index:3,type:"top-tree", visited:null,sprite:[{x:192,y:0,w:64,h:64}]	},
    4 :  { colour:"#678fd9", floor:floorTypes.solid,index:4,type:"bottom-tree", visited:null,sprite:[{x:256,y:0,w:64,h:64}]	},
    5 :  { colour:"#678fd9", floor:floorTypes.road,index:5,type:"fiume", visited:null,sprite:[{x:320,y:0,w:64,h:64}]	},
    6 :  { colour:"#678fd9", floor:floorTypes.shadow,index:6,type:"orso", visited:false,sprite:[{x:0,y:128,w:64,h:64}]	},
    7 :  { colour:"#678fd9", floor:floorTypes.shadow,index:7,type:"tigre", visited:false,sprite:[{x:64,y:128,w:64,h:64}]	},
    8 :  { colour:"#678fd9", floor:floorTypes.shadow,index:8,type:"scimmia", visited:false,sprite:[{x:128,y:128,w:64,h:64}]	},
    9 :  { colour:"#678fd9", floor:floorTypes.shadow,index:9,type:"leone", visited:false,sprite:[{x:192,y:128,w:64,h:64}]	},
    10 : { colour:"#678fd9", floor:floorTypes.shadow,index:10,type:"elefante", visited:false,sprite:[{x:256,y:128,w:64,h:64}]	},
    11 : { colour:"#678fd9", floor:floorTypes.shadow,index:11,type:"ippopotamo", visited:false,sprite:[{x:320,y:128,w:64,h:64}]	},
    12 : { colour:"#678fd9", floor:floorTypes.shadow,index:12,type:"lama", visited:false,sprite:[{x:0,y:192,w:64,h:64}]	},
    13 : { colour:"#678fd9", floor:floorTypes.shadow,index:13,type:"serpente", visited:false,sprite:[{x:64,y:192,w:64,h:64}]	},
    14 : { colour:"#678fd9", floor:floorTypes.shadow,index:14,type:"uccello", visited:false,sprite:[{x:128,y:192,w:64,h:64}]	},
    15 : { colour:"#678fd9", floor:floorTypes.shadow,index:15,type:"scoiattolo", visited:false,sprite:[{x:192,y:192,w:64,h:64}]	},
    16 : { colour:"#678fd9", floor:floorTypes.shadow,index:16,type:"bar", visited:false,sprite:[{x:256,y:192,w:64,h:64}]	},
    17 : { colour:"#678fd9", floor:floorTypes.shadow,index:17,type:"toilette", visited:false,sprite:[{x:320,y:192,w:64,h:64}]	},
};

var directions = {
    pause:-1,
    up: 0,
    right: 1,
    down: 2,
    left: 3
};

var keysDown = {
    36 : false,
    37 : false,
    38 : false,
    39 : false,
    40 : false
};

var player = new Character();

function Character()
{
    this.tilePrevious = new Array([0,10]);  //nuova var (tile precedenti in cui sono stato)
    this.tileFrom	= [0,16];
    this.tileTo		= [0,16];
    this.timeMoved	= 0;
    this.dimensions	= [32,32];
    this.position	= [0,512];
    this.delayMove	= 400;

    this.direction	= directions.right;
    this.sprites = {};
    this.sprites[directions.up]		= [{x:0,y:64, w:64,h:64}, {x:320,y:64,w:64,h:64}];
    this.sprites[directions.right]	= [{x:192,y:64,w:64,h:64}];
    this.sprites[directions.pause]      = [{x:192,y:64,w:64,h:64}];
    this.sprites[directions.left]	= [{x:128,y:64,w:64,h:64}];
    this.sprites[directions.down]	= [{x:64,y:64,w:64,h:64}, {x:256,y:64,w:64,h:64}];
}

Character.prototype.placeAt = function(x, y)
{
    //aggiungo la posizione precedente
    this.tilePrevious.push(this.tileFrom);
    console.log(listNodesVisited);

    this.tileFrom	= [x,y];
    this.tileTo		= [x,y];
    this.position	= [((tileW*x)+((tileW-this.dimensions[0])/2)),
            ((tileH*y)+((tileH-this.dimensions[1])/2))];
    
    console.log(x + " " + y);

    switch("["+ x +","+ y +"]")
    {
        // entrata
        case "[6,16]":
                console.log("entrata");
                //if(listNodesVisited[listNodesVisited.length - 1] != "entrata")
                        listNodesVisited.push("entrata");
                break;
        // n1	
        case "[11,16]":
                console.log("n1");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n1")
                        listNodesVisited.push("n1");
                break;
        // n2
        case "[16,16]":
                console.log("n2");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n2")
                        listNodesVisited.push("n2");
                break;
        // n3
        case "[6,13]":
                console.log("n3");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n3")
                        listNodesVisited.push("n3");
                break;
        // n4	
        case "[6,10]":
                console.log("n4");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n4")
                        listNodesVisited.push("n4");
                break;
        // n5
        case "[2,10]":
                console.log("n5");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n5")
                        listNodesVisited.push("n5");
                break;
        // n6
        case "[17,8]":
                console.log("n6");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n6")
                        listNodesVisited.push("n6");
                break;
        // n7
        case "[12,8]":
                console.log("n7");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n7")
                        listNodesVisited.push("n7");
                break;
        // n8
        case "[17,3]":
                console.log("n8");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n8")
                        listNodesVisited.push("n8");
                break;
        // n9
        case "[8,3]":
                console.log("n9");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n9")
                        listNodesVisited.push("n9");
                break;
        // n10
        case "[2,3]":
                console.log("n10");
                //if(listNodesVisited[listNodesVisited.length - 1] != "n10")
                        listNodesVisited.push("n10");
                break;
        // n11
        case "[16,17]":
                console.log("n11 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n12
        case "[17,11]":
                console.log("n12 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n13
        case "[8,13]":
                console.log("n13 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n14
        case "[12,11]":
                console.log("n14 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n15
        case "[2,13]":
                console.log("n15 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n16
        case "[22,11]":
                console.log("n16 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n17
        case "[2,6]":
                console.log("n17 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n18
        case "[14,5]":
                console.log("n18 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n19
        case "[17,1]":
                console.log("n19 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n20
        case "[8,1]":
                console.log("n20 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n21
        case "[1,3]":
                console.log("n21 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;
        // n22
        case "[23,3]":
                console.log("n22 " + tileTypes[gameMap[toIndex(x,y)]].type);
                //if(listNodesVisited[listNodesVisited.length - 1] != tileTypes[gameMap[toIndex(x,y)]].type)
                        listNodesVisited.push(tileTypes[gameMap[toIndex(x,y)]].type);
                break;

        default:
                break;
    }
};

Character.prototype.processMovement = function(t)
{

    if(this.tileFrom[0] == this.tileTo[0] && this.tileFrom[1] == this.tileTo[1])
    {
            return false;
    }

    if((t-this.timeMoved) >= this.delayMove)
    {
            this.placeAt(this.tileTo[0], this.tileTo[1]);
    }
    else
    {
        this.position[0] = (this.tileFrom[0] * tileW) + ((tileW-this.dimensions[0])/2);
        this.position[1] = (this.tileFrom[1] * tileH) + ((tileH-this.dimensions[1])/2);

        if(this.tileTo[0] != this.tileFrom[0])
        {
                var diff = (tileW / this.delayMove) * (t-this.timeMoved);
                this.position[0]+= (this.tileTo[0]<this.tileFrom[0] ? 0 - diff : diff);
        }
        if(this.tileTo[1] != this.tileFrom[1])
        {
                var diff = (tileH / this.delayMove) * (t-this.timeMoved);
                this.position[1]+= (this.tileTo[1]<this.tileFrom[1] ? 0 - diff : diff);
        }

        this.position[0] = Math.round(this.position[0]);
        this.position[1] = Math.round(this.position[1]);
    }

    return true;
}

Array.prototype.diff = function(a) {
    return this.filter(function(i) { return a.indexOf(i) < 0; });
};

Array.prototype.compare = function(testArr) {
    if (this.length != testArr.length) return false;
    for (var i = 0; i < testArr.length; i++) {
        if (this[i].compare) { 
            if (!this[i].compare(testArr[i])) return false;
        }
        else if (this[i] != testArr[i]) return false;
    }
    return true;
}

Character.prototype.canMoveTo = function(x, y)
{
    if(x < 0 || x >= mapW || y < 0 || y >= mapH) { return false; }
    if(tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.solid) { return false; }

    //if per far concludere il gioco
    if(tileTypes[gameMap[toIndex(x,y)]].index >= 6 && 
                    //tileTypes[gameMap[toIndex(x,y)]].visited == false &&
                    listGoals.sort().compare(diffGoals.sort()) &&
                    tileTypes[gameMap[toIndex(x,y)]].type == inFinalPlace) 
    {
            winStatus = true;

            console.log("diffGoals: " + diffGoals.sort());
            console.log("listGoals: "+ listGoals.sort());
            console.log("liste uguali e posizione corretta");
    }

    //controllo per far scrivere il div con la lista obiettivi e mostra la snackbar
    if(tileTypes[gameMap[toIndex(x,y)]].index >= 6 &&
                    tileTypes[gameMap[toIndex(x,y)]].visited == false &&
                    (listGoals.indexOf(tileTypes[gameMap[toIndex(x,y)]].type) != -1)) 
    {

        tileTypes[gameMap[toIndex(x,y)]].visited = true;


        snackBar.innerHTML = "<table><tr><td>Congratulazioni, hai visitato l'obiettivo: " +
                                tileTypes[gameMap[toIndex(x,y)]].type + ".</td><td><img src=resources/images/zoo/" +
                                tileTypes[gameMap[toIndex(x,y)]].type+".jpg" +
                                " alt=" + tileTypes[gameMap[toIndex(x,y)]].type +
                            "></td></tr></table>";
        snackBar.className = "show";
        setTimeout(function(){ snackBar.className = snackBar.className.replace("show", ""); }, 3000);

        diffGoals.push(tileTypes[gameMap[toIndex(x,y)]].type);

        //console.log(listGoals.toString());
        var elenco = "";
        for(var i = 0; i < listGoals.length; ++i) {
            if(diffGoals.indexOf(listGoals[i]) != -1){
                    elenco += "<li><s><b>" + listGoals[i] + "</b></s></li>";
            }else{ //<img src=resources/images/zoo/"+ listGoals[i]+ ".jpg/>
                    elenco += "<li><b>" + listGoals[i] + "</b></li>";
            }
            divGoals.innerHTML = "<ul>" + elenco + "</ul>";
        }
    }

    if((tileTypes[gameMap[toIndex(this.tileFrom[0], this.tileFrom[1])]].floor == floorTypes.shadow) &&
                    (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.shadow))
    {		
        return true;
            //diffGoals += listGoals.diff([tileTypes[gameMap[toIndex(x,y)]].type]);
    } else{
        for (var i = 0; i < this.tilePrevious.length; ++i) {
            if(x == this.tilePrevious[i][0] && y == this.tilePrevious[i][1] && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.road))
            {
                return false;
            } else if(x == this.tilePrevious[i][0] && y == this.tilePrevious[i][1] && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.shadow)) {
                if(x == this.tilePrevious[this.tilePrevious.length -1 ][0] && y == this.tilePrevious[this.tilePrevious.length - 1][1]) {
                    return false;
                }
            }
        }
    }
    return true;
};

Character.prototype.canMoveToGameOver = function(x, y)
{
    if(x < 0 || x >= mapW || y < 0 || y >= mapH) { return false; }
    if(tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.solid) { return false; }
    if((tileTypes[gameMap[toIndex(this.tileFrom[0], this.tileFrom[1])]].floor == floorTypes.shadow) && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.shadow))
    {		
        return true;
    } else{
        for (var i = 0; i < this.tilePrevious.length; ++i) {
            if(x == this.tilePrevious[i][0] && y == this.tilePrevious[i][1] && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.road))
            {
                return false;
            } else if(x == this.tilePrevious[i][0] && y == this.tilePrevious[i][1] && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.shadow)) {
                if(x == this.tilePrevious[this.tilePrevious.length -1 ][0] && y == this.tilePrevious[this.tilePrevious.length - 1][1]) {
                        return false;
                }
            }
        }
    }
    return true;
};

Character.prototype.canMoveUp = function()
{
    return this.canMoveTo(this.tileFrom[0], this.tileFrom[1] - 1);
};

Character.prototype.canMoveDown = function()
{
    return this.canMoveTo(this.tileFrom[0], this.tileFrom[1] + 1);
};

Character.prototype.canMoveLeft = function()
{
    return this.canMoveTo(this.tileFrom[0] - 1, this.tileFrom[1]);
};

Character.prototype.canMoveRight = function()
{
    return this.canMoveTo(this.tileFrom[0] + 1, this.tileFrom[1]);
};

Character.prototype.checkGameOver = function()
{
    if(!this.canMoveToGameOver(this.tileFrom[0], this.tileFrom[1] - 1) &&
        !this.canMoveToGameOver(this.tileFrom[0], this.tileFrom[1] + 1) &&
        !this.canMoveToGameOver(this.tileFrom[0] - 1, this.tileFrom[1]) && 
        !this.canMoveToGameOver(this.tileFrom[0] + 1, this.tileFrom[1])
    ) {
            return true;
    }else {
            return false;
    }
};

Character.prototype.moveLeft = function(t)
{
    this.tileTo[0] -= 1;
    this.timeMoved = t;
    this.direction = directions.left;
};

Character.prototype.moveRight = function(t)
{
    this.tileTo[0] += 1;
    this.timeMoved = t;
    this.direction = directions.right;
};

Character.prototype.movePause = function(t)
{
    this.tileTo[0] += 0;
    this.timeMoved = t;
    this.direction = directions.pause;
};

Character.prototype.moveUp = function(t)
{
    this.tileTo[1] -= 1;
    this.timeMoved = t;
    this.direction = directions.up;
};
Character.prototype.moveDown = function(t)
{
    this.tileTo[1]+=1;
    this.timeMoved = t;
    this.direction = directions.down;
};

function toIndex(x, y)
{
    return( (y * mapW) + x);
}

window.onload = function()
{   
    vstart=false;
    ctx = document.getElementById('game').getContext("2d");
    divGoals = document.getElementById('ids');
    snackBar = document.getElementById("snackbar");
     
    requestAnimationFrame(drawGame);
    ctx.font = "bold 10pt sans-serif";

//aggiunto l'ascoltatore dell'evento start
 window.addEventListener("keydown", function(e) {
        if(e.keyCode==32) { keysDown[e.keyCode] = true; vstart=true; 
            function pad ( val ) { return val > 9 ? val : "0" + val; }

setInterval( function() {
    secondsLabel = (pad(++pTime%60));
    minutesLabel = (pad(parseInt(pTime/60,10)));
}, 1000);
        }
    });


    window.addEventListener("keydown", function(e) {
        if(e.keyCode>=37 && e.keyCode<=40) { keysDown[e.keyCode] = true; }
    });
    window.addEventListener("keyup", function(e) {
        if(e.keyCode>=37 && e.keyCode<=40) { keysDown[e.keyCode] = false; }
    });

    tileset = new Image();
    tileset.onerror = function()
    {
        ctx = null;
        alert("Failed loading tileset.");
    };
    tileset.onload = function() { tilesetLoaded = true; };
    tileset.src = tilesetURL;


    var elenco = "";
    for(var i = 0; i < listGoals.sort().length; ++i) {
        //<img src=resources/images/zoo/"+ listGoals[i]+ ".jpg style='width: 5%; height: 5%'/>
        elenco += "<li><b>" + listGoals[i] + "</b></li>";
    }
    divGoals.innerHTML = "<ul>" + elenco + "</ul>";
  
};

function drawGame()
{
    if(ctx == null) { return; }
    if(!tilesetLoaded) { requestAnimationFrame(drawGame); return; }

     var currentFrameTime = Date.now();
    var timeElapsed = currentFrameTime - lastFrameTime;

  var sec = Math.floor(Date.now()/1000);
    if(sec != currentSecond)
    {
        currentSecond = sec;
        framesLastSecond = frameCount;
        frameCount = 1;
    }
    else
    {
        frameCount++;
    }

    if(!player.processMovement(currentFrameTime))
    {
        if(keysDown[38] && player.canMoveUp())			{ player.moveUp(currentFrameTime); }
        else if(keysDown[40] && player.canMoveDown())	{ player.moveDown(currentFrameTime); }
        else if(keysDown[37] && player.canMoveLeft())	{ player.moveLeft(currentFrameTime); }
        else if(keysDown[39] && player.canMoveRight())	{ player.moveRight(currentFrameTime); }
        else if(keysDown[36] )	{ player.movePause(currentFrameTime); }
        else if(winStatus) {
            console.log("WIN");

            ctx.fillStyle="#54BCFC";
            ctx.fillRect(0,0,800,608);

            ctx.font='bold 60px Comic Sans MS';
            ctx.textAlign="center"; 
            ctx.textBaseline = "middle";
            ctx.fillStyle = "white";		

            //ctx.fillText("score:" + framesLastSecond, 400, 50);
            ctx.fillText("Esercizio concluso!", 400, 304);	
            
            endWindow(true);
            return;
        }
        else if(player.checkGameOver() || gameOverStatus) {
            console.log("GAMEOVER");

            ctx.fillStyle="#54BCFC";
            ctx.fillRect(0,0,800,608);

            ctx.font='bold 60px Comic Sans MS';
            ctx.textAlign="center"; 
            ctx.textBaseline = "middle";
            ctx.fillStyle = "white";		

            //ctx.fillText("score:" + framesLastSecond, 400, 50);
            ctx.fillText("Esercizio concluso!", 400, 304);	
            endWindow(false);
            return;
        }
    }

    for(var y = 0; y < mapH; ++y)
    {
        for(var x = 0; x < mapW; ++x)
        {
            var tile = tileTypes[gameMap[toIndex(x,y)]];
            if(tile.index >= 6){
                    ctx.drawImage(tileset, 128, 0, tile.sprite[0].w, tile.sprite[0].h, (x*tileW), (y*tileH), tileW, tileH);
            }else{
                    ctx.drawImage(tileset, tile.sprite[0].x, tile.sprite[0].y, tile.sprite[0].w, tile.sprite[0].h, (x*tileW), (y*tileH), tileW, tileH);
            }
        }
    }

        // la uso per disegnare gli animali piu grandi
    for(var y = 0; y < mapH; ++y)
    {
        for(var x = 0; x < mapW; ++x)
        {
            var tile = tileTypes[gameMap[toIndex(x,y)]];
            if(tile.index >= 6){
                //zoom animale quando ci passo sopra
                //if(gameMap[toIndex(x,y)] == gameMap[toIndex(player.position[0]/tileW,player.position[1]/tileH)])
                //{
                //	if(y == 17){
                //		ctx.drawImage(tileset,tile.sprite[0].x, tile.sprite[0].y, tile.sprite[0].w, tile.sprite[0].h, (x*tileW)-16, (y*tileH), 100, 100);
                //	}else {
                //		ctx.drawImage(tileset,tile.sprite[0].x, tile.sprite[0].y, tile.sprite[0].w, tile.sprite[0].h, (x*tileW)-16, (y*tileH)-16, 100, 100);
                //	}
                //}else
                if(y == 17){
                    ctx.drawImage(tileset,tile.sprite[0].x, tile.sprite[0].y, tile.sprite[0].w, tile.sprite[0].h, (x*tileW)-16, (y*tileH), 64, 64);
                }else {
                    ctx.drawImage(tileset,tile.sprite[0].x, tile.sprite[0].y, tile.sprite[0].w, tile.sprite[0].h, (x*tileW)-16, (y*tileH)-16, 64, 64);
                }
            }
        }
    }
    
    ctx.fillStyle = "#000000";
   
    //aggiunto start per attivare il timer
    if(!vstart){ 
         ctx.font='18px Arial';
        ctx.fillText("MOSSE: "+ listNodesVisited.length, 10, 40);
    ctx.fillText("TIMER: 00:00", 10, 20);}
    else{
    if(minutesLabel != null && secondsLabel != null) {
        ctx.fillText("TIMER: " + minutesLabel + ":"+ secondsLabel, 10, 20);
    } else {
        ctx.fillText("TIMER: 00:00", 10, 20);
    }
    ctx.font='18px Arial';
    ctx.fillText("MOSSE: "+ listNodesVisited.length, 10, 40);
    //ctx.fillText(" Entrata ", 1, 342);
    //ctx.fillText(" Passeggiate sul Cammello ", 364, 246);

    var sprite = player.sprites[player.direction];
    // disegno il visitatore sul gommone se è nel passaggio
    if(player.position[1] >= 128 && player.position[1] <= 320  && player.position[0] == 544 && player.direction != 1 && player.direction != 3 ) {
        ctx.drawImage(tileset, sprite[1].x, sprite[1].y, sprite[1].w, sprite[1].h, player.position[0]-9, player.position[1], 50, 50);
    }else {
        ctx.drawImage(tileset, sprite[0].x, sprite[0].y, sprite[0].w, sprite[0].h, player.position[0], player.position[1], player.dimensions[0], player.dimensions[1]);
    }
    
//     if(difficulty == "medium") {
//        // in difficolta medium mostro il sentiero non attraversabile
//        for(var y = 0; y < mapH; ++y)
//        {
//            for(var x = 0; x < mapW; ++x)
//            {
//                for (var i = 0; i < player.tilePrevious.length; ++i) {
//                    if(x == player.tilePrevious[i][0] && y == player.tilePrevious[i][1] && (tileTypes[gameMap[toIndex(x,y)]].floor == floorTypes.road))
//                    {
//                        ctx.fillStyle = "rgba(56, 56, 56, 0.3)";
//                        ctx.fillRect((x*tileW), (y*tileH), 32, 32);
//                    }
//                }
//            }
//        }
//    }

    //ctx.font='18px Arial';
    //ctx.fillText(" Entrata ", 1, 342);
  }  
    lastFrameTime = currentFrameTime;
    requestAnimationFrame(drawGame);
}

function endWindow(solved){
    $('#myModal').modal('show',{"backdrop":"static","keyboard":false});
    var modal = $('#myModal');
    var mbody= modal.find('.modal-body');
    mbody.html('');
    if(solved){
        modal.find('.modal-title').html("Hai completato il piano con successo!");
        mbody.html('<p>Hai trovato una soluzione in '+listNodesVisited.length+' mosse!</p><p id="loadperformance">Caricamento risultati performance...</p>');
    }
    else{
        modal.find('.modal-title').html("Non sei riuscito a completare il piano!");
        mbody.html('<p>Purtroppo non sei riuscito a risolvere il problema</p><p id="loadperformance">Caricamento risultati performance...</p>');
    }
    $("#loadperformance").load('getzooperformance?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(listNodesVisited)+'&sessid='+sessid+'&color='+colorenable);
    var fbody=modal.find('.modal-footer');
    fbody.html('<a type="button" class="btn btn-default" href="pianificazione2phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(listNodesVisited)+'&sessid='+sessid+'&color='+colorenable+'" >Ok</a>');
    $('#myModal').on('hidden.bs.modal', function () {
        location.href='pianificazione2phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&pTime='+pTime+'&actions='+encodeURI(ACTIONS)+'&sessid='+sessid+'&color='+colorenable;
    });
}