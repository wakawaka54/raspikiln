
/**
 * Request and parse json from given address
 * @param url {URL}
 * @param callback
 * @return Promise<Json>
 */
function getJSON(url, callback) {

    function handleErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    try {
        fetch(url, {
            method: 'GET',
            headers: {
                Accept: 'application/json',
            }
        })
            .then(handleErrors)
            .then(response => response.json())
            .then(json => callback(json))
            .catch(error => console.log(error));
    } catch (e) {
        alert("Fetch of plot data failed with error: " + e)
    }
}