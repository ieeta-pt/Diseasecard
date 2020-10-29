import React, { useState} from 'react';
import { useDispatch } from "react-redux";
import { Form, InputGroup } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSearch } from '@fortawesome/free-solid-svg-icons'
import { getResults, getAutocomplete } from "./searchSlice";
import { Link } from "react-router-dom";
import { AsyncTypeahead, Token } from 'react-bootstrap-typeahead';
import { unwrapResult } from "@reduxjs/toolkit";


export const SearchForm = () => {
    const [searchInput, setSearchInput] = useState('')
    const [isLoading, setIsLoading] = useState(false);
    const [options, setOptions] = useState([]);
    const props = {};

    props.multiple = true;
    props.renderToken = (option, {onRemove}, index) => (
        <Token key={option.omim} onRemove={onRemove} option={option} onClick={console.log("omim:" + option.omim)}> {`${option.info} (OMIM: ${option.omim})`} </Token>
    )

    const dispatch = useDispatch();

    const onSearchButtonClicked = () => {
        if (searchInput) {
            dispatch(getResults(searchInput))
            setSearchInput('')
        }
    }

    const handleSearchAutocomplete = async query => {
        setIsLoading(true);
        setSearchInput(query);
        const results = await dispatch(getAutocomplete(query))
        setOptions(unwrapResult(results))
        setIsLoading(false);
    };



    return (
        <div className="container" id="index">
            <div id="logo">
                <a href="#about" title="About Diseasecard" >
                    <img className="logo img-responsive" width="434" height="59" src={ process.env.PUBLIC_URL + 'logo.png' } alt="" />
                </a>
            </div>
            <Form id="search_form">
                <Form.Group>
                    <Form.Label htmlFor="inlineSearch" srOnly>
                        Search
                    </Form.Label>
                    <InputGroup value={ searchInput }>
                        <AsyncTypeahead
                            {...props}
                            id="async_search"
                            isLoading={isLoading}
                            labelKey={(option) => `${option.info}`}
                            minLength={4}
                            onSearch={ handleSearchAutocomplete }
                            options={options}
                            placeholder="Search..."
                        />
                        <InputGroup.Append>
                            <Link to={'/searchResults'} onClick={ onSearchButtonClicked } className="btn btn-primary"><FontAwesomeIcon icon={ faSearch }/></Link>
                        </InputGroup.Append>
                    </InputGroup>

                </Form.Group>
            </Form>
        </div>
    );
}