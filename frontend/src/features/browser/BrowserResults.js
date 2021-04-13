import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getResults, getStatus, selectBrowserResults} from "./browserSlice";
import {Badge, Container, ProgressBar, Row} from "react-bootstrap";
import {ToggleButton, ToggleButtonGroup} from "@material-ui/lab";
import DataTable from "react-data-table-component";
import {getDiseaseByOMIM} from "../disease/diseaseSlice";
import {useHistory} from "react-router-dom";
import { makeStyles, withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import ArrowDownward from '@material-ui/icons/ArrowDownward';

export const BrowserResults = ({ match })  => {
    const [letter, setLetter] = useState(["A"]);
    const dispatch = useDispatch();
    const status = useSelector(getStatus)
    const results = useSelector(selectBrowserResults)
    const history = useHistory();

    // DataTable Fields
    let content;

    const sortIcon = <ArrowDownward />;

    const customStyles = {
        rows: {
            style: {
                fontSize: '13px',
                color: 'rgba(0, 0, 0, 0.87)',
                backgroundColor: '#ffffff',
                minHeight: '48px',
                '&:not(:last-of-type)': {
                    borderBottomStyle: 'solid',
                    borderBottomWidth: '1px',
                    borderRadius: '20px',
                    borderBottomColor: 'rgba(0,0,0,0)',
                },
            }
        }
    };

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
            selector: 'c',
            width:"20%",
            sortable: true,
            //TODO: MUDAR PARA IR BUSCAR O TYPE!
            cell: row => <ProgressBar style={{width: "100%"}} variant={row.type} now={row.c} label={row.c}/>
        }
    ];

    const useStyles = makeStyles((theme) => ({
        paper: {
            display: 'flex',
            border: `1px solid ${theme.palette.divider}`,
            flexWrap: 'wrap',
        },
        divider: {
            margin: theme.spacing(1, 0.5),
        },
    }));

    const StyledToggleButtonGroup = withStyles((theme) => ({
        grouped: {
            margin: theme.spacing(0.5),
            border: 'none',
            '&:not(:first-child)': {
                borderRadius: theme.shape.borderRadius,
            },
            '&:first-child': {
                borderRadius: theme.shape.borderRadius,
            },
        },
    }))(ToggleButtonGroup);

    useEffect(() => {
        dispatch(getResults(letter))
    }, [letter])

    const prepareAlphabets = () => {
        // TODO: Adicionar #
        let result = [];
        for(let i=65; i<91; i++) {
            result.push(
                <ToggleButton key={i} value={String.fromCharCode(i)} aria-label={String.fromCharCode(i)} style={{width: "10%", padding: "7px"}}>
                    <b>{String.fromCharCode(i)} </b>
                </ToggleButton>
            )
        }
        return result;
    };

    const handleChose = (event, newLetter) => {
        if (newLetter) setLetter(newLetter);
    };

    const handleSelectedOption = ( selected ) => {
        dispatch(getDiseaseByOMIM(selected.omim))
        history.push('/disease/' + selected.omim)
    }

    const classes = useStyles();

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
                pagination={true}
                keyField="omim"
                sortIcon={sortIcon}
                customStyles={customStyles}
                onRowClicked={ handleSelectedOption }
                highlightOnHover
            />
        }
        else {
            content = <div>There is no data to display.</div>
        }
    }
    else if (status === 'error') {
        content = <div>Ups...</div>
    }

    return (
        <Container style={{marginTop:"2%"}}>
            <Row className="justify-content-md-center">
                <Paper elevation={0} className={classes.paper} style={{width: "100%"}}>
                    <StyledToggleButtonGroup value={letter} exclusive onChange={handleChose} aria-label="text alignment" style={{width: "100%"}}>
                        {prepareAlphabets()}
                    </StyledToggleButtonGroup>
                </Paper>
            </Row>
            <Row className="justify-content-md-center">
                    <div style={{marginTop: 3+"rem", minWidth: "100%"}}>{content}</div>
            </Row>
        </Container>
    );
}