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
        default:                               return "";
    }
};

/*
 * Function to build the get webservices path
 */
const buildGETPath = function (globalPath, urlPath, parameters) {
    if (urlPath === undefined)
        return globalPath + "/";

    if (parameters === undefined)
        return globalPath + "/" + urlPath + "/";

    return globalPath + "/" + urlPath + "?query=" + parameters;
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
    let path = buildGETPath(url, urlPath, parameters);
    return API.get(path)
};

export default API;