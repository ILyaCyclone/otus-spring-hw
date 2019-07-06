import {throwIfError} from "./errorHandler";
import {alertError} from "./alert";

export function getFromApi(url) {
    return fetch(url)
        .then(response => throwIfError(response))
        .then(response => response.json())
        .catch(error => alertError(error));
}