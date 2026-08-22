function post(path, params, method)
{
	method = method || "post"; 

	var form = document.createElement("form");
	form.setAttribute("method", method);
	form.setAttribute("action", path);

	for ( var key in params) {
		if (params.hasOwnProperty(key)) {
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type", "hidden");
			hiddenField.setAttribute("name", key);
			hiddenField.setAttribute("value", params[key]);

			form.appendChild(hiddenField);
		}
	}

	document.body.appendChild(form);
	form.submit();
}


/*

Tablet 1: Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36
Tablet 2: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36
Tablet 3: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36

Il primo e' corretto, gli altri due derivano dall'abilitazione del desktop mode

 */

function isMobile() {
	const regex = new RegExp(/Mobi|Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i);
	//const tabletRegex = new RegExp("Mozilla\/5\.0 \(X11; Linux x86_64\) AppleWebKit\/537\.36 \(KHTML, like Gecko\) Chrome\/[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+ Safari\/537\.36");
	const userAgent = navigator.userAgent;


	return regex.test(userAgent);
}


function addBackground(parentImg, backgroundColor, opacity = 0.2)
{
	if (typeof parentImg === 'string' || parentImg instanceof String)
		parentImg = document.getElementById(parentImg);

	if(parentImg == null)
		console.log("parentImg is null");

	// Crea un contenitore relativo per gestire il posizionamento
	const container = document.createElement('div');
	container.style.position = 'relative';
	container.style.display = 'inline-block';
	parentImg.parentNode.insertBefore(container, parentImg);
	container.appendChild(parentImg);
	parentImg.style.backgroundColor = "transparent";

	// Crea l'immagine figlia
	const childImg = document.createElement('div');

	// Copia gli attributi rilevanti dal padre
	childImg.src = parentImg.src;
	childImg.alt = parentImg.alt;
	childImg.className = parentImg.className;

	// Imposta lo stile per il posizionamento assoluto
	childImg.style.position = 'absolute';
	childImg.style.top = '0';
	childImg.style.left = '0';
	childImg.style.width = '100%';
	childImg.style.height = '100%';
	childImg.style.backgroundColor = backgroundColor;
	childImg.style.opacity = opacity;
	childImg.style.pointerEvents = 'none'; // Permette di cliccare attraverso l'immagine figlia

	// Aggiungi l'immagine figlia al contenitore
	container.appendChild(childImg);

	return childImg;
}