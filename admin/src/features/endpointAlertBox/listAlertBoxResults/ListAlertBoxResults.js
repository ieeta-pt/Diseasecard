import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import BootstrapTable from 'react-bootstrap-table-next';
import {getAlertBoxResults, getListAlertBoxResults} from "../alertBoxSlice";


export const ListSourcesURLs = () => {
    const dispatch = useDispatch()
    const sourcesURLs = useSelector(getListAlertBoxResults)

    const columns = [
        {
            dataField: 'source',
            text: 'Source',
            headerStyle: () => {
                return { width: "70%" };
            }
        },
        {
            dataField: 'numberOfErrors',
            text: 'Base URL',
            headerStyle: () => {
                return { width: "30%" };
            }
        }
    ];

    const columnsExpand = [
        {
            dataField: "url",
            text: "URL",
            headerStyle: () => {
                return { width: "75%" };
            }
        },
        {
            dataField: "error",
            text: "Error",
            headerStyle: () => {
                return { width: "30%" };
            }
        },
    ];
    const expandRow = {
        renderer: row => (
            <div style={{marginRight: "-12px", marginLeft: "1.7%", marginTop: "-1.05rem", paddingLeft: "-12px"}}>
                <BootstrapTable
                    keyField="uri"
                    data={row.info}
                    columns={columnsExpand}
                    hover
                />
            </div>

        ),
        showExpandColumn: true,
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

    return (
        <div style={{margin: "-30px -25px"}}>
            <BootstrapTable
                keyField='source'
                data={sourcesURLs}
                columns={columns}
                hover
                expandRow={expandRow}
                headerClasses="header-class"
            />
        </div>

    )
}