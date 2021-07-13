import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    getAlert,
    getAllResources, getBtnBuild,
    getResources,
    getSystemBuild,
    getSystemBuildValue,
    startSystemBuild, startUnbuildSystem, updateBtnBuild
} from "./systemStatusSlice";
import BootstrapTable from "react-bootstrap-table-next";
import {Avatar, Button, Chip} from "@material-ui/core";
import {deepOrange, green, grey, red} from "@material-ui/core/colors";
import SockJsClient from 'react-stomp';
import { api_url } from '../../../../package.json';
import {makeStyles} from "@material-ui/core/styles";
import {Col, Row} from "react-bootstrap";
import Alert from '@material-ui/lab/Alert';

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
        borderColor: "#E94F3F",
        color: "#E94F3F",
        '&:hover':{
            borderColor: "#f3ab9b",
            color: "#f3ab9b"
        },
    },
    buttonBuild: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#E94F3F",
        color: "#fff",
        marginTop: "10px",
        marginRight: "12%",
        '&:hover':{
            borderColor: "#E94F3F",
        },
        background: "linear-gradient(-135deg, #E94F3F 0%, #f3ab9b 100%)",
    },
    buttonBuildDisable: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#E94F3F",
        color: "#fff",
        marginTop: "10px",
        marginRight: "10%",
        '&:hover':{
            borderColor: "#E94F3F",
        },
        background: "rgba(0, 0, 0, 0.26)",
    },
    buttonUnbuild: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        marginTop: "10px",
        marginRight: "10%",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#fbe9e7",
        color: "#fff",
        '&:hover':{
            borderColor: "#fbe9e7",
        },
        background: "linear-gradient(-135deg, #f3ab9b 0%, #fbe9e7 100%)"
    },
    buttonUnbuildDisable: {
        borderRadius: "15px",
        boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)",
        padding: "0.5px 10px 1px 10px",
        marginTop: "10px",
        marginRight: "8%",
        fontSize: "14px",
        textTransform: "none",
        borderColor: "#fbe9e7",
        color: "#fff",
        '&:hover':{
            borderColor: "#fbe9e7",
        },
        background: "rgba(0, 0, 0, 0.26)",
    }
}));

export const SystemStatus = () => {
    const classes = useStyles();
    const dispatch = useDispatch();
    const allResources = useSelector(getResources)
    const btnBuild = useSelector(getBtnBuild)
    const systemBuild = useSelector(getSystemBuildValue)
    const alert = useSelector(getAlert)
    const [message, setMessage] = useState('You server message here.');

    console.log("Alert in System Status:")
    console.log(alert)

    useEffect(() => {
        dispatch(getAllResources())
        dispatch(getSystemBuild())

        console.log("systemBuild")
        console.log(systemBuild)
    }, [])

    function colorForStatus(status) {
        switch (status) {
            case "completed":
                return green;
            case "blocked":
                return deepOrange;
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
            headerStyle:  { width: "3%", textAlign: "center"},
            formatter: (cell) => {
                if (cell === false) {
                    return (
                        <Chip label="Unbuilt" size="small" style={{ backgroundColor: colorForStatus("blocked")[200], color: "white", textAlign: "center", marginLeft: "13%"  }}/>
                    )
                }
                else
                {
                    return (
                        <Chip label="Built"  size="small" style={{ backgroundColor: colorForStatus("completed")[300], color: "white" , textAlign: "center", marginLeft:"23%" }}/>
                    )
                }


            }
        },
    ]

    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = async (msg) => {
        console.log("Message: ")
        console.log(msg)
        setMessage(msg);

        await dispatch(getSystemBuild())
        dispatch(getAllResources())
    }

    let handleClickStartBuild = () => {
        console.log("AQUIIIIIII")

        dispatch(updateBtnBuild(true))
        dispatch(startSystemBuild())
        dispatch(getSystemBuild())
    }

    let handleClickStartUnbuild = () => {
        dispatch(updateBtnBuild(true))
        dispatch(startUnbuildSystem())
        dispatch(getSystemBuild())
    }

    let content ;
    if (allResources.length === 0)  content = <div style={{ marginTop: "20px", marginLeft: "10px" }}>The system does not contain any resource.</div>
    else                            content = <div>
        <Row style={{ width: "100%", marginRight: 0, marginLeft: 0}}>
            <Col sm={10}>
                { systemBuild &&
                <p style={{marginTop: "14px", marginBottom: "14px"}}>The system is not built.</p>
                }
                { !systemBuild &&
                    <div>
                        { alert &&
                            <Alert variant="filled" severity="warning" style={{paddingTop: "1px", paddingBottom: "1px", marginTop: "8px", marginBottom: "8px", maxWidth: "850px"}}>
                                The system status is inconsistent. You made changes to the ontology structure that are not reflected in the system.
                            </Alert>
                        }
                        { !alert &&
                        <p style={{marginTop: "14px", marginBottom: "14px"}}>
                            The system is built. The Unbuild will result in the removal of all the information in the system, including the Redis DB and the Solr Index.
                        </p>
                        }
                    </div>
                }
            </Col>
            <Col sm={2} className="text-right" style={{ paddingRight: 0 }}>
                { systemBuild &&
                <Button variant="outlined" size="small" color="primary" className={btnBuild ? classes.buttonBuildDisable : classes.buttonBuild} type="submit" disabled={btnBuild} onClick={handleClickStartBuild} >
                    Build
                </Button>
                }
                { !systemBuild &&
                <Button  variant="outlined" size="small" color="primary" className={btnBuild ? classes.buttonUnbuildDisable : classes.buttonUnbuild} type="submit" disabled={btnBuild} onClick={handleClickStartUnbuild}>
                    Unbuild
                </Button>
                }
            </Col>
        </Row>
        <BootstrapTable
            keyField="uri"
            data={allResources}
            columns={columns}
            hover
            headerClasses="header-class"
        />
   </div>

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

            {content}

        </div>
    )
}