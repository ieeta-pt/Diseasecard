import React from 'react';
import * as am4core from "@amcharts/amcharts4/core";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import am4themes_dataviz from "@amcharts/amcharts4/themes/dataviz";
import * as am4plugins_forceDirected from "@amcharts/amcharts4/plugins/forceDirected";
import { useSelector} from "react-redux";
import { getDescription, getStatus, selectNetwork } from "./diseaseSlice";
import { Container } from "react-bootstrap";
import { DotLoader } from "react-spinners";
import { css } from "@emotion/core";
import store from "../../app/store";

am4core.useTheme(am4themes_dataviz);
am4core.useTheme(am4themes_animated);

export const DiseasePage = ({ match }) => {
    const { omim } = match.params
    const status = useSelector(getStatus)
    const network = useSelector(selectNetwork)
    const description = useSelector(getDescription)
    let content;

    console.log(store.getState())

    const override = css`
      margin: 0;
      position: absolute;
      top: 40%;
      left: 50%;
    `;

    // Create chart
    let chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);

    // Create series
    let series = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())

    if (status === 'loading') {
        content = <DotLoader
            css={override}
            size={80}
            color={"#e4592e"}
        />
    }
    else if (status === 'succeeded'){

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

        // Remove watermark of graph
        const element = document.querySelectorAll('[aria-labelledby^="id-"][aria-labelledby$="-title"]');
        [].forEach.call(element, div => {
            div.style.visibility = "hidden";
        });

        content = <div className="diseaseDescription" style={{ textAlign: "center", paddingTop: "1vh"} }><h5><b>{description}</b></h5></div>;
    }

    // When user clicks on a node
    series.nodes.template.events.on("hit", (ev) => { console.log(ev.target._dataItem.name) })

    return (
        <Container >

            { content }
            <div id="chartdiv" style={{ width: "100%", height: "80vh"}} />
            <iframe is="x-frame-bypass" src={"https://omim.org/entry/" + omim} style={{ width: "100%", height: "90vh" }}> </iframe>
        </Container>

    );
}