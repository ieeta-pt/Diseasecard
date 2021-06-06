import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import BootstrapTable from 'react-bootstrap-table-next';
import {getListSourcesURLS, getSourcesURLS} from "./listSourcesURLS";
import {IconButton} from "@material-ui/core";

export const ListSourcesURLS = () => {
    const dispatch = useDispatch()
    const sourcesURLs = useSelector(getListSourcesURLS)


    useEffect(() => {
        dispatch(getSourcesURLS())
    }, [])


    const GetActionFormat = (cell, row) => {
        return(<div style={{paddingTop: "3%"}}>
                <IconButton aria-label="upload picture" component="span" title="Edit" onClick={(e)=> {e.stopPropagation(); handleModelEdit(cell, row) }}>
                    <i className="feather icon-edit" style={{fontSize:"14px"}}></i>
                </IconButton>
                <IconButton aria-label="upload picture" component="span" title="Remove" onClick={(e)=> {e.stopPropagation(); handleModelRemove(cell, row) }}>
                    <i className="feather icon-x-square" style={{fontSize:"14px"}}></i>
                </IconButton>
            </div>
        )
    }


    const handleModelEdit = (cell, row) => {
        console.log(row)
    }


    const handleModelRemove = (cell, row) => {
        console.log(row)
    }


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
        },
        {
            text : 'Action',
            dataField : '',
            formatter : GetActionFormat,
            classes : 'p-1',
            headerStyle: () => {
                return { width: "3%" };
            }
        }
    ];


    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable keyField='source' data={sourcesURLs} columns={columns} hover/>
        </div>

    )
}