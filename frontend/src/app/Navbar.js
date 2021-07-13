import React from 'react';
import {Navbar, Nav, OverlayTrigger, Tooltip} from 'react-bootstrap'

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBook , faAlignJustify } from '@fortawesome/free-solid-svg-icons'
import {Link} from "react-router-dom";

import logo from "../images/logo_bw.png"
import {showFrame} from "../features/disease/diseaseSlice";
import {useDispatch} from "react-redux";

export const NavbarD = () => {

    const dispatch = useDispatch();

    const prepare = () => {
        dispatch(showFrame("graph"));
    }

    return (
        <Navbar style={{background: "#283250"}} variant="dark">
            <Navbar.Brand as={Link} to={'/'}><img alt="" className="d-inline-block align-top" src={logo} onClick={prepare}/> </Navbar.Brand>

            <Nav className="ml-auto">
                <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Diseasecard about section</Tooltip>}>
                        <Nav.Link as={Link} to={'/about'} ><FontAwesomeIcon icon={faBook}/></Nav.Link>
                </OverlayTrigger>

                <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip-2">Jump to Diseasecard rare diseases browsing</Tooltip>}>
                    <Nav.Link as={Link} to={'/browse'} style={{marginLeft: 10}} onClick={prepare}><FontAwesomeIcon icon={faAlignJustify}/></Nav.Link>
                </OverlayTrigger>

            </Nav>
        </Navbar>
    );

}