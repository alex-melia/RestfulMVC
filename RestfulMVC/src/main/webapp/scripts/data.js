/**
 * Uses format parameter to set the callback function
 */
function findHandler(format) {
  if (format == "application/xml") {
    return(displayFilmsXml);
  } else if (format == "application/json") {
    return(displayFilmsJson);
  } else {
    return(displayFilmsText);
  } 
}

/**
 * Parses response from the HTTP Request and returns data to client in XML
 */
function displayFilmsXml(request, resultRegion) {
	if ((request.readyState == 4) && (request.status == 200)) {

		var response = request.response;
	    var parser = new DOMParser();
	    var xmlDoc = parser.parseFromString(response, "text/xml");
	    
	    var films = xmlDoc.getElementsByTagName('film');
	    var headings = ["year", "director", "stars", "review"];
	    var cards = [];
    
    	html = "";
    	//If response is empty...
	    if (films.length == 0) {
			html += "<h5>No Results!</h5>";
			noResults(resultRegion, html);
		}
		//Loop through films
		else if (films.length > 0) {
		    for (i = 0; i< films.length; i++) {
				html = "";
				html += "<div class='col'><div class='card h-100'>";
				html += "<h5 class='card-header'>" + films[i].getElementsByTagName('title')[0].textContent + "</h5><div class='card-body'><ul class='list-group list-group-flush'>";
				for (j = 0; j<headings.length; j++) {
					html += "<li class='list-group-item'>" + `<h5>${headings[j]}</h5>` + films[i].getElementsByTagName(headings[j])[0].textContent + "</li>";
				}
				html += "</ul></div>";
				html += "<div class='card-footer'><button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#updateModal' data-id='" + films[i].getElementsByTagName('id')[0].textContent + "' data-title='" + films[i].getElementsByTagName('title')[0].textContent + "' data-director='" + films[i].getElementsByTagName('director')[0].textContent + "' data-year='" + films[i].getElementsByTagName('year')[0].textContent + "' data-stars='" + films[i].getElementsByTagName('stars')[0].textContent + "' data-review='" + films[i].getElementsByTagName('review')[0].textContent + "' onclick='populateModal(this)'>Update Film</button>";
				html += "<button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#deleteModal' data-id='" + films[i].getElementsByTagName('id')[0].textContent + "' onclick='populateModal(this)'>Delete Film</div></div></div>";
				cards.push(html);
		  	}
		    htmlInsert(cards);
	    }
  	}
}

/**
 * Parses response from the HTTP Request and returns data to client in JSON
 */
function displayFilmsJson(request, resultRegion) {
	if ((request.readyState == 4) && (request.status == 200)) {
	
	    var rawData = request.responseText;
	    var film = JSON.parse(rawData);
	    
	    cards = [];
	    html = "";
		
		if (film.length == 0) {
			html += "<h5>No Results!</h5>";
			noResults(resultRegion, html);
		}
		else if (film.length > 0) {
		    for (i = 0; i<film.length; i++) {
				html = "";
				html += "<div class='col'><div class='card h-100'>";
				html += "<h5 class='card-header'>" + `${film[i].title}` + "</h5><div class='card-body'><ul class='list-group list-group-flush'>";
				
				html += "<li class='list-group-item'>" + `<h5>year</h5>` + `${film[i].year}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>director</h5>` + `${film[i].director}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>stars</h5>` + `${film[i].stars}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>review</h5>` + `${film[i].review}` + "</li>";
				html += "</ul></div><div class='card-footer'><button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#updateModal' data-id='" + film[i].id + "' data-title='" + film[i].title + "' data-director='" + film[i].director + "' data-year='" + film[i].year + "' data-stars='" + film[i].stars + "' data-review='" + film[i].review + "' onclick='populateModal(this)'>Update Film</button>";
				html += "<button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#deleteModal' data-id='" + film[i].id + "' onclick='populateModal(this)'>Delete Film</button></div></div></div>";
				cards.push(html);
			}
		    htmlInsert(cards);
	    }
	}
}

/**
 * Parses response from the HTTP Request and returns data to client in Text
 */
function displayFilmsText(request, resultRegion) {
	if ((request.readyState == 4) && (request.status == 200)) {
	
	    var rawData = request.responseText;
	    var array = rawData.split("#");
	    var blank = array.indexOf("");
	    if ( ~blank ) array.splice(blank, 1);
	    
		html = "";
		cards = [];
		num = 0;
		
		if (array.length == 0) {
			html += "<h5>No Results!</h5>";
			noResults(resultRegion, html);
		}
		else {
		    for (const element of array) {
			
				const data = element.split("|");
				html = "";
				html += "<div class='col'><div class='card h-100'>";
				html += "<h5 class='card-header'>" + `${data[1]}` + "</h5><div class='card-body'><ul class='list-group list-group-flush'>";
				html += "<li class='list-group-item'>" + `<h5>year</h5>` + `${data[2]}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>director</h5>` + `${data[3]}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>stars</h5>` + `${data[4]}` + "</li>";
				html += "<li class='list-group-item'>" + `<h5>review</h5>` + `${data[5]}` + "</li>";
				html += "</ul></div><div class='card-footer'><button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#updateModal' data-id='" + data[0] + "' data-title='" + data[1] + "' data-director='" + data[3] + "' data-year='" + data[2] + "' data-stars='" + data[4] + "' data-review='" + data[5] + "' onclick='populateModal(this)'>Update Film</button>";
				html += "<button type='button' class='btn btn-success' data-bs-toggle='modal' data-bs-target='#deleteModal' data-id='" + data[0] + "' onclick='populateModal(this)'>Delete Film</button></div></div></div>";
				cards.push(html);
			  }
			htmlInsert(cards);
		}
	}
}

/**
 * Gathers the query, search type, data format set by client and sends request to the API with ajaxGet
 */
function displayResults(queryField, searchTypeField, formatField, resultRegion) {
	
	//Declare the API address
	var address = "films-api";
	var query = getValue(queryField);
	var searchType = getValue(searchTypeField);
	var format = getValue(formatField);
	
	//If no format set, specify XML as default.
	if (format == "") {
		format = "application/xml";
	}
	var data = "query=" + query + "&searchType=" + searchType + "&format=" + format;
	
	//Declare callback function as result of 'findHandler' with format as a parameter
	var responseHandler = findHandler(format);
	//Set parameters of AJAX function, include callbackfunction to handle response
	ajaxGet(address, data, format, function(request) {
		responseHandler(request, resultRegion);
	});
}

/**
 * Gathers the query, search type, data format set by client and sends request to the API with ajaxPost
 */
function addFilm(formatField) {
	var address = "films-api";
	var format = getValue(formatField);
	
	//Declare xml, json and text variables + set the values as the result of the 'formTo' functions
	if (format == "application/xml") {
		var xml = formToXml("add-film-form");
		var data = xml;
	}
	else if (format == "application/json") {
		var json = formToJson("add-film-form");
		var data = json;
	}
	else {
		var text = formToTextAdd("add-film-form");
		var data = text;
	}
	ajaxPost(address, data, format, function() {
		displayResults("query", "search-type", "data-type", "results");
	});
}

/**
 * Gathers the format set by client and sends request to API with ajaxPut
 */
function updateFilm(formatField) {
	
	var address = "films-api";
	var format = getValue(formatField);

	if (format == "application/xml") {
		var xml = formToXml("update-film-form");
		var data = xml;
	}
	else if (format == "application/json") {
		var json = formToJson("update-film-form");
		var data = json;
	}
	else {
		var text = formToTextUpdate("update-film-form");
		var data = text;
	}

	ajaxPut(address, data, format, function() {
		displayResults("query", "search-type", "data-type", "results");
	});
	
}

/**
 * Gathers the format set by client and sends request to API with ajaxDelete
 */
function deleteFilm(formatField) {
	
	var address = "films-api";
	var format = getValue(formatField);
	
	if (format == "application/xml") {
		var xml = formToXml("delete-film-form");
		var data = xml;
	}
	else if (format == "application/json") {
		var json = formToJson("delete-film-form");
		var data = json;
	}
	else {
		var text = formToText2("delete-film-form");
		var data = text;
	}

	ajaxDelete(address, data, format, function() {
		displayResults("query", "search-type", "data-type", "results");
	});
}
