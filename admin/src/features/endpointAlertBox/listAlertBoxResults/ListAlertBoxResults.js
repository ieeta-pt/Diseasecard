import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import BootstrapTable from 'react-bootstrap-table-next';
import {getAlertBoxResults, getListAlertBoxResults} from "../alertBoxSlice";


export const ListSourcesURLs = () => {
    const dispatch = useDispatch()
    const sourcesURLs = useSelector(getListAlertBoxResults)

    useEffect(() => {
        dispatch(getAlertBoxResults())
    }, [])


    const columns = [
        {
            dataField: 'source',
            text: 'Source',
            headerStyle: () => {
                return { width: "20%" };
            }
        },
        {
            dataField: 'url',
            text: 'Base URL',
            headerStyle: () => {
                return { width: "60%" };
            }
        },
        {
            dataField: 'error',
            text: 'HTTP Error',
            headerStyle: () => {
                return { width: "20%" };
            }
        }
    ];


    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable keyField='source' data={sourcesURLs} columns={columns} hover/>
        </div>

    )
}