import React, {useCallback, useEffect, useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {
    getDescription,
    getDiseaseByOMIM, getInitialTreeStructure, getSourceURL,
    getStatus, showFrame,
} from "./diseaseSlice";
import { DotLoader } from "react-spinners";
import { css } from "@emotion/core";
import {Col, Jumbotron, Row} from "react-bootstrap";

export const DiseaseHyperTree = ({ omim }) => {
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
        await dispatch(getInitialTreeStructure())
        let response = await dispatch(getDiseaseByOMIM(omim))
        setNetwork(response.payload.results)
    }, [omim])

    useEffect( () => {
        getInfo()
    }, [getInfo])


    useEffect(() => {
        if (network.length !== 0) {
            const script = document.createElement("script");

            script.async = true;
            var json =  { "name": "OMIM: " + omim , "children" : network }

            var infovis = document.getElementById('infovis');
            var w = infovis.offsetWidth - 10, h = infovis.offsetHeight - 100;

            var ht = new window.$jit.Hypertree({
                //id of the visualization container
                injectInto: 'infovis',
                //canvas width and height
                width: w,
                height: "700",
                //Change node and edge styles such as
                //color, width and dimensions.
                Node: {
                    dim: 8,
                    color: "#e15620"
                },
                Edge: {
                    lineWidth: 2,
                    color: "#283250"
                },
                onBeforeCompute: function(node){
                },
                //Attach event handlers and add text to the
                //labels. This method is only triggered on label
                //creation
                onCreateLabel: function(domElement, node){
                    domElement.innerHTML = node.name;
                    window.$jit.util.addEvent(domElement, 'click', function () {
                        if (node.id && node.id.includes(":")) {
                            console.log(node.id)
                            dispatch(getSourceURL(node.id))
                            dispatch(showFrame(true))
                        }
                        ht.onClick(node.id, {
                            onComplete: function() {
                                ht.controller.onComplete();
                            }
                        });
                    });
                },
                //Change node styles when labels are placed
                //or moved.
                onPlaceLabel: function(domElement, node){
                    var style = domElement.style;
                    style.display = '';
                    style.cursor = 'pointer';
                    if (node._depth <= 1) {
                        style.fontSize = "0.8em";
                        style.color = "#283250";

                    } else if(node._depth == 2){
                        style.fontSize = "0.7em";
                        style.color = "#283250";

                    } else {
                        style.display = 'none';
                    }

                    var left = parseInt(style.left);
                    var w = domElement.offsetWidth;
                    style.left = (left - w / 2) + 'px';
                },

                onComplete: function(){
                }
            });
            //load JSON data.
            ht.loadJSON(json);
            //compute positions and plot.
            ht.refresh();
            //end
            ht.controller.onComplete();

            document.body.appendChild(script);
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
                <div className="application">

                    <div id="infovis" style={{ paddingLeft:"-5%", paddingTop: "2%",  width: "100%", height: "90%"}}> </div>
                </div>



            }
        </div>
    );
}