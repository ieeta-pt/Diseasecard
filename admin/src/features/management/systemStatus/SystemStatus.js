import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getAllResources, getResources, getSystemBuild, startSystemBuild} from "./systemStatusSlice";
import BootstrapTable from "react-bootstrap-table-next";
import {Avatar, Button, Chip} from "@material-ui/core";
import {green, grey, red} from "@material-ui/core/colors";
import SockJsClient from 'react-stomp';
import { api_url } from '../../../../package.json';
import {makeStyles} from "@material-ui/core/styles";
import {Col, Row} from "react-bootstrap";

const useStyles = makeStyles((theme) => ({
    margin: {
        margin: theme.spacing(1),
    },
    extendedIcon: {
        marginRight: theme.spacing(1),
    },
    button: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#1de9b6",
        color: "#1de9b6",
        '&:hover':{
            borderColor: "#1dc4e9",
            color: "#1dc4e9"
        },
    },
    buttonG: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#1de9b6",
        color: "#fff",
        '&:hover':{
            borderColor: "#1de9b6",
        },
        background: "linear-gradient(-135deg, #1de9b6 0%, #1dc4e9 100%)"
    },
    buttonP: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        marginTop: "10px",
        marginRight: "5%",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#A389D4",
        color: "#fff",
        '&:hover':{
            borderColor: "#A389D4",
        },
        background: "linear-gradient(-135deg, #899FD4 0%, #A389D4 100%)"
    }
}));

export const SystemStatus = () => {
    const classes = useStyles();
    const dispatch = useDispatch();
    const allResources = useSelector(getResources)
    const systemBuild = useSelector(getSystemBuild)
    const [message, setMessage] = useState('You server message here.');
    const [go, setGo] = useState(false);

    useEffect(() => {
        dispatch(getAllResources())
        dispatch(getSystemBuild())
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
                        <Chip label="Unbuilt" size="small" style={{ backgroundColor: colorForStatus("blocked")[300], color: "white", marginLeft: "2%" }}/>
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

    let handleClickStartBuild = () => {
        setGo(true)
        dispatch(startSystemBuild())
    }

    let handleClickStartUnbuild = () => {
        setGo(true)
    }

    return (
        <div style={{margin: "-30px -25px"}}>
            <Row style={{ width: "100%", marginRight: 0, marginLeft: 0}}>
                <Col md={10}>
                    { !systemBuild &&
                        <p style={{marginTop: "14px", marginBottom: "14px"}}>...</p>
                    }
                    { systemBuild &&
                        <p style={{marginTop: "14px", marginBottom: "14px"}}>The system is fully built. The Unbuild will result in the removal of the Redis DB and the Solr Index.  </p>
                    }
                </Col>
                <Col md={2} className="text-right" style={{ paddingRight: 0 }}>
                    { !systemBuild &&
                        <Button  variant="outlined" size="small" color="primary" className={classes.buttonP} onClick={handleClickStartBuild} style={{paddingRight: "1%"}}>
                            Build
                        </Button>
                    }
                    { systemBuild &&
                        <Button  variant="outlined" size="small" color="primary" className={classes.buttonP} onClick={handleClickStartUnbuild}>
                            Unbuild
                        </Button>
                    }
                </Col>
            </Row>

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
        </div>
    )
}