/**
 * Author: Margherita Donnici
 * Description: This file contains all locations for the Weekend in Rome exercise
 */

// Create location objects
var colosseo = {
    id:0,
    name: "Colosseo",
    pddlName: "colosseo",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/colosseo.png'),
    coordinates: {
        x: 0.89, // 584
        y: 0.85 //438
    },
    infoIconOffset: {
        x: 55,
        y: 60,
    },
    adjacentLocationsBus: [13,1],
    adjacentLocationsWalk: [7],
    busSchedules: {
    	13: [5,7,9,11,13,15,17,19,21,23,25,31,34,37,40,43,46],
    	1: [8,11,14,17,20,24,32,36,40,44]
    },
    openingHours: {
    	saturday:[9,18],
    	sunday:[38,42]
    },
    activity: ["Visita"],
    currentlyAccessible: false,
    doneActivity: false
};

var treviFountain = {
    id:1,
    name: "Fontana di Trevi",
    pddlName: "trevi-fountain",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/fountain.png'),
    coordinates: {
        x: 0.68,
        y: 0.59
    },
    infoIconOffset: {
        x: 80,
        y: 65,
    },
    adjacentLocationsBus: [0],
    adjacentLocationsWalk: [2,10,7],
    busSchedules: {
    	0: [8,11,14,17,20,24,32,36,40,44]
    },
    currentlyAccessible: false
};

var pantheon = {
    id:2,
    name: "Pantheon",
    pddlName: "pantheon",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/pantheon.png'),
    coordinates: {
        x: 0.5,
        y: 0.45
    },
    infoIconOffset: {
        x: 60,
        y: 50,
    },
    adjacentLocationsBus: [12],
    adjacentLocationsWalk: [1,4],
    busSchedules: {
    	12: [8,11,14,17,20,32,36,40,44]
    },
    openingHours: {
    	saturday:[9,13],
    	sunday:[38,42]
    },
    activity: ["Visita"],
    currentlyAccessible: false,
    doneActivity: false
};

var auditorium = {
    id:3,
    name: "Auditorium",
    pddlName: "auditorium",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/auditorium.png'),
    coordinates: {
        x: 0.5,
        y: 0.22
    },
    infoIconOffset: {
        x: 60,
        y: 60,
    },
    adjacentLocationsBus: [12],
    adjacentLocationsWalk: [5],
    busSchedules: {
    	12: [8,10,12,14,16,18,20,32,34,37,41,44]
    },
    activity: ["Vedi il concerto"],
    currentlyAccessible: false
};

var araPacis = {
    id:4,
    name: "Ara Pacis",
    pddlName: "ara-pacis",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/arapacis.png'),
    coordinates: {
        x: 0.26,
        y: 0.59
    },
    infoIconOffset: {
        x: -50,
        y: 55,
    },
    adjacentLocationsBus: [6,11],
    adjacentLocationsWalk: [10,2],
    busSchedules: {
    	6: [7,10,13,16,19,31,35,39,43],
    	11: [7,10,13,16,19,22,25,26,27,31,35,39,43]
    },
    openingHours: {
    	saturday:[14,18],
    	sunday:[33,37]
    },
    activity: ["Visita Mostra"],
    currentlyAccessible: false,
    doneActivity: false
};

var stadio = {
    id:5,
    name: "Stadio Olimpico",
    pddlName: "stadio-olimpico",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/stadio.png'),
    coordinates: {
        x: 0.14,
        y: 0.06
    },
    infoIconOffset: {
        x: -90,
        y: 35,
    },
    adjacentLocationsBus: [8,13],
    adjacentLocationsWalk: [3,9],
    busSchedules: {
    	8: [8,12,16,20,33,38,43],
    	13: [5,7,9,11,13,15,17,19,21,23,25,29,32,35,38,41,44,47]
    },
    activity: ["Vedi la partita"],
    currentlyAccessible: false
};

var sanPietro = {
    id:6,
    name: "San Pietro",
    pddlName: "san-pietro",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/sanpietro.png'),
    coordinates: {
        x: 0.11,
        y: 0.6
    },
    infoIconOffset: {
        x: -65,
        y: 60,
    },
    adjacentLocationsBus: [4],
    adjacentLocationsWalk: [9,11],
    busSchedules: {
    	4: [7,10,13,16,19,31,35,39,43]
    },
    openingHours: {
    	saturday:[9,18],
    	sunday:[33,42]
    },
    activity: ["Visita"],
    currentlyAccessible: false,
    doneActivity: false
};

var hotel1 = {
    id:7,
    name: "Hotel Aurora",
    pddlName: "aurora",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/hotel.png'),
    coordinates: {
        x: 0.63,
        y: 0.87
    },
    infoIconOffset: {
        x: 65,
        y: 55,
    },
    adjacentLocationsBus: [],
    adjacentLocationsWalk: [0,1],  
    activity: ["Dormi (8h)", "Dormi (4h)", "Fai colazione", "Vai a correre"],
    currentlyAccessible: false
};

var hotel2 = {
    id:8,
    name: "Hotel Belvedere",
    pddlName: "belvedere",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/hotel.png'),
    coordinates: {
        x: 0.93,
        y: 0.08
    },
    infoIconOffset: {
        x: -80,
        y: 50,
    },
    adjacentLocationsBus: [5],
    adjacentLocationsWalk: [13],
    busSchedules: {
    	5: [8,12,16,20,33,38,43]
    },
    activity: ["Dormi (8h)", "Dormi (4h)","Entra in hotel", "Fai colazione", "Vai a correre"],
    currentlyAccessible: false
};

var hotel3 = {
    id:9,
    name: "Hotel Europa",
    pddlName: "europa",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/hotel.png'),
    coordinates: {
        x: 0.065,
        y: 0.35
    },
    infoIconOffset: {
        x: 65,
        y: 55,
    },
    adjacentLocationsBus: [],
    adjacentLocationsWalk: [5,6],
    activity: ["Dormi (8h)", "Dormi (4h)", "Fai colazione", "Vai a correre"],
    currentlyAccessible: false
};

var hotel4 = {
    id:10,
    name: "Hotel Centrale",
    pddlName: "centrale",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/hotel.png'),
    coordinates: {
        x: 0.39,
        y: 0.58
    },
    infoIconOffset: {
        x: 68,
        y: 58,
    },
    adjacentLocationsBus: [],
    adjacentLocationsWalk: [11,1,4],
    activity: ["Dormi (8h)", "Dormi (4h)", "Fai colazione", "Vai a correre"],
    currentlyAccessible: false
};

var trastevere = {
    id:11,
    name: "Trastevere",
    pddlName: "trastevere",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/trastevere.png'),
    coordinates: {
        x: 0.32,
        y: 0.86
    },
    infoIconOffset: {
        x: 45,
        y: 60,
    },
    adjacentLocationsBus: [4],
    adjacentLocationsWalk: [10,6],
    busSchedules: {
    	4: [7,10,13,16,19,22,25,26,27,31,35,39,43]
    },
    activity: ["Incontra amici", "Prendi una birra con amici", "Mangia"],
    currentlyAccessible: false,
};

var termini = {
    id:12,
    name: "Termini",
    pddlName: "termini",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/trainstation.png'),
    coordinates: {
        x: 0.73,
        y: 0.36
    },
    infoIconOffset: {
        x: 50,
        y: 60,
    },
    adjacentLocationsBus: [3,13,2],
    adjacentLocationsWalk: [],
    busSchedules: {
    	13: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47],
    	3: [8,10,12,14,16,18,20,32,34,37,41,44],
    	2: [8,11,14,17,20,32,36,40,44]
    },
    currentlyAccessible: false
};

var tiburtina = {
    id:13,
    name: "Tiburtina",
    pddlName: "tiburtina",
    texture: PIXI.Texture.fromImage('resources/images/weekend-rome/location-icons/trainstation.png'),
    coordinates: {
        x: 0.94,
        y: 0.31
    },
    infoIconOffset: {
        x: -50,
        y: 70,
    },
    adjacentLocationsBus: [5,0,12],
    adjacentLocationsWalk: [8],
    busSchedules: {
        5: [5,7,9,11,13,15,17,19,21,23,25,29,32,35,38,41,44,47],
    	0: [8,10,12,14,16,18,20,22,24,32,35,38,41,44,47],
    	12: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47]
    },
    currentlyAccessible: false
};

const locationArray = [colosseo, treviFountain, pantheon, auditorium, araPacis, stadio, sanPietro, hotel1, hotel2, hotel3, hotel4, trastevere, termini, tiburtina];