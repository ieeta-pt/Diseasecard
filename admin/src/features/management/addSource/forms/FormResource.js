import React, {useCallback, useMemo, useState} from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import {
    Button,
    FormLabel,
    Grid,
    MenuItem
} from "@material-ui/core";
import {
    renderDropzoneInput,
    FootForm,
    renderSelectField,
    renderTextField,
    useStyles,
    renderSwitchField
} from "./FormElements";
import {getConceptsLabels, getOrdersLabels, getPluginsLabels, getResourcesLabels} from "../addSourceSlice";
import { useSelector } from "react-redux";
import {Col, Row} from "react-bootstrap";
import { change } from "redux-form";

const validate = values => {
    const errors = {}
    const requiredFields = [
        'titleResource',
        'labelResource',
        'descriptionResource',
        'resourceOf',
        'extendsResource',
        'orderResource',
        'publisherEndpoint',
    ]
    requiredFields.forEach(field => {
        if (!values[field]) {
            errors[field] = 'Required'
        }
    })
    if ( values.email && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
        errors.email = 'Invalid email address'
    }
    return errors
}


const AddResourceForm = props => {
    const { handleSubmit, classes } = props
    const c = useStyles();

    const baseStyle = {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '20px',
        borderWidth: 1,
        borderRadius: 5,
        background: "#000000",
        opacity: 0.8,
        borderStyle: 'solid',
        backgroundColor: '#fafafa',
        color: '#bdbdbd',
        outline: 'none',
        transition: 'border .24s ease-in-out'
    };
    const style = useMemo(() => ({
        ...baseStyle,
    }), []);

    const conceptLabels = useSelector(getConceptsLabels)
    const pluginLabels = useSelector(getPluginsLabels)
    const ordersLabels = useSelector(getOrdersLabels)
    const resourceLabels = useSelector(getResourcesLabels)


    const [publisher, setPublisher] = useState('');
    const [endpoint, setEndpoint] = useState(false);
    const [disableTextField, setDisableTextField] = useState(false);

    const goBack = () => {
        props.formDetails.goToStep(5);
    };
    const goNext = () => {
        props.formDetails.goToStep(9);
    };

    const handlePublisherChoice = (e) => {
        setPublisher(e.target.value);
        if (e.target.value === "OMIM") {
            setDisableTextField(true);
            props.change('labelResource', "resource_OMIM");
        }
        else
        {
            setDisableTextField(false);
            props.change('labelResource', "");
        }
    }

    const checkIfLabelExists = (value, allValues, props, name) => {
        for (let key of Object.keys(resourceLabels)) {
            if (value && resourceLabels[key].toLowerCase() === value.toLowerCase()) return 'Label already used.'
        }
    }

    return (
        <div style={{width: "94%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="addResourceForm">
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="titleResource"
                            component={renderTextField}
                            label="Title"
                            className={c.field}
                            labelText="olaaa"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="labelResource"
                            component={renderTextField}
                            label="Label"
                            className={c.field}
                            labelText="olaaa"
                            props={{ disabled: disableTextField }}
                            validate={checkIfLabelExists}
                        />
                    </Grid>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="descriptionResource"
                            component={renderTextField}
                            label="Description"
                            className={c.field}
                            labelText="olaaa"
                            multiline
                            rows={2}
                        />
                    </Grid>
                    <Grid item xs={6}  style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            name="resourceOf"
                            component={renderSelectField}
                            label="Resource Of"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(conceptLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={key}> {conceptLabels[key]} </MenuItem>)
                                })
                            }
                        </Field>
                    </Grid>
                    <Grid item xs={6}  style={{marginTop: "-3.5%"}}>
                        <Field
                            classes={classes}
                            size="small"
                            name="extendsResource"
                            component={renderSelectField}
                            label="Extends"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(conceptLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={key}> {conceptLabels[key]} </MenuItem>)
                                })
                            }
                        </Field>
                    </Grid>

                    <Grid item xs={6} style={{marginTop: "0.5%"}} >
                        <Field
                            classes={classes}
                            size="small"
                            name="orderResource"
                            component={renderSelectField}
                            label="Order"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {ordersLabels.map((option) => (
                                <MenuItem key={option} value={option+""}>
                                    {option}
                                </MenuItem>
                            ))}
                        </Field>
                    </Grid>
                    <Grid item xs={6} style={{marginTop: "0.5%"}} >
                        <Field
                            size="small"
                            name="publisherEndpoint"
                            component={renderSelectField}
                            label="Publisher"
                            onChange={e => handlePublisherChoice(e)}
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {pluginLabels.map((option) => (
                                <MenuItem key={option} value={option}>
                                    {option}
                                </MenuItem>
                            ))}
                        </Field>
                    </Grid>


                    {publisher==='OMIM' && (
                        <Grid container spacing={2}>
                            <Grid item xs={6} style={{marginLeft: "24px", paddingRight: "16px"}}>
                                <Field
                                    name="genemap"
                                    style={style}
                                    component={renderDropzoneInput}
                                />
                                <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Upload Genemap file</FormLabel>
                            </Grid>
                            <Grid item xs={6} style={{marginLeft: "-16px", marginRight: "-8px", paddingLeft: "16px"}}>
                                <Field
                                    name="morbidmap"
                                    style={style}
                                    component={renderDropzoneInput}
                                />
                                <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Upload Morbidmap file</FormLabel>
                            </Grid>
                        </Grid>
                    )}
                    {publisher!=='OMIM' && publisher!== '' && !endpoint && (
                        <Grid container>
                            <Grid container spacing={2} justify="flex-end">
                                <Field
                                    name="isEndpointFile"
                                    component={renderSwitchField}
                                    label="Upload local File"
                                    className={c.switch}
                                    checked={endpoint}
                                    labelPlacement="start"
                                    onChange={e => setEndpoint(e.target.checked)}
                                    labelText="olaaa"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <Field
                                    size="small"
                                    variant="outlined"
                                    name="endpointResource"
                                    component={renderTextField}
                                    label="Endpoint's URL"
                                    className={c.field}
                                    labelText="olaaa"
                                />
                            </Grid>
                        </Grid>
                    )}
                    {publisher!=='OMIM' && publisher!== '' && endpoint && (
                        <Grid container>
                            <Grid container spacing={2} justify="flex-end">
                                <Field
                                    name="isEndpointFile"
                                    component={renderSwitchField}
                                    label="Upload local File"
                                    className={c.switch}
                                    checked={endpoint}
                                    onChange={e => setEndpoint(e.target.checked)}
                                    labelText="olaaa"
                                />
                            </Grid>
                            <Grid item xs={12} style={{marginLeft: "16px", marginRight: "-16px"}}>
                                <Field
                                    name="files"
                                    style={style}
                                    component={renderDropzoneInput}
                                />
                                <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Endpoint</FormLabel>
                            </Grid>
                        </Grid>
                    )}

                    <div style={{ marginTop: '20px', marginBottom: "20px", width: "100%"}}>
                        <Row className="justify-content-md-center">
                            <Col sm="6" className="centerStuff">
                                <Button variant="outlined" color="primary" className={ c.buttonG } type="button" onClick={goBack}>
                                    Go Back
                                </Button>
                            </Col>
                            <Col sm="6" className="centerStuff">
                                <Button variant="outlined" className={ c.buttonG } type="submit" disabled={ props.pristine || props.submitting || props.invalid } onClick={goNext}>
                                    Next
                                </Button>
                            </Col>
                        </Row>
                    </div>
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddResourceForm', // a unique identifier for this form
    validate,
    asyncValidate,
    initialValues: { isEndpointFile: false }
})(AddResourceForm)
