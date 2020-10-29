import axios from 'axios';
import { api_url } from '../../package.json';

const API = axios.create({
    baseURL: api_url,
    xsrfHeaderName: "X-CSRFToken",
    xsrfCookieName: "csrftoken"
});



const getModuleURL = function (module) {
    switch (module){
        case "searchResults":                  return "/services/results";
        case "searchAutocomplete":             return "/services/autocomplete";
        default:                               return "";
    }
};

const getTypeParameters = function (module) {
    switch (module){
        case "searchResults":                  return ["query"];
        case "searchAutocomplete":             return ["query"];
        default:                               return "";
    }
}

/*
 * Function to build the get webservices path
 * The parameters in array must be presented in the same order establish in the typeParameters array.
 */
const buildGETPath = function (globalPath, urlPath, typeParameters, parameters) {
    let path = globalPath;

    if (urlPath !== "") path = path + "/" + urlPath;

    for (let i = 0 ; i < typeParameters.length; i++) path = path + "?" + typeParameters[i] + "=" + parameters[i]

    return path;
};

/*
 * Function to build the post webservices path
 */
const buildPOSTPath = function (globalPath, urlPath) {
    if (urlPath === undefined || urlPath === null)
        return globalPath + "/";

    return globalPath + "/" + urlPath + "/";
};

/*
 * The function receive:
 * module: which is a string to define which webservice module to call
 * utlPath: is the rest of the path
 * parameters: are the parameters to send in the post
 * */
API.PATCH = function (module, urlPath, parameters) {
    let url = getModuleURL(module);
    let path = buildPOSTPath(url, urlPath);
    return API.patch(path, parameters);
};

/*
 * The function receive:
 * module: which is a string to define which webservice module to call
 * utlPath: is the rest of the path
 * parameters: are the parameters to send in the post
 * */
API.POST = function (module, urlPath, parameters) {
    let url = getModuleURL(module);
    let path = buildPOSTPath(url, urlPath);
    return API.post(path, parameters);
};

/*
 * The function receive:
 * module: which is a string to define which webservice module to call
 * utlPath: is the rest of the path
 * extraPath: is a array to have more fields in the path (maybe it is not necessary)
 * */
API.GET = function (module, urlPath, parameters) {
    let url = getModuleURL(module);
    let typeParameters = getTypeParameters(module)
    let path = buildGETPath(url, urlPath, typeParameters, parameters);
    return API.get(path)
};

export default API;