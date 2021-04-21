import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {AddWizard} from "./addSource/AddWizard";
import {ListResources} from "./listResources/ListResources";


class SourceManagementPage extends Component {

    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='Add Sources' isOption collapseCard={true} >
                            <AddWizard />
                        </MainCard>
                        <MainCard title='List Of Existent Resources' isOption >
                            <ListResources />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default SourceManagementPage;