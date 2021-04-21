import React from 'react';
import { useSelector} from "react-redux";
import { showFrameSource } from "./diseaseSlice";
import { Col, Row } from "react-bootstrap";
import { DiseaseGraph } from "./DiseaseGraph";
import { DiseaseContentTree } from "./DiseaseContentTree";
import { DiseaseSourceFrame } from "./DiseaseSourceFrame";


export const DiseasePage = ({ match }) => {
    const { omim } = match.params
    const showFrame = useSelector(showFrameSource)

    return (
        <Row style={{marginRight: "0", height:"calc(100vh - 3.5em)"}}>
            <Col sm={2} style={{ paddingRight:0 }}> <DiseaseContentTree /> </Col>
            <Col sm={10} style={{ paddingLeft:0, paddingRight:0  }}> {(showFrame) ? <DiseaseSourceFrame /> : <DiseaseGraph omim={omim}/> }</Col>
        </Row>
    );
}