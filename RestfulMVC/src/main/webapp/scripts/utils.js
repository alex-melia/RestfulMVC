// Get the browser-specific request object, either for
// Firefox, Safari, Opera, Mozilla, Netscape, or IE 7 (top entry);
// or for Internet Explorer 5 and 6 (bottom entry). 

function getRequestObject() {
	if (window.XMLHttpRequest) {
		return(new XMLHttpRequest());
	} else if (window.ActiveXObject) { 
    	return(new ActiveXObject("Microsoft.XMLHTTP"));
	} else {
    	return(null); 
	}
}

// Return escaped value of textfield that has given id.
// The builtin "escape" function url-encodes characters.

function getValue(id) {
	return(document.getElementById(id).value);
}

/**
 * Returns data to client and creates pagination
 */
function htmlInsert(cards) {
	var cardsPerPage = 9;
	var currentPage = 1;
	createPaginationButtons(cardsPerPage, cards);
	updateDisplayedCards(currentPage, cards);
	changePage(currentPage, cards);
}

/**
 * Returns 'no results' to client
 */
function noResults(id, htmlData) {
	document.getElementById(id).innerHTML = htmlData;
	document.getElementById("pagination-buttons").innerHTML = "";
}

///////////////////////////////// AJAX FUNCTIONS //////////////////////////////////////

/**
 * The AJAX functions create a request and send it to API address
 *
 * @param address
 * @param data
 * @param format
 * @param responseHandler
 */
 

function ajaxGet(address, data, format, responseHandler) {
	var request = getRequestObject();
	//Set callback function to handle response
	request.onreadystatechange = function() { responseHandler(request); };
	request.open("GET", `${address}?${data}`, true);
	request.setRequestHeader("Accept", format);
	request.send(data);
}

function ajaxPost(address, data, format, displayResults) {
	var request = getRequestObject();
	//Set callback function to handle response + only execute at readyState of 4
	request.onreadystatechange = function() { if (request.readyState == 4) { displayResults(); } };
	request.open("POST", address, true);
	request.setRequestHeader("Accept", format);
	request.send(data);
}

function ajaxPut(address, data, format, displayResults) {
	var request = getRequestObject();
	request.onreadystatechange = function() { if (request.readyState == 4) { displayResults(); } };
	request.open("PUT", address, true);
	request.setRequestHeader("Accept", format);
	request.send(data);
}

function ajaxDelete(address, data, format, displayResults) {
	var request = getRequestObject();
	request.onreadystatechange = function() { if (request.readyState == 4) { displayResults(); } };
	request.open("DELETE", address, true);
	request.setRequestHeader("Accept", format);
	request.send(data);
}

///////////////////////////////// CLIENT INTERACTION FUNCTIONS //////////////////////////////////////

/**
 * Sets the hidden input 'data-type'
 */
function selectDataFormat(dataType) {
	const dt = document.getElementById('data-type');
	var data = getValue(dataType);	
	dt.value = data;
}

/**
 * Sets the hidden input 'search-type'
 */
function selectSearchType(searchType) {
	const st = document.getElementById('search-type');
	var data = getValue(searchType);
	st.value = data;
}

function htmlToXML(html) {
	if (window.ActiveXObject) {
	    var doc = new ActiveXObject("Microsoft.XMLDOM");
	    doc.async = false;
	    doc.loadXML(html);
	    return doc;
	} 
	else if (document.implementation && document.implementation.createDocument) {
	    var parser = new DOMParser();
	    var doc = parser.parseFromString(html, "text/xml");
	    return doc;
	}
}

function htmlToJSON(html) {
	var obj = {};
	var elements = html.querySelectorAll("input, textarea");
	for (var i = 0; i < elements.length; ++i) {
	    var element = elements[i];
	    var name = element.name;

	    var value = element.value;
	    console.log(value);
    	if (name) {
      		obj[name] = value;
    	}
  	}
	return JSON.stringify(obj);
}

function populateModal(button) {
	
	var deleteIdField = document.getElementById('delete-film-id');
	var idField = document.getElementById('update-film-id');
	var titleField = document.getElementById('update-film-title');
	var directorField = document.getElementById('update-film-director');
	var yearField = document.getElementById('update-film-year');
	var starsField = document.getElementById('update-film-stars');
	var reviewField = document.getElementById('update-film-review');

	deleteIdField.value = button.getAttribute('data-id');
	idField.value = button.getAttribute('data-id');
	titleField.value = button.getAttribute('data-title');
	directorField.value = button.getAttribute('data-director');
	yearField.value = button.getAttribute('data-year');
	starsField.value = button.getAttribute('data-stars');
	reviewField.value = button.getAttribute('data-review');
}

///////////////////////////////// FORM FUNCTIONS //////////////////////////////////////

function formToXml(formName) {
	var form = document.getElementById(formName);
	var xml = htmlToXML("<film></film>");
	
	for (var i = 0; i < form.elements.length; i++) {
	    var element = form.elements[i];
	    var node = xml.createElement(element.name);
	    node.appendChild(xml.createTextNode(element.value));
	    xml.documentElement.appendChild(node);
	    }
	return xml;
}

function formToJson(formName) {
	var form = document.getElementById(formName);
	var data = htmlToJSON(form);

	return data;
}

function formToTextAdd(formName) {
	var form = document.getElementById(formName);
	var f = form.querySelectorAll("input, textarea");
	data = "#" + f[0].value + "|" + f[1].value + "|" + f[2].value + "|" + f[3].value + "|" + f[4].value;
	return data;
}

function formToTextUpdate(formName) {
	var form = document.getElementById(formName);
	var f = form.querySelectorAll("input, textarea");
	data = "#" + f[0].value + "|" + f[1].value + "|" + f[2].value + "|" + f[3].value + "|" + f[4].value + "|" + f[5].value;
	return data;
}

function formToText2(formName) {
	var form = document.getElementById(formName);
	var f = form.querySelectorAll("input, textarea");
	console.log(f[0].value);
	data = "" + f[0].value;
	return data;
}

///////////////////////////////// DISPLAY FUNCTIONS //////////////////////////////////////

function createPaginationButtons(cardsPerPage, cards) {
	
	//Set number of pages as result of length of cards array / cardsPerPage rounded up
	var numPages = Math.ceil(cards.length / cardsPerPage);
	var paginationButtons = '';
	var pagesPerRow = 12;
	
	//Build buttons
	paginationButtons += '<div class="row">';
	for (var i = 1; i <= numPages; i++) {
    	paginationButtons += '<div class="col"><button class="page-button" onclick="changePage()" data-page="' + i + '">' + i + '</button></div>';
   
        if (i % pagesPerRow == 0 && i !==numPages) {
			paginationButtons += '</div><div class="row">';		
		}
		else if(i == numPages) {
			paginationButtons += '</div>';
		}
    }
	document.getElementById('pagination-buttons').innerHTML = paginationButtons;
}

function updateDisplayedCards(currentPage, cards) {
	var cardsPerPage = 9;
    var startIndex = (currentPage - 1) * cardsPerPage;
    var endIndex = startIndex + cardsPerPage;
    var displayedCards = '';
    for (var i = startIndex; i < endIndex; i++) {
    	if (i < cards.length) {
        	displayedCards += cards[i];
    	}
    }
    document.getElementById('results').innerHTML = displayedCards;
}
  
function changePage(currentPage, cards) {
	
	document.getElementById('pagination-buttons').addEventListener('click', function(e) {
		
    	if (e.target.classList.contains('page-button')) {
			currentPage = e.target.dataset.page;
			updateDisplayedCards(currentPage, cards);
    	}
	});
}
