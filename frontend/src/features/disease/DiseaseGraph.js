import React, {useEffect} from 'react';
import * as am4core from "@amcharts/amcharts4/core";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import am4themes_dataviz from "@amcharts/amcharts4/themes/dataviz";
import * as am4plugins_forceDirected from "@amcharts/amcharts4/plugins/forceDirected";
import {useDispatch, useSelector} from "react-redux";
import {getDescription, getSourceURL, getStatus, selectNetwork, showFrame} from "./diseaseSlice";
import { DotLoader } from "react-spinners";
import { css } from "@emotion/core";

am4core.useTheme(am4themes_dataviz);
am4core.useTheme(am4themes_animated);

export const DiseaseGraph = ({ omim }) => {
    const status = useSelector(getStatus)
    const network = useSelector(selectNetwork)
    const description = useSelector(getDescription)
    const dispatch = useDispatch();

    let content;
    const override = css`
      margin: 0;
      position: absolute;
      top: 40%;
      left: 45%;
    `;
    let series;
    useEffect(() => {
        // Create chart
        let chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);
        series = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries())

        if (status === 'succeeded') {
            //console.log(network)
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
        }

        const element = document.querySelectorAll('[aria-labelledby^="id-"][aria-labelledby$="-title"]');
        [].forEach.call(element, div => {
            div.style.visibility = "hidden";
        });

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
    })

    if (status === 'loading') {
        content = <DotLoader
            css={override}
            size={80}
            color={"#e4592e"}
        />
    }
    else if (status === 'succeeded'){
        // Remove watermark of graph
        content = <div className="diseaseDescription" style={{ paddingLeft:"-10%",  textAlign: "center", paddingTop: "1.5%"} }><h5><b>{description}</b></h5></div>;
    }


    return (
        <div style={{height: "90%"}}>
            { content }
            <div id="chartdiv" style={{ paddingLeft:"-5%", width: "100%", height: "90%"}} />
        </div>
    );
}