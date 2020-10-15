import React from 'react';
import {Navbar, Nav, OverlayTrigger, Tooltip} from 'react-bootstrap'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBook , faAlignJustify } from '@fortawesome/free-solid-svg-icons'

import logo from '../images/logo_bw.png';


class NavbarD extends React.Component {

    render() {
        return (
            <Navbar bg="dark" variant="dark">
                <Navbar.Brand href="#home"><img alt="" className="d-inline-block align-top" src={logo}/> </Navbar.Brand>

                <Nav className="ml-auto">
                    <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Diseasecard about section</Tooltip>}>
                        {({...triggerHandler}) => (
                            <Nav.Link href="#home" {...triggerHandler}><FontAwesomeIcon icon={faBook}/></Nav.Link>
                        )}
                    </OverlayTrigger>

                    <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Diseasecard rare diseases browsing</Tooltip>}>
                        {({...triggerHandler}) => (
                            <Nav.Link href="#features" {...triggerHandler} style={{marginLeft: 10}}><FontAwesomeIcon
                                icon={faAlignJustify}/></Nav.Link>
                        )}
                    </OverlayTrigger>
                </Nav>
            </Navbar>
        );
    }
}

export default NavbarD;