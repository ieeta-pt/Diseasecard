import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getAllEntities, getOntologyStructure, getOntologyStructureInfo} from "./listResourcesSlice";


export const ListResources = () => {
    const dispatch = useDispatch();
    const ontologyStructure = useSelector(getOntologyStructure)

    useEffect(() => {
        dispatch(getOntologyStructureInfo())
        dispatch(getAllEntities())
    }, [])


    return (
        <div><pre>{JSON.stringify(ontologyStructure, null, 2) }</pre></div>
    )
}