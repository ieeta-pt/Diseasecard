import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {ListSourcesURLs} from "./listSourcesURLS/ListSourcesURLs";
import {AddSourceBaseURL} from "./addSourceBaseURL/AddSourceBaseURL";


class EndpointManagementPage extends Component {
    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='Add Source Base URL' collapseCard={true} >
                            <AddSourceBaseURL/>
                        </MainCard>
                        <MainCard title='Sources Base URLs'>
                            <ListSourcesURLs />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default EndpointManagementPage;