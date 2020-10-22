import React, { useState } from 'react';
import { useDispatch } from "react-redux";
import { Form, InputGroup, FormControl } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSearch } from '@fortawesome/free-solid-svg-icons'
import { getResults } from "./searchSlice";
import { Link } from "react-router-dom";


export const SearchForm = () => {
    const [searchInput, setSearchInput] = useState('')
    const dispatch = useDispatch();

    const onSearchInputChanged = e => setSearchInput(e.target.value)

    const onSearchButtonClicked = () => {
        if (searchInput) {
            dispatch(getResults(searchInput))
            setSearchInput('')
        }
    }

    //TODO: Adicionar validação de input! input.lenght >= 4
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
                    <InputGroup value={ searchInput } onChange={ onSearchInputChanged }>
                        <FormControl id="inlineFormInputGroupUsername" placeholder="Search here..." />
                        <InputGroup.Append>
                            <Link to={'/searchResults'} onClick={ onSearchButtonClicked } className="btn btn-primary"><FontAwesomeIcon icon={ faSearch }/></Link>
                        </InputGroup.Append>
                    </InputGroup>
                </Form.Group>
            </Form>
        </div>
    );
}