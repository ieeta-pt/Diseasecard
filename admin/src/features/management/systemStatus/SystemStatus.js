import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getAllResources, getResources} from "./systemStatusSlice";
import BootstrapTable from "react-bootstrap-table-next";
import {Avatar, Chip} from "@material-ui/core";
import {green, grey, red} from "@material-ui/core/colors";
import SockJsClient from 'react-stomp';
import { api_url } from '../../../../package.json';

export const SystemStatus = () => {
    const dispatch = useDispatch();
    const allResources = useSelector(getResources)
    const [message, setMessage] = useState('You server message here.');

    useEffect(() => {
        dispatch(getAllResources())
    }, [])

    function colorForStatus(status) {
        switch (status) {
            case "completed":
                return green;
            case "blocked":
                return red;
            default:
                return grey;
        }
    }

    const columns = [
        {
            dataField: "label",
            text: "Resource Label",
            headerStyle: () => {
                return { width: "30%" };
            }
        },
        {
            dataField: "isResourceOf",
            text: "Extended Concept",
            headerStyle: () => {
                return { width: "30%" };
            },
            formatter: (cell) => {
                return <>{cell.replace("http://bioinformatics.ua.pt/diseasecard/resource/","")}</>
            },
        },
        {
            dataField: "built",
            text: "Status",
            headerStyle: () => {
                return { width: "3%" };
            },
            formatter: (cell) => {
                if (cell === false) {
                    return (
                        <Chip label="Unbuilt" size="small" style={{ backgroundColor: colorForStatus("blocked")[300], color: "white" }}/>
                    )
                }
                else
                {
                    return (
                        <Chip label="Built"  size="small" style={{ backgroundColor: colorForStatus("completed")[300], color: "white" }}/>

                    )
                }


            }
        },
    ]

    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log("Message: ")
        console.log(msg)
        setMessage(msg);
        dispatch(getAllResources())
    }

    return (
        <div style={{margin: "-30px -25px"}}>
            <SockJsClient
                url={api_url}
                topics={['/topic/message']}
                onConnect={onConnected}
                onDisconnect={console.log("Disconnected!")}
                onMessage={msg => onMessageReceived(msg)}
                debug={false}
            />
            <BootstrapTable
                keyField="uri"
                data={allResources}
                columns={columns}
                hover
                headerClasses="header-class"
            />
            <div>{message}</div>
        </div>
    )
}