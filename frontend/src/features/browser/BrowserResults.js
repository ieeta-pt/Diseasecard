import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {getResults} from "./browserSlice";
import {Col, Container, Row} from "react-bootstrap";
import {ToggleButton, ToggleButtonGroup} from "@material-ui/lab";

export const BrowserResults = ({ match })  => {
    const [results, setResults] = useState([]);
    const [letter, setLetter] = useState(["A"]);
    const dispatch = useDispatch();


    useEffect( () => {
        dispatch(getResults(letter))
        console.log(results)
    })

    // TODO: Adicionar uma data table para ler os dados


    const prepareAlphabets = () => {
        // TODO: Adicionar #
        let result = [];
        for(let i=65; i<91; i++) {
            result.push(
                <ToggleButton key={i} value={String.fromCharCode(i)} aria-label={String.fromCharCode(i)} style={{minWidth: "52px"}}>
                    <b>{String.fromCharCode(i)} </b>
                </ToggleButton>
            )
        }
        return result;
    };

    const handleChose = (event, newLetter) => {
        setLetter(newLetter);
        // TODO: Chamada à API
    };

    return (
        <div id="browserContainer">
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <ToggleButtonGroup value={letter} exclusive onChange={handleChose} aria-label="text alignment">
                        {prepareAlphabets()}
                    </ToggleButtonGroup>
                </Col>
            </Row>
            <Row className="justify-content-md-center">

            </Row>
        </div>
    );
}