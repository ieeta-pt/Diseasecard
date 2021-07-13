import React, {Component} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import MainCard from "../../template/components/MainCard";
import {QuerySystem} from "./querySystem/QuerySystem";
import {ListPrefixes} from "./listPrefixes/ListPrefixes";


class SPARQLEndpoint extends Component {
    render() {
        return (
            <Aux>
                <Row>
                    <Col>
                        <MainCard title='SPARQL Prefixes' collapseCard={true}>
                            <ListPrefixes />
                        </MainCard>
                        <MainCard title='Query System'>
                            <QuerySystem />
                        </MainCard>
                    </Col>
                </Row>
            </Aux>
        );
    }
}

export default SPARQLEndpoint;