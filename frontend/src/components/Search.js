import React from 'react';
import logo from '../images/logo.png'
import { Form, InputGroup, FormControl, Button } from 'react-bootstrap'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSearch} from '@fortawesome/free-solid-svg-icons'


class Search extends React.Component {

    render() {
        return (
            <div className="container" id="index">
                <div id="logo">
                    <a href="#about" title="About Diseasecard" >
                        <img className="logo img-responsive" width="434" height="59" src={ logo } alt="" />
                    </a>
                </div>
                <Form id="search_form">
                    <Form.Group>
                        <Form.Label htmlFor="inlineSearch" srOnly>
                            Search
                        </Form.Label>
                        <InputGroup>
                            <FormControl id="inlineFormInputGroupUsername" placeholder="Search here..." />
                            <InputGroup.Append>
                                <Button className="btn" ><FontAwesomeIcon icon={ faSearch }/></Button>
                            </InputGroup.Append>
                        </InputGroup>
                    </Form.Group>
                </Form>
            </div>
        );
    }
}


export default Search;