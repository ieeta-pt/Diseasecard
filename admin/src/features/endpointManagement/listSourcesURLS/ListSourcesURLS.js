import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import BootstrapTable from 'react-bootstrap-table-next';
import {getListSourcesURLS, getSourcesURLS} from "./listSourcesURLS";

export const ListSourcesURLS = () => {
    const dispatch = useDispatch()
    const sourcesURLs = useSelector(getListSourcesURLS)

    useEffect(() => {
        dispatch(getSourcesURLS())
    }, [])

    const columns = [
        {
            dataField: 'source',
            text: 'Source',
            headerStyle: () => {
                return { width: "25%" };
            }
        },
        {
            dataField: 'url',
            text: 'Base URL'
        }
    ];


    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable keyField='source' data={sourcesURLs} columns={columns}/>
        </div>

    )
}