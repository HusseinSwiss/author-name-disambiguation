var searchElement;
var results;
//Based on exemple from http://www.siteduzero.com/informatique/tutoriels/dynamisez-vos-sites-web-avec-javascript/correction-20
function getResults(keywords,city) { // Effectue une requête et récupère les résultats
        var xhr = new XMLHttpRequest();
        xhr.open('GET', './autocomplete.jsp?search='+ encodeURIComponent(keywords)+'&city='+encodeURIComponent(city));
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                displayResults(xhr.responseText);
 
            }
        };
        xhr.send(null);
        return xhr;
    }
	function chooseResult(result) { // Choisit un des résultats d'une requête et gère tout ce qui y est attaché
       
        searchElement.value = result.innerHTML; // On change le contenu du champ de recherche et on enregistre en tant que 
        results.style.display = 'none'; // On cache les résultats
        searchElement.focus();
 
    }
    function displayResults(response) { // Affiche les résultats d'une requête
       if (response.length>0) { // On ne modifie les résultats que si on en a obtenu
        	results.style.display ='block';
        	
            response = response.split('|');
            var responseLen = response.length;
            results.innerHTML = ''; // On vide les résultats
 
            for (var i = 0, div ; i < responseLen ; i++) {
            	
                div = results.appendChild(document.createElement('div'));
                div.innerHTML = response[i];
                 
                div.onclick = function() {
                    chooseResult(this);
                };
 
            }
 
        }else{
        	results.style.display ='none';
        }
 
    }
function loadAutocomplete(){
    results = document.getElementById('autocomplete_result');
	searchElement = document.getElementById('location');
}