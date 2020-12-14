import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getResults, selectBrowserResults} from "./browserSlice";
import {getStatus} from "../search/searchSlice";

export const BrowserResults = ({ match })  => {
    const { letter } = match.params
    const [results, setResults] = useState([]);
    const status = useSelector(getStatus)
    const dispatch = useDispatch();
    let content;

    useEffect( () => {
        console.log(letter)
        dispatch(getResults(letter))
        console.log(results)
    })

    // TODO: Adicionar uma data table para ler os dados


    return (
        <div>ola</div>
    );
}