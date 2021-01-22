import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getResults, getStatus, selectBrowserResults} from "./browserSlice";
import {Badge, Col, Container, ProgressBar, Row} from "react-bootstrap";
import {ToggleButton, ToggleButtonGroup} from "@material-ui/lab";
import DataTable from "react-data-table-component";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleRight} from "@fortawesome/free-solid-svg-icons";
import {faBookmark} from "@fortawesome/free-regular-svg-icons";

export const BrowserResults = ({ match })  => {
    const [letter, setLetter] = useState(["A"]);
    const dispatch = useDispatch();
    const status = useSelector(getStatus)
    const results = useSelector(selectBrowserResults)


    // DataTable Fields
    let content;
    const columns = [
        {
            name: 'OMIM',
            selector: 'omim',
            width:"20%",
            sortable: true,
        },
        {
            name: 'Name',
            selector: 'name',
            width:"60%",
            sortable: true,
        },
        {
            name: 'Progress',
            selector: 'progress',
            width:"20%",
            //TODO: MUDAR PARA IR BUSCAR O TYPE!
            cell: row => <ProgressBar style={{width: "100%"}} variant="danger" now={row.progress} label={row.c}/>
        }
    ];


    useEffect(() => {
            dispatch(getResults(letter))
    }, [letter])

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
    };

    // Content management
    if (status === 'loading') {
        content = <div>Loading...</div>
    }
    else if (status === 'succeeded') {
        if ( results.length !== 0 ) {
            content = <DataTable
                title={<h4> Diseases started with letter <Badge variant="primary" id="queryField">{ letter }</Badge></h4>}
                columns={columns}
                data={results}
                sortIcon={<FontAwesomeIcon icon={ faAngleRight }/>}
                pagination={true}
                keyField="omim"
                highlightOnHover
            />
        }
        else {
            content = <div>TODO: inserir cena de pesquisa e dizer que não há resultados. </div>
        }
    }
    else if (status === 'error') {
        content = <div>Ups...</div>
    }

    return (
        <Container style={{marginTop:"2%"}}>
            <Row className="justify-content-md-center">
                    <ToggleButtonGroup value={letter} exclusive onChange={handleChose} aria-label="text alignment">
                        {prepareAlphabets()}
                    </ToggleButtonGroup>
            </Row>
            <Row className="justify-content-md-center">
                    <div style={{marginTop: 3+"rem", width: "100%"}}>{content}</div>
            </Row>
        </Container>
    );
}