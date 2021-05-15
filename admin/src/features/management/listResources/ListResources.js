import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import { getOntologyStructure, getOntologyStructureInfo} from "./listResourcesSlice";
import BootstrapTable from 'react-bootstrap-table-next';


export const ListResources = () => {
    const dispatch = useDispatch();
    const ontologyStructure = useSelector(getOntologyStructure)

    useEffect(() => {
        dispatch(getOntologyStructureInfo())
    }, [])

    const getNonExpandableRows = () => {
        let nonExpandableEntities = []
        let nonExpandableConcepts = []
        ontologyStructure.map( (values) => {
            if(values.isEntityOf.length == 0) {
                nonExpandableEntities.push(values.uri)
            }
            else {
                values.isEntityOf.map( (conceptsExtended) => {
                    if (conceptsExtended.hasResource.length == 0) {
                        nonExpandableConcepts.push(conceptsExtended.uri)
                    }
                })
            }
        })
        return [nonExpandableEntities,nonExpandableConcepts]
    }

    const GetActionFormat = (cell, row) => {
        return(<div>
                <button type="button" className="btn btn-outline-primary btn-sm ts-buttom" size="sm" onClick={()=> handleModelEdit(cell, row) }>Edit</button>
                <button type="button" className="btn btn-outline-danger btn-sm ml-2 ts-buttom" size="sm">Delete</button>
            </div>
        )
    }

    const handleModelEdit = (cell, row) => {
        console.log("hello");
        console.log(row)
    }


    const columns = [
        {
            dataField: "title",
            text: "Entity Title"
        },
        {
            dataField: "label",
            text: "Entity Label",
            sort: true
        },
        {
            dataField: "description",
            text: "Entity Description"
        },
        {
            text : 'Action',
            dataField : '',
            formatter : GetActionFormat,
            classes : 'p-1'
        }
    ];
    const expandRowConcept = {
        renderer: row => (
            <BootstrapTable
                keyField="uri"
                data={row.isEntityOf}
                columns={columns}
                expandRow={expandRowResource}
            />
        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[0]
    };

    const columnsResource = [
        {
            dataField: "title",
            text: "Entity Title"
        },
        {
            dataField: "label",
            text: "Entity Label",
            sort: true
        },
        {
            dataField: "description",
            text: "Entity Description"
        },
        {
            dataField: "order",
            text: "Entity Order"
        },
        {
            dataField: "publisher",
            text: "Entity Publisher"
        },
        {
            dataField: "endpoint",
            text: "Entity Endpoint"
        }
    ];
    const expandRowResource = {
        renderer: row => (
            <BootstrapTable
                keyField="label"
                data={row.hasResource}
                columns={columnsResource}
            />
        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[1]
    };


    return (
        // <div><pre>{JSON.stringify(ontologyStructure, null, 2) }</pre></div>
        <div style={{ padding: "20px" }}>
            <BootstrapTable
                keyField="uri"
                data={ontologyStructure}
                columns={columns}
                expandRow={expandRowConcept}
            />
        </div>
    )
}