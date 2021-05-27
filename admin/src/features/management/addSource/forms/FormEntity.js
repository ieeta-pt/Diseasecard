import React from 'react'
import { Field, reduxForm } from 'redux-form'
import asyncValidate from './asyncValidate'
import { Grid, MenuItem } from "@material-ui/core";
import {FootForm, renderSelectField, renderTextField, useStyles} from "./FormElements";
import {useSelector} from "react-redux";
import {getConceptsLabels, getEntitiesLabels} from "../addSourceSlice";


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
    return errors
}


const AddEntityFrom = props => {
    const { handleSubmit, classes} = props

    const c = useStyles();

    const conceptsLabels = useSelector(getConceptsLabels)
    const entitiesLabels = useSelector(getEntitiesLabels)

    const checkIfEntityLabelExists = (value, allValues, props, name) => {
        for (let key of Object.keys(entitiesLabels)) {
            if (value && entitiesLabels[key].toLowerCase() === value.toLowerCase()) {
                return 'Label already used.'
            }
        }
    }

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
                            labelText="olaaa"
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
                            labelText="This field works as the internal ID."
                            validate={checkIfEntityLabelExists}
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
                            labelText="olaaa"
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
                            labelText="olaaa"
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
