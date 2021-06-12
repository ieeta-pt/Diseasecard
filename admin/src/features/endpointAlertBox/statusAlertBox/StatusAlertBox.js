import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {
    getAlertBoxResults,
    getAlertBoxStatus,
    getGraphData,
    getGraphLabel,
    getRequest,
    getTotalErrors
} from "../alertBoxSlice";
import {Col, Row} from "react-bootstrap";
import {Chip, CircularProgress, Divider} from "@material-ui/core";
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import { Doughnut } from 'react-chartjs-2';
import {Alert} from "@material-ui/lab";

export const StatusAlertBox = () => {
    const dispatch = useDispatch()
    const request = useSelector(getRequest)
    const status = useSelector(getAlertBoxStatus)
    const graphData = useSelector(getGraphData)
    const graphLabels = useSelector(getGraphLabel)
    const totalErrors = useSelector(getTotalErrors)

    let content;

    useEffect(async () => {
        await dispatch(getAlertBoxResults())
    }, [])

    const theme = createMuiTheme({
        palette: {
            primary: {
                main: '#a5d6a7'
            },
            secondary: {
                main: '#81d4fa'
            },
            base: {
                main: '#eeeeee'
            }
        },
    });

    const data = {
        labels: graphLabels,
        datasets: [
            {
                label: '# of Votes',
                data: graphData,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)',
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                ],
                borderWidth: 1,
            },
        ],
    };

    if (request === 'loading') {
        content = (
            <Row>
                <Col sm={12} className="text-center">
                    <ThemeProvider theme={theme}>
                        <CircularProgress color="base"/>
                    </ThemeProvider>
                </Col>
            </Row>
        )
    }
    else if (request === 'succeeded') {
        content = (
            <Row>
                <Col sm={9}>
                    <Alert severity="info" style={{marginBottom: "30px"}}>The system will perform an automatic validation on the 1st and 15th of each month.</Alert>
                    { status.isValidating &&
                    <Row>
                        <Col sm={3}><b>Validation Start Date:</b> </Col>
                        <Col sm={5}> {status.beginValidation} </Col>
                        <Col sm={4} className="text-right" style={{paddingRight: "50px"}} >
                            <ThemeProvider theme={theme}>
                                <Chip color="primary" label="System is Validating" style={{color: "#fff"}} />
                            </ThemeProvider>
                        </Col>
                    </Row>
                    }
                    <Row style={{ paddingTop: "10px", paddingBottom: "30px"}}>
                        <Col sm={3} ><b>Validation Last Date:</b></Col>
                        <Col sm={5}> {status.lastValidation} </Col>
                    </Row>

                    <Divider />
                    <Row style={{ paddingTop: "25px"}}>
                        <Col sm={3} ><b>Total Number of Sources with Errors:</b></Col>
                        <Col sm={5}> {graphLabels.length} </Col>
                    </Row>
                    <Row style={{ paddingTop: "15px", paddingBottom: "30px"}}>
                        <Col sm={3} ><b>Total Number of Errors:</b></Col>
                        <Col sm={5}> {totalErrors} </Col>
                    </Row>
                    <Divider />
                    {/*{status.isValidating === false &&*/}
                    <Row style={{marginTop: "30px"}}>
                        <Col sm={9}>As system endpoint validation is a process that only takes place twice a month, on specific schedules, you may need to force a system endpoint validation now. If yes, left click to start the process.</Col>
                        <Col sm={3} className="text-right" style={{paddingRight: "50px"}} >
                            <ThemeProvider theme={theme}>
                                <Chip color="secondary" label="Force Endpoints Validation" style={{color: "#fff"}} />
                            </ThemeProvider>
                        </Col>
                    </Row>
                    {/*}*/}
                </Col>
                <Col sm={3}>
                    <Doughnut data={data} />
                </Col>
            </Row>
        )
    }

    return (
        <div>
            {content}
        </div>
    )
}