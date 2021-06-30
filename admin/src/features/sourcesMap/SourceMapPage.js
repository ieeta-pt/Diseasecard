import React, {Component, useEffect} from 'react';
import {Row, Col} from 'react-bootstrap';
import Aux from "../../template/aux";
import * as am4core from "@amcharts/amcharts4/core";
import MainCard from "../../template/components/MainCard";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import am4themes_dataviz from "@amcharts/amcharts4/themes/dataviz";
import * as am4plugins_forceDirected from "@amcharts/amcharts4/plugins/forceDirected";

am4core.useTheme(am4themes_dataviz);
am4core.useTheme(am4themes_animated);

const SourceMapPage = () => {



    let series;

    useEffect(()=> {
        let chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);
        series = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())

        series.data = [
            {
                name: "Core",
                children: [
                    {
                        name: "First",
                        children: [
                            { name: "A1", value: 100 },
                            { name: "A2", value: 60 }
                        ]
                    },
                    {
                        name: "Second",
                        children: [
                            { name: "B1", value: 135 },
                            { name: "B2", value: 98 }
                        ]
                    },
                    {
                        name: "Third",
                        children: [
                            {
                                name: "C1",
                                children: [
                                    { name: "EE1", value: 130 },
                                    { name: "EE2", value: 87 },
                                    { name: "EE3", value: 55 }
                                ]
                            },
                            { name: "C2", value: 148 },
                            {
                                name: "C3", children: [
                                    { name: "CC1", value: 53 },
                                    { name: "CC2", value: 30 }
                                ]
                            },
                            { name: "C4", value: 26 }
                        ]
                    },
                    {
                        name: "Fourth",
                        children: [
                            { name: "D1", value: 415 },
                            { name: "D2", value: 148 },
                            { name: "D3", value: 89 }
                        ]
                    },
                    {
                        name: "Fifth",
                        children: [
                            {
                                name: "E1",
                                children: [
                                    { name: "EE1", value: 33 },
                                    { name: "EE2", value: 40 },
                                    { name: "EE3", value: 89 }
                                ]
                            },
                            {
                                name: "E2",
                                value: 148
                            }
                        ]
                    }

                ]
            }
        ];

        // Set up data fields
        series.dataFields.value = "value";
        series.dataFields.name = "name";
        series.dataFields.children = "children";

        // Add labels
        series.nodes.template.label.text = "{name}";
        series.fontSize = 14;
        series.minRadius = 30;
        series.maxRadius = 100;

        const element = document.querySelectorAll('[aria-labelledby^="id-"][aria-labelledby$="-title"]');
        [].forEach.call(element, div => {
            div.style.visibility = "hidden";
        });

        return () => { chart.dispose(); }
    })

    return (
        <Aux>
            <Row>
                <Col>
                    <MainCard title='Ontology Structure'  >
                        <div id="chartdiv" style={{ paddingLeft:"-5%", width: "100%", height: "90%", minHeight: "680px"}} />
                    </MainCard>
                </Col>
            </Row>
        </Aux>
    );

}

export default SourceMapPage;