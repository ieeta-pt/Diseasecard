import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getAllResources, getResources} from "./systemStatusSlice";

export const SystemStatus = () => {
    const dispatch = useDispatch();
    const allResources = useSelector(getResources)

    useEffect(() => {
        dispatch(getAllResources())
    }, [])

    console.log(allResources)

    /*
        Return of component
     */
    return (
        <div><pre>{JSON.stringify(allResources, null, 2) }</pre></div>
    )
}