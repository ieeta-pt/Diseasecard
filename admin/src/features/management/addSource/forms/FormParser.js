import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { Grid } from "@material-ui/core";
import {FootForm, renderTextField, useStyles} from "./FormElements";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'titleConcept',
        'labelConcept',
        'descriptionConcept',
        'hasEntityConcept',
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


const AddParserForm = props => {
    const { handleSubmit, classes} = props

    const c = useStyles();

    return (
        <div style={{width: "94%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="addConceptForm">
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="titleConcept"
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
                            name="labelConcept"
                            component={renderTextField}
                            label="Label"
                            className={c.field}
                            labelText="olaaa"
                        />
                    </Grid>


                    {FootForm(props, c, 'Parser')}

                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddParserForm', // a unique identifier for this form
    validate,
    asyncValidate
})(AddParserForm)
