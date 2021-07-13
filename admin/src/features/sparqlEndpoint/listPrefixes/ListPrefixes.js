import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getListOfPrefixes, getPrefixes} from "./listPrefixesSlice";
import BootstrapTable from 'react-bootstrap-table-next';

export const ListPrefixes = () => {
    const dispatch = useDispatch()
    const prefixesList = useSelector(getListOfPrefixes)

    useEffect(() => {
        dispatch(getPrefixes())
    }, [])

    const columns = [
        {
            dataField: 'prefix',
            text: 'Prefix',
            headerStyle: () => {
                return { width: "25%" };
            }
        },
        {
            dataField: 'uri',
            text: 'URI'
        }
    ];


    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable keyField='prefix' data={prefixesList} columns={columns}/>
        </div>

    )
}