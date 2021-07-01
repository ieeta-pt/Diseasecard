import React, {Component, useEffect} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import * as am4core from "@amcharts/amcharts4/core";
import MainCard from "../../template/components/MainCard";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import am4themes_dataviz from "@amcharts/amcharts4/themes/dataviz";
import * as am4plugins_forceDirected from "@amcharts/amcharts4/plugins/forceDirected";
import {useDispatch, useSelector} from "react-redux";
import {getOntology, getOntologyStructure, getStatus} from "./sourceMapSlice";

am4core.useTheme(am4themes_dataviz);
am4core.useTheme(am4themes_animated);

const SourceMapPage = () => {
    const dispatch = useDispatch();
    const ontology = useSelector(getOntologyStructure);
    const status = useSelector(getStatus)
    let series;

    useEffect(async () => {
        console.log(await dispatch(getOntology()))

        let chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);
        series = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())

        console.log(ontology)
        series.data = [ontology];

        // Set up data fields
        series.dataFields.value = "value";
        series.dataFields.name = "name";
        series.dataFields.children = "children";

        // Add labels
        series.nodes.template.label.text = "{name}";
        series.fontSize = 10;
        series.minRadius = 39;
        series.maxRadius = 120;

        const element = document.querySelectorAll('[aria-labelledby^="id-"][aria-labelledby$="-title"]');
        [].forEach.call(element, div => {
            div.style.visibility = "hidden";
        });

        if (status === 'fulfilled') {
            console.log("olaaa")
            return () => {
                chart.dispose();
            }
        }

    }, [status])

    return (
        <Aux>
            <Row>
                <Col>
                    <MainCard title='Ontology Structure'  >
                        <div id="chartdiv" style={{ paddingLeft:"-5%", width: "100%", height: "90%", minHeight: "710px"}} />
                    </MainCard>
                </Col>
            </Row>
        </Aux>
    );

}

export default SourceMapPage;