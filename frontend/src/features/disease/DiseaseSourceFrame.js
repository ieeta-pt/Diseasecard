import React from 'react';
import { useSelector } from "react-redux";
import {getProtection, getStatus, getURL} from "./diseaseSlice";
import Iframe from 'react-iframe'

export const DiseaseSourceFrame = () => {
    const url = useSelector(getURL)
    const protection = useSelector(getProtection)
    const status = useSelector(getStatus)
    let content;

    if (status === 'succeeded') {
        if (protection) content = <iframe is="x-frame-bypass" title={url} src={ url } style={{ width: "100%", height: "100%"}}> </iframe>;
        else            content = <Iframe id="myId" className="Iframe" title={url} url={ url } width="100%" height="100%"> </Iframe>;
    }

    return (
        <div style={{height: "100%"}}>
            {content}
        </div>
    );

}