import React from 'react';
import { useSelector} from "react-redux";
import { showFrameSource } from "./diseaseSlice";
import { Col, Row } from "react-bootstrap";
import { DiseaseContentTree } from "./DiseaseContentTree";
import { DiseaseSourceFrame } from "./DiseaseSourceFrame";
import {DiseaseHyperTree} from "./DiseaseHyperTree";

export const DiseasePage = ({ match, location }) => {
    let { omim } = match.params
    if (!omim ) { omim = location.search.split('=').pop(); }

    const showFrame = useSelector(showFrameSource)

    return (
        <Row style={{marginRight: "0", height:"calc(99vh - 3.8em)"}}>
            <Col sm={2} style={{ paddingRight:0 }}> <DiseaseContentTree /> </Col>
            <Col sm={10} style={{ paddingLeft:0, paddingRight:0  }}> {(showFrame) ? <DiseaseSourceFrame /> : <DiseaseHyperTree omim={omim}/> }</Col>
        </Row>
    );
}