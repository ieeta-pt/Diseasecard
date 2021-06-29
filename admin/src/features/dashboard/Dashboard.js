import React, {useEffect} from 'react';
import {Row, Col, Card, Table, Tabs, Tab} from 'react-bootstrap';
import Aux from "../../template/aux";
import { Bar } from 'react-chartjs-2';
import {useDispatch, useSelector} from "react-redux";
import {
    getGraphLabels, getInvalidItems, getInvalidPercentage,
    getNumberOfConcepts,
    getNumberOfEntities, getNumberOfInvalidItems,
    getNumberOfItems,
    getNumberOfResources,
    getSystemStats, getValidItems
} from "./dashboardSlice";


const Dashboard = () => {
    const dispatch = useDispatch()
    const nEntities = useSelector(getNumberOfEntities)
    const nConcepts = useSelector(getNumberOfConcepts)
    const nResources = useSelector(getNumberOfResources)
    const nItems = useSelector(getNumberOfItems)
    const nInvalidItems = useSelector(getNumberOfInvalidItems)
    const graphLabels = useSelector(getGraphLabels)
    const validItems = useSelector(getValidItems)
    const invalidItems = useSelector(getInvalidItems)
    const invalidPercentage = parseFloat(useSelector(getInvalidPercentage)).toFixed(2)


    useEffect(async () => {
        await dispatch(getSystemStats())
    }, [])

    const data = {
        labels: graphLabels,
        datasets: [
            {
                label: '# of Items',
                data: validItems,
                backgroundColor: 'rgba(137, 159, 212, 0.2)',
                borderColor: 'rgba(137, 159, 212, 1)',
                borderWidth: 1
            },
            {
                label: '# of Invalid Items',
                data: invalidItems,
                backgroundColor: 'rgba(29, 233, 182, 0.2)',
                borderColor: 'rgba(29, 233, 182, 1)',
                borderWidth: 1
            },
        ],
    };

    const options = {
            responsive: true,
            scales: {
                x: {
                    stacked: true,
                },
                y: {
                    stacked: true
                }
            }

    };

    return (
        <Aux>
            <Row>
                <Col md={6} xl={4}>
                    <Card className='card-event' style={{paddingTop: "13px", paddingBottom: "13px"}}>
                        <Card.Body>
                            <div className="row align-items-center justify-content-center">
                                <div className="col">
                                    <h5 className="m-0">Number of Entities</h5>
                                </div>
                            </div>
                            <h2 className="mt-2 f-w-300">{nEntities}</h2>
                            <h6 className="text-muted mt-3 mb-0">Entities aggregate concepts that have the same data type or data relating to the same property.</h6>
                            <i className="fa fa-angellist text-c-purple f-50"/>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={6} xl={4}>
                    <Card className='card-event' style={{paddingTop: "13px", paddingBottom: "13px"}}>
                        <Card.Body>
                            <div className="row align-items-center justify-content-center">
                                <div className="col">
                                    <h5 className="m-0">Number of Concepts</h5>
                                </div>
                            </div>
                            <h2 className="mt-2 f-w-300">{nConcepts}</h2>
                            <h6 className="text-muted mt-3 mb-0">Each concept is associated with a different external data source and has resources responsible for specifying how the data is read.</h6>
                            <i className="fa fa-angellist text-c-purple f-50"/>
                        </Card.Body>
                    </Card>
                </Col>
                <Col xl={4}>
                    <Card className='card-event' style={{paddingTop: "13px", paddingBottom: "13px"}}>
                        <Card.Body>
                            <div className="row align-items-center justify-content-center">
                                <div className="col">
                                    <h5 className="m-0">Number of Resources</h5>
                                </div>
                            </div>
                            <h2 className="mt-2 f-w-300">{nResources}</h2>
                            <h6 className="text-muted mt-3 mb-0">Resources are the instances that contains the information about how to parse the information from the external data sources.</h6>
                            <i className="fa fa-angellist text-c-purple f-50"/>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={6} xl={8}>
                    <Card className='Recent-Users' style={{ minHeight: "510px"}}>
                        <Card.Header>
                            <Card.Title as='h5'>Distribution of Items according to existing Concepts</Card.Title>
                        </Card.Header>
                        <Card.Body className='px-0 py-2' style={{width: "83%", marginLeft: "7%"}}>
                            <Bar data={data} options={options} />
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={6} xl={4}>
                    <Card className='card-event' style={{paddingTop: "13px", paddingBottom: "13px", minHeight: "238px"}}>
                        <Card.Body>
                            <div className="row align-items-center justify-content-center">
                                <div className="col">
                                    <h5 className="m-0">Number of Items</h5>
                                </div>
                            </div>
                            <h2 className="mt-2 f-w-300">{nItems}<sub className="text-muted f-14"> Endpoints</sub></h2>
                            <h6 className="text-muted mt-3 mb-0">Each external data source added to the system has a set of items that allow the system to present information directly from sources. This number corresponds to the total items in the system. </h6>
                            <i className="fa fa-angellist text-c-purple f-50"/>
                        </Card.Body>
                    </Card>
                    <Card className='card-event' style={{paddingTop: "13px", paddingBottom: "13px", minHeight: "238px"}}>
                        <Card.Body>
                            <div className="row align-items-center justify-content-center">
                                <div className="col">
                                    <h5 className="m-0">Number of Invalid Items</h5>
                                </div>
                                <div className="col-auto">
                                    <label className="label theme-bg2 text-white f-14 f-w-400 float-right">{invalidPercentage}%</label>
                                </div>
                            </div>
                            <h2 className="mt-2 f-w-300">{nInvalidItems}<sub className="text-muted f-14"> Endpoints</sub></h2>
                            <h6 className="text-muted mt-3 mb-0">Number of items that at the time of validation were not active/valid. You can check the errors associated with each of them in the AlertBox menu.</h6>
                            <i className="fa fa-angellist text-c-purple f-50"/>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Aux>
    );

}

export default Dashboard;