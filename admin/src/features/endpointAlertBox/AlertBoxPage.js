import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {ListSourcesURLs} from "./listAlertBoxResults/ListAlertBoxResults";


class AlertBoxPage extends Component {
    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='List of Alerts'>
                            <ListSourcesURLs />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default AlertBoxPage;