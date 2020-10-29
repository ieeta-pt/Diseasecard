import React  from 'react';
import DataTable from "react-data-table-component";
import { useSelector } from "react-redux";
import { selectAllResults, selectQuery, getNumberOfResults, getStatus } from "./searchSlice";
import { Container, Badge } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBookmark } from '@fortawesome/free-regular-svg-icons'
import { faAngleRight } from '@fortawesome/free-solid-svg-icons'
import {AsyncTypeahead} from "react-bootstrap-typeahead";



export const SearchResults = () => {
    const results = useSelector(selectAllResults)
    const query = useSelector(selectQuery)
    const numberOfResults = useSelector(getNumberOfResults)
    const status = useSelector(getStatus)

    // DataTable Fields
    let content;
    const columns = [
        {
            sortable: true,
            cell: result =>
                <div>
                    <FontAwesomeIcon icon={ faBookmark }/> &nbsp;
                    <span style={{fontWeight: "bold"}}>
                        { result.omim } - { result.alias.length > 0 ? result.alias[0].split(":")[2] : result.name }
                    </span>
                </div>,
        },
    ];

    const ExpandableComponent = ({ data }) =>
        <div style={{paddingLeft:4+"rem", paddingTop:0.6+'rem'}}>
            <ul className="results_items">
                { data.links.map(link => <li key={ link } > <FontAwesomeIcon icon={ faAngleRight }/>  { link } </li>) }
            </ul>
        </div>;


    // Content management
    if (status === 'loading') {
        content = <div>Loading...</div>
    }
    else if (status === 'succeeded') {
        if ( results.length !== 0 ) {
            content = <DataTable
                title={<h4><span id="results_size">{ numberOfResults }</span> total results for <Badge variant="primary" id="queryField">{ query }</Badge></h4>}
                columns={columns}
                data={results}
                sortIcon={<FontAwesomeIcon icon={ faAngleRight }/>}
                pagination={true}
                keyField="omim"
                highlightOnHover
                expandableRows
                expandableRowDisabled={data => data.links.length === 0}
                expandableRowsComponent={<ExpandableComponent />}
                // onRowClicked={(row) =>  redirectToProject(row)}    -> https://github.com/jbetancur/react-data-table-component/issues/195
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
        <Container id="search" >
            <div style={{marginTop: 3+"rem"}}>{content}</div>
        </Container>
    )
}