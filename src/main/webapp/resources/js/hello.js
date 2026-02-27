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