import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {ListSourcesURLS} from "./listSourcesURLS/ListSourcesURLS";


class EndpointManagementPage extends Component {
    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='TODO'>
                            <ListSourcesURLS />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default EndpointManagementPage;