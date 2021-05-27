import React, {useState, useMemo, useCallback, useEffect} from 'react';
import StepWizard from 'react-step-wizard';
import {Col, Container, Row} from "react-bootstrap";
import { useSpring, animated } from 'react-spring';
import styled from 'styled-components';
import { useDropzone } from 'react-dropzone'
import { useDispatch, useSelector } from "react-redux";
import { addConcept, addEntity, addOMIMResource, addResource, addResourceWithURLEndpoint, getFormLabels, getInvalidEndpoints, getResource, storeResource, uploadEndpoints, uploadOntology} from "./addSourceSlice";
import Dropzone from 'react-dropzone'
import AddConceptForm from "./forms/FormConcept";
import AddEntityFrom from "./forms/FormEntity";
import AddResourceForm from "./forms/FormResource";
import AddParserCSVForm from "./forms/FormParserCSV";
import AddParserXMLForm from "./forms/FormParserXML";
import {CircularProgress} from "@material-ui/core";
import AddParserOMIMForm from "./forms/FormParserOMIM";
import {getOntologyStructureInfo} from "../listResources/listResourcesSlice";
import {getAllResources} from "../systemStatus/systemStatusSlice";

const MethodIcon = styled(animated.div)``;


/*
    Description
 */
export const AddWizard = () => {
    const [state, updateState] = useState( {
        form: {}
    } )
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(getFormLabels())
    }, [])

    const updateForm = (key, value) => {
        const { form } = state;

        form[key] = value;
        updateState({
            ...state,
            form,
        });
    };

    const onStepChange = (stats) => {
        //console.log(stats);
    };

    const setInstance = SW => updateState({
        ...state,
        SW,
    });

    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col xs lg="6">
                    <StepWizard
                        onStepChange={onStepChange}
                        instance={setInstance}
                    >
                        <SelectMethod update={updateForm}/>
                        <UploadOntology  update={updateForm} form={state.form}/>
                        <ValidateEndpoints  update={updateForm} form={state.form}/>

                        <Instructions  update={updateForm} form={state.form}/>
                        <SelectBetweenECR  update={updateForm} form={state.form}/>
                        <AddEntities  update={updateForm} form={state.form}/>
                        <AddConcepts  update={updateForm} form={state.form}/>
                        <AddResources  update={updateForm} form={state.form}/>
                        <AddParsers  update={updateForm} form={state.form}/>
                    </StepWizard>
                </Col>
            </Row>
        </Container>
    );
}


/*
    Description
 */
export const Stats = ({ nextStep, previousStep, totalSteps, step, permissionToGo, goToStep }) => ( <div >

        { (step === 6 || step === 7 ) &&
            <div style={{ marginTop: '20px', marginBottom: "20px"}}>
                <Row className="justify-content-md-center">
                    <Col sm="6" className="centerStuff">
                        <a href="#" className='label theme-bg text-white f-14' style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ previousStep }>Add More</a>
                    </Col>
                    <Col sm="6" className="centerStuff">
                        <a href="#" className={permissionToGo ? 'label theme-bg text-white f-14' : 'label theme-wait text-white f-14'} style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ nextStep }> Finish </a>
                    </Col>
                </Row>
            </div>
        }
        {(step !== 6 && step !== 7) &&
            <div style={{ marginTop: '50px' }}>
                <hr />
                <Row className="justify-content-md-center">
                    <Col sm="6" className="centerStuff">
                        { step > 1 &&
                        <a href="#" className='label theme-bg text-white f-14' style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ previousStep }>Go Back</a>
                        }
                    </Col>
                    <Col sm="6" className="centerStuff">
                        { step < totalSteps ?
                            <a href="#" className={permissionToGo ? 'label theme-bg text-white f-14' : 'label theme-wait text-white f-14'} style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ permissionToGo ? nextStep : null}>Continue</a>
                            :
                            <a href="#" className={permissionToGo ? 'label theme-bg text-white f-14' : 'label theme-wait text-white f-14'} style={{ borderRadius: "15px", boxShadow: "0 5px 10px 0 rgba(0,0,0,0.2)" }} onClick={ permissionToGo ? nextStep : null}>Finish</a>
                        }
                    </Col>
                </Row>
                <hr />
            </div>
        }

    </div>);


/*
    First step of the wizard.
    It allows the user to select between "Submit an Ontology" or "Add Manually a Source".
 */
const SelectMethod = props => {
    const [isHoverR, setHoverR] = useState(true);
    const [isHoverM, setHoverM] = useState(true);
    const [method, setMethod] = useState('');
    const [permissionToGo, setPermissionToGo] = useState(false);
    const [nextStep, setNextStep] = useState(1)

    const update = (e) => {
        if (e === "R")      setNextStep(2);
        else if (e === "M") setNextStep(4);

        setMethod(e);
        setPermissionToGo(true);
        props.update('method', e)
    };

    const go = () => {
        props.goToStep(nextStep)
    }

    const methodIconAnimationR = useSpring({
        transform: isHoverR ? `translate(0px, 6px)` : `translate(0px, 0px) `,
        config: { tension: 200, friction: 4},
        cursor: isHoverR ? 'auto' : 'pointer'
    });
    const methodIconAnimationM = useSpring({
        transform: isHoverM ? `translate(0px, 6px)` : `translate(0px, 0px) `,
        config: { tension: 200, friction: 4},
        cursor: isHoverM ? 'auto' : 'pointer'
    });

    return (
        <div>
            <h6 className='text-center'>Please, start by choosing one of the available methods:</h6>
            <Row className="justify-content-md-center" style={{marginTop: "40px"}}>
                <Col sm="6" className="centerStuff">
                    <div className="centerStuff">
                        <MethodIcon style={methodIconAnimationR} onMouseEnter={()=>setHoverR(false)} onMouseLeave={()=>setHoverR(true)} onClick={() => update('R')}>
                            <i className={method === 'R' ? 'iconB feather icon-file-text' : 'feather icon-file-text'} style={{fontSize: "60px"}}/>
                        </MethodIcon>
                    </div>
                    <p style={{marginTop: "10px"}}>Enter an RDF File.</p>
                </Col>
                <Col sm="6" className="centerStuff">
                    <div className="centerStuff">
                        <MethodIcon style={methodIconAnimationM} onMouseEnter={()=>setHoverM(false)} onMouseLeave={()=>setHoverM(true)} onClick={() => update('M')}>
                            <i className={method === 'M' ? 'iconB feather icon-layers' : 'feather icon-layers'} style={{fontSize: "60px"}}/>
                        </MethodIcon>
                    </div>
                    <p style={{marginTop: "10px"}}>Add Manually a Source.</p>
                </Col>
            </Row>
            <Stats step={1} {...props} permissionToGo={permissionToGo} nextStep={go}/>
        </div>
    );
};


/*
    It corresponds to the second step of the wizard in case the user had selected "Submit an Ontology".
    The file submitted by the user is send to Diseasecard backend, to be pre-processed to identify the endpoints.
 */
const UploadOntology = props => {
    const [permissionToGo, setPermissionToGo] = useState(false);
    const dispatch = useDispatch();

    const baseStyle = {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '20px',
        borderWidth: 2,
        borderRadius: 2,
        borderColor: '#eeeeee',
        borderStyle: 'dashed',
        backgroundColor: '#fafafa',
        color: '#bdbdbd',
        outline: 'none',
        transition: 'border .24s ease-in-out'
    };
    const style = useMemo(() => ({
        ...baseStyle,
    }), []);

    const onDrop = useCallback((acceptedFiles) => {
        setPermissionToGo(true)
    }, [])
    const {acceptedFiles, getRootProps, getInputProps} = useDropzone( { maxFiles:1, onDrop } );

    const submitOntology = () => {
        let formData = new FormData();
        formData.append('file', acceptedFiles[0]);

        dispatch(uploadOntology(formData))

        props.update('file', acceptedFiles[0])
        props.nextStep()
    };

    return (
        <div>
            <p className='text-center'style={{color: "#1dc4e9"}}><b>Please, select a file:</b></p>
            <section className="container">
                <div {...getRootProps({style})}>
                    <input {...getInputProps()} />
                    <p style={{marginBottom: "0"}}>Drag 'n' drop a RDF file here, or click to select one:</p>
                </div>
                {acceptedFiles.length!==0 &&
                    <Row style={{ marginTop: "20px" }} className="justify-content-md-center" >
                        <p><b>Selected File:</b>&nbsp;{acceptedFiles[0].path}</p>
                    </Row>
                }
            </section>
            <Stats step={2} {...props} nextStep={submitOntology} permissionToGo={permissionToGo}/>
        </div>
    );
};


/*
    It corresponds to the third step of the wizard in case the user had selected "Submit an Ontology".
    Description
 */
const ValidateEndpoints = props => {
    const [permissionToGo, setPermissionToGo] = useState(false);
    const [endpointFiles, setEndpointFiles] = useState(new Map())
    const invalidEndpoints = useSelector(getInvalidEndpoints)
    const dispatch = useDispatch();

    const updateInvalidEndpoints = (k,v) => {
        setEndpointFiles(new Map(endpointFiles.set(k,v)));
    }

    const baseStyle = {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '20px',
        borderWidth: 2,
        borderRadius: 2,
        borderColor: '#eeeeee',
        borderStyle: 'dashed',
        backgroundColor: '#fafafa',
        color: '#bdbdbd',
        outline: 'none',
        transition: 'border .24s ease-in-out'
    };
    const style = useMemo(() => ({
        ...baseStyle,
    }), []);


    const onDrop = (resource, files) => {
        updateInvalidEndpoints(resource, files[0])
        if (endpointFiles.size === invalidEndpoints.length) {
            setPermissionToGo(true)
        }
    }

    const submitEndpoints = () => {
        let formData = new FormData();
        endpointFiles.forEach((value, key) => { formData.append('information[]', value, key) })

        dispatch(uploadEndpoints(formData))

        props.update('endpointFiles', endpointFiles)
        props.goToStep(1)
        dispatch(getOntologyStructureInfo())
        dispatch(getAllResources())
        dispatch(getFormLabels())
    };

    const items = invalidEndpoints.map((d) =>
        <div style={{borderTop: "2px solid #f1f1f1", marginTop: "40px"}}>
            {d.resource === "resource_OMIM" &&
                <div style={{marginTop: "10px"}}>
                    <p>The endpoint of <b>{d.resource}</b> needs two files to properly operate: <b>Genemap</b> and <b>Morbidmap</b>.</p>
                    <Row>
                        <Col sm={6}>
                            <p style={{fontSize: "12px", textAlign: "center"}}>Upload Genemap file:</p>
                            <Dropzone onDrop={acceptedFiles => onDrop("omim_genemap", acceptedFiles)}>
                                {({getRootProps, getInputProps}) => (
                                    <section>
                                        <div {...getRootProps({style})}>
                                            <input {...getInputProps()} />
                                            <p style={{marginBottom: "0", fontSize: "12px"}} >Drag 'n' drop some files here, or click to select files</p>
                                        </div>
                                    </section>
                                )}
                            </Dropzone>
                            {endpointFiles.has("omim_genemap") &&
                                <Row style={{ marginTop: "20px" }} className="justify-content-md-center" >
                                    <p style={{fontSize: "12px" }}><b>Selected File:</b>&nbsp;{endpointFiles.get("omim_genemap").path}</p>
                                </Row>
                            }
                        </Col>
                        <Col sm={6}>
                            <p style={{fontSize: "12px", textAlign: "center"}}>Upload Morbidmap file:</p>
                            <Dropzone onDrop={acceptedFiles => onDrop("omim_morbidmap", acceptedFiles)}>
                                {({getRootProps, getInputProps}) => (
                                    <section>
                                        <div {...getRootProps({style})}>
                                            <input {...getInputProps()} />
                                            <p style={{marginBottom: "0", fontSize: "12px"}} >Drag 'n' drop some files here, or click to select files</p>
                                        </div>
                                    </section>
                                )}
                            </Dropzone>
                            {endpointFiles.has("omim_morbidmap") &&
                                <Row style={{ marginTop: "20px" }} className="justify-content-md-center" >
                                    <p style={{fontSize: "12px" }}><b>Selected File:</b>&nbsp;{endpointFiles.get("omim_morbidmap").path}</p>
                                </Row>
                            }
                        </Col>
                    </Row>
                </div>
            }
            {d.resource !== "resource_OMIM" &&
                <div style={{marginTop: "10px"}}>
                    <p>
                        The endpoint of <b>{d.resource}</b> is invalid. Please upload a correct file.
                        <br/>
                        <span style={{fontSize: "10px"}}>[Specified Endpoint is unknown to the system: <i>{d.invalidPath}</i>]</span>
                        <br/>
                    </p>
                    <Dropzone onDrop={acceptedFiles => onDrop(d.resource, acceptedFiles)}>
                        {({getRootProps, getInputProps}) => (
                            <section>
                                <div {...getRootProps({style})}>
                                    <input {...getInputProps()} />
                                    <p style={{marginBottom: "0"}} >Drag 'n' drop some files here, or click to select files</p>
                                </div>
                            </section>
                        )}
                    </Dropzone>
                    {endpointFiles.has(d.resource) &&
                    <Row style={{ marginTop: "20px" }} className="justify-content-md-center" >
                        <p><b>Selected File:</b>&nbsp;{endpointFiles.get(d.resource).path}</p>
                    </Row>
                    }
                </div>
            }

        </div>
    );


    return (
        <div>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Validate the specified endpoints:</b></p>
            <section className="container">
                {items}
            </section>
            <Stats step={3} {...props} nextStep={submitEndpoints} permissionToGo={permissionToGo}/>
        </div>
    );
}


/*
    Description
 */
const Instructions = props => {
    const [permissionToGo, setPermissionToGo] = useState(true);

    const previousStep = () => { props.goToStep(1) }

    return (
        <div>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Brief Contextualization</b></p>
            <p align="justify">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut
                labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
                laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in
                voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat
                non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
            <Stats step={4} {...props} permissionToGo={permissionToGo} previousStep={previousStep}/>
        </div>
    );
}


/*
    Description
 */
const SelectBetweenECR = props => {
    const [isHoverE, setHoverE] = useState(true);
    const [isHoverC, setHoverC] = useState(true);
    const [isHoverR, setHoverR] = useState(true);
    const [method, setMethod] = useState('');
    const [permissionToGo, setPermissionToGo] = useState(false);
    const [nextStep, setNextStep] = useState(1)
    const dispatch = useDispatch()

    const update = (e) => {
        if      (e === "E")     setNextStep(6);
        else if (e === "C")     setNextStep(7);
        else if (e === "R")     setNextStep(8);

        setMethod(e);
        setPermissionToGo(true);
        props.update('method', e)
        dispatch(getFormLabels())
    };

    const go = () => {
        props.goToStep(nextStep)
    }

    const methodIconAnimationR = useSpring({
        transform: isHoverR ? `translate(0px, 6px)` : `translate(0px, 0px) `,
        config: { tension: 200, friction: 4},
        cursor: isHoverR ? 'auto' : 'pointer'
    });
    const methodIconAnimationE = useSpring({
        transform: isHoverE ? `translate(0px, 6px)` : `translate(0px, 0px) `,
        config: { tension: 200, friction: 4},
        cursor: isHoverE ? 'auto' : 'pointer'
    });
    const methodIconAnimationC = useSpring({
        transform: isHoverC ? `translate(0px, 6px)` : `translate(0px, 0px) `,
        config: { tension: 200, friction: 4},
        cursor: isHoverC ? 'auto' : 'pointer'
    });

    return (
        <div>
            <h6 className='text-center'>What do you desire to add?</h6>
            <Row className="justify-content-md-center" style={{marginTop: "40px"}}>
                <Col sm="4" className="centerStuff">
                    <div className="centerStuff">
                        <MethodIcon style={methodIconAnimationE} onMouseEnter={()=>setHoverE(false)} onMouseLeave={()=>setHoverE(true)} onClick={() => update('E')}>
                            <i className={method === 'E' ? 'iconB feather icon-anchor' : 'feather icon-anchor'} style={{fontSize: "60px"}}/>
                        </MethodIcon>
                    </div>
                    <p style={{marginTop: "10px"}}>An Entity.</p>
                </Col>
                <Col sm="4" className="centerStuff">
                    <div className="centerStuff">
                        <MethodIcon style={methodIconAnimationC} onMouseEnter={()=>setHoverC(false)} onMouseLeave={()=>setHoverC(true)} onClick={() => update('C')}>
                            <i className={method === 'C' ? 'iconB feather icon-codepen' : 'feather icon-codepen'} style={{fontSize: "60px"}}/>
                        </MethodIcon>
                    </div>
                    <p style={{marginTop: "10px"}}>A Concept.</p>
                </Col>
                <Col sm="4" className="centerStuff">
                    <div className="centerStuff">
                        <MethodIcon style={methodIconAnimationR} onMouseEnter={()=>setHoverR(false)} onMouseLeave={()=>setHoverR(true)} onClick={() => update('R')}>
                            <i className={method === 'R' ? 'iconB feather icon-cpu' : 'feather icon-cpu'} style={{fontSize: "60px"}}/>
                        </MethodIcon>
                    </div>
                    <p style={{marginTop: "10px"}}>A Resource.</p>
                </Col>
            </Row>
            <Stats step={4} {...props} permissionToGo={permissionToGo} nextStep={go}/>
        </div>
    );
}


/*
    Description
 */
const AddEntities = props => {
    const dispatch = useDispatch();

    const submit = (values) => {
        let formData = new FormData(document.forms.namedItem("addEntityForm"))
        dispatch(addEntity(formData))
    }

    return (
        <div>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Add an Entity</b></p>

            <AddEntityFrom onSubmit={submit} formDetails={props}/>
        </div>
    );
}


/*
    Description
 */
const AddConcepts = props => {
    const dispatch = useDispatch();

    const submit = (values) => {
        let formData = new FormData(document.forms.namedItem("addConceptForm"))
        dispatch(addConcept(formData))
    }

    return (
        <div>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Add a Concept</b></p>
            <AddConceptForm onSubmit={submit} formDetails={props}/>
        </div>
    );
}


/*
    Description
 */
const AddResources = props => {
    const [permissionToGo, setPermissionToGo] = useState(false);
    const dispatch = useDispatch()
    const submit = (values) => {
        dispatch(storeResource(values))
        setPermissionToGo(true)
    }

    return (
        <div>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Add a Resource</b></p>
            <AddResourceForm onSubmit={submit} formDetails={props} permissionToGo={permissionToGo}/>
        </div>
    );
}


/*
    Description
 */
const AddParsers = props => {
    const dispatch = useDispatch()
    const resource = useSelector(getResource)
    const plugin = resource.publisherEndpoint
    const submit = (values) => {
        let forms = {resource, values}

        if ( resource.publisherEndpoint === 'OMIM' ) dispatch(addOMIMResource(forms))
        else {
            if (!resource.isEndpointFile) dispatch(addResourceWithURLEndpoint(forms))
            else dispatch(addResource(forms))
        }

        dispatch(getFormLabels())
    }

    let content;
    if(plugin) {
        content = (
            <div>
                {plugin === "CSV"  && <AddParserCSVForm  onSubmit={submit} formDetails={props}/> }
                {plugin === "XML"  && <AddParserXMLForm  onSubmit={submit} formDetails={props}/> }
                {plugin === "OMIM" && <AddParserOMIMForm onSubmit={submit} formDetails={props}/> }
            </div>
        )
    }
    else content = <CircularProgress color="inherit" style={{marginTop: "40px"}}/>

    return (
        <div style={{textAlign: "center"}}>
            <p className='text-center' style={{color: "#1dc4e9"}}><b>Add a Parser</b></p>
            {content}
        </div>
    );
}
