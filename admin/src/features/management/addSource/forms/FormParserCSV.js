import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import {Divider, Grid, Typography} from "@material-ui/core";
import {FootForm, renderTextField, useStyles} from "./FormElements";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'resourceID',
        'resourceRegex',
        'externalResourceID',
        'externalResourceRegex',
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


const AddParserCSVForm = props => {
    const { handleSubmit, classes} = props
    const c = useStyles();

    return (
        <div style={{width: "100%"}}>
            <form className={c.root} style={{width: "100%", textAlign:"left"}} onSubmit={handleSubmit} name="addConceptForm">
                <Typography variant="subtitle3" gutterBottom style={{textAlign: "left", width: "100%"}}>
                    Resource Identification
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="resourceID"
                            component={renderTextField}
                            label="Number of Column"
                            className={c.field}
                            labelText="Column with Resource ID (Starting in 0)"
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
                <Typography variant="subtitle3" gutterBottom style={{textAlign: "left", width: "100%"}}>
                    External Resource Identification
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="externalResourceID"
                            component={renderTextField}
                            label="Number of Column"
                            className={c.field}
                            labelText="Column with External Resource ID (Starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="externalResourceRegex"
                            component={renderTextField}
                            label="Regex Expression"
                            className={c.field}
                            labelText="Regex to retrieve correct External Resource ID"
                        />
                    </Grid>

                    {FootForm(props, c, 'Parser')}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddParserCSVForm', // a unique identifier for this form
    validate,
    asyncValidate
})(AddParserCSVForm)

