import React from 'react';
import {Navbar, Nav, OverlayTrigger, Tooltip} from 'react-bootstrap'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBook , faAlignJustify } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";


export const NavbarD = () => {

    return (
        <Navbar bg="dark" variant="dark">
            <Navbar.Brand as={Link} to={'/'}><img alt="" className="d-inline-block align-top" src={ process.env.PUBLIC_URL + 'logo_bw.png'}/> </Navbar.Brand>

            <Nav className="ml-auto">
                <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Diseasecard about section</Tooltip>}>
                        <Nav.Link as={Link} to={'/browse/A'} ><FontAwesomeIcon icon={faBook}/></Nav.Link>
                </OverlayTrigger>

                <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Disease ard rare diseases browsing</Tooltip>}>
                    <Nav.Link as={Link} to={'/browse/A'} style={{marginLeft: 10}}><FontAwesomeIcon icon={faAlignJustify}/></Nav.Link>
                </OverlayTrigger>

            </Nav>
        </Navbar>
    );

}