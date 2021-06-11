import React from "react";
import { useSelector} from "react-redux";
import { getAlertBoxStatus} from "../alertBoxSlice";
import {Col, Row} from "react-bootstrap";
import {Chip, Divider} from "@material-ui/core";
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles';


export const StatusAlertBox = () => {
    const status = useSelector(getAlertBoxStatus)

    const theme = createMuiTheme({
        palette: {
            primary: {
                main: '#a5d6a7',
                color: '#fff'
            }
        },
    });

    return (
        <div>
            { status.isValidating &&
                <Row>
                    <Col sm={2}><b>Validation Start Date:</b> </Col>
                    <Col sm={3}> {status.beginValidation} </Col>
                    <Col sm={7} className="text-right" style={{paddingRight: "50px"}} >
                        <ThemeProvider theme={theme}>
                        <Chip color="primary" label="System is Validating" style={{color: "#fff"}} />
                        </ThemeProvider>
                    </Col>
                </Row>
            }
            <Row style={{ paddingTop: "10px", paddingBottom: "25px"}}>
                <Col sm={2} ><b>Validation Last Date:</b></Col>
                <Col sm={3}> {status.lastValidation} </Col>
            </Row>
            <Divider />
            <Row style={{ paddingTop: "15px"}}>
                <Col sm={2} ><b>Number of Errors:</b></Col>
                <Col sm={3}> ... </Col>
            </Row>
            <Row style={{ paddingTop: "15px"}}>
                <Col sm={2} ><b>TODO:</b></Col>
                <Col sm={10}> https://www.chartjs.org/docs/latest/samples/other-charts/doughnut.html </Col>
            </Row>
        </div>

    )
}