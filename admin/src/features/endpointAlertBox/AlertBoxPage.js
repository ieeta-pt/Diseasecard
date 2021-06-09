import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";


class AlertBoxPage extends Component {
    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='List of Alerts'>
                            <p>
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                                Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                            </p>
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default AlertBoxPage;