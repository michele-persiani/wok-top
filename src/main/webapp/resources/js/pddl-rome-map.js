/**
 * Author: Margherita Donnici Description: This file handles the map UI for the
 * weekend in Rome exercise
 */

const width = 1000;
const height = 612;
const GREY = 0x686868;
const RED = 0xff0000;
const WHITE = 0xFFFFFF;
const BREAKFAST_HOURS = [31,35];
const SLEEP_HOURS=[22,28];

// Create a Pixi Application
var app = new PIXI.Application({width: width, height: height});

// Control variables
var inMovement = false;
var choosingHotel = false;
var currentTime = 0;
var startTime = 8;
var startLocation;
var prova;

var goHome=false;

// Variables to keep track of user actions
var inHotel=false;
var inHotelOrario=false;
var hotelIdArray = [7,8,9,10];
var bookedHotelId;
var bookedReturnTrain = {};
var hadBreakfast = false;
var slept = false;
var notifiedNotSlept = false; // for keeping track of failed goals
var terminato=false;								// notifications
var completedGoals = false;
var userActions = [];
var sleepHour = $("#sleepHour").text().split(" ")[5]; // Get sleephour from
//sleep sabato o domenica
var sleepHour2 = parseInt($("#sleepHour2").text().split(" ")[5]); // Get sleephour domenica
var nCorrect=0;
//click errati e corretti
var nOk=0;
var nErr=0;
var nWrong=0;
var nTot =  0;
//n click su obiettivi
var nClickTarget=0;
var initTime=0;
var nClickPrenotazioni=0;


if(!isNaN(sleepHour2)){
    sleepHour=sleepHour2+24;// goal string
}
console.log("vado a letto alle "+sleepHour);
var level = $("#level").text();
//variabili per performance

// Create list with goal IDs from goals modal
var goalList = {};
$('#goalList li').each(function(i) {
	if(!isNaN(parseInt($(this).attr('id')))){
            //nTot++;//numero totali di goals
		goalList[parseInt($(this).attr('id'))] = {
				completed: false,
				notifiedFail: false
		};
	}
});

// Create user sprite
userTex = PIXI.Texture.fromImage('resources/images/weekend-rome/currentPos.png')
userSprite = new PIXI.Sprite(userTex);

$(document).ready(function(){
        initTime = new Date().getTime();
	$('#goalModal').modal('show');
	console.log(goalList);
	console.log("level: " + level);
    // Add the canvas that Pixi automatically created for you to the HTML
	// document
    $("#map").append(app.view);
    app.stage.interactive = false;
    app.stage.click = handleClick;

    // Create a new background sprite
    if (level < 4) {
    	// Load EASY map
    	var background = new PIXI.Sprite.fromImage('resources/images/weekend-rome/map-background-easy.png');
    } else if (level < 7){
    	// Load MEDIUM map
    	var background = new PIXI.Sprite.fromImage('resources/images/weekend-rome/map-background-medium.png');
    } else {
    	// Load DIFFICULT map
    	var background = new PIXI.Sprite.fromImage('resources/images/weekend-rome/map-background.png');
    }
    background.width = width;
    background.height = height;
    app.stage.addChild(background);

    // Create location sprites
    for (i=0; i<locationArray.length; i++){
    	if (level < 4) {
    		// If the difficulty is EASY, skip HOTEL BELVEDERE & STAZIONE TIBURTINA
    		if (i == 8 || i == 13){
    			continue;
    		}
    	} else if (level < 7) {
    		// If the difficulty is MEDIUM, skip HOTEL BELVEDERE
    		if (i == 8){
    			continue;
    		}
    	}
        locationArray[i].sprite = new PIXI.Sprite(locationArray[i].texture);
        console.log("come funziona location Array.sprite "+locationArray[i].sprite.id);
        var absoluteCoordinates = getAbsoluteCoordinates(locationArray[i].coordinates.x, locationArray[i].coordinates.y);
        locationArray[i].sprite.x = absoluteCoordinates.x;
        locationArray[i].sprite.y = absoluteCoordinates.y;
        locationArray[i].sprite.anchor.set(0.5);
        locationArray[i].sprite.id = i;
        // Make the sprite unclickable
        locationArray[i].sprite.interactive = false;
        locationArray[i].sprite.buttonMode = false;
        app.stage.addChild(locationArray[i].sprite);
        // Add info button only
        //modifica sara livello >3 con livello>0 e il bottone è presente
        console.log("nel nodo i "+i+" devo inserire il pulsante delle info? "+showButtonInfo(i));
        if (level > 0 && showButtonInfo(i)){
            infoTex = PIXI.Texture.fromImage('resources/images/weekend-rome/info.png');
            locationArray[i].infoSprite = new PIXI.Sprite(infoTex);
            locationArray[i].infoSprite.anchor.set(0.5);
            locationArray[i].infoSprite.x = absoluteCoordinates.x + locationArray[i].infoIconOffset.x; // 40
            locationArray[i].infoSprite.y = absoluteCoordinates.y + locationArray[i].infoIconOffset.y;       
	        // Make it clickable
	        locationArray[i].infoSprite.interactive = true;
	        locationArray[i].infoSprite.buttonMode = true;
	        locationArray[i].infoSprite.id = i;
	        app.stage.addChild(locationArray[i].infoSprite);
                
            //aggiunto l'evento sul puslante
            locationArray[i].infoSprite.on("pointertap", function(event){
	            showLocationInfo(event);
	        });
        }
    }

    // MOVEMENT
    var accessibleContainer;
    busTex= PIXI.Texture.fromImage('resources/images/weekend-rome/bus.png');
    walkTex = PIXI.Texture.fromImage('resources/images/weekend-rome/walk.png');
    
    // Clicking on current position
    // Deprecated!
    //userSprite.on('pointertap', function() {});

    // Clicking on locations
    for (i=0; i<locationArray.length; i++){
    	if (level < 4) {
    		// If the difficulty is EASY, skip HOTEL BELVEDERE & STAZIONE TIBURTINA
    		if (i == 8 || i == 13){
    			continue;
    		}
    	} else if (level < 7) {
    		// If the difficulty is MEDIUM, skip HOTEL BELVEDERE
    		if (i == 8){
    			continue;
    		}
    	}
        locationArray[i].sprite.on("pointertap", function(event){
            if (choosingHotel) {
                
            	console.log("choosingHotel ma ci entro??");
                loc = getLocationFromId(event.currentTarget.id);
                $("#warningTitle").text("Scegli Albergo");
                $("#warningMsg").text("Stai prenotando l'albergo " + loc.name + ". Vuoi procedere con la prenotazione?")
                $("#bookHotel").attr("locId", event.currentTarget.id.toString());
                $("#warningModal").modal("show");
            } else {
            	console.log("choosingHotel false");
            	moveToLocation(event, accessibleContainer);
            }
        });
    }

    // Clicking on info icons
    //modifica sara era level >3  anticipo questo codice dove si inseriscono le icone info.png
   // if (level > 0){
        
	/*    for (i=0; i<locationArray.length; i++){
	    	if (level < 4) {
	    		// If the difficulty is EASY, skip HOTEL BELVEDERE & STAZIONE TIBURTINA
	    		if (i == 8 || i == 13){
	    			continue;
	    		}
	    	} else if (level < 7) {
	    		// If the difficulty is MEDIUM, skip HOTEL BELVEDERE
	    		if (i == 8){
	    			continue;
	    		}
	    	}
                
	        locationArray[i].infoSprite.on("pointertap", function(event){
	            showLocationInfo(event);
	        });
	    }*/
 //   }

    // UTIL FUNCTION FOR COORDINATES
    function handleClick(event) {
        var point = event.data.getLocalPosition(app.stage);
        // console.log(point);
    }

    // ************************** Functions for control panel
	// ************************** //
    // TRAINS
    // Selected options is green in trains modal
    $('input:radio[name="trains"]').change(
        function(){
            if (this.checked) {
                $(this).parent().parent().parent().parent().addClass("success");
                $("input:radio[name='trains']:not(:checked)").parent().parent().parent().parent().removeClass("success");
            }
    });

    // Book trains button click event
    $("#bookTrains").click(function(){
        // Disable Choose Trains button
        $("#chooseTrains").hide();
        // Save chosen train
        var outwardTrainString = $("input:radio[name='trains']:checked").parent().parent().parent().next().text();
        // Get arrival time from string
        startTime = parseInt(outwardTrainString.split(" ")[6].split(":")[0]);
        // Get arrival station from string; remove parenthesis and line break
		// characters
        var startLocationString = outwardTrainString.split(" ")[7].replace(/\(|\)/g, "").replace(/^\s+|\s+$/g, '');
        // Save start location
        startLocationString == "Termini"? startLocation = termini : startLocation = tiburtina;
        var returnTrainString = $("input:radio[name='trains']:checked").parent().parent().parent().next().next().text();
        // Get return departure time from string
        bookedReturnTrain.departureTime = parseInt(returnTrainString.split(" ")[2].split(":")[0])+24;
        // Get return departure location from string
        bookedReturnTrain.departureLocation = returnTrainString.split(" ")[3].replace(/\(|\)/g, "").replace(/^\s+|\s+$/g, '').split("\n")[0].toLowerCase();
        // Build PDDL string and save user action in the userActions array
        var firstHour = startTime - 2; // train trip takes 2 hours
        var middleHour = startTime - 1;
        var returnMiddleHour = bookedReturnTrain.departureTime + 1;
        var returnLastHour = bookedReturnTrain.departureTime + 2;
        var actionString = "(book-train-round-trip " + startLocationString.toLowerCase() + " hour" + firstHour.toString() + " hour" + middleHour.toString() + " hour" + startTime.toString()
        					+ " hour" + bookedReturnTrain.departureTime.toString() + " hour" + returnMiddleHour.toString() + " hour" + returnLastHour.toString() + ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
        // Show chosen trains
        $("#noTrains").hide();
        $("#trenoAndata").text(outwardTrainString);
        $("#trenoRitorno").text(returnTrainString);
        $("#bookedTrains").show();
        // Enable Choose Hotel button
        $("#chooseHotel").show(500);
    });



    // HOTELS
    // Choose hotel button click event
    $("#chooseHotel").click(function(){
        choosingHotel = true;
        for (i=0; i<locationArray.length; i++){
        	if (level < 4){
        		if (i==8 || i == 13){
        			continue;
        		}
        	} else if (level < 7){
        		if (i==8){
        			continue;
        		}
        	}
            if (!hotelIdArray.includes(i)){
                // The location is not a hotel
                locationArray[i].sprite.tint = GREY;
                if (level > 3){
                    locationArray[i].infoSprite.tint = GREY;	
                }
            } else {
                // Enable click only for hotels
                locationArray[i].sprite.interactive = true;
                locationArray[i].sprite.buttonMode = true;
            }
        }
        // Disable Choose Hotel button
        $("#chooseHotel").prop('disabled', true);
    });

    // Book hotels button click event
    $("#bookHotel").click(function(){
        // Get chosen hotel id
        var hotelId = $(this).attr("locId");
        console.log("Booked hotel ID: " + hotelId);
        // Remove chosen hotel from hotel ids array
        var index = hotelIdArray.indexOf(parseInt(hotelId));
        if (index > -1) {
            hotelIdArray.splice(index, 1);
        }
        // Remove grey tint from all sprites and set grey tint to NOT chosen
		// hotels
        for (i=0; i<locationArray.length; i++){
        	if (level < 4){
        		if (i==8 || i == 13){
        			continue;
        		}
        	} else if (level < 7){
        		if (i==8){
        			continue;
        		}
        	}
            if (!hotelIdArray.includes(i)){
                locationArray[i].sprite.tint = WHITE;
                if (level > 3){
                    locationArray[i].infoSprite.tint = WHITE;
                }
            } else {
            	if (level < 4){
            		// only set grey tint if level is EASY
                    locationArray[i].sprite.tint = GREY;
                    if (level > 3){
                        locationArray[i].infoSprite.tint = WHITE;
                    }
            	}
            }
        }
        choosingHotel = false;
        // Disable Choose Hotel button
        $("#chooseHotel").hide();
        // Show booked Hotel
        $("#noHotel").hide();
        $("#hotelName").text(locationArray[hotelId].name);
        $("#bookedHotel").show();
        // Save booked hotel
        bookedHotelId = hotelId;
        // Build PDDL String and add user action to user actions list
        var hotelName = locationArray[hotelId].pddlName;
        var actionString = "(book-hotel " + hotelName +")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
        // Show "Begin" button
        $("#beginButton").show(500);
    });

    // BEGIN BUTTON
    $("#beginButton").click(function(){
        // Hide Begin button
        $("#beginButton").hide();
        // Waiting dialog
        var dialog = bootbox.dialog({
            message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Partendo...</h3>',
            closeButton: false
        });
        setTimeout(function(){
        	dialog.modal('hide');
        	// Show Action Buttons;
	        $("#waitButton").show();
	        $("#checkSolution").show();
	        $("#hintButton").show();
	        $("#compass").show();
	        // Show date and time
	        currentTime = startTime;
	        updateTime();
	        // Show userSprite
	        initializeUserSprite(startLocation);

        }, 2000);
    });

    // COMPASS
    $("#compass").click(function(){
    	// Show red circles around the places I can reach
        if (!inMovement){
            // If I am not in movement, highlight all the places you can move to
            // Get current coordinates
            var source = findClickedLocation(userSprite.x, userSprite.y);
            // Create accessibleContainer which will contain all the
			// circles/sprites for indicating accessible locations (for easy
			// removal)
            accessibleContainer = new PIXI.Container();
            app.stage.addChild(accessibleContainer);
            // For each location, check if it is accessible
            for (i=0; i<locationArray.length; i++){
            	if (level < 4){
            		if ( i == 8 || i == 13){
            			continue;
            		}
            	} else if (level < 7){
            		if (i == 8){
            			continue;
            		}
            	}
                if (i != source){
                    if (locationArray[source].adjacentLocationsBus.includes(locationArray[i].id)){
                        // If the location is accessible from where I am by BUS
                    	// Check if there is a bus at this hour
                    	if (level < 4) {
                    		// If difficulty is EASY, there is a bus every hour, no need to check schedules
	                        locationArray[i].currentlyAccessible = true;
	                        drawAccessibleLocation(accessibleContainer,busTex,i);
                                console.log("debug creazione mappa "+locationArray[i]+" source "+locationArray[source]);
	                        // Make the sprite clickable
	                        locationArray[i].sprite.interactive = true;
	                        locationArray[i].sprite.buttonMode = true;
                    	} else {
                    		// If difficulty is MEDIUM or HARD, buses check schedules
                    		console.log(locationArray[source].busSchedules[locationArray[i].id] + currentTime);
		                    if(locationArray[source].busSchedules[locationArray[i].id] != null && locationArray[source].busSchedules[locationArray[i].id].includes(currentTime)){
		                        locationArray[i].currentlyAccessible = true;
		                        drawAccessibleLocation(accessibleContainer,busTex,i);
		                        // Make the sprite clickable
		                        locationArray[i].sprite.interactive = true;
		                        locationArray[i].sprite.buttonMode = true;
	                    	}

                    	}
                    }
                    if(locationArray[source].adjacentLocationsWalk.includes(locationArray[i].id)){
                        // If the location is accessible from where I am WALKING
                        locationArray[i].currentlyAccessible = true;
                        drawAccessibleLocation(accessibleContainer,walkTex,i);
                        // Make the sprite clickable
                        locationArray[i].sprite.interactive = true;
                        locationArray[i].sprite.buttonMode = true;
                    }
                }
            }
            inMovement = true;
        } else {
            // If I already am in movement, clear location highlights (end
			// movement action)
            resetTextures(accessibleContainer);
            inMovement = false;
        }
    });
    
    // WAIT BUTTON
    $("#waitButton").click(function(){
        // Waiting dialog
        var dialog = bootbox.dialog({
            message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Aspettando 1 ora...</h3>',
            closeButton: false
        });
        var startTime = currentTime; // Save start time for building PDDL
	var endTime = startTime + 1;									// string and update time
        setTimeout(function(){
            // Update date and time
        	currentTime++;
                updateTime();
        	dialog.modal('hide');
        	var sourceId = findClickedLocation(userSprite.x, userSprite.y);
        	var currentLocation = getLocationFromId(sourceId);
        	updateButtons(currentLocation);
                makeAdjacentLocationsClickable(currentLocation);//aggiunto per sistemare la mappa
        
                 }, 2000);
        // Build PDDL String and add user action to user actions list
        
        var actionString = "(wait " + currentLocation.pddlName + " hour" + startTime + " hour" + endTime + ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
        
    });
    
    // VERIFY BUTTON
    /* $("#checkSolution").click(function(){
        // Waiting dialog
        var dialog = bootbox.dialog({
            message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Verifica...</h3>',
            closeButton: false
        });
        console.log("Sending: " + userActions);
        
        // POST request with list of actions up until now
        axios.post('verify', userActions)
          .then(function (response) {
              console.log("sono qui !!");
        	  dialog.modal('hide');
        	  if (response.data.hasSolution){
        		  // Problem is solvable!
        		  bootbox.alert("<h3>Bravo! Sei sulla buona strada!</h3>");
        	  } else {
				  // Problem is unsolvable!
				  // Create message string
				  var unsolvableGoalsStringList = [];
					for (var goal in goalList){
						// Get user-friendly goal string from goalsModal
						var goalString = $("#" + goal).text();
						if(goalList[goal].notifiedFail){
							// If notifiedFail is true, it means the goals isn't solvable anymore
							unsolvableGoalsStringList.push('<li><i class="fa fa-times" style="color:red;"></i>'+ goalString +'</li>');
						}
					}
					
					var sleepString = $("#sleepHour").text(); // Get user-friendly sleep
					console.log(sleepString);
					// string from goalsModal					
					if (!slept) {
						console.log("not slept");
						// Put in non-completed goals list
						unsolvableGoalsStringList.push('<li><i class="fa fa-times" style="color:red;"></i>'+ sleepString +'</li>');
					}
				var msg ='<h4>Questi obiettivi non possono più essere risolti: </h4>' +
			    			'<ul style="list-style-type: none" class="lead">';
				for (var i = 0; i < unsolvableGoalsStringList.length; i++){
					msg = msg + unsolvableGoalsStringList[i];
				}
			    msg = msg + '</ul>';
        		  bootbox.confirm({
        			  title:"Il problema non ha soluzione, vuoi provare a risolvere il resto degli obiettivi?",
        			    message: msg,
        			    buttons: {
        			        cancel: {
        			            label: 'Continua',
        			            className: 'btn-success'
        			        },
        			        confirm: {
        			            label: 'Termina esercizio',
        			            className: 'btn-danger'
        			        }
        			    },
        			    closeButton: false,
        			    size: 'large',
        			    callback: function (result) {
        			    	(result? endExercise() : dialog.modal('hide'));
        			    }
        			});
        	  }
        	  userActions.length = 0;
          })
          .catch(function (error) {
        	  dialog.modal('hide');
        	  console.log("Error!");
        	  console.log(error);
          });
    });*/
    
    // VERIFY BUTTON
    /*$("#hintButton").click(function(){
        // Waiting dialog
        var dialog = bootbox.dialog({
            message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Attendi...</h3>',
            closeButton: false
        });
        setTimeout(function(){
        	dialog.modal('hide');
        	bootbox.alert("<h3><i class='fa fa-lightbulb-o'></i> &nbsp;" + "Prova ad andare alla stazione Termini..." + "</h3>"); 
        }, 3000);
        console.log("Sending: " + userActions);
        
        // POST request with list of actions up until now
        axios.post('getHint', userActions)
          .then(function (response) {
        	  dialog.modal('hide');
        	  if (response.data.hasSolution){
        		  // Problem is solvable!
        		  bootbox.alert("<h3><i class='fa fa-lightbulb-o'></i> &nbsp;" + response.data.nextAction + "</h3>");
        	  } else {
				  // Problem is unsolvable!
				  // Create message string
				  var unsolvableGoalsStringList = [];
					for (var goal in goalList){
						// Get user-friendly goal string from goalsModal
						var goalString = $("#" + goal).text();
						if(goalList[goal].notifiedFail){
							// If notifiedFail is true, it means the goals isn't solvable anymore
							unsolvableGoalsStringList.push('<li><i class="fa fa-times" style="color:red;"></i>'+ goalString +'</li>');
						}	
					}
                                        if(sleepHour>24)
                                        var sleepString = $("#sleepHour2").text();
					else
                                        var sleepString = $("#sleepHour").text(); // Get user-friendly sleep
					console.log(sleepString);
					// string from goalsModal
                                        
					if (!slept) {
						console.log("not slept");
						// Put in non-completed goals list
						unsolvableGoalsStringList.push('<li><i class="fa fa-times" style="color:red;"></i>'+ sleepString +'</li>');
					}
				var msg ='<h4>Questi obiettivi non possono più essere risolti: </h4>' +
			    			'<ul style="list-style-type: none" class="lead">';
				for (var i = 0; i < unsolvableGoalsStringList.length; i++){
					msg = msg + unsolvableGoalsStringList[i];
				}
			    msg = msg + '</ul>';
        		  bootbox.confirm({
        			  title:"Il problema non ha soluzione, vuoi provare a risolvere il resto degli obiettivi?",
        			    message: msg,
        			    buttons: {
        			        confirm: {
        			            label: 'Termina esercizio',
        			            className: 'btn-danger'
        			        },
        			        cancel: {
        			            label: 'Continua',
        			            className: 'btn-success'
        			        },
        			    },
        			    closeButton: false,
        			    size: 'large',
        			    callback: function (result) {
        			        endExercise();
        			    }
        			});
        	  }
        	  userActions.length = 0;
          })
          .catch(function (error) {
        	  dialog.modal('hide');
        	  console.log("Error!");
        	  console.log(error);
          });
    });*/

    
    // Fill end exercise modal with solved/unsolved goals
  /*  $( "#endExerciseModal" ).on('shown.bs.modal', function (e){
        console.log("sono dentro suggerimento");
        endExercise();
    });*/
    
});

// Given the central point of a circle and an external point, returns true if
// the external point is inside the circle, false otherwise
function isInsideRange(x0, y0, x1, y1) {
    radius = 100;
    return Math.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0)) < radius
}

// Given 2 relative coordinates, returns the absolute coordinates of it
function getAbsoluteCoordinates(relX, relY) {
    var absCoord = {
        x: 0,
        y: 0
    }
    absCoord.x = width * relX;
    absCoord.y = height * relY;
    return absCoord;
}

// Given the coordinates, finds the corresponding location in a fixed radius
// (100)c
function findClickedLocation(x,y){
    x1 = Math.floor(x);
    y1 = Math.floor(y);
    locationId = -1;
    for (i=0; i<locationArray.length; i++){
        var point = getAbsoluteCoordinates(locationArray[i].coordinates.x, locationArray[i].coordinates.y);
        if (isInsideRange(point.x, point.y, x1, y1)){
            locationId=i;
        }
    }
    return locationId;
}

// Given an id, returns the corresponding location from the locationArray
function getLocationFromId(id){
    for (i=0; i<locationArray.length; i++){
        if (locationArray[i].id == id){
            return locationArray[i];
        }
    }
    return null;
}

// Removes accessible location highlighting, makes them unclickable and resets
// currentlyAccessible flags for location
// Called at the end of/when in interrupting a movement action
function resetTextures(container){
    // Reset Textures
    container.visible = false;
    // Reset accessible flag
    for (i=0; i<locationArray.length; i++){
    	if (level < 4) {
    		// If the difficulty is EASY, skip HOTEL BELVEDERE & STAZIONE TIBURTINA
    		if (i == 8 || i == 13){
    			continue;
    		}
    	} else if (level < 7) {
    		// If the difficulty is MEDIUM, skip HOTEL BELVEDERE
    		if (i == 8){
    			continue;
    		}
    	}
        locationArray[i].currentlyAccessible = false;
        // Make the sprite unclickable
        locationArray[i].sprite.interactive = false;
        locationArray[i].sprite.buttonMode = false;
    }
}

// Moves user to clicked location (if accessible) and increments time
function moveToLocation(event, container) {
    //modifica click sulla stessa posizione
    loc = getLocationFromId(event.currentTarget.id);
    if (loc.currentlyAccessible == true  && loc != currentLocation) {
        userSprite.x = loc.coordinates.x * width;
        userSprite.y = loc.coordinates.y * height;
        if (inMovement){
        	// If I drew the red circles, remove them
	        resetTextures(container);
        }
        inMovement = false;
        // Store start time for building PDDL string then update time
        var startTime = currentTime++;
        updateTime();
        var src = currentLocation; // Save src location before updating current
									// location (for building pddlString)
        currentLocation = loc;
        updateButtons(loc);
        // Check if a goal was satisfied
	notifySatisfiedGoal(locationGoalIds[loc.id]);
        // Make new reachable locations clickable
        makeAdjacentLocationsClickable(loc);
        // Build PDDL String and add user action to user actions list
		// Check if traveling by bus or walking
		var travelMode;
		if (src.adjacentLocationsBus.includes(currentLocation.id)){
			travelMode = "bus";
		} else if (src.adjacentLocationsWalk.includes(currentLocation.id)){
			travelMode = "foot";
		}
        var endTime = currentTime;
        var actionString = "(travel-by-" + travelMode + " " + src.pddlName + " " + currentLocation.pddlName + " hour" + startTime + " hour" + endTime + ")";
        userActions.push(actionString);
    }
}


//mostra icona solo se necessaria
function  showButtonInfo(id){
    var info=false;
    if (locationArray[id].openingHours != undefined) {
    info=true;
    }
  if((bookedHotelId == undefined && hotelIdArray.includes(id)) || (bookedHotelId == id))
    info=true;
    
   if ((locationArray[id].adjacentLocationsBus.length != 0)&&(level>3))
       info=true;
    return info;
}


// Opens modal with location info
function showLocationInfo(event) {
    var info=false;
    loc = getLocationFromId(event.currentTarget.id);
    console.log("Showing info for " + loc.name + "(id: " + loc.id + ")");
    // Fill modal with info
 
    // OPENING HOURS
    if (locationArray[loc.id].openingHours != undefined) {
    	$("#saturdayOpenHours").text(locationArray[loc.id].openingHours.saturday[0] +":00 - " + locationArray[loc.id].openingHours.saturday[1] + ":00");
    	$("#sundayOpenHours").text(locationArray[loc.id].openingHours.sunday[0]%24 +":00 - " + locationArray[loc.id].openingHours.sunday[1]%24 + ":00");
    	$("#breakfastHoursRow").hide();
    	$("#openingHoursRow").show();
        info=true;
    } else if ((bookedHotelId == undefined && hotelIdArray.includes(loc.id)) || (bookedHotelId == loc.id)) {
    	// Show breakfast hours for every hotel if hotel has not been booked
		// yet, or ONLY for the booked hotel after booking
                info=true;
    	$("#openingHoursRow").hide();
    	$("#breakfastHoursRow").show();
    } else {
    	$("#openingHoursRow").hide();
    	$("#breakfastHoursRow").hide();
    }
    // BUS SCHEDULES
    //modifica Sara
    if(level<4){
    $("#bus").hide();
    $("#noBus").hide();
    }
     else{
    if (locationArray[loc.id].adjacentLocationsBus.length != 0){
        info=true;
    	$("#noBus").hide();
    	$("#busSchedulelist").empty();
    	$("#busSchedulelist").append("<tr><th>Destinazione</th><th>Sabato</th><th>Domenica</th>");
	    for(key in locationArray[loc.id].busSchedules){
	    	// Key corresponds to a destination
	    	var rowNumber = Object.keys(locationArray[loc.id].busSchedules).indexOf(key) + 1;
	    	// Vars for layout purposes
	    	var sunday = false;
	    	var indexNextDay = 0;
	    	// Search for the index of the first hour of the next day (layout
			// purposes)
	    	while (locationArray[loc.id].busSchedules[key][indexNextDay] < 24){
	    		indexNextDay++;
	    	}
	    	// Open destination row and create Destination name cell
	    	$("#busSchedulelist").append("<tr><td>"+getLocationFromId(key).name + "</td>");
	    	for (i = 0; i < locationArray[loc.id].busSchedules[key].length; i++) {
	    		if (i==0){
	    			// First hour of saturday; open "Saturday" cell
	    			$("#busSchedulelist tr:eq("+rowNumber+")").append("<td>");
	    		} else if (locationArray[loc.id].busSchedules[key][i] >= 24 && sunday==false){
	    			// First hour of sunday, close "saturday" cell and open
					// "sunday" cell
	    			$("#busSchedulelist tr:eq("+rowNumber+")").append("</td>");
	    			$("#busSchedulelist tr:eq("+rowNumber+")").append("<td>");
	    			sunday=true;
	    		}
	    		if(locationArray[loc.id].busSchedules[key][i] < 24){
	    			// It's a Saturday hour
	    			$("#busSchedulelist tr:eq("+rowNumber+") td:eq(1)").append(locationArray[loc.id].busSchedules[key][i]+":00");
		    		if (i != indexNextDay - 1) {
		    			// It's not the last hour of saturday
		    			$("#busSchedulelist tr:eq("+rowNumber+") td:eq(1)").append(" - ");
		    		}
	    		} else {
	    			// It's a sunday hour
	    			$("#busSchedulelist tr:eq("+rowNumber+") td:eq(2)").append(locationArray[loc.id].busSchedules[key][i]%24+":00");
		    		if (i != locationArray[loc.id].busSchedules[key].length - 1) {
		    			// It's not the last hour of sunday
		    			$("#busSchedulelist tr:eq("+rowNumber+") td:eq(2)").append(" - ");
		    		}
	    		}
	    	}
	    	// Close "Sunday" cell
	    	$("#busSchedulelist tr:eq("+rowNumber+")").append("</td>");
	    	// Close this destination's row
	    	$("#busSchedulelist").append("</tr>");
	    }
    }
} 
console.log("informazioni presenti "+info);
//mostro infoModal solo se è presente almeno un'informazione
if(info){
    $('#infoTitle').text(loc.name + " - Informazioni");
    $('#infoModal').modal('show');
    }
}

// Highlights the location corresponding to locationId as accessible by bus or
// by foot (depending on given texture)
function drawAccessibleLocation(container, texture, locationId){
    // Draw red circle around location
    var pixiCircle = new PIXI.Graphics();
    pixiCircle.lineStyle(5, RED);  // (thickness, color)
    pixiCircle.alpha = 0.5;
    var absoluteCoordinates = getAbsoluteCoordinates(locationArray[locationId].coordinates.x, locationArray[locationId].coordinates.y);
    pixiCircle.drawCircle(absoluteCoordinates.x, absoluteCoordinates.y, 60);   // (x,y,radius)
    pixiCircle.endFill(); 
    container.addChild(pixiCircle);
    // Create bus icon
    var sprite = new PIXI.Sprite(texture);
    sprite.anchor.set(0.5);
    sprite.scale.set(0.3);
    // If the location is on the left half, put icon on the right of the circle
	// (and vice-versa)
    if (locationArray[locationId].coordinates.x * width < width/2) {
        sprite.x = width * locationArray[locationId].coordinates.x + 55;
        sprite.y = height * locationArray[locationId].coordinates.y;
    } else {
        sprite.x = width * locationArray[locationId].coordinates.x - 55;
        sprite.y = height * locationArray[locationId].coordinates.y;                       
    }
    container.addChild(sprite);
}

// Inizializes user sprite at given location
function initializeUserSprite(startLocation){
    var absoluteCoordinates = getAbsoluteCoordinates(startLocation.coordinates.x, startLocation.coordinates.y);
    userSprite.x = absoluteCoordinates.x + 5;
    userSprite.y = absoluteCoordinates.y;
    userSprite.anchor.set(0.5);
    userSprite.scale.set(0.5);
    userSprite.interactive = false;
    userSprite.buttonMode = false;
    app.stage.addChild(userSprite);
    currentLocation = startLocation;
    makeAdjacentLocationsClickable(startLocation);
}

// Updates clock (called when an action has been done and time has passed)
function updateTime(){
	$("#date").text("");
	$("#date").append('<span class="glyphicon glyphicon-time align-middle" aria-hidden="true"></span>');
    if (currentTime < 24){
        $("#date").append(" Sabato, " + currentTime.toString() + ":00");
    } else {
    	$("#date").append(" Domenica, " + (currentTime%24).toString() + ":00");
    }
    // CHECK FAILURE CONDITIONS
    if(currentTime > bookedReturnTrain.departureTime){
    //	notifyFailedExercise("Hai perso il treno di ritorno!")
        nWrong++;
        nErr++;
    	endExercise();
        console.log("sono dentro perdi treno");
    }
    if (currentTime > sleepHour && !slept  && !notifiedNotSlept) {
    	showSnackbar("Non sei tornato in hotel all\'ora prevista!");
    	notifiedNotSlept = true;
        
    	// Show red cross on goalsModal because unsolvable
       	$("#sleepHour").removeClass("fa-square-o");
    	$("#sleepHour").addClass("fa-times");
    	$("#sleepHour").css("color","red");
    }
    for (i=0; i < timedGoals.length; i++){
        if (goalList.hasOwnProperty(timedGoals[i].id) && !goalList[timedGoals[i].id].completed && !goalList[timedGoals[i].id].notifiedFail && currentTime > timedGoals[i].time){
              
        	showSnackbar(timedGoals[i].failMsg + timedGoals[i].time);
        	goalList[timedGoals[i].id].notifiedFail = true;
                nErr++;
        	// Show red cross on goalsModal because unsolvable
        	$("#"+timedGoals[i].id+" i").removeClass("fa-square-o");
        	$("#"+timedGoals[i].id+" i").addClass("fa-times");
        	$("#"+timedGoals[i].id+" i").css("color","red");
        	break;
        }
    }
    $("#date").show();
}

// Updates buttons in top toolbar (adds/removes activities buttons if necessary)
function updateButtons(location){
	// Clear all previous buttons
	$("#waitButton").nextAll("button").remove();
        console.log("aggiorno i bottoni " +location.activity);
	if (location.activity != undefined){
		// This location has possible activities
		for (i=0; i<location.activity.length; i++){
			if (!hotelIdArray.includes(location.id)){ // The location is not
														// one of the hotels
														// which are NOT booked
				var newButton = document.createElement('button');
				newButton.type = "button";
				newButton.className = "btn btn-danger navbar-btn btn-lg pull-right";
				switch(location.activity[i]){
					case "Visita":
					case "Visita Mostra":
					case "Mangia":
						var text = document.createTextNode(location.activity[i]);
						newButton.appendChild(text);
						newButton.addEventListener('click', function(){
							var activity;
							location.activity[i]== "Mangia" ? activity = "Mangiando..." : activity = "Visitando...";
						    doActivityAction(location.openingHours, location, activity);
						});
						$("#topToolbar").append(newButton);
						break;
					case "Dormi (8h)":
						var text = document.createTextNode(location.activity[i]);
						newButton.appendChild(text);
						newButton.addEventListener('click', function(){
						    longSleepAction(location);
						});
						$("#topToolbar").append(newButton);
						break;
					case "Dormi (4h)":
						var text = document.createTextNode(location.activity[i]);
						newButton.appendChild(text);
						newButton.addEventListener('click', function(){
						    shortSleepAction(location);
						});
						$("#topToolbar").append(newButton);						
						break;
                                      
					case "Fai colazione":
						if (currentTime >= BREAKFAST_HOURS[0] && currentTime < BREAKFAST_HOURS[1] && !hadBreakfast){
							var text = document.createTextNode(location.activity[i]);
							newButton.appendChild(text);
							newButton.addEventListener('click', function(){
							   breakfastAction(location);
							});
							$("#topToolbar").append(newButton);
						}
						break;
					case "Vai a correre":
						if (currentTime>= BREAKFAST_HOURS[0]-2 && currentTime < BREAKFAST_HOURS[1]+2){
							var text = document.createTextNode("Fai ginnastica");
							newButton.appendChild(text);
							newButton.addEventListener('click', function(){
							    runAction(location);
							});
							$("#topToolbar").append(newButton);
						}
						break;
					case "Incontra amici":
						for (let j=14; j<=17; j++){
							// Fetch Goal object with id j
                                                    var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
                                                         console.log("goal "+goal + " j = "+j);
							if (goalList.hasOwnProperty(j)){
                                        			var text = document.createTextNode(location.activity[i]);
                                                                 console.log("location  "+i);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Incontra amici...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
                                                for (let j=22; j<=23; j++){
							// Fetch Goal object with id j
                                                    var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
                                                         console.log("goal "+goal + " j = "+j);
							if (goalList.hasOwnProperty(j)){
                                        			var text = document.createTextNode(location.activity[i]);
                                                                 console.log("location  "+i);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Incontra amici...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
						break;
					case "Prendi una birra con amici":
						for (let j=18; j<=21; j++){
							// Fetch Goal object with id j
							var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
							if (goalList.hasOwnProperty(j) && currentTime == goal.time){
								var text = document.createTextNode(location.activity[i]);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Bevendo una birra...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
						break;
					case "Vedi la partita":
						for (let j=24; j<28; j++){
							// Fetch Goal object with id j
							var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
							if (goalList.hasOwnProperty(j) && currentTime == goal.time){
								var text = document.createTextNode(location.activity[i]);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Guardando la partita...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
						break;
					case "Vedi il concerto":
						for (let j=28; j<32; j++){
							// Fetch Goal object with id j
							var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
							if (goalList.hasOwnProperty(j) && currentTime == goal.time){
								var text = document.createTextNode(location.activity[i]);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Guardando il concerto...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
						break;
                                      /*  case "Mangia":
						for (let j=14; j<20; j++){
							// Fetch Goal object with id j
							var goal = timedGoals.filter(function(obj) { return obj.id == j;})[0];
							if (goalList.hasOwnProperty(j) && currentTime == goal.time){
								var text = document.createTextNode(location.activity[i]);
								newButton.appendChild(text);
								newButton.addEventListener('click', function(){
								    doActivityTimedAction(location, "Mangiando...");
								});
								$("#topToolbar").append(newButton);	
							}
						}
						break;*/
					default:
						break;
				}
			}
		}
	}
	if (location.pddlName == bookedReturnTrain.departureLocation && currentTime == bookedReturnTrain.departureTime) {
		var newButton = document.createElement('button');
		newButton.type = "button";
		newButton.className = "btn btn-success navbar-btn btn-lg pull-right";
		var text = document.createTextNode("Torna a casa");
		newButton.appendChild(text);
		newButton.addEventListener('click', function(){
		    returnHome();
		});
		$("#topToolbar").append(newButton);
	}
}

// ACTIVITIES FUNCTIONS

// DO-ACTIVTY
// Called for: visita, visita mostra, mangia
function doActivityAction(openingHours,location, activity){
	if (location.doneActivity){
		bootbox.alert("Hai già visitato questo luogo!");
	} else {
		var startTime = currentTime; // save startTime for building PDDL
										// string
		// Check if it is possible to do that action (location is open/has no
		// opening hours)
		if (openingHours == undefined || (currentTime >= openingHours.saturday[0] && currentTime < openingHours.saturday[1] - 1) || 
			(currentTime >= openingHours.sunday[0] && currentTime < openingHours.sunday[1] - 1)) {		
			    // Waiting dialog
			    var dialog = bootbox.dialog({
			        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp;' + activity + '</h3>',
			        closeButton: false
			    });
			    setTimeout(function(){
			    	dialog.modal('hide');
				    // Update date and time
				    currentTime += 2;
				    updateTime();
				    updateButtons(location);
				    // Check if a goal was satisfied and notify
				    if (location.id == 0){
				    	notifySatisfiedGoal(10);
				    } else if (location.id == 6){
				    	notifySatisfiedGoal(9);
				    } else if (location.id == 11){
				    	notifySatisfiedGoal(11);
				    } else if (location.id == 4){
				    	notifySatisfiedGoal(12);
				    } else if (location.id == 2){
				    	notifySatisfiedGoal(13);
				    }
                                    //aggiunto
                                    makeAdjacentLocationsClickable(location);
			    }, 2000);
		        // Build PDDL String and add user action to user actions list
			    // String : do-activity (?p ?hour1 ?hour2 ?hour3 ?opening-time
				// ?closing-time)
			    var middleHour = startTime + 1;
		        var endTime = startTime + 2;
		        var openHour;
		        var closeHour;
		        if (currentLocation.openingHours == undefined) {
		        	openHour = 1;
		        	closeHour = 48;
		        } else {
		        	if (currentTime <= 24) {
		        		// Use saturday hours
		        		openHour = currentLocation.openingHours.saturday[0];
		        		closeHour = currentLocation.openingHours.saturday[1];
		        	} else {
		        		// Use sunday hours
		        		openHour = currentLocation.openingHours.sunday[0];
		        		closeHour = currentLocation.openingHours.sunday[1];
		        	}
		        }
		        var actionString = "(do-activity " + currentLocation.pddlName + " hour" + startTime + " hour" + middleHour + " hour" + endTime + " hour"
		        					+ openHour + " hour" + closeHour + ")";
		        userActions.push(actionString);
		        console.log("Pushed action: " + actionString);
			    location.doneActivity = true;
		} else {
			bootbox.alert("Il luogo è chiuso o sta per chiudere; riprova durante gli orari di apertura!");
		}
	}
}

// DO-ACTIVTY TIMED
// Called for activities with specific start time (partita, concerto, mangia con
// amici)
function doActivityTimedAction(location, activity){
	    // Waiting dialog
	    var dialog = bootbox.dialog({
	        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp;' + activity + '</h3>',
	        closeButton: false
	    });
	    var startTime = currentTime; // save start time for building pddl
		console.log("attività "+ activity);								// string
	    setTimeout(function(){
	    	dialog.modal('hide');
		    // Check if a goal was satisfied and notify
                    
                    /*
                    
	
        
	
		id: 22,
		time: 34,
		failMsg: "Non hai incontrato i tuoi amici a trastevere"
	
		id: 23,
		time: 35,
		failMsg: "Non hai incontrato i tuoi amici a trastevere"

                     */
                    
                    
                    
                    
		   if (activity == "Incontra amici..." && currentTime == 12){
			   notifySatisfiedGoal(14);
		   } else if (activity == "Incontra amici..." && currentTime == 13){
			   notifySatisfiedGoal(15);
		   } else if (activity == "Incontra amici..." && currentTime == 36){
			   notifySatisfiedGoal(16);
		   } else if (activity == "Incontra amici..." && currentTime == 37){
			   notifySatisfiedGoal(17);
		   } else if (activity == "Bevendo una birra..." && currentTime == 20){
			   notifySatisfiedGoal(18);
		   } else if (activity == "Bevendo una birra..." && currentTime == 21){
			   notifySatisfiedGoal(19);
		   } else if (activity == "Bevendo una birra..." && currentTime == 18){
			   notifySatisfiedGoal(20);
		   } else if (activity == "Bevendo una birra..." && currentTime == 19){
			   notifySatisfiedGoal(21);
		   } else if (activity == "Incontra amici..." && currentTime == 34){
			   notifySatisfiedGoal(22);
		   } else if (activity == "Incontra amici..." && currentTime == 35){
			   notifySatisfiedGoal(23);
		   } else if (activity == "Guardando la partita..." && currentTime == 15){
			   notifySatisfiedGoal(24);
		   } else if (activity == "Guardando la partita..." && currentTime == 12){
			   notifySatisfiedGoal(25);
		   } else if (activity == "Guardando la partita..." && currentTime == 39){
			   notifySatisfiedGoal(26);
		   } else if (activity == "Guardando la partita..." && currentTime == 38){
			   notifySatisfiedGoal(27);
		   } else if (activity == "Guardando il concerto..." && currentTime == 20){
			   notifySatisfiedGoal(28);
		   } else if (activity == "Guardando il concerto..." && currentTime == 18){
			   notifySatisfiedGoal(29);
		   } else if (activity == "Guardando il concerto..." && currentTime == 42){
			   notifySatisfiedGoal(30);
		   } else if (activity == "Guardando il concerto..." && currentTime == 39){
			   notifySatisfiedGoal(31);
		   }
		    // Update date and time
			currentTime += 2;
		    updateTime();
		    updateButtons(location);
                    //aggiornamento mappa
                    makeAdjacentLocationsClickable(location);
	    }, 2000);
        // Build PDDL String and add user action to user actions list
	    // String : do-activity (?p ?hour1 ?hour2 ?hour3 ?opening-time
		// ?closing-time)
	    var middleHour = startTime + 1;
        var endTime = startTime + 2;
        var openHour;
        var closeHour;
        if (currentLocation.openingHours == undefined) {
        	openHour = 1;
        	closeHour = 48;
        } else {
        	if (currentTime <= 24) {
        		// Use saturday hours
        		openHour = currentLocation.openingHours.saturday[0];
        		closeHour = currentLocation.openingHours.saturday[1];
        	} else {
        		// Use sunday hours
        		openHour = currentLocation.openingHours.sunday[0];
        		closeHour = currentLocation.openingHours.sunday[1];
        	}
        }
        var actionString = "(do-activity " + currentLocation.pddlName + " hour" + startTime + " hour" + middleHour + " hour" + endTime + " hour"
        					+ openHour + " hour" + closeHour + ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
}

// LONG SLEEP
function longSleepAction(location){
	if (currentTime != sleepHour){
//	if(!inHotel){	
      //  bootbox.alert("<h3 class='text-center'> <i class='fa fa-exclamation-triangle' style='color: red;'></i>&nbsp; Non è il momento di dormire!</h3>");
       var startTime = currentTime;
        var dialog = bootbox.dialog({
	        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Dormendo 8 ore...</h3>',
	        closeButton: false
	    });
                setTimeout(function(){
	    	dialog.modal('hide');
		    // Update date and time
		    
		    currentTime += 8;
		    updateTime();
		    updateButtons(location);
		    // Notify goal satisfied
		   // notifySatisfiedGoal("sleepHour");
                    makeAdjacentLocationsClickable(location);//aggiunto problema non aggiorna mappa
	    }, 2000);
       var actionString = "(sleep no in time" + currentLocation.pddlName;
        for (i = 0; i < 9; i++){
        	actionString += " hour" + parseInt(i + startTime);
        }
        actionString += ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
                
    } 
    if (currentTime == sleepHour) {
    //  if(inHotel){
	    // Waiting dialog
            nOk++;
	    var dialog = bootbox.dialog({
	        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Dormendo 8 ore...</h3>',
	        closeButton: false
	    });
	    var startTime = currentTime; // save start time for building pddl string
	    setTimeout(function(){
	    	dialog.modal('hide');
		    // Update date and time
		    slept = true;
		    currentTime += 8;
		    updateTime();
		    updateButtons(location);
		    // Notify goal satisfied
		    notifySatisfiedGoal("sleepHour");
                    makeAdjacentLocationsClickable(location);//aggiunto problema non aggiorna mappa
	    }, 2000);
        // Build PDDL String and add user action to user actions list
        var actionString = "(sleep " + currentLocation.pddlName;
        for (i = 0; i < 9; i++){
        	actionString += " hour" + parseInt(i + startTime);
        }
        actionString += ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);
	}
}

// SHORT SLEEP
function shortSleepAction(location){
	if (currentTime != sleepHour){
   //     if(!inHotel){
          var startTime = currentTime;
		//bootbox.alert("<h3 class='text-center'> <i class='fa fa-exclamation-triangle' style='color: red;'></i>&nbsp; Non è il momento di dormire!</h3>");
                 var dialog = bootbox.dialog({
	        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Dormendo 4 ore...</h3>',
	        closeButton: false
	    });
                setTimeout(function(){
	    	dialog.modal('hide');
		    currentTime += 4;
		    updateTime();
		    updateButtons(location);
		    // Notify goal satisfied
                    makeAdjacentLocationsClickable(location); //aggiunto per aggiornamento mappa
	    	}, 2000);
               
                var actionString = "(short-sleep no in time" + currentLocation.pddlName;
        for (i = 0; i < 5; i++){
        	actionString += " hour" + parseInt(i + startTime);
        }
        actionString += ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);

                
    } 
    if(currentTime == sleepHour){
 //  if(inHotel){
    // Waiting dialog
            nOk++;
	    var dialog = bootbox.dialog({
	        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Dormendo 4 ore...</h3>',
	        closeButton: false
	    });
	    var startTime = currentTime; // save start time for building pddl
										// string
	    setTimeout(function(){
	    	dialog.modal('hide');
		    slept = true;
		    // Update date and time
		    currentTime += 4;
		    updateTime();
		    updateButtons(location);
		    // Notify goal satisfied
			notifySatisfiedGoal("sleepHour");
                        makeAdjacentLocationsClickable(location); //aggiunto per aggiornamento mappa
	    	}, 2000);
        // Build PDDL String and add user action to user actions list
        var actionString = "(short-sleep " + currentLocation.pddlName;
        for (i = 0; i < 5; i++){
        	actionString += " hour" + parseInt(i + startTime);
        }
        actionString += ")";
        userActions.push(actionString);
        console.log("Pushed action: " + actionString);

	}
}


//funzione sul pulsante obiettivi
function goalsAction(){
nClickTarget++;
console.log("N Click su obiettivi "+nClickTarget);
}
function bookAction(){
nClickPrenotazioni++;
console.log("N Click su obiettivi "+nClickPrenotazioni);
}

// BREAKFAST
function breakfastAction(location){
    // Waiting dialog
    var dialog = bootbox.dialog({
        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Facendo colazione...</h3>',
        closeButton: false
    });
    var startTime = currentTime; // save start time for building pddl string
    setTimeout(function(){
    	dialog.modal('hide');
        // Update date and time
    	currentTime++;
        updateTime();
        hadBreakfast = true;
        updateButtons(location);
	    // Notify goal satisfied
		notifySatisfiedGoal(33); 
                console.log( "location "+location);
                makeAdjacentLocationsClickable(location);//agginto

    }, 2000);
    // Build PDDL String and add user action to user actions list
    // String: (have-breakfast ?p ?hour1 ?hour2 ?start-breakfast ?end-breakfast)
    var endTime = startTime + 1;
    var actionString = "(have-breakfast " + currentLocation.pddlName + " hour" + startTime + " hour" + endTime + " hour" + BREAKFAST_HOURS[0] + " hour" + BREAKFAST_HOURS[1] +")";
    userActions.push(actionString);
    nOk++;
    console.log("Pushed action: " + actionString);

}

// RUN
function runAction(location){
    // Waiting dialog
    var dialog = bootbox.dialog({
        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Correndo...</h3>',
        closeButton: false
    });
    var startTime = currentTime; // save start time for building pddl string
    setTimeout(function(){
    	dialog.modal('hide');
        // Update date and time
    	currentTime++;
        updateTime();
        updateButtons(location);
        // Notify goal satisfied
	    notifySatisfiedGoal(32);
	    run = true;
            makeAdjacentLocationsClickable(location);//aggiunto
    	}, 2000);
    // Build PDDL String and add user action to user actions list
    // String: (have-breakfast ?p ?hour1 ?hour2 ?start-breakfast ?end-breakfast)
    var endTime = startTime + 1;
    var actionString = "(go-running " + currentLocation.pddlName + " hour" + startTime + " hour" + endTime + " hour" + BREAKFAST_HOURS[0] + " hour" + BREAKFAST_HOURS[1] +")";
    userActions.push(actionString);
    nOk++;
    console.log("Pushed action: " + actionString);
}

// RETURN HOMEm
function returnHome(){
    // Waiting dialog
    var dialog = bootbox.dialog({
        message: '<h3 class="text-center"><i class="fa fa-spin fa-spinner"></i> &nbsp; &nbsp; Tornando a casa...</h3>',
        closeButton: false
    });
   
    // Build PDDL String and add user action to user actions list
    var middleHour = currentTime + 1;
    var endTime = currentTime + 2;
    var actionString = "(return-home " + currentLocation.pddlName + " hour" + currentTime + " hour" + middleHour + " hour" + endTime +")";
    userActions.push(actionString);
    goHome=true;
    nOk++;
    setTimeout(function(){dialog.modal('hide');
    endExercise();
    console.log("sono dentro torna a casa");
    }, 2000);
    console.log("Pushed action: " + actionString);
    
}

// NOTIFY SATISFIED GOAL
function notifySatisfiedGoal(id){
	if (id != "sleepHour"){
	    // Notify goal satisfied
	    if (goalList.hasOwnProperty(id) && !goalList[id].completed) { // if
		goalList[id].completed = true;
	    	showSnackbar("Hai soddisfatto un obiettivo!")
	    	$("#"+id+" i").removeClass("fa-square-o");
	    	$("#"+id+" i").addClass("fa-check-square-o");
	    	$("#"+id+" i").css("color","green");
	    }
	} else {
            if(sleepHour<25)
                id="sleepHour";
            else
                id="sleepHour2";
        
    	showSnackbar("Hai soddisfatto un obiettivo!")
    	/*$("#"+id+" i").removeClass("fa-square-o");
    	$("#"+id+" i").addClass("fa-check-square-o");
    	$("#"+id+" i").css("color","green");*/
        $("#"+id+" i").removeClass("fa-square-o");
    	$("#"+id+" i").addClass("fa-check-square-o");
    	$("#"+id+" i").css("color","green");
	}
	// console.log(goalList);
    // Check if all goals are satisfied and notify
 
    /*  if (!completedGoals && allTrue(goalList) && currentTime > sleepHour){
    	console.log("All goals are satisfied!");
    	completedGoals = true;//variabile che mi dice se tutte le attività sono state svolte
    	showSnackbar("<i class='fa fa-trophy'></i> &nbsp; Congratulazioni! Hai raggiunto tutti gli obiettivi! Ora devi solo tornare a Bologna.")
    }*/

}

function notifyFailedExercise(msg){
	var dialog = bootbox.dialog({
	    message: '<h3 class="text-center"><i class="fa fa-exclamation-triangle" style="color: red;"></i>'+ msg +'</h3>',
	    closeButton: false
	});
	// TODO: FAIL EXERCISE
}

// SHOW SNACKBAR
function showSnackbar(msg) {
	$("#snackbar").empty();
	$("#snackbar").append(msg);
	$("#snackbar").addClass("show");
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ $("#snackbar").removeClass("show"); }, 3000);
}

// Displays summary of completed/non-completed goals in endExerciseModal
/* function displayGoalSummary(){
	// Empty lists to avoid re-adding stuff
	$("#unsolvedGoalsList").empty();
	$("#solvedGoalsList").empty();
	// Count goals
	var numCompletedGoals = countCompleted(goalList);
	var numTotalGoals =  Object.keys(goalList).length + 1; // +1 because there
	if (sleepHour>24)
        var sleepString=$("#sleepHour2").text();    // is the sleep goal
	else
         var sleepString = $("#sleepHour").text(); // Get user-friendly sleep
												// string from goalsModal
	if (slept) {
		// If I slept, it means I completed the "Go to sleep at X hour on
		// Saturday" goal
		numCompletedGoals++;
		// Add to completed goals
		$("#solvedGoalsList").append("<li><i class='fa fa-check-square-o' style='color:green;'></i>"+ sleepString +"</li>");
	} else {
		// Put in non-completed goals list
                nwrong++;
		$("#unsolvedGoalsList").append("<li><i class='fa fa-times' style='color:red;'></i>"+ sleepString +"</li>");
	}
	//da modificare con la performance
        if (numCompletedGoals == numTotalGoals){
		$("#summarySentence").text("Complimenti, sei riuscito a risolvere tutti gli obiettivi!");
	} else if (numCompletedGoals == 1){
		$("#summarySentence").text("Hai risolto " + numCompletedGoals.toString() + " obiettivo su " + numTotalGoals.toString() + ".");
	} else {
		$("#summarySentence").text("Hai risolto " + numCompletedGoals.toString() + " obiettivi su " + numTotalGoals.toString() + ".");
	}
	for (var goal in goalList){
		// Get user-friendly goal string from goalsModal
		var goalString = $("#" + goal).text();
		if(goalList[goal].completed){
			$("#noSolved").hide();
			// Put in completed goals list
			$("#solvedGoalsList").append("<li><i class='fa fa-check-square-o' style='color:green;'></i>"+ goalString +"</li>");
		} else {
			$("#noUnsolved").hide();
			// Put in non-completed goals list
			$("#unsolvedGoalsList").append("<li><i class='fa fa-times' style='color:red;'></i>"+ goalString +"</li>");
		}
	}
}
*/


// Return true if all goals in goalList are completed
function allTrue(obj) {
  for(var o in obj)
      if(obj.hasOwnProperty(o) && !obj[o].completed) return false;
  return true;
}
// Return number of completed goals in goalList
 function countCompleted(obj) {
    var goalNum = 0;
	for(var o in obj){
    //        console.log(obj[o].toString());

        if(obj.hasOwnProperty(o) && obj[o].completed) goalNum++;}
    return goalNum;
 }

function makeAdjacentLocationsClickable(location){
	var source = location.id;
    // For each location, check if it is accessible
    for (i=0; i<locationArray.length; i++){
    	if (level < 4){
    		if ( i == 8 || i == 13){
    			continue;
    		}
    	} else if (level < 7){
    		if (i == 8){
    			continue;
    		}
    	}
        if (i != source){
        	// Reset to non-clickable
               
            locationArray[i].sprite.interactive = false;
            locationArray[i].sprite.buttonMode = false;
          //   console.log("source "+source+ " adjbus " +locationArray[source].adjacentLocationsBus.includes(locationArray[i].id));
            if (locationArray[source].adjacentLocationsBus.includes(locationArray[i].id)){
                // If the location is accessible from where I am by BUS
            	// Check if there is a bus at this hour
            	if (level < 4) {
            		// If difficulty is EASY, there is a bus every hour, no need to check schedules
                    locationArray[i].currentlyAccessible = true;
                    // Make the sprite clickable
                    locationArray[i].sprite.interactive = true;
                    locationArray[i].sprite.buttonMode = true;
                    
            	} else {
            		// If difficulty is MEDIUM or HARD, buses check schedules
            	//	console.log(locationArray[source].busSchedules[locationArray[i].id] + " debug "+ currentTime);
            		
                        if(locationArray[source].busSchedules[locationArray[i].id] != null && locationArray[source].busSchedules[locationArray[i].id].includes(currentTime)){
                    	locationArray[i].currentlyAccessible = true;
                        // Make the sprite clickable
                        locationArray[i].sprite.interactive = true;
                        locationArray[i].sprite.buttonMode = true;
                	}
            	}
            }
            if(locationArray[source].adjacentLocationsWalk.includes(locationArray[i].id)){
                // If the location is accessible from where I am WALKING
                locationArray[i].currentlyAccessible = true;
                // Make the sprite clickable
                locationArray[i].sprite.interactive = true;
                locationArray[i].sprite.buttonMode = true;
            }
        }
    }
}

function endExercise1(){
    $('#endExerciseModal').modal('show');
}


function endExercise(){
    
    
    	$("#unsolvedGoalsList").empty();
	$("#solvedGoalsList").empty();
	// Count goals
        var endTime = new Date().getTime();
        var numCompletedGoals = countCompleted(goalList);
        nCorrect=countCompleted(goalList);
	var numTotalGoals =  Object.keys(goalList).length + 1+1; // +1 because there +1 ritorna a casa
        nTot=numTotalGoals;
	if (sleepHour>24)
        var sleepString=$("#sleepHour2").text();    // is the sleep goal
	else
        var sleepString = $("#sleepHour").text(); // Get user-friendly sleep
	var targetNonRag="";											// string from goalsModal
	if (slept) {
		// If I slept, it means I completed the "Go to sleep at X hour on
		// Saturday" goal
		nCorrect++;
		// Add to completed goals
		$("#solvedGoalsList").append("<li><i class='fa fa-check-square-o' style='color:green;'></i>"+ sleepString +"</li>");
	} else {
		// Put in non-completed goals list
                nWrong++;
		$("#unsolvedGoalsList").append("<li><i class='fa fa-times' style='color:red;'></i>"+ sleepString +"</li>");
                targetNonRag="No dormito alle "+sleepHour;
	}
        if(goHome){
            nCorrect++;
            $("#solvedGoalsList").append("<li><i class='fa fa-check-square-o' style='color:green;'></i>"+ "Ha preso il treno" +"</li>");

        }
        else{ nWrong++;
        	$("#unsolvedGoalsList").append("<li><i class='fa fa-times' style='color:red;'></i>"+ "Ha perso il treno" +"</li>");
                targetNonRag=targetNonRag+" No treno di ritorno";	
    }

    var solved=false;
    nWrong=nTot-nCorrect;
	for (var goal in goalList){
		// Get user-friendly goal string from goalsModal
		var goalString = $("#" + goal).text();
		if(goalList[goal].completed){
			$("#noSolved").hide();
			// Put in completed goals list
			$("#solvedGoalsList").append("<li><i class='fa fa-check-square-o' style='color:green;'></i>"+ goalString +"</li>");
		} else {
			$("#noUnsolved").hide();
			// Put in non-completed goals list
			$("#unsolvedGoalsList").append("<li><i class='fa fa-times' style='color:red;'></i>"+ goalString +"</li>");
                        targetNonRag=targetNonRag+" No "+goalString;
        }
                
	}
    

 
    pTime=0;
    
   var p;     
console.log("user Action "+userActions.toString()+ "goal "+ goalString);
  // $("#loadperformance").load('getweperformance?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+solved+'&nWrong='+nWrong+'&nCorrect='+nCorrect+'&sessid='+sessid);
 $.get("getweperformance",
 {"difficulty" :difficulty,
  "level":level,
  "patientid":patientid,
  "exerciseid":exerciseid,
  "idproblem":idproblem,
  "passed":solved,
  'nWrong':nWrong,
  'nCorrect':nCorrect,
  'sessid':sessid},
  function(data,status){
    var js= JSON.parse(data);               
    var p = js.passed;
    var perf=js.perf;
    $("#summarySentence").append("<p><b>Prestazione</b>: " + Math.round(perf*100) + " % </p>");  
   //  $('#summarySentence').append('<a type="button" class="btn btn-default" href="pianificazione3phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+p+'&nWrong='+nWrong+'&nCorrect='+nCorrect+'&sessid='+sessid+'" >Ok</a>');
     if(p){
          $("#summarySentence").append("<p>Bene hai superato l'esercizio</p>");  
            console.log("sono dentro if sbagliati :"+nWrong+" corretti "+nCorrect+ "nTot "+nTot);
        //$("#summarySentence").text("Complimenti, sei riuscito a risolvere tutti gli obiettivi!");
	} else {
            console.log("sono dentro else");
            solved=false;
		 $("#summarySentence").append("<p> Non hai superato l'esercizio</p> ");

   
     }
      console.log("elementi ok "+nCorrect+"obiettivi sbagliati "+nWrong);
    //$("#unsolvedGoalsList").append("<li id='fine'></li>")
    
    $('#fine').append('<a type="button" class="btn btn-default" href="pianificazione3phase3?difficulty='+difficulty+'&level='+level+'&patientid='+patientid+'&exerciseid='+exerciseid+'&idproblem='+idproblem+'&passed='+p+'&nWrong='+nWrong+'&nCorrect='+nCorrect+'&sessid='+sessid+'&actions='+userActions.toString()+'&initTime='+initTime+'&endTime='+ endTime+'&nClickTarget='+ nClickTarget+'&targetNonRaggi='+ targetNonRag+'&nClickPrenotazioni='+nClickPrenotazioni+'" >Ok</a>');

     
     });
     
    

 
    
      $('#endExerciseModal').modal('show');
    
}
    
    
