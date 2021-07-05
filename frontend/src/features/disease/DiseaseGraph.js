import React, {useCallback, useEffect, useState} from 'react';
import * as am4core from "@amcharts/amcharts4/core";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import am4themes_dataviz from "@amcharts/amcharts4/themes/dataviz";
import * as am4plugins_forceDirected from "@amcharts/amcharts4/plugins/forceDirected";
import {useDispatch, useSelector} from "react-redux";
import {
    getDescription,
    getDiseaseByOMIM,
    getReady,
    getSourceURL,
    getStatus,
    selectNetwork,
    showFrame
} from "./diseaseSlice";
import { DotLoader } from "react-spinners";
import { css } from "@emotion/core";
import {Col, Jumbotron, Row} from "react-bootstrap";

am4core.useTheme(am4themes_dataviz);
am4core.useTheme(am4themes_animated);

export const DiseaseGraph = ({ omim }) => {
    const status = useSelector(getStatus)
    const [network, setNetwork] = useState([])
    const description = useSelector(getDescription)
    const dispatch = useDispatch();

    let content;
    const override = css`
      margin: 0;
      position: absolute;
      top: 40%;
      left: 45%;
    `;

    const getInfo = useCallback(async () => {
        let response = await dispatch(getDiseaseByOMIM(omim))
        setNetwork(response.payload.results)
    }, [omim])

    useEffect( () => {
        getInfo()
    }, [getInfo])


    let series;
    useEffect(() => {
        // Create chart
        let chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);
        series = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())

        const element = document.querySelectorAll('[aria-labelledby^="id-"][aria-labelledby$="-title"]');
        [].forEach.call(element, div => {
            div.style.visibility = "hidden";
        });

        console.log(network)

        if (network.length !== 0) {
            console.log("ola")
            series.data = [ { "name": "OMIM: " + omim , "children" : network} ]

            // Set up data fields
            series.dataFields.value = "value";
            series.dataFields.name = "name";
            series.dataFields.children = "children";

            // Add labels
            series.nodes.template.label.text = "{name}";
            series.fontSize = 14;
            series.minRadius = 25;
            series.maxRadius = 80;
            series.maxLevels = 2;


            // When user clicks on a node
            series.nodes.template.events.on("hit", (ev) => {
                //console.log(ev)
                const name = ev.target._dataItem._dataContext.name
                if (! name.includes("OMIM") ) {
                    const id = ev.target._dataItem._dataContext.id
                    if (id.includes(":")) {
                        dispatch(getSourceURL(id))
                        dispatch(showFrame(true))
                    }
                }
            })
            return () => { chart.dispose(); }
        }

    }, [network])


    if (status === 'loading') {
        content = <DotLoader
            css={override}
            size={80}
            color={"#e4592e"}
        />
    }
    else if (status === 'succeeded') {
        content = <div className="diseaseDescription" style={{ paddingLeft:"-10%",  textAlign: "center", paddingTop: "1.5%"} }><h5><b>{description}</b></h5></div>;
    }
    else if (status === 'failed') {
        content = <div>
            <Jumbotron>
                <h2>UNKNOWN DISEASE ID</h2>
            </Jumbotron>
            <Row>
                <Col style={{marginLeft: "40px"}}>
                    Diseasecard was not able to retrieve any information about the selected Disease ID ({omim}).
                </Col>
            </Row>
        </div>
    }


    return (
        <div style={{height: "90%"}}>
            { content }
            { status !== 'failed' &&
                <div id="chartdiv" style={{ paddingLeft:"-5%", width: "100%", height: "90%"}} />
            }

        </div>
    );
}