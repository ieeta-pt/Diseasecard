import React from 'react';
import {Col, Container, Row} from "react-bootstrap";
import {
    Collapse,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    ListSubheader,
    MenuItem,
    MenuList,
    Paper
} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import Scrollspy from 'react-scrollspy'

const useStyles = makeStyles((theme) => ({

    paper: {
        marginRight: theme.spacing(2),
    },
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
    },
    nested: {
        paddingLeft: theme.spacing(4),
    },
}));


export const AboutPage = ({}) => {
    const classes = useStyles();


    return (
        <Container>
            <Row style={{ marginTop: "3%" }} className={"header"}>
                <Col md={{ span: 5, offset: 3 }}>
                    <h2><b>About Diseasecard</b></h2>
                </Col>
            </Row>
            <Row style={{marginTop: '5%'}}>
                <Col md={3}>
                    <Scrollspy
                        className="scrollspy" items={ ['section-1', 'section-2', 'section-3', 'section-4', 'section-5'] }
                        currentClassName="isCurrent"
                        style={{position: "fixed", width: '14%'}}
                        offset={-100}
                    >
                        <Paper className={classes.paper}>
                            <List
                                component="nav"
                                aria-labelledby="nested-list-subheader"
                                subheader={
                                    <ListSubheader component="div" id="nested-list-subheader">
                                        Content
                                    </ListSubheader>
                                }
                                className={classes.root}
                            >
                                <ListItem button>
                                    <ListItemText><a href="#section-1">Overview</a></ListItemText>
                                </ListItem>
                                <ListItem button>
                                    <ListItemText><a href="#section-2">Main Features</a></ListItemText>
                                </ListItem>
                                <Collapse in="true" timeout="auto" unmountOnExit>
                                    <List component="div" disablePadding>
                                        <ListItem button className={classes.nested}>
                                            <ListItemText><a href="#section-search">Search</a></ListItemText>
                                        </ListItem>
                                        <ListItem button className={classes.nested}>
                                            <ListItemText><a href="#section-browser">Browser</a></ListItemText>
                                        </ListItem>
                                        <ListItem button className={classes.nested}>
                                            <ListItemText><a href="#section-explorer">Explorer</a></ListItemText>
                                        </ListItem>
                                        <ListItem button className={classes.nested}>
                                            <ListItemText><a href="#section-liveview">LiveView</a></ListItemText>
                                        </ListItem>
                                    </List>
                                </Collapse>
                                <ListItem button>
                                    <ListItemText><a href="#section-3">Disclaimer</a></ListItemText>
                                </ListItem>
                                <ListItem button>
                                    <ListItemText><a href="#section-4">Contacts</a></ListItemText>
                                </ListItem>
                            </List>
                        </Paper>
                    </Scrollspy>
                </Col>
                <Col md={9} style={{overflowY: "scroll", scrollbarWidth: "none", height:"75vh"}} id="scrollStuff">

                        <section id="section-1" style={{marginBottom: "10%"}}>
                            <h3><b>Overview</b></h3>
                            <p align="justify">
                                Diseasecard is an Information retrieval tool for accessing and integrating genetic and medical information for health applications.
                                Resorting to this integrated environment, clinicians are able to access and relate diseases data already available in the Internet, scattered along multiple databases.
                            </p>
                            <p align="justify">
                                This new version of Diseasecard was developed by <a href="http://www.ua.pt">University of Aveiro</a> <a href="http://bioinformatics.ua.pt/">Bioinformatics Group</a>.
                            </p>
                            <p align="justify"> Feel free to evaluate and give us <b>feedback</b> on Diseasecard <a href="https://forms.gle/qCv5junYiVaFHNTw7" >here</a>. </p>
                        </section>

                        <section id="section-2" style={{marginBottom: "10%"}} >
                            <h3><b>Main Features</b></h3>
                            <p align="justify" style={{marginBottom: "5%"}}>
                                The new Diseasecard improves the exiting system with extended functionalities for searching and exploring the most relevant rare diseases knowledge.
                            </p>

                            <section id="section-search">
                                <h4>Search</h4>
                                <Row style={{marginBottom: "7%"}}>
                                    <Col md={5} className="my-auto">
                                        <p align="justify" style={{marginBottom: 0}}>Home search lets you search for any identifier in Diseasecard or access the powerful full-text search.</p>
                                    </Col>
                                    <Col md={7}>
                                        <img src={require('./prints/search.png')} style={{width: "100%"}}/>
                                    </Col>
                                </Row>
                            </section>

                            <section id="section-browser">
                                <h4>Browser</h4>
                                <Row style={{marginBottom: "7%"}}>
                                    <Col md={5} className="my-auto">
                                        <p align="justify">
                                            The browser displays all rare diseases arranged by their name. Each division can be classified by the number of connections or even filtered according to the disease name.
                                        </p>
                                    </Col>
                                    <Col md={7}>
                                        <img src={require('./prints/browser.png')} style={{width: "100%"}}/>
                                    </Col>
                                </Row>
                            </section>

                            <section id="section-explorer">
                                <h4>Explore</h4>
                                <Row style={{marginBottom: "1%", marginTop: "5%"}}>
                                    <Col md={3}>
                                        <img src={require('./prints/tree.png')} style={{width: "100%"}}/>
                                    </Col>
                                    <Col md={9}>
                                        <Row>
                                            <Col md={6}>
                                                <img src={require('./prints/bubble.png')} style={{width: "100%"}}/>
                                            </Col>
                                            <Col md={6}>
                                                <img src={require('./prints/hypertree.png')} style={{width: "100%"}}/>
                                            </Col>
                                        </Row>
                                    </Col>
                                </Row>
                                <Row style={{marginBottom: "10%"}}>
                                    <Col md={3}>
                                        <p align="justify">Sidebar tree display a list of all the connected resources for the rare disease entry being studied.</p>
                                    </Col>
                                    <Col md={9}>
                                        <p align="justify">
                                            To quickly assess the resource network for a specific rare disease, Diseasecard features two different strategies: the "Bubble Graph" and the "Hypertree Graph".
                                            Both graphical representations are innovative navigation methods, and for larger amounts of data Hypertree is the most effective visualization method.
                                        </p>
                                    </Col>
                                </Row>
                            </section>

                            <section id="section-liveview">
                                <h4>LiveView</h4>
                                <Row style={{marginBottom: "10%"}}>
                                    <Col md={5} className="my-auto">
                                        <p align="justify">
                                            LiveView displays external resources within Diseasecard, letting you keep focused on your research.
                                        </p>
                                    </Col>
                                    <Col md={7}>
                                        <img src={require('./prints/liveview.png')} style={{width: "100%"}}/>
                                    </Col>
                                </Row>
                            </section>

                        </section>

                        <section id="section-3" style={{marginBottom: "10%"}}>
                            <h2 style={{marginBottom: "5%"}}><b>Disclaimer</b></h2>
                            <ol>
                                <li>
                                    <p align="justify">
                                        <strong>Diseasecard</strong> is an on-going research project, aiming at the creation of a comprehensive rare diseases knowledge base.
                                    </p>
                                </li>
                                <li>
                                    <p align="justify">
                                        The premise behind <strong>Diseasecard</strong> is the autonomous lightweight integration of data in a single workspace, the <em>card</em>. The connected resources referenced in each rare disease card are external to the project, and maintained by their owners. The <strong>Diseasecard</strong> does not provide, nor maintains a database of disease information. In addition, the application does not change in any way the information retrieved from external sites.
                                    </p>
                                </li>
                                <li>
                                    <p align="justify">
                                        Collected data are freely available online and remain the intellectual property of the various original data sources. Any questions regarding acceptable use, copying, storage or distribution of individual data items should be directed to those sources. <strong>Diseasecard</strong> claims ownership and copyright on the total compilation of records in this database, and provides open and free access to the full database content for searching, browsing, record copying, and further dissemination on the strict understanding that no records are to be altered in any way (other than trivial format changes) and that <strong>Diseasecard</strong> and the original data sources are acknowledged in any subsequent usage of this information.<ul><li>An additional note must be made regarding OMIM's database. The current OMIM web application does not allow the display of internal pages within external frames. As this would disrupt Diseasecard's LiveView features, a workaround was put in place to ensure the best user experience for Diseasecard's users. Consequently, the OMIM application displayed in Diseasecard's LiveView mode may not reflect the latest version available in the original OMIM page.</li></ul>
                                    </p>
                                </li>
                                <li>
                                    <p align="justify">
                                        Even if this application can be helpful to navigate the web, it does not substitute healthcare professionals. Please note that <strong>Diseasecard</strong> is not validated for clinical use nor did any official body approve it.
                                    </p>
                                </li>
                                <li>
                                    <p align="justify">
                                        <strong>Diseasecard</strong> developers cannot be held responsible for the erroneous use of any information retrieved and displayed by the tool. Whilst all reasonable efforts have been made to ensure that <strong>Diseasecard</strong> and its data are of consistent high quality, we make no warranty, express or implied, as to their completeness, accuracy or utility for any particular purpose. <strong>Diseasecard</strong> software and data may be used by others on the clear understanding that no liability for any damage or negative outcome whatsoever, either direct or indirect, shall rest upon the <strong>Diseasecard</strong> team or its sponsors, or any of their employees or agents.
                                    </p>
                                </li>
                            </ol>
                        </section>

                        <section id="section-4" style={{marginBottom: "20%"}}>
                            <h2 style={{marginBottom: "5%"}}><b>Contacts</b></h2>
                            <Row>
                                <Col md={4}>
                                    <dl>
                                        <dt>José Luís Oliveira</dt>
                                        <dd><i className="icon-envelope-alt"></i> <a href="mailto:jlo@ua.pt">jlo@ua.pt</a></dd>
                                    </dl>
                                </Col>
                                <Col md={4}>
                                    <dl>
                                        <dt>João Rafael Almeida</dt>
                                        <dd><i className="icon-envelope-alt"></i> <a href="mailto:joao.rafael.almeida@ua.pt">joao.rafael.almeida@ua.pt</a></dd>
                                    </dl>
                                </Col>
                            </Row>

                        </section>



                </Col>
            </Row>





        </Container>
    );
}