import React, {useMemo, useState} from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { FormLabel, Grid, MenuItem } from "@material-ui/core";
import { renderDropzoneInput, FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";
import { getConceptsLabels, getPluginsLabels } from "../addSourceSlice";
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Switch from '@material-ui/core/Switch';
import { useSelector } from "react-redux";


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
        'regexResource',
        'queryResource'
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


const currencies = [
    {
        value: 'USD',
        label: '$',
    },
    {
        value: 'EUR',
        label: '€',
    },
    {
        value: 'BTC',
        label: '฿',
    },
    {
        value: 'JPY',
        label: '¥',
    },
];


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

    const [publisher, setPublisher] = useState(true);
    const [endpoint, setEndpoint] = useState(false);

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
                            {conceptLabels.map((option) => (
                                <MenuItem key={option} value={option}>
                                    {option}
                                </MenuItem>
                            ))}
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
                            {conceptLabels.map((option) => (
                                <MenuItem key={option} value={option}>
                                    {option}
                                </MenuItem>
                            ))}
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
                            {currencies.map((option) => (
                                <MenuItem key={option.value} value={option.value}>
                                    {option.label}
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
                            onChange={e => setPublisher(e.target.value)}
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

                    <Grid container spacing={2} justify="flex-end">
                        <FormControlLabel
                            control={<Switch color="primary" />}
                            label="Upload local File"
                            checked={endpoint}
                            onChange={e => setEndpoint(e.target.checked)}
                            className={c.switch}
                            labelPlacement="start"
                        />
                    </Grid>


                    {publisher==='OMIM' && (
                        <Grid container spacing={2}>
                            <Grid item xs={6} style={{marginLeft: "24px", paddingRight: "16px"}}>
                                <Field
                                    name="genecard"
                                    style={style}
                                    component={renderDropzoneInput}
                                />
                                <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Upload Genecard file</FormLabel>
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
                    {publisher!=='OMIM' && !endpoint && (
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
                    )}
                    {publisher!=='OMIM' && endpoint && (
                        <Grid item xs={12} style={{marginLeft: "16px", marginRight: "-16px"}}>
                            <Field
                                name="files"
                                style={style}
                                component={renderDropzoneInput}
                            />
                            <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em", marginTop: "12px" }}>Endpoint</FormLabel>
                        </Grid>
                    )}

                    <Grid item xs={6} style={{marginTop: "0.5%"}} >
                        <Field
                            classes={classes}
                            size="small"
                            name="regexResource"
                            component={renderTextField}
                            label="Column"
                            variant="outlined"
                            className={c.field}
                            labelText="Extended Resource Identifier"
                        />
                    </Grid>
                    <Grid item xs={6} style={{marginTop: "0.5%"}} >
                        <Field
                            size="small"
                            name="queryResource"
                            component={renderTextField}
                            label="Column"
                            variant="outlined"
                            className={c.field}
                            labelText="Resource Identifier"
                        />
                    </Grid>

                    {FootForm(props, c)}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddResourceForm', // a unique identifier for this form
    validate,
    asyncValidate
})(AddResourceForm)
