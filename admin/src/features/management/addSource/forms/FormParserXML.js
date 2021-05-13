import React, {useState} from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import {Divider, Grid, Typography} from "@material-ui/core";
import {FootForm, renderSwitchField, renderTextField, useStyles} from "./FormElements";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'mainNode',
        'resourceID',
        'externalResourceNode',
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


const AddParserXMLForm = props => {
    const { handleSubmit, classes} = props

    const c = useStyles();
    const [methodByReplaceFlag, setMethodByReplaceFlag] = useState(false);
    const [resourceInfoInAttribute, setResourceInfoInAttribute] = useState(false);
    const [externalResourceInfoInAttribute, setExternalResourceInfoInAttribute] = useState(false);
    const [uniqueResource, setUniqueResource] = useState(false);
    const [uniqueExternalResource, setUniqueExternalResource] = useState(false);

    return (
        <div style={{width: "100%"}}>
            <form className={c.root} style={{width: "100%", textAlign:"left"}} onSubmit={handleSubmit} name="addParserXMLForm">
                <Typography variant="inherit" gutterBottom style={{textAlign: "left", width: "100%", color: "rgb(29, 196, 233)"}}>
                    Global Information
                </Typography>
                <Divider light/>
                <Grid container spacing={2} style={{marginBottom: "20px"}}>
                    <Grid item xs={8}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="mainNode"
                            component={renderTextField}
                            label="Node Name"
                            className={c.field}
                            labelText="Main Node"
                        />
                    </Grid>
                    <Grid item xs={4}>
                        <Field
                            name="isMethodByReplace"
                            component={renderSwitchField}
                            label="Method By Replace"
                            className={c.switch}
                            labelPlacement="start"
                            checked={methodByReplaceFlag}
                            onChange={e => setMethodByReplaceFlag(e.target.checked)}
                            style={{width: "100%", paddingTop: "17px", paddingLeft: "4%"}}
                        />
                    </Grid>
                </Grid>


                <Typography variant="inherit" gutterBottom style={{textAlign: "left", width: "100%", color: "rgb(29, 196, 233)"}}>
                    Resource Identification
                </Typography>
                <Divider light/>
                <Grid container spacing={2} style={{marginBottom: "20px"}}>
                    <Grid item xs={6}>
                        <Field
                            name="resourceInfoInAttribute"
                            component={renderSwitchField}
                            label="Resource Info in Attribute"
                            className={c.switch}
                            labelPlacement="end"
                            checked={resourceInfoInAttribute}
                            onChange={e => setResourceInfoInAttribute(e.target.checked)}
                            style={{width: "100%", paddingTop: "17px", paddingLeft: "7%"}}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="resourceID"
                            component={renderTextField}
                            label="Node / Attribute Name"
                            className={c.field}
                            labelText="Node Name or Attribute Name of Resource ID "
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            name="uniqueResource"
                            component={renderSwitchField}
                            label="Unique Resource"
                            className={c.switch}
                            labelPlacement="end"
                            checked={uniqueResource}
                            onChange={e => setUniqueResource(e.target.checked)}
                            style={{width: "100%", paddingTop: "17px", paddingLeft: "7%"}}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="resourceRegex"
                            component={renderTextField}
                            label="Regex Expression"
                            className={c.field}
                            labelText="Regex to retrieve correct Resource ID"
                        />
                    </Grid>
                </Grid>


                <Typography variant="inherit" gutterBottom style={{textAlign: "left", width: "100%", color: "rgb(29, 196, 233)"}}>
                    External Resource Identification
                </Typography>
                <Divider light/>
                <Grid container spacing={2} style={{marginBottom: "20px"}}>

                    <Grid item xs={12}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="externalResourceNode"
                            component={renderTextField}
                            label="Node / Attribute Name"
                            className={c.field}
                            labelText="Node Name or Attribute Name that contains External Resource ID "
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            name="externalResourceInfoInAttribute"
                            component={renderSwitchField}
                            label="External Resource Info in Attribute"
                            className={c.switch}
                            labelPlacement="end"
                            checked={externalResourceInfoInAttribute}
                            onChange={e => setExternalResourceInfoInAttribute(e.target.checked)}
                            style={{width: "100%", paddingTop: "17px", paddingLeft: "7%"}}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="externalResourceID"
                            component={renderTextField}
                            label="Node / Attribute Name"
                            className={c.field}
                            labelText="Node Name or Attribute Name of External Resource ID "
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            name="uniqueExternalResource"
                            component={renderSwitchField}
                            label="Unique External Resource"
                            className={c.switch}
                            labelPlacement="end"
                            checked={uniqueExternalResource}
                            onChange={e => setUniqueExternalResource(e.target.checked)}
                            style={{width: "100%", paddingTop: "17px", paddingLeft: "7%"}}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="regexExternalResource"
                            component={renderTextField}
                            label="Regex Expression"
                            className={c.field}
                            labelText="Regex to retrieve correct External Resource ID"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="filterBy"
                            component={renderTextField}
                            label="Node / Attribute Name"
                            className={c.field}
                            labelText="Filter External Resources by Node / Attribute Name"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="filterValue"
                            component={renderTextField}
                            label="Filter Value"
                            className={c.field}
                            labelText="Value of the filter"
                        />
                    </Grid>

                    {FootForm(props, c, 'Parser')}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddParserXMLForm', // a unique identifier for this form
    validate,
    asyncValidate,
    initialValues: {
        uniqueResource: false,
        isMethodByReplace: false,
        uniqueExternalResource: false,
        resourceInfoInAttribute: false,
        externalResourceInfoInAttribute:false,
        resourceRegex: "",
        externalResourceNode: "",
        regexExternalResource: "",
        filterBy:"",
        filterValue:""
    }
})(AddParserXMLForm)