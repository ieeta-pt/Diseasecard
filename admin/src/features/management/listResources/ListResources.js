import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import { getOntologyStructure, getOntologyStructureInfo} from "./listResourcesSlice";
import BootstrapTable from 'react-bootstrap-table-next';
import {Button, IconButton} from "@material-ui/core";


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
        return(<div style={{paddingTop: "4%"}}>
                <IconButton aria-label="upload picture" component="span" title="Build">
                    <i className="feather icon-settings" style={{fontSize:"14px"}}></i>
                </IconButton>
                <IconButton aria-label="upload picture" component="span" title="Edit" onClick={(e)=> {e.stopPropagation(); handleModelEdit(cell, row)} }>
                    <i className="feather icon-edit" style={{fontSize:"14px"}}></i>
                </IconButton>
                <IconButton aria-label="upload picture" component="span" title="Remove">
                    <i className="feather icon-x-square" style={{fontSize:"14px"}}></i>
                </IconButton>
            </div>
        )
    }

    const handleModelEdit = (cell, row) => {
        console.log("hello");
        console.log(row)
    }

    const columnsEntities = [
        {
            dataField: "title",
            text: "Entity Title",
            headerStyle: () => {
                return { width: "31%" };
            }
        },
        {
            dataField: "label",
            text: "Entity Label",
            sort: true,
            headerStyle: () => {
                return { width: "31%" };
            }
        },
        {
            dataField: "description",
            text: "Entity Description",
            headerStyle: () => {
                return { width: "31%" };
            }
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

    const columnsConcept = [
        {
            dataField: "title",
            text: "Concept Title",
            headerStyle: () => {
                return { width: "28.75%" };
            }
        },
        {
            dataField: "label",
            text: "Concept Label",
            sort: true,
            headerStyle: () => {
                return { width: "31.15%" };
            }
        },
        {
            dataField: "description",
            text: "Concept Description",
            headerStyle: () => {
                return { width: "31%" };
            }
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
    const expandRowConcept = {
        renderer: row => (
            <div style={{marginRight: "-12px", marginLeft: "1.6%", marginTop: "-1.05rem", paddingLeft: "-12px"}}>
                <BootstrapTable
                    keyField="uri"
                    data={row.isEntityOf}
                    columns={columnsConcept}
                    expandRow={expandRowResource}
                    hover
                />
            </div>

        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[0],
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
    };

    const columnsResource = [
        {
            dataField: "title",
            text: "Resource Title",
            headerStyle: () => {
                return { width: "13.6%" };
            }
        },
        {
            dataField: "label",
            text: "Resource Label",
            sort: true,
            headerStyle: () => {
                return { width: "13.55%" };
            }
        },
        {
            dataField: "description",
            text: "Resource Description",
            headerStyle: () => {
                return { width: "16.2%" };
            }
        },
        {
            dataField: "order",
            text: "Resource Order",
            headerStyle: () => {
                return { width: "15.9%" };
            }
        },
        {
            dataField: "publisher",
            text: "Resource Publisher",
            headerStyle: () => {
                return { width: "16%" };
            }
        },
        {
            dataField: "endpoint",
            text: "Resource Endpoint",
            headerStyle: () => {
                return { width: "16%" };
            }
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
    const expandRowResource = {
        renderer: row => (
            <div style={{marginRight: "-12px", marginLeft: "1.45%", marginTop: "-1.05rem", paddingLeft: "-12px"}}>
                <BootstrapTable
                    keyField="label"
                    data={row.hasResource}
                    columns={columnsResource}
                    hover
                    expandRow={expandRowParser}
                />
            </div>
        ),
        showExpandColumn: true,
        nonExpandable: getNonExpandableRows()[1],
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
    };

    const expandRowParser = {
        renderer: row => (
            <div>
                <p>You can render anything here, also you can add additional data on every row object</p>
            </div>
        ),
        showExpandColumn: true,
        expandHeaderColumnRenderer: ({ isAnyExpands }) => {
            if (isAnyExpands) {
                return <i className="feather icon-minus-square"></i>;
            }
            return <i className="feather icon-plus-square"></i>;
        },
        expandColumnRenderer: ({ expanded, rowKey, expandable  }) => {
            if (expanded) {
                return ( <i className="feather icon-minus-circle"></i> );
            }
            if (expandable) {
                return ( <i className="feather icon-plus-circle"></i> );
            }
        },
    };

    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable
                keyField="uri"
                data={ontologyStructure}
                columns={columnsEntities}
                expandRow={expandRowConcept}
                hover
                headerClasses="header-class"
            />
        </div>
    )
}