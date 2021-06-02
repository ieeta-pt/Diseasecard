import React, {useEffect} from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { Grid, MenuItem } from "@material-ui/core";
import {FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";


const validate = values => {
    const errors = {}
    const requiredFields = [
        'titleEntity',
        'labelEntity',
        'descriptionEntity',
    ]
    requiredFields.forEach(field => {
        if (!values[field]) {
            errors[field] = 'Required'
        }
    })
    if ( values.email && !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
        errors.email = 'Invalid email address'
    }
    if ( values.labelEntity ) {
        for (let key of Object.keys(entitiesLabels)) {
            if (entitiesLabels[key].toLowerCase() === values.labelEntity.toLowerCase()) {
                errors.labelEntity = 'Label already used.'
            }
        }
    }
    return errors
}

let entitiesLabels = {}

const AddEntityFrom = props => {
    const { handleSubmit, classes, labels} = props
    const c = useStyles();

    const conceptsLabels = labels['conceptsLabels']
    entitiesLabels = labels['entitiesLabels']

    return (
        <div style={{width: "94%"}}>
            <form className={c.root} style={{width: "100%"}} onSubmit={handleSubmit} name="addEntityForm">
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="titleEntity"
                            component={renderTextField}
                            label="Title"
                            className={c.field}
                            labelText="Entity Title."
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="labelEntity"
                            component={renderTextField}
                            label="Label"
                            className={c.field}
                            labelText="This field works as the internal ID. Follow the recommendation in last steps. "
                        />
                    </Grid>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}>
                        <Field
                            size="small"
                            variant="outlined"
                            name="descriptionEntity"
                            component={renderTextField}
                            label="Description"
                            className={c.field}
                            labelText="Entity Description."
                            multiline
                            rows={2}
                        />
                    </Grid>
                    <Grid item xs={12} style={{marginTop: "-3.5%"}}  >
                        <Field
                            classes={classes}
                            size="small"
                            name="isEntityOfEntity"
                            component={renderSelectField}
                            label="Entity Of"
                            variant="outlined"
                            className={c.field}
                            labelText="List of Concepts that are related to this Entity."
                        >
                            <MenuItem value=""><em>None</em></MenuItem>
                            {
                                Object.keys(conceptsLabels).map((key,i) => {
                                    return (<MenuItem key={i} value={key}> {conceptsLabels[key]} </MenuItem>)
                                })
                            }

                        </Field>
                    </Grid>

                    {FootForm(props, c, 'Entity')}
                </Grid>
            </form>
        </div>
    )
}


export default reduxForm({
    form: 'AddEntityFrom', // a unique identifier for this form
    validate,
    asyncValidate
})(AddEntityFrom)
