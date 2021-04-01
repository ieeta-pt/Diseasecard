import React, {useMemo} from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { FormLabel, Grid, MenuItem } from "@material-ui/core";
import {renderDropzoneInput, FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";
import {getConceptsLabels, getInvalidEndpoints, getPluginsLabels, getResourcesLabels} from "../addSourceSlice";
import {useSelector} from "react-redux";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'titleResource',
        'labelResource',
        'descriptionResource',
        'commentResource',
        'hasEntityResource',
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
    const { handleSubmit, classes} = props
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

    return (
        <div style={{width: "94%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit}>
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
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="commentResource"
                            component={renderTextField}
                            label="Comment"
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

                    <Grid item xs={12} style={{marginLeft: "16px", marginRight: "-16px"}}>
                        <FormLabel style={{ fontSize: "12px", marginLeft: "16px", letterSpacing: "0.0333em" }}>Endpoint</FormLabel>
                        <Field
                            name="files"
                            style={style}
                            component={renderDropzoneInput}
                        />
                    </Grid>

                    <Grid item xs={3} style={{marginTop: "0.5%"}} >
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
                    <Grid item xs={3} style={{marginTop: "0.5%"}} >
                        <Field
                            size="small"
                            name="publisherEndpoint"
                            component={renderSelectField}
                            label="Publisher"
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
                    <Grid item xs={3} style={{marginTop: "0.5%"}} >
                        <Field
                            classes={classes}
                            size="small"
                            name="regexResource"
                            component={renderTextField}
                            label="Regex"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        />
                    </Grid>
                    <Grid item xs={3} style={{marginTop: "0.5%"}} >
                        <Field
                            size="small"
                            name="queryResource"
                            component={renderTextField}
                            label="Query"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
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
