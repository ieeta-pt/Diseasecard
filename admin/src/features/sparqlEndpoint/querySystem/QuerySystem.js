import React, {useEffect, useState} from "react";
import {Col, Row} from "react-bootstrap";
import FormPerformQuery from "./form/FormPerformQuery";
import ReactJson from 'react-json-view'
import {useDispatch, useSelector} from "react-redux";
import {getQueryResult, getStatus, sendQuery, updateStatus} from "./querySystemSlice";
import {CircularProgress} from "@material-ui/core";


export const QuerySystem = (props) => {
    const results = useSelector(getQueryResult);
    const status = useSelector(getStatus)
    const dispatch = useDispatch();
    let content;

    const submit = async (value) => {
        console.log("a")
        dispatch(updateStatus("loading"))

        const formData = new FormData()
        formData.append("query", value.query)

        await dispatch(sendQuery(formData))
    }


    if (status === 'loading') {
        content = (
            <Col sm={12} className="text-center">
                <CircularProgress />
            </Col>
        )
    }
    else if (status === 'succeeded') {
        content =  (
            <Col sm={12} >
                <p style={{ paddingLeft: "2%", fontSize: "16px", paddingTop: "2%"}}><b>Results</b></p>
                <ReactJson style={{ paddingLeft: "2%" }} src={results} theme={"grayscale:inverted"}/>
            </Col>
        )
    }
    else if (status === 'failed') {
        content = (
            <Col sm={12}>
                <p>The system returned an error. Check if your query is valid. </p>
            </Col>
        )
    }


    return (
        <Row>
            <Col sm={12}>
                <FormPerformQuery onSubmit={submit} formDetails={props}/>
            </Col>

            {content}
        </Row>
    )
}