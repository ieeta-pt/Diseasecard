import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { Grid, MenuItem } from "@material-ui/core";
import {FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";


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

    if (values.labelConcept) {
        for (let key of Object.keys(conceptLabels)) {
            if (conceptLabels[key].toLowerCase() === values.labelConcept.toLowerCase()) {
                errors.labelConcept = 'Label already used.'
            }
        }
    }

    return errors
}

let conceptLabels = {}

const AddConceptForm = props => {
    const { handleSubmit, classes, labels} = props

    const c = useStyles();

    const resourcesLabels = labels['resourcesLabels']
    conceptLabels = labels['conceptsLabels']
    const entitiesLabels = labels['entitiesLabels']

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
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="descriptionConcept"
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
                            classes={classes}
                            size="small"
                            name="hasEntityConcept"
                            component={renderSelectField}
                            label="Related Entity"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(entitiesLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={key}> {entitiesLabels[key]} </MenuItem>)
                                })
                            }
                        </Field>
                    </Grid>
                    <Grid item xs={6}  style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            name="hasResourceConcept"
                            component={renderSelectField}
                            label="Related Resource"
                            variant="outlined"
                            className={c.field}
                            labelText="olaaa"
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(resourcesLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={key}> {resourcesLabels[key]} </MenuItem>)
                                })
                            }
                        </Field>
                    </Grid>

                    {FootForm(props, c, 'Concept')}

                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddConceptForm', // a unique identifier for this form
    validate,
    asyncValidate
})(AddConceptForm)
