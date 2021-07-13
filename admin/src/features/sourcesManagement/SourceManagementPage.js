import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {AddWizard} from "./addSource/AddWizard";
import {ListResources} from "./listResources/ListResources";
import {SystemStatus} from "./systemStatus/SystemStatus";


class SourceManagementPage extends Component {

    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='Add Instances' isOption collapseCard={true} >
                            <AddWizard />
                        </MainCard>
                        <MainCard title='Ontology Structure' isOption collapseCard={true}>
                            <ListResources />
                        </MainCard>
                        <MainCard title='System Status' isOption>
                            <SystemStatus />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default SourceManagementPage;