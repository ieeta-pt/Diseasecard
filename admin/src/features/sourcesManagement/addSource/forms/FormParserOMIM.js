import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import {Divider, Grid, Typography} from "@material-ui/core";
import {FootForm, renderTextField, useStyles} from "./FormElements";


const validate = values => {
    const errors = {}
    const requiredFields = [
        "genecardName",
        "genecardOMIM",
        "genecardLocation",
        "genecardGenes",
        "morbidmapName",
        "morbidmapGene",
        "morbidmapOMIM",
        "morbidmapLocation"
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


const AddParserOMIMForm = props => {
    const { handleSubmit, classes} = props
    const c = useStyles();

    return (
        <div style={{width: "100%"}}>
            <form className={c.root} style={{width: "100%", textAlign:"left"}} onSubmit={handleSubmit} name="addParserOMIMForm">
                <Typography variant="inherit" gutterBottom style={{textAlign: "left", width: "100%"}}>
                    Genemap File Fields
                </Typography>
                <Divider light/>
                <Grid container spacing={2} style={{marginBottom: "30px"}}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="genecardName"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Disease Name (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="genecardOMIM"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of OMIM number (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="genecardLocation"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Location (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="genecardGenes"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Gene List (starting in 0)"
                        />
                    </Grid>
                </Grid>


                <Typography variant="inherit" gutterBottom style={{textAlign: "left", width: "100%"}}>
                    Morbidmap File Fields
                </Typography>
                <Divider light/>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="morbidmapName"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Disease Name (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="morbidmapGene"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Gene List (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="morbidmapOMIM"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of OMIM number (starting in 0)"
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="morbidmapLocation"
                            component={renderTextField}
                            label="Column Number"
                            className={c.field}
                            labelText="Column Number of Location (starting in 0)"
                        />
                    </Grid>

                    {FootForm(props, c, 'Parser')}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddParserOMIMForm', // a unique identifier for this form
    validate,
    initialValues: {
        resourceRegex: "",
        externalResourceRegex: ""
    }
})(AddParserOMIMForm)

