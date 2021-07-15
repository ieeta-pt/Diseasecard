import React from 'react';
import { useSelector } from "react-redux";
import {getProtection, getStatus, getURL} from "./diseaseSlice";
import Iframe from 'react-iframe';
import { bypass_url } from '../../../package.json';

export const DiseaseSourceFrame = () => {
    let url = useSelector(getURL)
    const protection = useSelector(getProtection)
    const status = useSelector(getStatus)
    let content;

    //const isSafari = navigator.vendor && navigator.vendor.indexOf('Apple') > -1 && navigator.userAgent && navigator.userAgent.indexOf('CriOS') == -1 && navigator.userAgent.indexOf('FxiOS') == -1;


    if (status === 'succeeded') {
        if (protection) url = bypass_url + "?url=" + url
        console.log("Final URL: " + url);
        content = <Iframe id="myId" className="Iframe" title={url} url={ url } width="100%" height="100%"> </Iframe>;

        //url = "http://localhost:3001?url=" + url;
        // content = <Iframe id="myId" className="Iframe" title={url} url={ url } width="100%" height="100%"> </Iframe>;

        // if (protection) content = <iframe is="x-frame-bypass" title={url} src={ url } style={{ width: "100%", height: "100%", marginBottom: 0}}> </iframe>;
        // else            content = <Iframe id="myId" className="Iframe" title={url} url={ url } width="100%" height="100%"> </Iframe>;
    }

    return (
        <div style={{height: "100%"}}>
            {content}
        </div>
    );

}